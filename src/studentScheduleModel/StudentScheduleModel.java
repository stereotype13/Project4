package studentScheduleModel;

import gurobi.*;
import inputModel.InputModel;

import outputModel.OutputModel;

public class StudentScheduleModel {
	private InputModel mInputModel;
	private GRBVar[][][] y_ijk;
	private int[] mProposedSchedule;
	private int[] mCurrentEnrollment;
	private int[] mClassPriorities;
	//private GRBVar[] mOptimizedSchedule;
	private GRBVar[] mOptimizedSchedule;
	private GRBVar x;
	private GRBEnv env;
	private GRBModel model;
	private GRBLinExpr obj;
	private int[][] A_s;
	private int nSemesters;
	private int nStudents;
	private int nClassesOffered;
	
	private static final int SEMESTER_COURSE_LIMIT = 2;
	private static final int MAX_CLASS_CAPACITY = 10;
	
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

		nStudents = 1;
		nSemesters = 1;

		mProposedSchedule = mInputModel.getStudentSchedule().getStudentSchedule();
		nClassesOffered = mProposedSchedule.length;

		mOptimizedSchedule = new GRBVar[nClassesOffered];

		mCurrentEnrollment = mInputModel.getMasterSchedule().getCurrentEnrollment();

		mClassPriorities = mInputModel.getStudentSchedule().getClassPriorities();

		for (int i = 0; i < nClassesOffered; ++i) {
			//mProposedSchedule[i] = model.addVar(0, 1, 1, GRB.BINARY, "class_" + i);
			//mOptimizedSchedule.addTerm(1, model.addVar(0, 1, 1, GRB.INTEGER, null));
			mOptimizedSchedule[i] = model.addVar(0, 1, 1, GRB.INTEGER, "class_" + i);
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
					semesterCourseLimitConstraint.addTerm(1, mOptimizedSchedule[j]);
				}
				model.addConstr(semesterCourseLimitConstraint, GRB.EQUAL, 2, "SEMESTER_CLASS_LIMIT_student_" + i);
				
			}
		}
	}
	
	private void setStudentMustTakeClassesFromProposedScheduleConstraint() throws GRBException {
		for (int i = 0; i < nStudents; i++) {		
			for (int j = 0; j < nClassesOffered; j++) {
				GRBLinExpr studentMustTakeClassesFromProposedScheduleConstraint = new GRBLinExpr();
				for (int k = 0; k < nSemesters; k++) {
					studentMustTakeClassesFromProposedScheduleConstraint.addTerm(1, mOptimizedSchedule[j]);
				}
				model.addConstr(studentMustTakeClassesFromProposedScheduleConstraint, GRB.LESS_EQUAL, mProposedSchedule[j], "CAN_TAKE_class" + j);
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
					maxClassCapacityConstraint.addTerm(1, mOptimizedSchedule[j]);
				}
				model.addConstr(maxClassCapacityConstraint, GRB.LESS_EQUAL, MAX_CLASS_CAPACITY - mCurrentEnrollment[j] - 1, "MAX_CLASS_CAPACITY");
			}
		}
	}
	
	/*
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
	*/

	public void setPriorityConstraint() throws GRBException {
		GRBLinExpr priorityConstraint = new GRBLinExpr();
		for (int i = 0; i < nClassesOffered; ++i) {
			priorityConstraint.addTerm(mClassPriorities[i], mOptimizedSchedule[i]);
		}
		model.addConstr(priorityConstraint, GRB.LESS_EQUAL, x, "X");
	}
	
	public StudentScheduleModel(InputModel ip) {
		this.mInputModel = ip;

		
		try {
			init();
			setMaxCoursesConstraint();
			setStudentMustTakeClassesFromProposedScheduleConstraint();
			setMaxClassCapacityConstraint();
			//setPrerequisiteConstraint();
			//setClassAvailabilityConstraint();
			setPriorityConstraint();
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

		        OutputModel om = new OutputModel(mOptimizedSchedule);
	
		 		om.printToFile();
		      }
		} catch (GRBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
}
