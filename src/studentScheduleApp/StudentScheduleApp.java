package studentScheduleApp;

import studentFileReader.StudentFileReader;
import studentScheduleModel.StudentScheduleModel;
import commandLineParser.CommandLineParser;

public class StudentScheduleApp {
	private CommandLineParser mClp;
	private StudentFileReader mSfr;
	private StudentScheduleModel mSsm;
	
	private static final int SEMESTERS = 12;
	private static final int AVAILABLE_COURSES = 18;
	
	public StudentScheduleApp(String[] commandLineArgs) {
		this.mClp = new CommandLineParser(commandLineArgs);
		this.mSfr = new StudentFileReader(mClp.getFilePath(), AVAILABLE_COURSES);
		this.mSsm = new StudentScheduleModel(mSfr.read(), SEMESTERS, AVAILABLE_COURSES);
	}
	
	public void run() {
		this.mSsm.optimize();
	}
}
