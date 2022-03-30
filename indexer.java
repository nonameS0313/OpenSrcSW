package mainFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class indexer {
	String out_path = "./index.post";
	String input_file = "";
	
	public indexer(String input) {
		this.input_file = input;
	}
	
	public void makeIndex() throws IOException {
		FileOutputStream fileStream = new FileOutputStream(out_path);
		
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		
		File input = new File(input_file);
		
		Document docsIn = Jsoup.parse(input, "UTF-8", "", Parser.xmlParser());
			
		String[] bodySt = new String[5];
		Elements docId0;
		Elements body0;
		ArrayList<ArrayList> list = new ArrayList<ArrayList>();
		HashMap FoodMap = new HashMap();
		
		for(int i=0; i<5; i++) {
			 docId0 = docsIn.select("doc#"+Integer.toString(i));
			 body0 = docId0.select("body");
			 bodySt[i] = body0.text();
		}
		

		for(int i=0; i<5; i++) {	
			int count = 0;
			String[] flag = bodySt[i].split("#|:");
			ArrayList<String> imsi_list = new ArrayList<String>();
			for(String st : flag) {
				imsi_list.add(st);
			}
			list.add(imsi_list);
		}
		
		for(ArrayList<String> li : list) {
			int[] index = new int[5];			//각 문서에서 등장하는 단어의 위치번호
			
			for(int i=0; i<li.size(); i+=2){
				ArrayList<String> weight = new ArrayList<String>();
				int count = 0;
				int index_count = 0;
				
				for(ArrayList<String> li2 : list) {				//단어 x의 문서 등장 빈도
					if(li2.indexOf(li.get(i)) % 2 == 0)
						index[index_count] = li2.indexOf(li.get(i));
					else
						index[index_count] = -1;
					index_count++;
					if(li2.indexOf(li.get(i)) != -1) {
						if(li2.indexOf(li.get(i)) % 2 == 0)
							count++;
					}
				}
				
				for(int j=0; j<5; j++) {

					if(index[j] == -1) {
//						weight.add(Integer.toString(j));
//						weight.add("0.0");
					}else {
						int num = Integer.parseInt((String)(list.get(j)).get(index[j] + 1));
						double rawWNum = Math.log((double)5/count) * (double)num;
						double wNum = ((double)Math.round(rawWNum*100))/100;				//가중치 반올림
						weight.add(Integer.toString(j));
						weight.add(Double.toString(wNum));
					}
				}
				if(weight.isEmpty() == false)
					FoodMap.put(li.get(i), weight);
			}
		}	
		
		objectOutputStream.writeObject(FoodMap);
		
		objectOutputStream.close();
	}
}
