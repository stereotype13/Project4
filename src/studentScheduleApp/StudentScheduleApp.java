package studentScheduleApp;

import jsonFileReader.JSONFileReader;
import jsonParser.JSONParser;
import studentScheduleModel.StudentScheduleModel;
import commandLineParser.CommandLineParser;

public class StudentScheduleApp {
	private CommandLineParser mClp;
	private JSONFileReader mJFR;
	private StudentScheduleModel mSsm;
	
	private static final int SEMESTERS = 12;
	private static final int AVAILABLE_COURSES = 18;
	
	public StudentScheduleApp(String[] commandLineArgs) {
		this.mClp = new CommandLineParser(commandLineArgs);
		this.mJFR = new JSONFileReader(mClp.getFilePath());
		this.mSsm = new StudentScheduleModel(new JSONParser(mJFR.read()).getInputModel());
	}
	
	public void run() {
		this.mSsm.optimize();
	}
}
