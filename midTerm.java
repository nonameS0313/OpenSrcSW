package midTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class midTerm {
	
	private String input_file;
	
	public midTerm(String file) {
		this.input_file = file;
	}
	
	public void midTermExe(String queryString) throws IOException {
		File input = new File(input_file);
		
		Document docsIn = Jsoup.parse(input, "UTF-8", "", Parser.xmlParser());
			
		String[] bodySt = new String[5];
		String[] titleSt = new String[5];
		Elements docId0;
		Elements body0;
		Elements title;
			
		for(int i=0; i<5; i++) {
			 docId0 = docsIn.select("doc#"+Integer.toString(i));
			 body0 = docId0.select("body");
			 title = docId0.select("title");
			 bodySt[i] = body0.text();
			 titleSt[i] = title.text();
		}		//collection.xml 추출
		
		ArrayList<String> snippet = new ArrayList<String>();
		ArrayList<Integer> score = new ArrayList<Integer>();
		
		for(int i=0; i<5; i++) {			
			int count = 0;
	 Label: while(true) {
				String imsi = "";
				for(int j=0; j<30; j++) {
					if(bodySt[i].length() == count) {
						snippet.add(imsi);
						break Label;
					}
					imsi += bodySt[i].charAt(count);
					count++;
				}
				snippet.add(imsi);
			}
		}													//스니펫 형성
		
		ArrayList<String> query = new ArrayList<String>();
		
		KeywordExtractor ke = new KeywordExtractor();
		for(int i=0; i<5; i++) {
			KeywordList kl = ke.extractKeyword(queryString, true);
			for(int j=0; j<kl.size(); j++) {
				Keyword kwrd = kl.get(j);
				query.add(kwrd.getString());
			}
		}
		
		for(String oneSnippet : snippet) {
			int count = 0;
			for(int i = 0; i<query.size(); i++) {
				int flag = 0;
				while(oneSnippet.indexOf(query.get(i), flag) == -1) {
					if(oneSnippet.indexOf(query.get(i), 0) != -1) {
						count++;
						flag = oneSnippet.indexOf(query.get(i), flag);
					}
				}
			}
			score.add(count);
		}							//score 계산
		
		int largeNum = 0;
		int largeNumAdd = 0;
		for(int i=0; i<score.size(); i++) {
			if(largeNum < score.get(i)) {
				largeNum = score.get(i);
				largeNumAdd = i;
			}
		}
		
		System.out.println(snippet.get(largeNumAdd) + "," + largeNum);		
	}
}
