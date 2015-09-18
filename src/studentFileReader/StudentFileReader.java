package studentFileReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StudentFileReader {
	private String mFileName;
	private int[][] A_s;
	private List<List<Integer>> A_list = new ArrayList<List<Integer>>();
	private int CLASSES_OFFERED;
	
	private void fillStudentArray() {
		FileInputStream fstream;
		
		try {
			fstream = new FileInputStream(mFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			
			while ((strLine = br.readLine()) != null) {
				if (!strLine.isEmpty()) {
					if (strLine.charAt(0) != '%') {
						//Clear out whitespaces
						strLine = strLine.replaceAll("\\s+", "");
						
						//Split the string at each '.'
						String[] parts = strLine.split("\\.");
						
						//A list of classes the student wishes to take
						List<Integer> studentClasses = new ArrayList<Integer>();
						
						//Add each class the student wants to take to the list
						for (String s : parts) {
							studentClasses.add(Integer.parseInt(s));
						}
						//Add this to the overall main list for all students
						A_list.add(studentClasses);
					}
				}
			}
			
			int nStudents = A_list.size();
			
			A_s = new int[nStudents][CLASSES_OFFERED];
			
			for (int i = 0; i < nStudents; ++i) {
				//For each student
				for (Integer c : A_list.get(i)) {
					A_s[i][c - 1] = 1;
				}
				
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public StudentFileReader(String fileName, int classesOffered) {
		this.CLASSES_OFFERED = classesOffered;
		this.mFileName = fileName;
		this.fillStudentArray();
	}
	
	public int[][] read() {
		return A_s;
	}
}
