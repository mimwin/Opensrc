package week_3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

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

	public void ReadHTMLS(String path) throws IOException, ParserConfigurationException, TransformerException {
		
		File assign = new File(path); 
		String message = "",inputLine;
		ArrayList<String> list = new ArrayList<String>();

		if(!assign.exists())System.out.println("Error Path");

			File[] flist = assign.listFiles();
			for(int i=0;i<flist.length;i++) {
				URL web = new URL("file:///"+flist[i].getAbsolutePath());
				BufferedReader in = new BufferedReader(new InputStreamReader(web.openStream()));
				message=""; 
				while ((inputLine = in.readLine()) != null){
				     message+=inputLine+"\n";
				}
				list.add(message);
			}
			Parsing_makeXML(list);
	}
	
	
	public void Parsing_makeXML(ArrayList<String> list) throws ParserConfigurationException, TransformerException, FileNotFoundException {
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element docs = doc.createElement("docs"); 
		doc.appendChild(docs);
		
		for(int i=0;i<list.size();i++) {
			//id
			Element indoc = doc.createElement("doc");
			indoc.setAttribute("id", Integer.toString(i));
			docs.appendChild(indoc);
			
			//Parsing with JSoup
			org.jsoup.nodes.Document JS =Jsoup.parse(list.get(i));
			
			//title
			Element title = doc.createElement("title");
			String Title = JS.getElementsByTag("title").text();
			title.appendChild(doc.createTextNode(Title));
			indoc.appendChild(title);
			
			//body
			Element body = doc.createElement("body");
			String Body = JS.getElementsByTag("body").text();	
			body.appendChild(doc.createTextNode(Body));
			indoc.appendChild(body);
		}
		
		//Make XML with doc
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		String xmlFilePath = ".\\collections.xml";
		Transformer transformer = null;
		transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		DOMSource source = new DOMSource(doc);
		StreamResult result;
	
		result = new StreamResult(new FileOutputStream(new File(xmlFilePath)));
		transformer.transform(source, result);
		System.out.println("성공!");
	}

}
