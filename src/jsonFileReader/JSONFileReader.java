package jsonFileReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class JSONFileReader {
	private String mFileName;
	private String json = "";
	
	private void getJSON() {
		FileInputStream fstream;
		
		try {
			fstream = new FileInputStream(mFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			
			
			while ((strLine = br.readLine()) != null) {
				json += strLine;
			}
			
			System.out.println(json);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JSONFileReader(String fileName) {
		
		this.mFileName = fileName;
	
	}
	
	public String read() {
		getJSON();
		return json;
	}
}
