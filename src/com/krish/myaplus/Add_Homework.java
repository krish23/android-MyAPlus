package com.krish.myaplus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.DatePicker;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Add_Homework extends Activity  
{
	   //EditText editor
		EditText txt_dueDate;
		EditText txt_dueTime;
		EditText txt_description;
		
		String description;
		String dueDate;
		String dueTime;
		String category;
		
		Button save_homeworks;
		
		public int year;
		public int month;
		public int day;
		public int selectedPolicyID;
		
		Spinner spn_homework_cat;
		Spinner spn_homework_course;
		
		private RadioGroup radioPriorityGroup;
		private RadioButton radioPriorityButton;
		Button addHomework;
		CheckBox reminder;
		String priority;
		int selectedCId=2;
		String current_semester;
		int current_semester_id;
		
		static final int DATE_DIALOG_ID = 999;
		
		ArrayList<String> rowList = new ArrayList<String>();
		ArrayList<String> rowList2 = new ArrayList<String>();
		
		ArrayList<String> rowList_hm = new ArrayList<String>();
		ArrayList<String> rowList2_hm = new ArrayList<String>();
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_homeworks);
		
		txt_dueDate = (EditText)findViewById(R.id.txt_hmdate);
		txt_dueTime = (EditText)findViewById(R.id.txt_hmtime);
		txt_description = (EditText)findViewById(R.id.txt_hm_desc);
		reminder = (CheckBox)findViewById(R.id.chk_hm_remind);
		addHomework = (Button)findViewById(R.id.btn_hm_add);
		spn_homework_cat = (Spinner)findViewById(R.id.spn_hmcat);
		spn_homework_course = (Spinner)findViewById(R.id.spn_hm_course);
		save_homeworks = (Button)findViewById(R.id.btn_hm_add);
		
		final course_dbconfig cCat_count = new course_dbconfig(this);
		cCat_count.open();
		
		final Dialog d = new Dialog(this);
	    final TextView tv = new TextView(this);
	    
	 
	    current_semester = cCat_count.getCurrentSemester();
	    
	    //Get the semester id
	    current_semester_id = cCat_count.getSemesterID(current_semester);
	    
	    //Adding course name to the spinner
	    int j = cCat_count.getsizeCourse(current_semester_id);
	    rowList2_hm.addAll(cCat_count.getrowidCourse(current_semester_id));
	    String count_course="";
	    j=rowList2_hm.size();
	    
	    for(int jj = 0; jj<=j-1;jj++)
        {
        	course_dbconfig getCat_info = new course_dbconfig(this);
        	getCat_info.open();
            rowList_hm.add(getCat_info.getCourseName(Integer.parseInt(rowList2_hm.get(jj))));
            getCat_info.close();
        }
		
		ArrayAdapter<String> adapter_hm = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,rowList_hm);
		adapter_hm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn_homework_course.setAdapter(adapter_hm);
		
		spn_homework_course.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	
		    	//reopen the database
		    	cCat_count.open();
		    	
		    	//Clear the spinner
		    	rowList.clear();
		    	rowList2.clear();
		    	
		    	//Activate the selected course item, and get the row id
				selectedCId = Integer.parseInt(rowList2_hm.get( (int) spn_homework_course.getSelectedItemId()));
				
				//Adding course policy to the spinner
		        int i = cCat_count.getsizePolicy(selectedCId);
		        
		        rowList2.addAll(cCat_count.getrowidPolicy(selectedCId));
		        String count23="";
		        i=rowList2.size();
		        cCat_count.close();
		        for(int jk = 0; jk<=i-1;jk++)
		        {
		        	//Add policies to the row list and to spinner
		        	course_dbconfig getCat_info = new course_dbconfig(Add_Homework.this);
		        	getCat_info.open();
		            rowList.add(getCat_info.getPolicyName(Integer.parseInt(rowList2.get(jk))));
		            getCat_info.close();
		            	
		        }
				
		        //Add the policies to the array adapter
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_Homework.this,android.R.layout.simple_spinner_item,rowList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spn_homework_cat.setAdapter(adapter);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    	return;
		    }

		});
		
		
		save_homeworks.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) 
			{
				
				boolean dbStored = false;
				String isReminder;
				
				//Get the id of selected course spinner
				selectedPolicyID = Integer.parseInt(rowList2.get( (int) spn_homework_cat.getSelectedItemId()));
				
				//Button event
				radioPriorityGroup = (RadioGroup) findViewById(R.id.rdo_hm_pr);
				int selectedId = radioPriorityGroup.getCheckedRadioButtonId();
				radioPriorityButton = (RadioButton) findViewById(selectedId);
				
				//Get the priority level
				if(radioPriorityButton.getText().equals("Low"))
				{
					priority = "low";
				}else if(radioPriorityButton.getText().equals("Medium"))
				{
					priority = "medium";
				}else if(radioPriorityButton.getText().equals("High"))
				{
					priority = "high";
				}
				
				if( reminder.isChecked())
				{
					isReminder = "yes";
				}else{isReminder ="no";}
				
				
				//Now add all the information to the database
				try
        		{
					category = spn_homework_cat.getSelectedItem().toString();
					description = txt_description.getText().toString();
					dueDate = txt_dueDate.getText().toString();
					dueTime = txt_dueTime.getText().toString();
					course_dbconfig db_entry = new course_dbconfig(Add_Homework.this);
	        		
	        		//Open the Database
	        		db_entry.open();
	        		
	        		//Implementation
	        		//Add information to the homeworks table
	        		db_entry.createEntryHomework(category.toString(),description.toString(), "no", isReminder.toString(), priority.toString(),dueDate.toString() ,dueTime.toString() );
	        		
	        		//Add information to the relational table
	        		db_entry.getLastIDHomework();
	        		db_entry.create_COURSE_HOMEWORK_Entry(selectedCId);
	        		db_entry.create_POLICY_HOMEWORK_Entry(selectedPolicyID,selectedCId);
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
        				// Database successfully stored
        				//Now redirect to the main category
        				Intent saved = new Intent(getApplicationContext(), Main_Homeworks.class);
        				saved.putExtra("data_stored", "true");
        				startActivity(saved);
        				finish();
        			}
        			else
        			{
        				//ERROR database didn't stored correctly
        				d.setTitle("ERRRRROOOOORRR");
        				tv.setText("The home works data didn't store correctly, please try again!");
        				d.setContentView(tv);
        				d.show();
        			}
        		}
			}
		});
		
		
		
		setDate();
		addListenerOnDate();
	}
	
	public void addListenerOnDate() 
	{
		txt_dueDate.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				showDialog(DATE_DIALOG_ID);
				return false;
			}	
	   });
	}
	
	public void setDate() 
	{
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListener, 
                         year, month,day);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener 
    = new DatePickerDialog.OnDateSetListener() {
	@Override
	public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
	
    txt_dueDate.clearFocus();
	year = selectedYear;
	month = selectedMonth;
	day = selectedDay;
	
	// set selected date into textview
	txt_dueDate.setText(new StringBuilder().append(year)
	   .append("-").append(month + 1).append("-").append(day)
	   .append(" "));
	}
  };
	
	
	
	// Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.course_menu, menu);
        return true;
    }
}
