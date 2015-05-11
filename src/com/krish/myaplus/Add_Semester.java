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

public class Add_Semester extends Activity {

	EditText txt_add_semester;
	Button saveSemester;
	String add_semester;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_semester);
		
		txt_add_semester = (EditText)findViewById(R.id.txt_add_sem);
		saveSemester = (Button)findViewById(R.id.btn_se_add);
		final Dialog d = new Dialog(this);
	    final TextView tv = new TextView(this);
	    
	    saveSemester.setOnClickListener(new OnClickListener() 
	    {
			public void onClick(View arg0) 
			{
				boolean dbStored = false;
				try
				{
					add_semester = txt_add_semester.getText().toString();
					course_dbconfig db_entry = new course_dbconfig(Add_Semester.this);
					
					//Open the Database
	        		db_entry.open();
	        		
	        		//Implementation
	        		//Add information to the semester table
					db_entry.createSemester(add_semester,0,0,0.0);
					
					//Add the last semester to the current semester
					db_entry.updatecurrentSemester(add_semester);
					
					
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
        				Intent saved = new Intent(getApplicationContext(), Main_Semester.class);
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
	}
	
}
