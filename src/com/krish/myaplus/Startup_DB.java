package com.krish.myaplus;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Startup_DB{

	
	//-------------Tables--------------------------
	/* Table : Courses */
	public static final String C_id = "C_id";
	public static final String C_Number = "C_Number";
	public static final String C_Name  = "C_Name";
	public static final String C_Lecture  = "C_Lecture";
	public static final String C_Semester  = "C_Semester";
	public static final String C_Room  = "C_Room";
	public static final String C_Credits  = "C_Credits";
	public static final String C_Grades  = "C_Grades";
	
	/* Table : Semester */
	public static final String Se_id = "Se_id";
	public static final String Se_Name = "Se_Name";
	public static final String Se_Tpoints = "Se_tpoints";
	public static final String Se_Tcredits = "Se_tcredits";
	public static final String Se_GPA = "Se_gpa";
	
	/* Table : Schedule */
	public static final String S_id = "S_id";
	public static final String S_Days = "S_Days";
	public static final String S_Time = "S_Time";
	
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
	
	/* Table : Policy */
	public static final String P_id = "P_id";
	public static final String P_name = "P_name";
	public static final String P_weight = "P_weight";
	
	/* Table : Grades */
	public static final String Gr_id = "Gr_id";
	public static final String Gr_points = "Gr_points";
	
	/* Table : User */
	public static final String U_id = "U_id";
	public static final String U_password = "U_password";
	public static final String U_passSet = "U_passSet";
	public static final String U_OGPA = "U_OGPA";
	public static final String U_CSem = "U_CSem";
	public static final String U_Logedin = "U_Logedin";
	
	private static final String DATABASE_NAME = "my_a_plus_db";
	
	// Main tables
	private static final String DATABASE_TABLE_COURSES = "courses";
	private static final String DATABASE_TABLE_SEMESTER = "semester";
	private static final String DATABASE_TABLE_SCHEDULE = "schedule";
	private static final String DATABASE_TABLE_HOMEWORK = "homework";
	private static final String DATABASE_TABLE_GRADESS = "gradess";
	private static final String DATABASE_TABLE_GPA = "gpa";
	private static final String DATABASE_TABLE_POLICY = "policy";
	private static final String DATABASE_TABLE_USER = "user";
	
	//Relations table
	private static final String DATABASE_TABLE_HAS = "hass";
	private static final String DATABASE_TABLE_SET = "sset";
	private static final String DATABASE_TABLE_INCLUDE = "iinclude";
	private static final String DATABASE_TABLE_LIST = "llist";
	private static final String DATABASE_TABLE_TPACKAGE = "_package";
	private static final String DATABASE_TABLE_STORE = "_store";
	private static final String DATABASE_TABLE_SAVE = "_save";
	private static final String DATABASE_TABLE_INSERT = "_insert";
	
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper userHelper;
	private final Context userContext;
	private SQLiteDatabase userDatabase;
	
	
	private static class DbHelper extends SQLiteOpenHelper
	{
		public DbHelper(Context context) {
			super(context, DATABASE_NAME,null,DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			//DDL Schema
			//Database schema for Table Courses
			
			
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_COURSES + "("  +
					C_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					C_Number + " TEXT, " +
					C_Name + " TEXT, " +
					C_Lecture + " TEXT, " +
					C_Semester + " TEXT, " +
					C_Room + " TEXT, " + 
					C_Credits + " INTEGER, " + 
					C_Grades + " TEXT " + ")"
		    );
			
			//Database schema for table semester
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_SEMESTER + "("  +
					Se_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					Se_Name + " TEXT, "  + 
					Se_Tpoints + " DOUBLE, "  +
					Se_Tcredits + " DOUBLE, "  + 
					Se_GPA + " DOUBLE "  + ")"
		    );
			
			//Database schema for table has ( semester -> courses )
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_HAS + "("  +
					Se_id + " INTEGER, " +
					C_id + " INTEGER, " + 
					"PRIMARY KEY("+Se_id+","+ C_id+")" +
					"FOREIGN KEY("+Se_id+") REFERENCES "+ DATABASE_TABLE_SEMESTER + " (" +Se_id+ ")" +
					"FOREIGN KEY("+C_id+") REFERENCES "+ DATABASE_TABLE_COURSES +" (" +C_id+ ")" +")"
		    );
			
			//Database schema for table Schedule
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_SCHEDULE + "("  +
					S_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					S_Days + " TEXT, " +
					S_Time + " TEXT "  +")"
		    );
			
			//Database schema for table set ( Courses -> Schedule )
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_SET + "("  +
					S_id + " INTEGER, " +
					C_id + " INTEGER, " + 
					"PRIMARY KEY("+S_id+","+ C_id+")" +
					"FOREIGN KEY("+S_id+") REFERENCES "+ DATABASE_TABLE_SCHEDULE + " (" +S_id+ ")" + 
					"FOREIGN KEY("+C_id+") REFERENCES "+ DATABASE_TABLE_COURSES + " (" +C_id+ ")" + ")"
		    );
			
			//Database schema for table HomeWorks
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_HOMEWORK + "("  +
					H_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					H_Category + " TEXT, " +
					H_Description + " TEXT, " +
					H_isComplete + " TEXT, " +
					H_isReminder + " TEXT, " +
					H_prioritylevel + " TEXT, " + 
					H_dueTime + " TEXT, " + 
					H_dueDate + " TEXT " + ")"
		    );
			
			
			//Database schema for table include ( Courses -> Homeworks )
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_INCLUDE + "("  +
					C_id + " INTEGER, " +
					H_id + " INTEGER, " + 
					"PRIMARY KEY("+C_id+","+ H_id+")" +
					"FOREIGN KEY("+H_id+") REFERENCES "+ DATABASE_TABLE_HOMEWORK + " (" +H_id+ ")" + 
					"FOREIGN KEY("+C_id+") REFERENCES "+ DATABASE_TABLE_COURSES + " (" +C_id+ ")" + ")"
		    );
			
			//Database schema for table GPA
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_GPA + "("  +
					G_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
					G_GPA + " TEXT " + ")"
		    );
			
			//Database schema for table Policy
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_POLICY + "("  +
					P_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					P_name + " TEXT, " + 
					P_weight + " INTEGER " + ")"
		    );
			
			//Database schema for table list ( Courses -> Policy )
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_LIST + "("  +
					C_id + " INTEGER, " +
					P_id + " INTEGER, " + 
					"PRIMARY KEY("+C_id+","+ P_id+")" +
					"FOREIGN KEY("+P_id+") REFERENCES "+ DATABASE_TABLE_POLICY + " (" +P_id+ ")" + 
					"FOREIGN KEY("+C_id+") REFERENCES "+ DATABASE_TABLE_COURSES + " (" +C_id+ ")" + ")"
		    );
			
			
			//Database schema for package ( Policy -> homework )
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_TPACKAGE + "("  +
					C_id + " INTEGER, " +
					P_id + " INTEGER, " + 
					H_id + " INTEGER, " + 
					"PRIMARY KEY("+C_id+","+ P_id+","+ H_id + ")" +
					"FOREIGN KEY("+P_id+") REFERENCES "+ DATABASE_TABLE_POLICY + " (" +P_id+ ")" + 
					"FOREIGN KEY("+C_id+") REFERENCES "+ DATABASE_TABLE_COURSES + " (" +C_id+ ")"  +
					"FOREIGN KEY("+H_id+") REFERENCES "+ DATABASE_TABLE_HOMEWORK + " (" +H_id+ ")" + ")" 
				    );
			
			
			//Database schema for store ( Courses -> GPA )
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_STORE + "("  +
					C_id + " INTEGER, " +
					G_id + " INTEGER, " + 
					"PRIMARY KEY("+C_id+","+ G_id+")" +
					"FOREIGN KEY("+G_id+") REFERENCES "+ DATABASE_TABLE_GPA + " (" +G_id+ ")" + 
					"FOREIGN KEY("+C_id+") REFERENCES "+ DATABASE_TABLE_COURSES + " (" +C_id+ ")" + ")"
		    );
			
			//Database schema for table Grades
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_GRADESS + "("  +
					Gr_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					Gr_points + " DOUBLE " + ")"
		    );
		
			
			//Database schema for save ( Grades -> Homeworks )
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_SAVE + "("  +
					H_id + " INTEGER, " +
					P_id + " INTEGER, " + 
					Gr_id + " INTEGER, " + 
					"PRIMARY KEY("+H_id+","+ P_id+","+ Gr_id+" )" +
					"FOREIGN KEY("+H_id+") REFERENCES "+ DATABASE_TABLE_HOMEWORK + " (" +H_id+ ")" + 
					"FOREIGN KEY("+P_id+") REFERENCES "+ DATABASE_TABLE_POLICY + " (" +P_id+ ")" + 
					"FOREIGN KEY("+Gr_id+") REFERENCES "+ DATABASE_TABLE_GRADESS + " (" +Gr_id+ ")" + ")"
		    );
			
			db.execSQL("CREATE TABLE "+ DATABASE_TABLE_USER + "("  +
					U_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					U_password + " TEXT, " +
					U_OGPA + " DOUBLE, " +
					U_CSem + " TEXT, " + 
					U_Logedin + " TEXT, " +  
					U_passSet + " TEXT " + ")"
		    );
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE_COURSES);
			onCreate(db);
			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE_SEMESTER);
			onCreate(db);
			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE_SCHEDULE);
			onCreate(db);
			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE_HOMEWORK);
			onCreate(db);
			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE_GRADESS);
			onCreate(db);
			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE_GPA);
			onCreate(db);
			
			
		}
	}
		
		public Startup_DB(Context c)
		{
			userContext = c;
		}
		
		public Startup_DB open() throws SQLException
		{
			userHelper = new DbHelper(userContext);
			userDatabase = userHelper.getWritableDatabase();
			return this;
		}
		
		public void close()
		{
			userHelper.close();
		}
}
