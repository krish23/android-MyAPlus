package com.krish.myaplus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

public class Main_Menu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void openAddCourse(View v)
	{
		Intent goToNextActivity = new Intent(getApplicationContext(), Add_Courses.class);
		startActivity(goToNextActivity);
	}
	
	public void openAddHomework(View v)
	{
		Intent goToNextActivity = new Intent(getApplicationContext(), Main_Homeworks.class);
		startActivity(goToNextActivity);
	}
	
	public void openGrades(View v)
	{
		Intent goToNextActivity = new Intent(getApplicationContext(), Main_Grades.class);
		startActivity(goToNextActivity);
	}
	
	public void openSemester(View v)
	{
		Intent goToNextActivity = new Intent(getApplicationContext(), Main_Semester.class);
		startActivity(goToNextActivity);
	}
	
	public void openSettings(View v)
	{
		Intent goToNextActivity = new Intent(getApplicationContext(), Settings.class);
		startActivity(goToNextActivity);
	}	
}
