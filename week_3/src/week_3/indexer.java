package week_3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class indexer {
	
	public void Invert(String path) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException {
		FileOutputStream filestream = new FileOutputStream("index.post");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(filestream);
		
		//HashMap for index.post
		HashMap<String, String> keyMap = new HashMap();
		
		//Parsing index.xml
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document Xml = docBuilder.parse(path);
		Element root = Xml.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		
		// Vector and HashMap for checking keywords 
		Vector<String[]> token = new Vector<String[]>();
		HashMap<String,Integer> hash = new HashMap<String,Integer>();
		
		
		for(int i=0;i<nodeList.getLength();i++) {
		
			Node node = nodeList.item(i);
			NodeList list = node.getChildNodes();
			
			//Parsing Term and Term Frequency
			String Body = list.item(1).getTextContent();
			token.add(Body.split("#"));
			
			//Term and Inverse Document Frequency --> HashMap
			for(int j=0;j<token.get(i).length;j++) {
			
				String[] a = token.get(i);
				String []id_weight = a[j].split(":");
				
				if(hash.containsKey(id_weight[0])) {
					
					int q = hash.get(id_weight[0]);
					hash.put(id_weight[0], q+1);
				
				}
				else hash.put(id_weight[0], 1);
			}	
		}
		
		
		for(int i=0;i<nodeList.getLength();i++) {
						
			for(int j=0;j<token.get(i).length;j++) {
				
				String[] a = token.get(i);
				String []id_weight = a[j].split(":");
				
				//Vector for keyMap value
				Vector<Double> v = new Vector<Double>();
				
				// keyword
				String id = id_weight[0];
				
				if(keyMap.containsKey(id))continue;
				
				//IDF 
				int cnt = hash.get(id);
				
				//TF
				int weight = Integer.parseInt(id_weight[1]);
				
				//가중치 -> Vector
				double W = weight * Math.log(5.0/cnt);
				v.add((double) i);
				v.add(Math.round(W*100)/100.0);

				
				//If IDF>1, update value
				if(cnt>1) {
					
					for(int k=0;k<5;k++) {
						
						if(k==i)continue;

						String[] b= token.get(k);
						int index=-1;
						Vector<String> vec = new Vector<String>();
						
						// Parsing Term and Term Frequency
						// Find the location of this.keyword and update value
						for(int n=0;n<b.length;n++) {
							String[] str = b[n].split(":");
							if(id.equals(str[0])) {
								int w = Integer.parseInt(str[1]);
								W = w * Math.log(5.0/cnt);
								v.add((double)k);
								v.add(Math.round(W*100)/100.0);
							}
						}
					}
				}
				
				// List -> HashMap value
				String value ="";
				for(int k=0;k<v.size();k+=2) {
					value+=Math.round(v.get(k))+" "+v.get(k+1)+" ";
				}
				keyMap.put(id, value);
			}

		}
		
		objectOutputStream.writeObject(keyMap);
		objectOutputStream.close();
		System.out.println("성공!");
		
		//Print index.post
		printPost();
	}
	
	public void printPost() throws IOException, ClassNotFoundException {
		FileInputStream filestream = new FileInputStream("C:\\Users\\kms10\\Desktop\\gitstudy\\week_3\\index.post");
		ObjectInputStream objectInputStream = new ObjectInputStream(filestream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		System.out.println("읽어온 객체의 type -> "+object.getClass());
		
		HashMap hashmap = (HashMap)object;
		Iterator it = hashmap.keySet().iterator();
		
		while(it.hasNext()) {
			String key = (String) it.next();
			String value = (String)hashmap.get(key);
			System.out.println(key + " -> " + value);
		}
		
	}

}
