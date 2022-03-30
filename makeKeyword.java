package mainFile;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class makeKeyword {

	private String input_file;
	private String output_flie = "./index.xml";
	
	public makeKeyword(String file) {
		this.input_file = file;
	}
	
	public void convertXml() throws IOException, Exception {
		// TODO Auto-generated method stub
		File input = new File(input_file);
			
		Document docsIn = Jsoup.parse(input, "UTF-8", "", Parser.xmlParser());
			
		String[] bodySt = new String[5];
		String[] titleSt = new String[5];
		String[] bodyStAfter = new String[5];
		Elements docId0;
		Elements body0;
		Elements title;
			
		for(int i=0; i<5; i++) {
			 docId0 = docsIn.select("doc#"+Integer.toString(i));
			 body0 = docId0.select("body");
			 title = docId0.select("title");
			 bodySt[i] = body0.text();
			 titleSt[i] = title.text();
		}
			
		for(int i=0; i<5; i++) {
			 bodyStAfter[i] = "";
		}
		
		KeywordExtractor ke = new KeywordExtractor();
		for(int i=0; i<5; i++) {
			KeywordList kl = ke.extractKeyword(bodySt[i], true);
			for(int j=0; j<kl.size(); j++) {
				Keyword kwrd = kl.get(j);
				bodyStAfter[i] += kwrd.getString() + ":" + kwrd.getCnt() + "#";
			}
		}
			
		Docbuild builder = new Docbuild(titleSt, bodyStAfter, output_flie);
		builder.build();
			
	}
	        
}
		


