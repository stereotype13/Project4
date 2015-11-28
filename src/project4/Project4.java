package project4;

import studentScheduleApp.StudentScheduleApp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Project4 {
	public static void main(String[] args) {
		Gson gson = new GsonBuilder().create();
		gson.toJson("hello", System.out);
		gson.toJson(123, System.out);
		StudentScheduleApp theApp = new StudentScheduleApp(args);
		theApp.run();
	}
}
