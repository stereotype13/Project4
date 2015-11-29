package project4;

import studentScheduleApp.StudentScheduleApp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import masterSchedule.MasterSchedule;
import studentSchedule.StudentSchedule;
import inputModel.InputModel;
import jsonParser.JSONParser;

public class Project4 {
	public static void main(String[] args) {
		Gson gson = new GsonBuilder().create();
		
		StudentScheduleApp theApp = new StudentScheduleApp(args);
		
		theApp.run();
		
	}
}
