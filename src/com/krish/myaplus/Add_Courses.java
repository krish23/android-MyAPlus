/*
 * My A Plus
 * Programmed By Krishanthan Krishanmoorthy
 * 
 * Add_Courses.java will add the course to the database
 * 
 * */


package com.krish.myaplus;

import java.util.ArrayList;
import java.util.Arrays;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Add_Courses extends Activity 
{
	//Labels
	TextView lbl_addcourses;
	TextView lbl_semester;
	TextView lbl_courseno;
	TextView lbl_coursename;
	TextView lbl_lecturername;
	TextView lbl_courseType;
	TextView lbl_room;
	
	//EditText editor
	EditText txt_courseNo;
	EditText txt_courseName;
	EditText txt_lecture;
    EditText txt_room; 	
    EditText txt_pol_cat;
    EditText txt_pol_perc;
    EditText txt_num_credits;
    
    //Spinner
    Spinner spn_semester;
    
    //Strings
    String semester;
	String courseNo;
	String courseName;
	String lecture;
	String courseType;
    String room;  
    String num_of_credits; 
    String days;
    String timeFrom;
    String timeTo;
    String time;
	
	
	Button addPolicy; 
	Button button_add;
	Button btn_addCourse;
	Button addSchdule;
	
	//List resources
	 String[] Policies = new String[] {};
	 long lposition;
	 int num_of_policy=0;
	 public int current_semester_id;
	 
	 
	private ListView viewPolicyList;
	ArrayList<String> PolicyList = new ArrayList<String>();
	private ArrayAdapter<String> listAdapter;
	private String selectedItem;
	static final private int CHOOSE_OK = 0;
	static final private int CHOOSE_EDIT = 1;
	
	//Array list to store the rows
	ArrayList<String> rowList = new ArrayList<String>();
	ArrayList<String> catList = new ArrayList<String>();
	ArrayList<String> percList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_courses);
		
		Intent intent = getIntent();
		current_semester_id = Integer.parseInt(intent.getExtras().getString("current_semester_id"));
		
		//Declarations
		
		lbl_addcourses = (TextView)findViewById(R.id.lbl_addcourses);
		lbl_courseno = (TextView)findViewById(R.id.lbl_courseno);
		lbl_coursename = (TextView)findViewById(R.id.lbl_coursename);
		lbl_lecturername = (TextView)findViewById(R.id.lbl_lecname);
		lbl_courseType = (TextView)findViewById(R.id.lbl_coursetype);
		lbl_room = (TextView)findViewById(R.id.lbl_room);

		//Setup fonts to the labels

		txt_courseNo = (EditText)findViewById(R.id.txt_courseno);
		txt_courseName = (EditText)findViewById(R.id.txt_hm_desc);
		txt_lecture = (EditText) findViewById(R.id.txt_lec_name);
		txt_room = (EditText) findViewById(R.id.txt_room);
		txt_num_credits = (EditText)findViewById(R.id.txt_num_credits);
		
		addPolicy = (Button)findViewById(R.id.btn_policy);
		btn_addCourse = (Button)findViewById(R.id.btn_save);
		addSchdule = (Button)findViewById(R.id.btn_add_schedule);
		
		final Dialog d = new Dialog(this);
        final TextView tv = new TextView(this);
        days="null";
        
        //Setting up list controller
        lposition=1;
        
        PolicyList.addAll( Arrays.asList(Policies));
        listAdapter = new ArrayAdapter<String>(this, R.layout.display_list, PolicyList);
        
        
        /*
         * When user clicks the add policy button, it will trigger this function and display the dialog box
         * */
		addPolicy.setOnClickListener(new OnClickListener() 
		{
			    @Override
                public void onClick(View v) 
                {
			    	//set up dialog
			        Dialog dialog = new Dialog(Add_Courses.this);
			        dialog.setContentView(R.layout.course_policy);
			        dialog.setTitle("Course Policy");
			        dialog.setCancelable(true);
			        
			        viewPolicyList = (ListView) dialog.findViewById( R.id.lst_policies ); 
			        listAdapter.notifyDataSetChanged();
			        viewPolicyList.setAdapter( listAdapter );
   
			        button_add = (Button) dialog.findViewById(R.id.btn_policyadd);
			        
			        button_add.setOnClickListener(new OnClickListener() 
					{
						    @Override
			                public void onClick(View v) 
			                {
			            	    //Show the create policy
				            	final Dialog dialog = new Dialog(Add_Courses.this);
				                dialog.setContentView(R.layout.add_policy);
				                dialog.setTitle("Create Course Policy");
				                dialog.setCancelable(true);

						        Button btnPolOk = (Button)dialog.findViewById(R.id.btn_polok);
						        
						        btnPolOk.setOnClickListener(new OnClickListener() 
								{
									@Override
									public void onClick(View v) {
										
										 //Get the information from the dialogbox, and store into db
						                
						                txt_pol_cat = (EditText)dialog.findViewById(R.id.txt_polCat);
								        txt_pol_perc = (EditText)dialog.findViewById(R.id.txt_polPerc);
								        
								        String policy_cat = txt_pol_cat.getText().toString();
								        String policy_percent = txt_pol_perc.getText().toString();
								        
								        //Add information to the temporary array list
								        
								        catList.add(policy_cat);
								        percList.add(policy_percent);
								        
								        //Update the list
								        listAdapter.add( catList.get(num_of_policy) + " - " +  policy_percent.toString() );
								        listAdapter.notifyDataSetChanged();
								        viewPolicyList.setAdapter( listAdapter );
								        
								        num_of_policy++; //update number of policy
								        dialog.dismiss();
									}
								});
				                dialog.show();
			                }
			         });			        
			        
			        dialog.show();
                }
         });
		
		/*
         * When user clicks the class time button, it will trigger this function and display the dialog box
         * */
		addSchdule.setOnClickListener(new OnClickListener() 
		{
			    @Override
                public void onClick(View v) 
                {
			    	//set up dialog
			        final Dialog dialog = new Dialog(Add_Courses.this);
			        dialog.setContentView(R.layout.course_time);
			        dialog.setTitle("Course Schedule");
			        dialog.setCancelable(true);
			        
			        //Get the dialogbox resources
			        //Get the checkboxes(days)
			        final CheckBox chk_mon = (CheckBox)dialog.findViewById(R.id.chk_mon);
			        final CheckBox chk_sun = (CheckBox)dialog.findViewById(R.id.chk_sun);
			        final CheckBox chk_tue = (CheckBox)dialog.findViewById(R.id.chk_tue);
			        final CheckBox chk_wed = (CheckBox)dialog.findViewById(R.id.chk_wed);
			        final CheckBox chk_thu = (CheckBox)dialog.findViewById(R.id.chk_thu);
			        final CheckBox chk_fri = (CheckBox)dialog.findViewById(R.id.chk_fri);
			        final CheckBox chk_sat = (CheckBox)dialog.findViewById(R.id.chk_sat);
			        
			        final EditText txt_timeFrom = (EditText)dialog.findViewById(R.id.txt_hm_desc);
			        final EditText txt_timeTo = (EditText)dialog.findViewById(R.id.txt_polPerc);
			        
			        Button btnTimeOk = (Button)dialog.findViewById(R.id.btn_timeok);
			        
			        btnTimeOk.setOnClickListener(new OnClickListener() 
					{
						    @SuppressLint("NewApi")
							@Override
			                public void onClick(View v) 
			                {
						    	//After user enters all the information in the dialog box
						    	//add the days, and add to database
						    	if(chk_mon.isChecked())
						    	{
						    		if(!days.equals("null"))
						    		{
						    			days = days + "," + "Mon";
						    		}
						    		else
						    		{
						    			days = "Mon";
						    		}
						    	}
						    	if(chk_tue.isChecked())
						    	{
						    		if(!days.equals("null"))
						    		{
						    			days = days + "," + "Tue";
						    		}
						    		else
						    		{
						    			days = "Tue";
						    		}
						    	}
						    	if(chk_wed.isChecked())
						    	{
						    		if(!days.equals("null"))
						    		{
						    			days = days + "," + "Wed";
						    		}
						    		else
						    		{
						    			days = "Wed";
						    		}
						    	}
						    	if(chk_thu.isChecked())
						    	{
						    		if(!days.equals("null"))
						    		{
						    			days = days + "," + "Thu";
						    		}
						    		else
						    		{
						    			days = "Thu";
						    		}
						    	}
						    	if(chk_fri.isChecked())
						    	{
						    		if(!days.equals("null"))
						    		{
						    			days = days + "," + "Fri";
						    		}
						    		else
						    		{
						    			days = "Fri";
						    		}
						    	}
						    	if(chk_sat.isChecked())
						    	{
						    		if(!days.equals("null"))
						    		{
						    			days = days + "," + "Sat";
						    		}
						    		else
						    		{
						    			days = "Sat";
						    		}
						    	}
						    	if(chk_sun.isChecked())
						    	{
						    		if(!days.equals("null"))
						    		{
						    			days = days + "," + "Sun";
						    		}
						    		else
						    		{
						    			days = "Sun";
						    		}
						    	}
						    	
						    	//Add time (-)
						    	String courseTime = txt_timeFrom.getText().toString() + "-" + txt_timeTo.getText().toString();
						    	time = courseTime;
						    	dialog.dismiss();
			                }
					});
			        dialog.show();
                }
         });
		
		//Add course button implementation
		btn_addCourse.setOnClickListener(new OnClickListener() 
		{
			    @Override
                public void onClick(View v) 
                {
			    	boolean dbStored = false;
			    	try
	        		{
			    		courseNo = txt_courseNo.getText().toString();
			    		courseName = txt_courseName.getText().toString();
			    		lecture = txt_lecture.getText().toString();
			    		num_of_credits = txt_num_credits.getText().toString();
			    	    room = txt_room.getText().toString();
			    	    
			    	    //Introducing the Database class
			    	    course_dbconfig db_entry = new course_dbconfig(Add_Courses.this);
			    	    
			    	    //Open the Database
		        		db_entry.open();
		        		
		        		//Implementation
		        		
		        		//Add information to the course entry
		        		db_entry.createEntry(courseNo.toString(), courseName.toString(), lecture.toString(),"2013", room.toString(), num_of_credits.toString(),"x");
		        		
		        		//Add information to the schedule entry
		        		db_entry.createScheduleEntry(days, time);
		        		
		        		//Add information to the relational table
		        		db_entry.getLastIDCourses();
		        		db_entry.getLastIDSchedule();
		        		db_entry.create_COURSE_SCHDU_Entry();
		        		
		        		//Add the course info to the has table ( Semester -> Course)
		        		db_entry.create_COURSE_SEMESTER_Entry(current_semester_id);
		        		

		        		//Now add the course policies to the database
		        		
		        		for(int k=0;k<catList.size();k++)
		        		{
			        		//Implementation, insert into the policy 
					        db_entry.createEntryPolicy(catList.get(k), percList.get(k));
			                
					        //Insert into relational table (list)
					        db_entry.getLastIDCourses();
					        db_entry.getLastIDPolicy();
					        db_entry.create_COURSE_POLICY_Entry();
		        		}
		        		
		        		db_entry.close();
		        		dbStored = true;
		        		
	        		}
			    	catch(Exception e) 
	        		{
	        			dbStored = false;
	        		}
			    	finally
			    	{
			    		if(dbStored)
			    		{
			    			//Now redirect to the main course activity, and update the list
			    			Intent saved = new Intent(Add_Courses.this, My_Courses.class);
			    			saved.putExtra("course_name",  courseName.toString());
	        				setResult(RESULT_OK,saved);
	        				finish();
			    		
			    		}
			    		else
			    		{
			    			d.setTitle("ERRRRROOOOORRR");
	        				tv.setText("DB connection success!");
	        				d.setContentView(tv);
	        				d.show();
			    		}
			    	}
                }
		});
	}
	
	// Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.course_menu, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) 
    	{
    	case R.id.mnu_course_edit:
    	//Show the create policy
		    	Dialog dialog_menu = new Dialog(Add_Courses.this);
		        dialog_menu.setContentView(R.layout.course_menu_item);
		        dialog_menu.setTitle("Customize your course");
		        dialog_menu.setCancelable(true);
		        dialog_menu.show();
    	return true;
        default:
        return super.onOptionsItemSelected(item);
        }
    }
}
