package studentScheduleModel;

import gurobi.*;

public class StudentScheduleModel {
	private GRBVar[][][] y_ijk;
	private GRBVar x;
	private GRBEnv env;
	private GRBModel model;
	private GRBLinExpr obj;
	private int[][] A_s;
	private int nSemesters;
	private int nStudents;
	private int nClassesOffered;
	
	private static final int SEMESTER_COURSE_LIMIT = 2;
	
	//Prequisite matrix
	private int[][] P = {
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	};
	
	//Class availability matrix
	private int[][] Av = {
			{1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0},
			{1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0},
			{1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0},
			{0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0},
			{1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0},
			{0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0}
	};
	
	private void init() throws GRBException {

		env = new GRBEnv();
		model = new GRBModel(env);
	
	
		y_ijk = new GRBVar[nStudents][nClassesOffered][nSemesters];
		
		for (int i = 0; i < nStudents; ++i) {
			//for each student
			for (int j = 0; j < nClassesOffered; ++j) {
				//for each class offered
				for (int k = 0; k < nSemesters; ++k) {
					//for each semester
					y_ijk[i][j][k] = model.addVar(0, 1, 1, GRB.BINARY, "y_" + i + "_" + j + "_" + k);
				}
			}
		}
		
		
		x = model.addVar(0, GRB.INFINITY, 1, GRB.INTEGER, null);
		obj = new GRBLinExpr();
		obj.addTerm(1, x);
		
		
		model.set(GRB.IntAttr.ModelSense, 1);
		model.update();
	
		
	}
	
	private void setMaxCoursesConstraint() throws GRBException {
		for (int i = 0; i < nStudents; ++i) {
			//For each student...
			for (int k = 0; k < nSemesters; ++k) {
				//...for each semester
				GRBLinExpr semesterCourseLimitConstraint = new GRBLinExpr();
				for (int j = 0; j < nClassesOffered; ++j) {
					semesterCourseLimitConstraint.addTerm(1, y_ijk[i][j][k]);
				}
				model.addConstr(semesterCourseLimitConstraint, GRB.LESS_EQUAL, 2, "SEMESTER_CLASS_LIMIT_student_" + i + "_semester_" + k);
				
			}
		}
	}
	
	private void setStudentMustTakeEachClassInListConstraint() throws GRBException {
		for (int i = 0; i < nStudents; i++) {		
			for (int j = 0; j < nClassesOffered; j++) {
				GRBLinExpr studentMustTakeEachClassInListConstraint = new GRBLinExpr();
				for (int k = 0; k < nSemesters; k++) {
					studentMustTakeEachClassInListConstraint.addTerm(1, y_ijk[i][j][k]);
				}
				model.addConstr(studentMustTakeEachClassInListConstraint, GRB.EQUAL, A_s[i][j], "MUST_TAKE_EACH_student_" + i + "_class_" + j);
			}
		}
	}
	
	private void setMaxClassCapacityConstraint() throws GRBException {
		//for each semester
		//for each class
		for (int k = 0; k < nSemesters; ++k) {
			for (int j = 0; j < nClassesOffered; ++j) {
				GRBLinExpr maxClassCapacityConstraint = new GRBLinExpr();
				for (int i = 0; i < nStudents; ++i) {
					maxClassCapacityConstraint.addTerm(1, y_ijk[i][j][k]);
				}
				model.addConstr(maxClassCapacityConstraint, GRB.LESS_EQUAL, x, "X");
			}
		}
	}
	
	private void setPrerequisiteConstraint() throws GRBException {
		for (int i = 0; i < nStudents; ++i) {
			for (int j = 0; j < nClassesOffered; ++j) {
				for (int p = 0; p < nClassesOffered; ++p) {
					if (P[j][p] == 1) {
						//p is prerequisite of j
						for (int k = 0; k < nSemesters; ++k) {
							//Add constraint here
							GRBLinExpr prerequisiteConstraint = new GRBLinExpr();
						
							for (int z = 0; z < k; ++z) {
								prerequisiteConstraint.addTerm(1, y_ijk[i][p][z]);
							}
							for (int z = 0; z < k + 1; ++z) {
								prerequisiteConstraint.addTerm(-1, y_ijk[i][j][z]);
							}
							model.addConstr(prerequisiteConstraint, GRB.GREATER_EQUAL, 0, "PREREQ_student_" + i + "_class_" + j + "_semester_" + k);

						}
					}
				}
			}
		}
	}
	
	private void setClassAvailabilityConstraint() throws GRBException {
		for (int i = 0; i < nStudents; ++i) {
			for (int j = 0; j < nClassesOffered; ++j) {
				
				for (int k = 0; k < nSemesters; ++k) {
					//Add constraint here
					GRBLinExpr classAvailabilityConstraint = new GRBLinExpr();
					classAvailabilityConstraint.addTerm(1, y_ijk[i][j][k]);
					model.addConstr(classAvailabilityConstraint, GRB.LESS_EQUAL, Av[j][k], "AVAILABILITY_student_" + i + "_class_" + j + "_semester_" + k);
				}
			}
		}
	}
	
	public StudentScheduleModel(int[][] A_s, int nSemesters, int nClassesOffered) {
		this.A_s = A_s;
		this.nSemesters = nSemesters;
		this.nClassesOffered = nClassesOffered;
		this.nStudents = A_s.length;
		
		try {
			init();
			setMaxCoursesConstraint();
			setStudentMustTakeEachClassInListConstraint();
			setMaxClassCapacityConstraint();
			setPrerequisiteConstraint();
			setClassAvailabilityConstraint();
			model.setObjective(obj);
			
		} catch (GRBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void optimize() {
		try {
			model.optimize();
			int status = model.get(GRB.IntAttr.Status);
			if (status == GRB.Status.OPTIMAL) {
		        System.out.println("X = " +
		            model.get(GRB.DoubleAttr.ObjVal));
		      }
		} catch (GRBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
