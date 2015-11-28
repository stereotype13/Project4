package inputModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import masterSchedule.MasterSchedule;
import studentSchedule.StudentSchedule;

public class InputModel {
	private Gson mGson;
	private MasterSchedule MS;
	private StudentSchedule SS;

	public InputModel(MasterSchedule ms, StudentSchedule ss) {
		MS = ms;
		SS = ss;
		//Gson gson = GsonBuilder().create();
		mGson = new GsonBuilder().create();
	}

	public String toString() {
		return (mGson.toJson(MS));
	}
}