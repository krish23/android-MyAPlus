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
import com.krish.myaplus.Main_Grades;
import com.krish.myaplus.R;
import com.krish.myaplus.course_dbconfig;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Main_Semester extends Activity 
{
	String[] Semester = new String[] {};
	private ListView viewSemesterList;
	ArrayList<String> SemesterList = new ArrayList<String>();
	private final Context context = this;
	private ArrayAdapter<String> semesterAdapater;
	ArrayList<String> rowList_sem = new ArrayList<String>();
	private String selectedItem;
	long listPosition;
	int Lposition;
	ArrayList<String> rowList = new ArrayList<String>();
	ArrayList<String> rowList_hws = new ArrayList<String>();
	
	static final private int MODE = 1;
	
	TextView showoverallGPA;
	int credits;
	int points;
	float totalPoints = 0;
	float totalCredits = 0;
	float gpaValue = 0;
	String semester_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_semester);
		listPosition=1;
		viewSemesterList = (ListView) findViewById( R.id.lst_semester ); 
		SemesterList.addAll( Arrays.asList(Semester) ); 
		semesterAdapater = new ArrayAdapter<String>(this, R.layout.display_list, SemesterList);
		
		//get the size
	    course_dbconfig semester_count = new course_dbconfig(Main_Semester.this);
	    semester_count.open();
		int i = semester_count.getsizeSemester();
		rowList.addAll(semester_count.getrowidSemester());
		
		String count23="";
		showoverallGPA = (TextView)findViewById( R.id.out_overalgpa ); 
		
		//Get all the semester name and put into the adapater
		for(int j = 0; j<=i-1;j++)
        {
            	count23=count23+rowList.get(j);
            	course_dbconfig db_count1 = new course_dbconfig(this);
                db_count1.open();
                semesterAdapater.add(db_count1.getSemsterName((Integer.parseInt(rowList.get(j)))));
                db_count1.close();
        }
		
		
		
		//Get the Overall GPA 
		for(int k=0;k<rowList.size();k++)
		{
			credits = semester_count.getSemsterCredits(Integer.parseInt(rowList.get(k)));
			points = semester_count.getSemsterTotalPoints(Integer.parseInt(rowList.get(k)));
			
			totalPoints = totalPoints + points;
			totalCredits = totalCredits + credits;
		}
		
		//calculate gpa
		
		try
		{
			gpaValue = totalPoints / totalCredits;
			if(gpaValue >=0)
			{
				showoverallGPA.setText(String.format("%.3f", gpaValue));
			}
		}
		
		catch(Exception e){}
		
		semester_count.close();
		semesterAdapater.notifyDataSetChanged();
	
		OnItemClickListener itemListener = new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View arg1, int position,long arg3)
			{
				Lposition = position;
				listPosition=Integer.parseInt(rowList.get(position));
				semester_name = semesterAdapater.getItem(Lposition);
				
				//Position is activated
				
			}
	    };
		
	    OnItemLongClickListener itemLongListener = new OnItemLongClickListener(){
	   		 public boolean onItemLongClick(AdapterView<?> parent, View arg1,final int position, long arg3) 
	   		 {
	   			 
	   			Lposition = position;
				listPosition=Integer.parseInt(rowList.get(position));
				semester_name = semesterAdapater.getItem(Lposition);
				
				//Dialog box that promt when user clicks the entry for a couple of seconds
				
	   			AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Do you really want to remove " + semester_name + "?");
				builder.setCancelable(false);
				builder.setPositiveButton("yes", new DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) 
					{
						//Remove the entry from the list
						
						semesterAdapater.remove(semester_name);
						semesterAdapater.notifyDataSetChanged();
						rowList.remove(position);
						
						//Refresh the list
						viewSemesterList.invalidate();
						
						//Delete the entry from the database
						course_dbconfig del = new course_dbconfig(Main_Semester.this);
						del.open();
						del.deleteEntrySemester(listPosition);
						rowList_sem.addAll( del.getrowidCourse((int)listPosition) );
						int c_size = rowList_sem.size();
						
						//Also, remove all the courses and homeworks
						for(int h=0;h<c_size;h++)
						{
							del.deleteEntryCourse(Integer.parseInt(rowList_sem.get(h)));
							
							rowList_hws.addAll( del.getrowidHomeworksByCID(Integer.parseInt(rowList_sem.get(h))) );
							int h_size = rowList_hws.size();
							
							for(int l=0;l<h_size;l++)
							{
								del.deleteEntryHomeworks(Long.parseLong(rowList_hws.get(l)));
							}
						}
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
	
	    viewSemesterList.setAdapter( semesterAdapater );
	    viewSemesterList.setOnItemClickListener(itemListener);
	    viewSemesterList.setOnItemLongClickListener(itemLongListener);	
	}

	public void addSemester(View v)
	{
		Intent goToNextActivity = new Intent(getApplicationContext(), Add_Semester.class);
		startActivity(goToNextActivity);
		finish();
	}
	
	public void viewCourse(View v)
	{
		//Once user click, it redirects to the assign grade activity with selected homework id
		Intent semester_ = new Intent(Main_Semester.this, com.krish.myaplus.My_Courses.class) {
		};
		course_dbconfig db_entry = new course_dbconfig(Main_Semester.this);
		db_entry.open();
		semester_.putExtra("current_semester_id", Long.toString(listPosition));
		db_entry.close();
		startActivityForResult(semester_, MODE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.semester_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
    	{
    	case R.id.mnu_semester_edit:
    	//Show the create policy
		    	final Dialog dialog_menu = new Dialog(Main_Semester.this);
		        dialog_menu.setContentView(R.layout.semester_menu_item);
		        dialog_menu.setTitle("Edit Semester Name");
		        dialog_menu.setCancelable(true);
		        
		        final EditText edit_semester = (EditText) dialog_menu.findViewById( R.id.txt_edit_semester ); 
		        Button update = (Button) dialog_menu.findViewById( R.id.btn_semester_update ); 
		        edit_semester.setText(semester_name.toString());
		        
		        update.setOnClickListener(new OnClickListener() 
				{
					    @Override
		                public void onClick(View v) 
		                {
					    	try
					    	{
					    		course_dbconfig update_db = new course_dbconfig(Main_Semester.this);
					    		update_db.open();
					    		update_db.updateSemesterName(listPosition, edit_semester.getText().toString());
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
	
}
