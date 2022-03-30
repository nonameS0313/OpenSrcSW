package mainFile;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Docbuild {
	String[] titleData;
	String[] bodyData;
	String path;
	
	public Docbuild(String[] titleData, String[] bodyData, String path){
		this.titleData = titleData;
		this.bodyData = bodyData;
		this.path = path;
	}
	
	public void build() throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document index = docBuilder.newDocument();
		
		Element docs = index.createElement("docs");
		index.appendChild(docs);
		
		for(int i=0; i<bodyData.length; i++) {
			Element doc = index.createElement("doc");
			docs.appendChild(doc);
		
			doc.setAttribute("id", Integer.toString(i));
		
			Element title = index.createElement("title");
			title.appendChild(index.createTextNode(titleData[i]));
			doc.appendChild(title);
		
			Element body = index.createElement("body");
			body.appendChild(index.createTextNode(bodyData[i]));
			doc.appendChild(body);
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(index);
		StreamResult result = new StreamResult(new FileOutputStream(new File(path)));
		
		transformer.transform(source, result);
	}
}