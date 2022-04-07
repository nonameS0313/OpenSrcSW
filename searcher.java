package mainFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class searcher {
	String post_path = "./index.post";
	String xml_path = "./index.xml";
	
	public searcher(){
		
	}
	
	public void InnerProduct(String query) throws Exception {
//		query = scan.nextLine();
		HashMap queryMap = new HashMap();
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		System.out.println(query);
		for(int i=0; i<kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			queryMap.put(kwrd.getString(), kwrd.getCnt());		//query�� ���� hashmap ����
		}
		
		//index.post �о����		
		FileInputStream fileStream = new FileInputStream(post_path);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		HashMap hashMap = (HashMap)object;		//index.post HashMap
		HashMap indexMap = new HashMap();		//index.post HashMap ������� �� ����ġ HashMap
		Iterator<String> it = indexMap.keySet().iterator();
		
		Set<String> keySet = queryMap.keySet();
		for(String key : keySet) {
			indexMap.put(key, hashMap.get(key));				//indexMap ����
		}
		
		double[] array = new double[5];
		for(int i =0; i<array.length; i++) {
			array[i] = 0;
		}														//array �ʱ�ȭ
		
		for(int i =0; i<5; i++) {
			for(String key : keySet) {
				ArrayList<String> list = (ArrayList)indexMap.get(key);
				double indexNum = Double.parseDouble((String)list.get(i * 2 + 1));
				array[i] += (int)queryMap.get(key) * indexNum;			//���絵 5�� �迭 ����
			}
		}
		
		ArrayList<Integer> third_List = new ArrayList<Integer>();		//���絵 ���� 3�� ���� id �迭
		
		while(third_List.size() < 3) {									//���絵 ���� 3�� ���� id �迭�� ����
			int flag = 0;
			for(int i =0; i<array.length; i++) {
				if((array[flag] < array[i]) && (third_List.contains(i) == false)) {
					flag = i;
				}
			}
			if(array[flag] == 0) {
				break;
			}
			third_List.add(flag);
		}
		
		File input = new File(xml_path);
		
		Document docsIn = Jsoup.parse(input, "UTF-8", "", Parser.xmlParser());
			
		String[] titleSt = new String[5];
		Elements docId0;
		Elements title;
			
		for(int i=0; i<5; i++) {
			 docId0 = docsIn.select("doc#"+Integer.toString(i));
			 title = docId0.select("title");
			 titleSt[i] = title.text();
		}
		
		if(third_List.size() == 0) {
			System.out.println("�˻��� ������ �����ϴ�.");
		}
		else if(third_List.size() < 3) {
			for(int i : third_List) {
				System.out.println(titleSt[i]);
			}
		}
	}
}