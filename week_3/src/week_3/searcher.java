package week_3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class searcher {
	
	//hashmap = index.post keyword
	HashMap<String,String> hashmap = new HashMap();
	
	//kkmaMap = Query keyword
	HashMap<String,Integer> kkmaMap = new HashMap();
	
	//Calculate & Ranking
	HashMap<Integer,Double> Cal = new HashMap();
	
	//Cosine similarity
	HashMap<Integer,Double> cosine = new HashMap();
	
	Double CoArr[][] = new Double[5][1];


	public void featuring(String path,String query) throws ClassNotFoundException, IOException, SAXException, ParserConfigurationException {
		
		//get query & Parsing using kkma
		KeywordExtractor ke = new KeywordExtractor();
		String KeywordString = "";
		KeywordList kl = ke.extractKeyword(query, true);
		
		for (int k = 0; k < kl.size(); k++) {
			Keyword kwrd = kl.get(k);
			kkmaMap.put(kwrd.getString(), 1);
		}

		//Get index.post
		getPost(path);
		
		//Calculate
		CalcSim();
		
		//find title base on ranking
		findTitle();
		
	}
	
	public void getPost(String path) throws IOException, ClassNotFoundException {
		
		FileInputStream filestream = new FileInputStream(path);
		ObjectInputStream objectInputStream = new ObjectInputStream(filestream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		hashmap = (HashMap)object;
		
	}
	
	public void CalcSim() {
		
		for(int i=0;i<5;i++) Cal.put(i,0.0);
		
		Iterator it = kkmaMap.keySet().iterator();
		int cnt = 0;
		
		for(int i=0;i<5;i++) {
			for(int j=0;j<1;j++) {
				CoArr[i][j] = 0.0;
			}
		}
		
		double wq = 0;
		double wi = 0;
		
		//Root( Wq ^ 2 )
		wq = Math.sqrt(kkmaMap.size());
		
		while(it.hasNext()) {
			
			String key = (String)it.next();
			double sum = 0.0;
			
			//Wq
			int w = kkmaMap.get(key);

			if(hashmap.containsKey(key)) {
								
				String hash = hashmap.get(key).replaceAll(",", "");
				hash = hash.substring(2,hash.length()-2);
				String[] tok = hash.split(" ");

				for(int i=0;i<tok.length;i++) {
					if(i<tok.length-2 && i%2==0 && tok[i].equals(tok[i+2]))i+=2;
					
					//tok[i+1] = Wi 
					sum=w*Double.parseDouble(tok[i+1])+Cal.get(Integer.parseInt(tok[i]));
					
					wi = Math.pow(Double.parseDouble(tok[i+1]), 2);
					
					CoArr[Integer.parseInt(tok[i])][0] += wi;
					
					Cal.put(Integer.parseInt(tok[i++]),sum);
				}
			}	
		}
		for(int i=0;i<CoArr.length;i++) {
			wi = Math.sqrt(CoArr[i][0]);
			double k = Cal.get(i);
			cosine.put(i, k/(wq*wi));
			System.out.println(i + "  "+ k/(wq*wi));
		}

	}
	
	public void findTitle() throws SAXException, IOException, ParserConfigurationException {
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document Xml = docBuilder.parse(".\\collections.xml");
		Element root = Xml.getDocumentElement();
		
		// value 값 내림차순 정렬
		List<Double> valueList = new ArrayList<>(cosine.values());
		
		System.out.println(valueList);
		valueList.sort(Double::compareTo);
		
		//가중치가 동일한 경우 사용되는 벡터
		Vector<Integer> ca = new Vector<Integer>();
		int count = 0;
		
		//3위까지 collections.xml에서 title 찾아 출력
		for(int i=4;i>=0;i--) {
			
			//벡터 초기화
			ca.removeAllElements();
			
			//가중치가 0이면 continue
			if(valueList.get(i)==0 || valueList.get(i).isNaN())continue;
			
			count++;
			if(count > 3) break;
			
			for(Integer ite : cosine.keySet()) {
				if(valueList.get(i).equals(cosine.get(ite))) {
					ca.add(ite);
				}
			}

			//가중치가 중복되는 경우
			if(ca.size()>1) {
				
				List<String> list = new ArrayList();
				
				//제목에 따라 오름차순 정렬
				for(int j=0;j<ca.size();j++) {
					int h = Integer.parseInt(String.valueOf(Math.round(ca.get(j))));
					list.add(root.getElementsByTagName("title").item(h).getTextContent());
					
				}
				Collections.sort(list);
				
				for(int j=0;j<list.size();j++) {
					System.out.println(count+" : "+list.get(j)+" "+Math.round(valueList.get(i)*100)/100.0);
					count++;	
					if(count > 3) break;
				}
			}
			//가중치가 중복되지 않는 경우
			else {
				int h = Integer.parseInt(String.valueOf(Math.round(ca.get(0))));
				String title = root.getElementsByTagName("title").item(h).getTextContent();
				System.out.println(count+" : "+title+" "+Math.round(valueList.get(i)*100)/100.0);
			}
		}

	}
	
}

