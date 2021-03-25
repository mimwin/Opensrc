package week_3;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class kuir {

	public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException, SAXException, ClassNotFoundException {
		
		System.out.println("201911155 김미승");
		
		/*
		 * makeCollection collect = new makeCollection(); collect.ReadHTMLS(args[0]);
		 * makeKeyword keyword = new makeKeyword(); keyword.ExtractKeyword(
		 * "C:\\Users\\kms10\\Desktop\\gitstudy\\week_3\\collections.xml");
		 */

		 if(args[0].equals("-c")) { 
			 makeCollection collect = new makeCollection();
			 collect.ReadHTMLS(args[1]); 
		 } 
		 else if(args[0].equals("-k")) { 
			 makeKeyword keyword = new makeKeyword(); 
			 keyword.ExtractKeyword(args[1]); 
		 }
		 else if(args[0].contentEquals("-i")) {
			makeInvert in = new makeInvert();
			in.Invert(args[1]);
		 }
	}

}
