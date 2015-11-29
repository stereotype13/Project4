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
		//MasterSchedule ms = gson.fromJson("{MASTER_SCHEDULE : [[0, 1],[1,0]]}", MasterSchedule.class);
		//JSONParser jsonParser = new JSONParser("{MS : {MASTER_SCHEDULE : [[1, 0, 0, 1],[0, 1, 1, 0],[0, 1, 0, 1]]}, SS : {STUDENT_SCHEDULE : [1, 1, 0, 1], CLASS_PRIORITIES : [1, 2, 0, 3]}}");
		MasterSchedule ms = gson.fromJson("{MASTER_SCHEDULE : [[1, 0, 0, 1],[0, 1, 1, 0],[0, 1, 0, 1]]}", MasterSchedule.class);
		StudentSchedule ss = gson.fromJson("{STUDENT_SCHEDULE : [1, 1, 0, 1], CLASS_PRIORITIES : [1, 2, 0, 3]}", StudentSchedule.class);
		InputModel im = gson.fromJson("{MS : {MASTER_SCHEDULE : [[1, 0, 0, 1],[0, 1, 1, 0],[0, 1, 0, 1]]}, SS : {STUDENT_SCHEDULE : [1, 1, 0, 1], CLASS_PRIORITIES : [1, 2, 0, 3]}}", InputModel.class);
		//System.out.println(im.toString());
		theApp.run();
		ms.getCurrentEnrollment();
		System.out.println(ms.toString());
		System.out.println(ss.toString());
		System.out.println(im.toString());

		//test
		//StudentSchedule ss = gson.fromJson("{STUDENT_SCHEDULE : [1, 1, 0, 1], CLASS_PRIORITIES : [1, 2, 0, 3]}", StudentSchedule.class);

		//System.out.println(gson.toJson(im));
	}
}
