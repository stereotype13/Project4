package studentFileReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StudentFileReader {
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
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public StudentFileReader(String fileName) {
		
		this.mFileName = fileName;
	
	}
	
	public String read() {
		getJSON();
		return json;
	}
}
