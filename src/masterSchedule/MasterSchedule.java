package masterSchedule;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MasterSchedule {
	//2-d matrix of [STUDENTS][COURSES] for the next semester.
	private int[][] MASTER_SCHEDULE;
	private int[] mCurrentEnrollment = null;

	public MasterSchedule(int[][] masterSchedule) {
		MASTER_SCHEDULE = masterSchedule;
		mCurrentEnrollment = new int[MASTER_SCHEDULE[0].length];
	}

	public int[] getCurrentEnrollment() {

		//We need to check this because if we use GSON to fill this object, the constructor isn't called.
		if (mCurrentEnrollment == null) {
			mCurrentEnrollment = new int[MASTER_SCHEDULE[0].length];
		}

		for (int i = 0; i < MASTER_SCHEDULE[0].length; ++i) {
			int tempSum = 0;
			for (int j = 0; j < MASTER_SCHEDULE.length; ++j) {
				if (MASTER_SCHEDULE[j][i] == 1) {
					++tempSum;
				}
			}
			mCurrentEnrollment[i] = tempSum;
		}
		return mCurrentEnrollment;
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}