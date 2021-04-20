package midterm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class genSnippet {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("201911155 김미승");
		
		
		if(args[0].contentEquals("-f") && args[2].contentEquals("-q")) { 
			//키워드열
			parse(args[3]);
			
		}
	}
	
	public static void parse(String args) throws FileNotFoundException {
		
		
		String[] tok = args.split(" ");
		int count = 0;
		int max = 0;
		String answer = "";
		
		File assign = new File("./input.txt");
		String content = "";
		
		try {
			Scanner scan = new Scanner(assign);
			while(scan.hasNext()) {
				
				content = scan.nextLine();

				String[] t = content.split(" ");
				
				for(int i=0;i<tok.length;i++) {
					for(int j=0;j<t.length;j++) {
						if(tok[i].equals(t[j])) {
							count++;
							break;
						}
					}
				}

				if(max<count) {
					answer = content;
					max=count;
				}
				count=0;

				
		}
			scan.close();
		} catch (FileNotFoundException e) {
			e.getStackTrace();
			System.out.println("파일 경로를 확인해주세요.");
		} catch (IOException e) {
			e.getStackTrace();
			System.out.println("파일 입출력 오류");
		}
		
		System.out.println(answer);
				
		
	}

}
