package studentSchedule;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StudentSchedule {
	private int[] STUDENT_SCHEDULE;
	private int[] CLASS_PRIORITIES;

	public StudentSchedule(int[] studentSchedule, int[] classPriorities) {
		STUDENT_SCHEDULE = studentSchedule;
		CLASS_PRIORITIES = classPriorities;
	}

	public int[] getStudentSchedule() {
		return STUDENT_SCHEDULE;
	}

	public int[] getClassPriorities() {
		return CLASS_PRIORITIES;
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}
}