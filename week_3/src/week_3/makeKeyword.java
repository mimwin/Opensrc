package week_3;

import java.io.BufferedReader;
import java.io.File;
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
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class makeKeyword {

	public void ExtractKeyword(String path)
			throws ParserConfigurationException, TransformerException, IOException, SAXException {
		
		//new Doc for index.xml
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element docs = doc.createElement("docs");
		doc.appendChild(docs);
		
		//Parsing collection.xml 
		Document Xml = docBuilder.parse(path);
		Element root = Xml.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			
			Node node = nodeList.item(i);
			NodeList list = node.getChildNodes();
			
			// id
			Element indoc = doc.createElement("doc");
			indoc.setAttribute("id", Integer.toString(i));
			docs.appendChild(indoc);
			
			
			for(int j=0; j<list.getLength(); j+=2) {
				
				// title,body from collections.xml
				String Col_title = list.item(j).getTextContent();
				String Col_body = list.item(j+1).getTextContent();
				
				// title
				Element title = doc.createElement("title");
				title.appendChild(doc.createTextNode(Col_title));
				indoc.appendChild(title);
				
				// body (use kkma to extract keyword)
				Element body = doc.createElement("body");
				KeywordExtractor ke = new KeywordExtractor();
				String KeywordString = "";
				KeywordList kl = ke.extractKeyword(Col_body, true);
				for (int k = 0; k < kl.size(); k++) {
					Keyword kwrd = kl.get(k);
					KeywordString += kwrd.getString() + ":" + kwrd.getCnt() + "#";
				}
				body.appendChild(doc.createTextNode(KeywordString));
				indoc.appendChild(body);
			}

		}

		// Make XML with doc
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		String xmlFilePath = ".\\index.xml";

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
