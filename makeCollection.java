package mainFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeCollection {
	
	private String data_path;
	private String output_file = "./collection.xml";
	
	public makeCollection(String path) {
		this.data_path = path;
	}
	

	public void makeXml() throws IOException, TransformerException, ParserConfigurationException {
		// TODO Auto-generated method stub
		File[] htmlfile = makeFileList(data_path);
		
		String[] titleData = new String[htmlfile.length];
		String[] bodyData = new String[htmlfile.length];
		
		for(int i=0; i<htmlfile.length; i++) {
			org.jsoup.nodes.Document html = Jsoup.parse(htmlfile[i], "UTF-8");
			titleData[i] = html.title();
			bodyData[i] = html.body().text();
		}
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document collection = docBuilder.newDocument();
		
		Element docs = collection.createElement("docs");
		collection.appendChild(docs);
		
		for(int i=0; i<htmlfile.length; i++) {
			Element doc = collection.createElement("doc");
			docs.appendChild(doc);
		
			doc.setAttribute("id", Integer.toString(i));
		
			Element title = collection.createElement("title");
			title.appendChild(collection.createTextNode(titleData[i]));
			doc.appendChild(title);
		
			Element body = collection.createElement("body");
			body.appendChild(collection.createTextNode(bodyData[i]));
			doc.appendChild(body);
		}
		
		
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(collection);
		StreamResult result = new StreamResult(new FileOutputStream(new File(output_file)));
		
		transformer.transform(source, result);
	}
	
	public File[] makeFileList(String path) {
		File dir = new File(path);
		return dir.listFiles();
	}

}
