package week_3;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class kuir {

	public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException, SAXException, ClassNotFoundException {
		
		System.out.println("201911155 김미승");

		 if(args[0].equals("-c")) { 
			 makeCollection collect = new makeCollection();
			 collect.ReadHTMLS(args[1]); 
		 } 
		 else if(args[0].equals("-k")) { 
			 makeKeyword keyword = new makeKeyword(); 
			 keyword.ExtractKeyword(args[1]); 
		 }
		 else if(args[0].contentEquals("-i")) {
			indexer in = new indexer();
			in.Invert(args[1]);
		 }
		 else if(args[0].contentEquals("-s") && args[2].contentEquals("-q")) {
			 searcher feat = new searcher();
			 feat.featuring(args[1],args[3]);				 
		 
		 }

	}

}
