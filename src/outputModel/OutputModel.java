package outputModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import gurobi.*;

public class OutputModel {
	public double[] OUTPUT;

	public OutputModel(GRBVar[] output) {
		OUTPUT = new double[output.length];

		try {
			for (int i = 0 ; i < output.length; ++i) {
				OUTPUT[i] = output[i].get(GRB.DoubleAttr.X);
			}
		} catch (GRBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

	public void printToFile() {
		try {

			String content = toString();

			File file = new File("./ouput.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}