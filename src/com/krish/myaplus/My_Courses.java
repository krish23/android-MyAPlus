package com.krish.myaplus;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import java.text.DecimalFormat;

public class My_Courses extends Activity 
{
	
	String[] Courses = new String[] {};
	private ListView viewCourseList;
	ArrayList<String> CourseList = new ArrayList<String>();
	private final Context context = this;
	private ArrayAdapter<String> courseAdapater;
	private String selectedItem;
	long listPosition;
	ArrayList<String> rowList = new ArrayList<String>();
	ArrayList<String> rowList_hws = new ArrayList<String>();
	public int current_semester_id;
	static final private int MODE = 1;
	TextView showGpa;
	
	String letterGrade;
	int credits;
	int points;
	int gradePoints;
	int Lposition;
	
	float totalPoints = 0;
	float totalCredits = 0;
	float gpaValue = 0;
	String showGrade;
	String course;
	String grade;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_courses);
		
		Intent intent = getIntent();
		current_semester_id = Integer.parseInt(intent.getExtras().getString("current_semester_id"));
		
		viewCourseList = (ListView) findViewById( R.id.lst_courses ); 
		CourseList.addAll( Arrays.asList(Courses) ); 
		courseAdapater = new ArrayAdapter<String>(this, R.layout.display_list, CourseList);
		showGpa = (TextView)findViewById(R.id.out_course_gpa);
		
		//get the size
	    course_dbconfig course_count = new course_dbconfig(My_Courses.this);
	    course_count.open();
		int i = course_count.getsizeCourse(current_semester_id);
		rowList.addAll(course_count.getrowidCourse(current_semester_id));
		
		//Every time the list loads, calculate the GPA and get the total points and total credits and store in the 
	    //corresponding semester
		
		for(int k=0;k<rowList.size();k++)
		{
			letterGrade = course_count.getLetterGrade( Integer.parseInt(rowList.get(k) ) );
			credits = course_count.getNumOfCredits( Integer.parseInt(rowList.get(k) ) );
			
			if(!letterGrade.equals("x"))
			{
				if(letterGrade.equals("A"))
				{
					gradePoints = 4;
				}
				else if(letterGrade.equals("B"))
				{
					gradePoints = 3;
				}
				else if(letterGrade.equals("C"))
				{
					gradePoints = 2;
				}
				else if(letterGrade.equals("D"))
				{
					gradePoints = 1;
				}
				else
				{
					gradePoints = 0;
				}
				
				points = credits * gradePoints;
				totalPoints = totalPoints + points;
				totalCredits = totalCredits + credits;
			}
		}
		
		//Add all information to the database
		
			try
			{
				gpaValue = totalPoints / totalCredits;
				if(gpaValue >= 0)
				{
					showGpa.setText(String.format("%.3f", gpaValue));
				}
				//update the total points, total credits and gpa
				course_count.updateSemester(current_semester_id, totalPoints, totalCredits, gpaValue);
			}
			catch(Exception e)
			{}

		course_count.close();
		String count23="";
		
		for(int j = 0; j<=i-1;j++)
        {
            count23=count23+rowList.get(j);
            course_dbconfig db_count1 = new course_dbconfig(this);
            db_count1.open();
            showGrade = db_count1.getLetterGrade((Integer.parseInt(rowList.get(j))));
            if(showGrade.equals("x"))
            {
            	courseAdapater.add(  db_count1.getCourseName((Integer.parseInt(rowList.get(j))))  );
            }
            else
            {
            	courseAdapater.add(db_count1.getCourseName((Integer.parseInt(rowList.get(j)))) + "    -  " +  showGrade );
            }
            
            db_count1.close();
        }
		
		courseAdapater.notifyDataSetChanged();
		
		OnItemClickListener itemListener = new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View arg1, int position,long arg3)
			{
				Lposition = position;
				listPosition=Integer.parseInt(rowList.get(position));
				course = courseAdapater.getItem(Lposition);
			}
		};
		
		OnItemLongClickListener itemLongListener = new OnItemLongClickListener(){
	   		 public boolean onItemLongClick(AdapterView<?> parent, View arg1,final int position, long arg3) 
	   		 {	 
	   			Lposition = position;
				listPosition=Integer.parseInt(rowList.get(position));
				course = courseAdapater.getItem(Lposition);
	   			 
				//Dialog box for delete record
	   			AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Do you really want to remove " + course + "?");
				builder.setCancelable(false);
				builder.setPositiveButton("yes", new DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						//Remove the record from the list
						courseAdapater.remove(course);
						courseAdapater.notifyDataSetChanged();
						rowList.remove(position);
						viewCourseList.invalidate();

						//Delete the record from the database
						course_dbconfig del = new course_dbconfig(My_Courses.this);
						del.open();
						del.deleteEntryCourse(listPosition);
						rowList_hws.addAll( del.getrowidHomeworksByCID(listPosition) );
						int c_size = rowList_hws.size();
						
						for(int l=0;l<c_size;l++)
						{
							del.deleteEntryHomeworks(Long.parseLong(rowList_hws.get(l)));
						}
						del.close();
					}
				});
				
					builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				builder.show();	
				
				
				return false;
	   		 }
		  };
	
	    viewCourseList.setAdapter( courseAdapater );
	    viewCourseList.setOnItemClickListener(itemListener);
	    viewCourseList.setOnItemLongClickListener(itemLongListener);
	    
	}
	
	public void addCourses(View v)
	{
		Intent courses_ = new Intent(My_Courses.this, com.krish.myaplus.Add_Courses.class) {
		};
		courses_.putExtra("current_semester_id", Integer.toString(current_semester_id));
		startActivityForResult(courses_, MODE);
		finish();
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
    	{
    	case R.id.mnu_course_edit:
    	//Show the create policy
		    	final Dialog dialog_menu = new Dialog(My_Courses.this);
		        dialog_menu.setContentView(R.layout.course_menu_item);
		        dialog_menu.setTitle("Edit Semester Name");
		        dialog_menu.setCancelable(true);
		        
		        
		        
		        final EditText edit_c_num = (EditText) dialog_menu.findViewById( R.id.edit_c_numb );
		        final EditText edit_c_lect = (EditText) dialog_menu.findViewById( R.id.edit_c_lect );
		        final EditText edit_c_name = (EditText) dialog_menu.findViewById( R.id.edit_c_name );
		        final EditText edit_c_numOfCredits = (EditText) dialog_menu.findViewById( R.id.edit_c_credits );
		        final EditText edit_c_room = (EditText) dialog_menu.findViewById( R.id.edit_c_room );
		        final EditText edit_c_grades = (EditText) dialog_menu.findViewById( R.id.edit_c_grades );
		        
		        String courseNumber = null;
		        final String lecturerName;
		        final String courseName;
		        final int numOfCredits;
		        final String room_no;
		        
	    		
	    		//Get the data to the edit text
		        course_dbconfig get_data = new course_dbconfig(My_Courses.this);
		        get_data.open();
		        courseNumber = get_data.getCourseNumber(listPosition);
		        lecturerName = get_data.getCourseLecturer(listPosition);
		        courseName = get_data.getCourseName(listPosition);
		        numOfCredits = get_data.getNumOfCredits(listPosition);
		        room_no = get_data.getCourseRoom(listPosition);
		        grade = get_data.getLetterGrade(listPosition);
		        
		        get_data.close();
		        
		        edit_c_num.setText(courseNumber);
		        edit_c_lect.setText(lecturerName);
		        edit_c_name.setText(courseName);
		        edit_c_numOfCredits.setText(Integer.toString(numOfCredits));
		        edit_c_room.setText(room_no);
		        
		        if(grade.equals("x")){ grade = "Not Available";}else{}
		        edit_c_grades.setText(grade);
		        
		         
		        Button update_course = (Button)dialog_menu.findViewById( R.id.btn_edit_update );
		        
		        update_course.setOnClickListener(new OnClickListener() 
				{
					    @Override
		                public void onClick(View v) 
		                {
					    	try
					    	{
					    		course_dbconfig update_db = new course_dbconfig(My_Courses.this);
					    		update_db.open();
					    		
					    		String get_courseNum = edit_c_num.getText().toString();
					    		String get_courseName = edit_c_name.getText().toString();
					    		String get_lect_name = edit_c_lect.getText().toString();
					    		String get_room = edit_c_room.getText().toString();
					    		int get_numOfCredits = Integer.parseInt(edit_c_numOfCredits.getText().toString());
					    		String getGrade = edit_c_grades.getText().toString();
					    		
					    		
					    		update_db.updateCourse(listPosition,get_courseNum,get_courseName ,get_lect_name ,get_room ,get_numOfCredits,getGrade);
					    		
					    		
					    		update_db.close();
					    		dialog_menu.dismiss();
					    	}
					    	catch(Exception e)
					    	{}
		                }
				});
		        
		        dialog_menu.show();
		        
    	return true;
        default:
        return super.onOptionsItemSelected(item);
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.course_menu, menu);
		return true;
	}
}
