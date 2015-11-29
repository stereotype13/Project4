package jsonParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import inputModel.InputModel;

public class JSONParser {
	private InputModel mInputModel;

	public JSONParser(String inputString) {
		//Example input
		/*
			{
				MS : {MASTER_SCHEDULE : [[1, 0, 0, 1],[0, 1, 1, 0],[0, 1, 0, 1]]},
				SS : {
					STUDENT_SCHEDULE : [1, 1, 0, 1],
					CLASS_PRIORITIES : [1, 2, 0, 3]
				}
			}
		*/
		Gson gson = new GsonBuilder().create();

		mInputModel = gson.fromJson(inputString, InputModel.class); 
		System.out.println(mInputModel.toString());
	}

	public InputModel getInputModel() {
		return mInputModel;
	}
}