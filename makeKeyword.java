package mainFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Element;

public class makeKeyword {

	private String input_file;
	private String output_file = "./index.xml";
	
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
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		org.w3c.dom.Document index = docBuilder.newDocument();
		
		Element docs = index.createElement("docs");
		index.appendChild(docs);
		
		for(int i=0; i<bodyStAfter.length; i++) {
			Element doc = index.createElement("doc");
			docs.appendChild(doc);
		
			doc.setAttribute("id", Integer.toString(i));
		
			Element title2 = index.createElement("title");
			title2.appendChild(index.createTextNode(titleSt[i]));
			doc.appendChild(title2);
		
			Element body = index.createElement("body");
			body.appendChild(index.createTextNode(bodyStAfter[i]));
			doc.appendChild(body);
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(index);
		StreamResult result = new StreamResult(new FileOutputStream(new File(output_file)));
		
		transformer.transform(source, result);
			
//		Docbuild builder = new Docbuild(titleSt, bodyStAfter, output_flie);
//		builder.build();
			
	}
	        
}
		


