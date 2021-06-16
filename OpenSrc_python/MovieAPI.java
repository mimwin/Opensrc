package naverapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MovieAPI {
	
	//API KEY (가렸습니다..!)
	String clientId = "3SB_RlHvSSt8iukL_2ix"; 
	String clientSecret = "4X8CFuMdQ8"; 
	String url = "https://openapi.naver.com/v1/search/movie.json";
	
	BufferedReader br = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MovieAPI movieApi = new MovieAPI();
		movieApi.getAPI();
		movieApi.show();
		
	}
	
	private void show() {
		// Print Response
		
			String line;
			String res = "";
			
			try {
				while((line = br.readLine())!=null) {
					//response.append(inputLine);
					res += line;
				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			//JSON parsing 
			
			JSONParser jsonParser = new JSONParser();
			try {
				JSONObject jsonObject = (JSONObject) jsonParser.parse(res);
				JSONArray infoArray = (JSONArray) jsonObject.get("items");
				
				for(int i=0; i<infoArray.size(); i++) {
					System.out.println("=item_" + i + " ==========================================================");
					JSONObject itemObject = (JSONObject) infoArray.get(i);
					System.out.println("title:\t\t"+itemObject.get("title"));
					System.out.println("subtitle:\t"+itemObject.get("subtitle"));
					System.out.println("director:\t"+itemObject.get("director"));
					System.out.println("actor:\t\t"+itemObject.get("actor"));
					System.out.println("userRating:\t"+itemObject.get("userRating"));
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
	}

	//Search Movie with Naver open API
	private void getAPI() {

		HttpURLConnection conn;
		
		Scanner scan = new Scanner(System.in);
		
		System.out.print("검색어를 입력하세요: ");
		String word = scan.nextLine();
		
		String query = "";
		try {
			query = URLEncoder.encode(word, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String apiurl = url + "?query=" + query;

		try {
			
			URL url = new URL(apiurl);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("X-Naver-Client-Id", clientId);
			conn.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			
			int response = conn.getResponseCode();
			
			if(response == 200) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		scan.close();
	}
}
