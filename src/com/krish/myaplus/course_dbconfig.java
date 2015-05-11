package com.krish.myaplus;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class course_dbconfig {
	
	/* Table : Courses */
	public static final String C_id = "C_id";
	public static final String C_Number = "C_Number";
	public static final String C_Name  = "C_Name";
	public static final String C_Lecture  = "C_Lecture";
	public static final String C_Semester  = "C_Semester";
	public static final String C_Room  = "C_Room";
	public static final String C_Credits  = "C_Credits";
	public static final String C_Grades  = "C_Grades";
	
	/* Table : Schedule */
	public static final String S_id = "S_id";
	public static final String S_Days = "S_Days";
	public static final String S_Time = "S_Time";
	
	/* Table : Policy */
	public static final String P_id = "P_id";
	public static final String P_name = "P_name";
	public static final String P_weight = "P_weight";
	
	/* Table : Homeworks */
	public static final String H_id = "H_id";
	public static final String H_Category = "H_Category";
	public static final String H_Description  = "H_Description";
	public static final String H_isComplete  = "H_isComplete";
	public static final String H_isReminder  = "H_isReminder";
	public static final String H_prioritylevel  = "H_prioritylevel";
	public static final String H_dueTime  = "H_dueTime";
	public static final String H_dueDate  = "H_dueDate";
	
	/* Table : GPA */
	public static final String G_id = "G_id";
	public static final String G_GPA = "G_GPA";
	
	/* Table : Grades */
	public static final String Gr_id = "Gr_id";
	public static final String Gr_points = "Gr_points";

	/* Table : Semester */
	public static final String Se_id = "Se_id";
	public static final String Se_Name = "Se_Name";
	public static final String Se_Tpoints = "Se_tpoints";
	public static final String Se_Tcredits = "Se_tcredits";
	public static final String Se_GPA = "Se_gpa";
	
	/* Table : User */
	public static final String U_id = "U_id";
	public static final String U_password = "U_password";
	public static final String U_OGPA = "U_OGPA";
	public static final String U_CSem = "U_CSem";
	public static final String U_Logedin = "U_Logedin";
	public static final String U_passSet = "U_passSet";
	
	private static final String DATABASE_NAME = "my_a_plus_db";
	
	private static final String DATABASE_TABLE_COURSES = "courses";
	private static final String DATABASE_TABLE_SET = "sset"; // Schedule
	private static final String DATABASE_TABLE_LIST = "llist"; //Policy
	private static final String DATABASE_TABLE_TPACKAGE = "_package"; //Homeworks
	private static final String DATABASE_TABLE_STORE = "_store"; //GPA
	private static final String DATABASE_TABLE_INCLUDE = "iinclude"; //Course -> Homeworks
	private static final String DATABASE_TABLE_SAVE = "_save"; // Homework -> Grades
	private static final String DATABASE_TABLE_HAS = "hass"; // Courses -> semester
	
	private static final String DATABASE_TABLE_SCHEDULE = "schedule";
	private static final String DATABASE_TABLE_POLICY = "policy";
	private static final String DATABASE_TABLE_HOMEWORK = "homework";
	private static final String DATABASE_TABLE_GRADES = "gradess";
	private static final String DATABASE_TABLE_GPA = "gpa";
	private static final String DATABASE_TABLE_SEMESTER = "semester";
	private static final String DATABASE_TABLE_USER = "user";
	
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper cHelper;
	private final Context cContext;
	private SQLiteDatabase cDatabase;
	
	long lastId_course;
	long lastId_schedule;
	long lastId_policy;
	long lastId_homework;
	long lastId_grades;
	long lastId_semester;
	int current_gpaID;
	boolean firstTime;
	
	private static class DbHelper extends SQLiteOpenHelper
	{
		public DbHelper(Context context) {
			super(context, DATABASE_NAME,null,DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE_COURSES);
			onCreate(db);
		}
	}
	
	public course_dbconfig(Context c)
	{
		cContext = c;
	}
	
	public course_dbconfig open() throws SQLException
	{
		cHelper = new DbHelper(cContext);
		cDatabase = cHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		cHelper.close();
	}
	
	//Access the create entry for add courses
	
	public long createEntry(String course_num, String course_name, String course_lec, String course_sem, String course_room, String course_credits, String grades )
	{
		ContentValues cv = new ContentValues();
		cv.put(C_Number, course_num);
		cv.put(C_Name, course_name);
		cv.put(C_Lecture, course_lec);
		cv.put(C_Semester, course_sem);
		cv.put(C_Room, course_room);
		cv.put(C_Credits, course_credits);
		cv.put(C_Grades, grades);
		return cDatabase.insert(DATABASE_TABLE_COURSES, null, cv);
	}
	
	//If user wants to save into class schedule, then create entry for schedule
	
	public long createScheduleEntry(String days, String time)
	{
		ContentValues cv = new ContentValues();
		cv.put(S_Days, days);
		cv.put(S_Time, time);
		return cDatabase.insert(DATABASE_TABLE_SCHEDULE, null, cv);
	}
	
	// Courses -> Schedule relational table
	
	public long create_COURSE_SCHDU_Entry()
	{
		ContentValues cv = new ContentValues();
		cv.put(S_id, lastId_schedule);
		cv.put(C_id, lastId_course);
		return cDatabase.insert(DATABASE_TABLE_SET, null, cv);
	}
	
	public long getLastIDCourses()
	{
		String query = "SELECT ROWID from courses order by ROWID DESC limit 1";
		Cursor c = cDatabase.rawQuery(query,null);
		if (c != null && c.moveToFirst()) {
		    lastId_course = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}
		return lastId_course;
	}
	
	public long getLastIDSchedule()
	{
		String query = "SELECT ROWID from schedule order by ROWID DESC limit 1";
		Cursor c = cDatabase.rawQuery(query,null);
		if (c != null && c.moveToFirst()) {
		    lastId_schedule = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}
		return lastId_schedule;
	}
	
	//Now codes for adding policy to the course
	//Add information to the policy
	public long createEntryPolicy(String policy_name, String policy_weight)
	{
		ContentValues cv = new ContentValues();
		cv.put(P_name, policy_name);
		cv.put(P_weight, policy_weight);
		return cDatabase.insert(DATABASE_TABLE_POLICY, null, cv);
	}
	
	public long getLastIDPolicy()
	{
		String query = "SELECT ROWID from policy order by ROWID DESC limit 1";
		Cursor c = cDatabase.rawQuery(query,null);
		if (c != null && c.moveToFirst()) {
		    lastId_policy = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}
		return lastId_policy;
	}
	
	//Add information to the relational table
	public long create_COURSE_POLICY_Entry()
	{
		//Guess the current coures id: last id+1, so 
		long current_courseId = lastId_course;
		
		ContentValues cv = new ContentValues();
		cv.put(C_id, current_courseId);
		cv.put(P_id, lastId_policy);
		return cDatabase.insert(DATABASE_TABLE_LIST, null, cv);
	}
	
	public int getsizePolicy(int current_id)
	{
		String[] args={Integer.toString(current_id)};
		
		Cursor c = cDatabase.rawQuery("SELECT count(P_id) FROM llist WHERE C_id=?", args);	
		int count=0;
		if(c.moveToNext())
		{
			count= c.getInt(c.getColumnIndex("count(P_id)"));
	    }
		int count2=count;
		count=0;
		c.close();
		return count2;
	}
	
	public int getsizeCourse(int semester_id)
	{
		
		String[] args={Integer.toString(semester_id)};
		
		Cursor c = cDatabase.rawQuery("SELECT count(C_id) FROM hass WHERE Se_id=?", args);	
		int count=0;
		if(c.moveToNext())
		{
			count= c.getInt(c.getColumnIndex("count(C_id)"));
	    }
		int count2=count;
		count=0;
		c.close();
		return count2;
	}
	
	public int getNumOfCredits(long listPosition)
	{
		String[] columns = new String[] {C_id,C_Number,C_Name,C_Lecture,C_Semester,C_Room,C_Credits,C_Grades};
		Cursor c = cDatabase.query(DATABASE_TABLE_COURSES, columns, C_id + "=" + listPosition, null, null, null, null);
			
		if(c != null)
		{
			c.moveToFirst();
			int credits = c.getInt(6);
			return credits;
		}
		return 0;
	}
	
	public String getLetterGrade(long listPosition)
	{
		String[] columns = new String[] {C_id,C_Number,C_Name,C_Lecture,C_Semester,C_Room,C_Credits,C_Grades};
		Cursor c = cDatabase.query(DATABASE_TABLE_COURSES, columns, C_id + "=" + listPosition, null, null, null, null);
			
		if(c != null)
		{
			c.moveToFirst();
			String letter = c.getString(7);
			return letter;
		}
		return null;
	}
	
	public long updateGrades(String grade,int course_id)
	{
		ContentValues cv = new ContentValues();
		cv.put(C_Grades, grade);
		return cDatabase.update(DATABASE_TABLE_COURSES, cv, "C_id=" + course_id, null);
	} 
	
	public ArrayList<String> getrowidPolicy(int current_id)
	{
		long selectedId = 0;	
		if(current_id==-1)
		{
			getLastIDCourses();
			selectedId = lastId_course+1;
		}
		else
		{
			selectedId =  Long.valueOf(current_id);
		}
				
		
		String[] args={Long.toString(selectedId)};

		String[] columns = new String[] {P_name,P_weight};
		Cursor c = cDatabase.rawQuery("SELECT P_id FROM llist WHERE C_id=?", args);
		ArrayList<String> rowList = new ArrayList<String>();
		int iRow = c.getColumnIndex(P_id);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			rowList.add(c.getString(0));
	    }

		return rowList;
	}
	
	public int getPolicyIdByHW(int homeworkId)
	{
		String[] args={Integer.toString(homeworkId)};
		
		Cursor c = cDatabase.rawQuery("SELECT P_id FROM _package WHERE H_id=?", args);	
		int count=0;
		if(c.moveToNext())
		{
			count= c.getInt(c.getColumnIndex("P_id"));
	    }
		int count2=count;
		count=0;
		c.close();
		return count2;
	}
	
	public String getWeight(long L)
	{
		String[] columns = new String[] {P_id,P_name,P_weight};
		Cursor c = cDatabase.query(DATABASE_TABLE_POLICY, columns, P_id + "=" + L, null, null, null, null);
			
		if(c != null)
		{
			c.moveToFirst();
			String name = c.getString(2);
			return name;
		}
		return null;
	}
	
	public ArrayList<String> getrowidCourse(int semester_id)
	{
		long selectedId = 0;	
		selectedId =  Long.valueOf(semester_id);
		
		String[] args={Long.toString(selectedId)};

		String[] columns = new String[] {Se_id,C_id};
		Cursor c = cDatabase.rawQuery("SELECT C_id FROM hass WHERE Se_id=?", args);
		ArrayList<String> rowList = new ArrayList<String>();
		int iRow = c.getColumnIndex(C_id);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			rowList.add(c.getString(0));
	    }

		return rowList;
	}
	
	public String getPolicyName(long L)
	{
		String[] columns = new String[] {P_id,P_name,P_weight};
		Cursor c = cDatabase.query(DATABASE_TABLE_POLICY, columns, P_id + "=" + L, null, null, null, null);
			
		if(c != null)
		{
			c.moveToFirst();
			String name = c.getString(1);
			return name;
		}
		return null;
	}
	
	public String getCourseName(long L)
	{
		String[] columns = new String[] {C_Number,C_Name,C_Lecture,C_Semester,C_Room,C_Credits,C_Grades};
		Cursor c = cDatabase.query(DATABASE_TABLE_COURSES, columns, C_id + "=" + L, null, null, null, null);
		
		if(c != null)
		{
			c.moveToFirst();
			String name = c.getString(1);
			return name;
		}
		return null;
	}
	
	public String getCourseNumber(long L)
	{
		String[] columns = new String[] {C_Number,C_Name,C_Lecture,C_Semester,C_Room,C_Credits,C_Grades};
		Cursor c = cDatabase.query(DATABASE_TABLE_COURSES, columns, C_id + "=" + L, null, null, null, null);
		
		if(c != null)
		{
			c.moveToFirst();
			String name = c.getString(0);
			return name;
		}
		return null;
	}
	
	public String getCourseLecturer(long L)
	{
		String[] columns = new String[] {C_Number,C_Name,C_Lecture,C_Semester,C_Room,C_Credits,C_Grades};
		Cursor c = cDatabase.query(DATABASE_TABLE_COURSES, columns, C_id + "=" + L, null, null, null, null);
		
		if(c != null)
		{
			c.moveToFirst();
			String name = c.getString(2);
			return name;
		}
		return null;
	}
	
	public String getCourseRoom(long L)
	{
		String[] columns = new String[] {C_Number,C_Name,C_Lecture,C_Semester,C_Room,C_Credits,C_Grades};
		Cursor c = cDatabase.query(DATABASE_TABLE_COURSES, columns, C_id + "=" + L, null, null, null, null);
		
		if(c != null)
		{
			c.moveToFirst();
			String name = c.getString(4);
			return name;
		}
		return null;
	}
	
	public int getCourseIDByHW(int homework_id)
	{
		String[] args={Integer.toString(homework_id)};
		
		Cursor c = cDatabase.rawQuery("SELECT C_id FROM iinclude WHERE H_id=?", args);	
		int count=0;
		if(c.moveToNext())
		{
			count= c.getInt(c.getColumnIndex("C_id"));
	    }
		int count2=count;
		count=0;
		c.close();
		return count2;
	}
	
	//--------------- HomeWorks--------------------------//
	//Get the information about the homework that does not completed yet
	
	public int getsizeHomeworks_NC()
	{
		Cursor c = cDatabase.rawQuery("SELECT count(H_id) FROM homework WHERE H_isComplete='no' ", null);	
		int count=0;
		if(c.moveToNext())
		{
			count= c.getInt(c.getColumnIndex("count(H_id)"));
	    }
		int count2=count;
		count=0;
		c.close();
		return count2;
	}
	
	public ArrayList<String> getrowidHomeworks_NC()
	{
		String[] columns = new String[] {H_id,H_Category,H_Description,H_isComplete,H_isReminder,H_prioritylevel,H_dueTime,H_dueDate};
		Cursor c = cDatabase.rawQuery("SELECT H_id FROM homework WHERE H_isComplete='no' ", null);
		ArrayList<String> rowList = new ArrayList<String>();
		int iRow = c.getColumnIndex(H_id);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			rowList.add(c.getString(0));
	    }

		return rowList;
	}
	
	public String getHomeworkName_NC(long L)
	{
		String[] columns = new String[] {H_Category,H_Description,H_isComplete,H_isReminder,H_prioritylevel,H_dueTime,H_dueDate};
		Cursor c = cDatabase.query( DATABASE_TABLE_HOMEWORK, columns, H_id + "=" + L, null, null, null, null);
		
		if(c != null)
		{
			c.moveToFirst();
			String name = c.getString(1);
			return name;
		}
		return null;
	}
	
	
	//Get the information about the homework that is completed
	
	public int getsizeHomeworks_C()
	{
		Cursor c = cDatabase.rawQuery("SELECT count(H_id) FROM homework WHERE H_isComplete='yes' ", null);	
		int count=0;
		if(c.moveToNext())
		{
			count= c.getInt(c.getColumnIndex("count(H_id)"));
	    }
		int count2=count;
		count=0;
		c.close();
		return count2;
	}
	
	public ArrayList<String> getrowidHomeworks_C()
	{
		String[] columns = new String[] {H_id,H_Category,H_Description,H_isComplete,H_isReminder,H_prioritylevel,H_dueTime,H_dueDate};
		Cursor c = cDatabase.rawQuery("SELECT H_id FROM homework WHERE H_isComplete='yes' ", null);
		ArrayList<String> rowList = new ArrayList<String>();
		int iRow = c.getColumnIndex(H_id);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			rowList.add(c.getString(0));
	    }

		return rowList;
	}
	
	public ArrayList<String> getrowidHomeworksByCID(long listPosition)
	{
		String[] columns = new String[] {H_id,H_Category,H_Description,H_isComplete,H_isReminder,H_prioritylevel,H_dueTime,H_dueDate};
		
		String[] args = {Long.toString(listPosition)};
		
		Cursor c = cDatabase.rawQuery("SELECT H_id FROM iinclude WHERE C_id=? ", args);
		ArrayList<String> rowList = new ArrayList<String>();
		int iRow = c.getColumnIndex(H_id);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			rowList.add(c.getString(0));
	    }

		return rowList;
	}
	
		
	public String getHomeWorkDesc(int hw_id)
	{
		String[] columns = new String[] {H_id,H_Category,H_Description,H_isComplete,H_isReminder,H_prioritylevel,H_dueTime,H_dueDate};
		Cursor c = cDatabase.query(DATABASE_TABLE_HOMEWORK, columns, H_id + "=" + hw_id, null, null, null, null);
		if(c != null)
		{
			c.moveToFirst();
			String number = c.getString(2);
			return number;
		}
		return null;
	}
	
      //Access the create entry for add homework
	
		public long createEntryHomework( String h_category, String h_description, String h_iscomplete, String h_isreminder, String h_prioritylevel, String h_duetime, String h_duedate  )
		{
			ContentValues cv = new ContentValues();
			cv.put(H_Category, h_category);
			cv.put(H_Description, h_description);
			cv.put(H_isComplete, h_iscomplete);
			cv.put(H_isReminder, h_isreminder);
			cv.put(H_prioritylevel, h_prioritylevel);
			cv.put(H_dueTime, h_duetime);
			cv.put(H_dueDate, h_duedate);
			return cDatabase.insert(DATABASE_TABLE_HOMEWORK, null, cv);
		}
		
		//Add information to the relational table(Course -> Homeworks)
		public long create_COURSE_HOMEWORK_Entry(long current_courseId )
		{
			ContentValues cv = new ContentValues();
			cv.put(C_id, current_courseId);
			cv.put(H_id, lastId_homework);
			return cDatabase.insert(DATABASE_TABLE_INCLUDE, null, cv);
		}
		
		//Add information to the relational table(Policy -> Homeworks)
		public long create_POLICY_HOMEWORK_Entry(long current_policyId, long current_courseId )
		{
			ContentValues cv = new ContentValues();
			cv.put(C_id, current_courseId);
			cv.put(P_id, current_policyId);
			cv.put(H_id, lastId_homework);
			return cDatabase.insert(DATABASE_TABLE_TPACKAGE, null, cv);
		}
		
		public void homeIsDone(long L)
		{
			 ContentValues cv = new ContentValues();
		     cv.put(H_isComplete, "yes");
		     cDatabase.update(DATABASE_TABLE_HOMEWORK, cv, "H_id=" + L, null);
		}
		
		public long getLastIDHomework()
		{
			String query = "SELECT ROWID from homework order by ROWID DESC limit 1";
			Cursor c = cDatabase.rawQuery(query,null);
			if (c != null && c.moveToFirst()) {
				lastId_homework = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
			}
			return lastId_homework;
		}
		
		//-------------------Get the homework information-------------------
		
		public String get_homework_category(long L)
		{
			String[] columns = new String[] {H_id,H_Category,H_Description,H_isComplete,H_isReminder,H_prioritylevel,H_dueTime,H_dueDate};
			Cursor c = cDatabase.query(DATABASE_TABLE_HOMEWORK, columns, H_id + "=" + L, null, null, null, null);
			if(c != null)
			{
				c.moveToFirst();
				String number = c.getString(1);
				return number;
			}
			return null;
		}
		
		public String get_homework_priority(long L)
		{
			String[] columns = new String[] {H_id,H_Category,H_Description,H_isComplete,H_isReminder,H_prioritylevel,H_dueTime,H_dueDate};
			Cursor c = cDatabase.query(DATABASE_TABLE_HOMEWORK, columns, H_id + "=" + L, null, null, null, null);
			if(c != null)
			{
				c.moveToFirst();
				String number = c.getString(5);
				return number;
			}
			return null;
		}
		
		public String get_homework_DueDate(long L)
		{
			String[] columns = new String[] {H_id,H_Category,H_Description,H_isComplete,H_isReminder,H_prioritylevel,H_dueTime,H_dueDate};
			Cursor c = cDatabase.query(DATABASE_TABLE_HOMEWORK, columns, H_id + "=" + L, null, null, null, null);
			if(c != null)
			{
				c.moveToFirst();
				String number = c.getString(6);
				return number;
			}
			return null;
		}
		
		public String get_homework_DueTime(long L)
		{
			String[] columns = new String[] {H_id,H_Category,H_Description,H_isComplete,H_isReminder,H_prioritylevel,H_dueTime,H_dueDate};
			Cursor c = cDatabase.query(DATABASE_TABLE_HOMEWORK, columns, H_id + "=" + L, null, null, null, null);
			if(c != null)
			{
				c.moveToFirst();
				String number = c.getString(7);
				return number;
			}
			return null;
		}
		
		public ArrayList<String> getrowidHomeworks_REM()
		{
			String[] columns = new String[] {H_id,H_Category,H_Description,H_isComplete,H_isReminder,H_prioritylevel,H_dueTime,H_dueDate};
			Cursor c = cDatabase.rawQuery("SELECT H_id FROM homework WHERE H_isReminder='yes' AND H_isComplete='no' ", null);
			ArrayList<String> rowList = new ArrayList<String>();
			int iRow = c.getColumnIndex(H_id);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
			{
				rowList.add(c.getString(0));
		    }

			return rowList;
		}
		
		
		//-------------------Updates/Create grades and GPA-------------------
		
		
		//Add information to the grades table
		public long createGrades(double points)
		{
			ContentValues cv = new ContentValues();
			cv.put(Gr_points, points);
			return cDatabase.insert(DATABASE_TABLE_GRADES, null, cv);
		}
		
		//Add information to the save relational table(Homeworks -> grades)
		//Add information to the relational table
		public long create_HOMEWORK_GRADES_Entry(int homework_ID, int policy_ID)
		{
			ContentValues cv = new ContentValues();
			cv.put(H_id, homework_ID);
			cv.put(P_id, policy_ID);
			cv.put(Gr_id, lastId_grades);
			return cDatabase.insert(DATABASE_TABLE_SAVE, null, cv);
		}
		
		public long getLastIDGrades()
		{
			String query = "SELECT ROWID from gradess order by ROWID DESC limit 1";
			Cursor c = cDatabase.rawQuery(query,null);
			if (c != null && c.moveToFirst()) {
				lastId_grades = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
			}
			return lastId_grades;
		}
		
		public ArrayList<String> getAllGradesID(int policyID)
		{
			String[] columns = new String[] {H_id,H_Category,H_Description,H_isComplete,H_isReminder,H_prioritylevel,H_dueTime,H_dueDate};
			String args[] = {Integer.toString(policyID)};
			Cursor c = cDatabase.rawQuery("SELECT Gr_id FROM _save WHERE P_id=? ", args);
			ArrayList<String> rowList = new ArrayList<String>();
			int iRow = c.getColumnIndex(Gr_id);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
			{
				rowList.add(c.getString(0));
		    }

			return rowList;
		}
		
		public double getPoints(int grade_id)
		{
			String[] columns = new String[] {Gr_id,Gr_points};
			Cursor c = cDatabase.query(DATABASE_TABLE_GRADES, columns, Gr_id + "=" + grade_id, null, null, null, null);
				
			if(c != null)
			{
				c.moveToFirst();
				double points = c.getDouble(1);
				return points;
			}
			return 0;
		}
		
		public int getGradeID(int homework_id)
		{
			String[] columns = new String[] {H_id,P_id,Gr_id};
			Cursor c = cDatabase.query(DATABASE_TABLE_SAVE, columns, H_id + "=" + homework_id, null, null, null, null);
				
			if(c != null)
			{
				c.moveToFirst();
				int points = c.getInt(2);
				return points;
			}
			return 0;
		}
		
		public void checkGpaRec(int courseID)
		{
			String args[] = {Integer.toString(courseID)};			
			Cursor c = cDatabase.rawQuery("SELECT count(G_id) FROM _store WHERE C_id=? ", args);	
			int count=0;
			if(c.moveToNext())
			{
				count= c.getInt(c.getColumnIndex("count(G_id)"));
		    }
			
			//Check the count
			
			if(count < 1)
			{
				firstTime = true;
			}
			else
			{
				firstTime = false;
			}
			c.close();
		}
		
		public void getCurrentGpaID(int courseID)
		{
			String[] columns = new String[] {C_id,G_id};
			Cursor c = cDatabase.query(DATABASE_TABLE_STORE, columns, C_id + "=" + courseID, null, null, null, null);
				
			if(c != null)
			{
				c.moveToFirst();
				current_gpaID = c.getInt(1);
			}
		}
		
		public long createGPA(double gpaValue, String gpaGrade)
		{
			ContentValues cv = new ContentValues();
			cv.put(G_GPA, gpaValue);

			if(firstTime)
			{
				return cDatabase.insert(DATABASE_TABLE_GPA, null, cv);
			}
			else
			{
				return cDatabase.update(DATABASE_TABLE_GPA, cv, "G_id=" + current_gpaID, null);
			}
		} 
		
		//---------------------Semester--------------------//
		//Add information to the semester table
		public long createSemester(String semesterName, double total_points, double total_credits, double gpaValue)
		{
			ContentValues cv = new ContentValues();
			cv.put(Se_Name, semesterName);
			cv.put(Se_Tpoints, total_points);
			cv.put(Se_Tcredits, total_credits);
			cv.put(Se_GPA, gpaValue);
			return cDatabase.insert(DATABASE_TABLE_SEMESTER, null, cv);
		}
		
		public long updateSemester(int semester_id,double total_points, double total_credits, double gpaValue)
		{
			ContentValues cv = new ContentValues();
			cv.put(Se_Tpoints, total_points);
			cv.put(Se_Tcredits, total_credits);
			cv.put(Se_GPA, gpaValue);
			return cDatabase.update(DATABASE_TABLE_SEMESTER, cv, "Se_id=" + semester_id, null);
		}
		
		public long updateSemesterName(long semester_id, String semester_name)
		{
			ContentValues cv = new ContentValues();
			cv.put(Se_Name, semester_name);
			return cDatabase.update(DATABASE_TABLE_SEMESTER, cv, "Se_id=" + semester_id, null);
		}
		
		public long getLastIDSemester()
		{
			String query = "SELECT ROWID from semester order by ROWID DESC limit 1";
			Cursor c = cDatabase.rawQuery(query,null);
			if (c != null && c.moveToFirst()) {
				lastId_semester = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
			}
			return lastId_semester;
		}
		
		//Add information to the has table
		public long create_COURSE_SEMESTER_Entry(int current_semster_Id)
		{
			ContentValues cv = new ContentValues();
			cv.put(Se_id, current_semster_Id);
			cv.put(C_id, lastId_course);
			return cDatabase.insert(DATABASE_TABLE_HAS, null, cv);
		}
		
		public int getsizeSemester()
		{
			String[] columns = new String[] {Se_id, Se_Name};
			Cursor c = cDatabase.query(DATABASE_TABLE_SEMESTER, columns, null, null, null, null, null);
			
			int iRow = c.getColumnIndex(Se_id);
			int iSe_Name = c.getColumnIndex(Se_Name);
			
			int count=0;
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
			{
				count=count+1;
		    }
			int count2=count;
			count=0;
			return count2;
		}
		
		public ArrayList<String> getrowidSemester()
		{
			String[] columns = new String[] {Se_id, Se_Name};
			Cursor c = cDatabase.query(DATABASE_TABLE_SEMESTER, columns, null, null, null, null, null);
			ArrayList<String> rowList = new ArrayList<String>();
			int iRow = c.getColumnIndex(Se_id);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
			{
				rowList.add(c.getString(0));
		    }
			return rowList;
		}
		
		public String getSemsterName(long L)
		{
			String[] columns = new String[] {Se_id, Se_Name};
			Cursor c = cDatabase.query( DATABASE_TABLE_SEMESTER, columns, Se_id + "=" + L, null, null, null, null);
			
			if(c != null)
			{
				c.moveToFirst();
				String name = c.getString(1);
				return name;
			}
			return null;
		}
		
		public int getSemsterCredits(long L)
		{
			String[] columns = new String[] {Se_id, Se_Name, Se_Tpoints,Se_Tcredits, Se_GPA };
			Cursor c = cDatabase.query( DATABASE_TABLE_SEMESTER, columns, Se_id + "=" + L, null, null, null, null);
			
			if(c != null)
			{
				c.moveToFirst();
				int credit = c.getInt(3);
				return credit;
			}
			return 0;
		}
		
		public int getSemsterTotalPoints(long L)
		{
			String[] columns = new String[] {Se_id, Se_Name, Se_Tpoints,Se_Tcredits, Se_GPA };
			Cursor c = cDatabase.query( DATABASE_TABLE_SEMESTER, columns, Se_id + "=" + L, null, null, null, null);
			
			if(c != null)
			{
				c.moveToFirst();
				int points = c.getInt(2);
				return points;
			}
			return 0;
		}
		
		public double getSemsterGpa(long L)
		{
			String[] columns = new String[] {Se_id, Se_Name, Se_Tpoints,Se_Tcredits, Se_GPA };
			Cursor c = cDatabase.query( DATABASE_TABLE_SEMESTER, columns, Se_id + "=" + L, null, null, null, null);
			
			if(c != null)
			{
				c.moveToFirst();
				double gpa = c.getInt(4);
				return gpa;
			}
			return 0;
		}
		
		
		public int getSemsterIDByCID(long course_id)
		{
			String[] columns = new String[] {Se_id, C_id };
			Cursor c = cDatabase.query( DATABASE_TABLE_HAS, columns, C_id + "=" + course_id, null, null, null, null);
			
			if(c != null)
			{
				c.moveToFirst();
				int id = c.getInt(0);
				return id;
			}
			return 0;
		}
		
		public int getSemesterID(String semester_name)
		{
			String args[] = {semester_name};
			
			Cursor c = cDatabase.rawQuery("SELECT Se_id FROM semester WHERE Se_Name=? ", args);	
			int id = 0;
			if(c.moveToNext())
			{
				id = c.getInt(c.getColumnIndex("Se_id"));
		    }
			c.close();
			return id;
		}
		
		//----------------------users--------------------
		
		public String getUserLogin()
		{
			try
			{
				String[] columns = new String[] {U_id, U_password, U_OGPA , U_CSem, U_Logedin, U_passSet };
				Cursor c = cDatabase.query( DATABASE_TABLE_USER, columns, U_id + "=" + 1, null, null, null, null);
				
				if(c != null)
				{
					c.moveToFirst();
					String answer = c.getString(4);
					return answer;
				}
				else
				{
					return "no";
				}
			}
			catch(Exception e){return "no";}
		}
		
		public String getUserPassword()
		{
			try
			{
				String[] columns = new String[] {U_id, U_password, U_OGPA , U_CSem, U_Logedin, U_passSet };
				Cursor c = cDatabase.query( DATABASE_TABLE_USER, columns, U_id + "=" + 1, null, null, null, null);
				
				if(c != null)
				{
					c.moveToFirst();
					String answer = c.getString(1);
					return answer;
				}
				else
				{
					return "no";
				}
			}
			catch(Exception e){return "no";}
		}
		
		public long createUser(String user_password, String user_ogpa, String current_semester, String user_login, String passSet)
		{
			ContentValues cv = new ContentValues();
			cv.put(U_password, user_password);
			cv.put(U_OGPA, user_ogpa);
			cv.put(U_CSem, current_semester);
			cv.put(U_Logedin, user_login);
			cv.put(U_passSet, passSet);
			return cDatabase.insert(DATABASE_TABLE_USER, null, cv);
		}
		
		public long updatePassword(String user_password)
		{
			ContentValues cv = new ContentValues();
			cv.put(U_password, user_password);
			return cDatabase.update(DATABASE_TABLE_USER, cv, "U_id=" + 1, null);
		}
		
		public long updateISPassword(String setPass)
		{
			ContentValues cv = new ContentValues();
			cv.put(U_passSet, setPass);
			return cDatabase.update(DATABASE_TABLE_USER, cv, "U_id=" + 1, null);
		}
		
		public String getUserIsPassword()
		{
			try
			{
				String[] columns = new String[] {U_id, U_password, U_OGPA , U_CSem, U_Logedin, U_passSet };
				Cursor c = cDatabase.query( DATABASE_TABLE_USER, columns, U_id + "=" + 1, null, null, null, null);
				
				if(c != null)
				{
					c.moveToFirst();
					String answer = c.getString(5);
					return answer;
				}
				else
				{
					return "no";
				}
			}
			catch(Exception e){return "no";}
		}
		
		public long updatecurrentSemester(String semester_name)
		{
			ContentValues cv = new ContentValues();
			cv.put(U_CSem, semester_name);
			return cDatabase.update(DATABASE_TABLE_USER, cv, "U_id=" + 1, null);
		}
		
		public String getCurrentSemester()
		{
			try
			{
				String[] columns = new String[] {U_id, U_password, U_OGPA , U_CSem, U_Logedin, U_passSet };
				Cursor c = cDatabase.query( DATABASE_TABLE_USER, columns, U_id + "=" + 1, null, null, null, null);
				
				if(c != null)
				{
					c.moveToFirst();
					String answer = c.getString(3);
					return answer;
				}
				else
				{
					return "no";
				}
			}
			catch(Exception e){return "no";}
		}
		
		
		//--------------------------------Delete Entry-----------------------------//
		
		public void deleteEntrySemester(long lRow) 
		{
			cDatabase.delete(DATABASE_TABLE_SEMESTER, Se_id + "=" + lRow, null);
		}
		public void deleteEntryCourse(long lRow) 
		{
			cDatabase.delete(DATABASE_TABLE_COURSES, C_id + "=" + lRow, null);
		}
		public void deleteEntryHomeworks(long lRow) 
		{
			cDatabase.delete(DATABASE_TABLE_HOMEWORK, H_id + "=" + lRow, null);
		}
		public void deleteEntrySchedule(long lRow) 
		{
			cDatabase.delete(DATABASE_TABLE_SCHEDULE, S_id + "=" + lRow, null);
		}
		public void deleteEntryPolicy(long lRow) 
		{
			cDatabase.delete(DATABASE_TABLE_POLICY, P_id + "=" + lRow, null);
		}
		public void deleteEntryGrades(long lRow) 
		{
			cDatabase.delete(DATABASE_TABLE_GRADES, Gr_id + "=" + lRow, null);
		}
		public void deleteEntryUser(long lRow) 
		{
			cDatabase.delete(DATABASE_TABLE_USER, U_id + "=" + lRow, null);
		}
		
		
		
		//--------------------------------Updates-----------------------------------------//
		public long updateCourse(long course_id,String course_num, String course_name, String course_lec, String course_room, int course_credits, String grades)
		{
			ContentValues cv = new ContentValues();
			cv.put(C_Number, course_num);
			cv.put(C_Name, course_name);
			cv.put(C_Lecture, course_lec);
			cv.put(C_Room, course_room);
			cv.put(C_Credits, course_credits);
			cv.put(C_Grades, grades);
			return cDatabase.update(DATABASE_TABLE_COURSES, cv, "C_id=" + course_id, null);
		}
		
		
		
}
