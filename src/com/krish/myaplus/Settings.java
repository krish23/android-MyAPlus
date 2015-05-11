package com.krish.myaplus;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.DatePicker;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.app.Service;
import android.app.AlarmManager;
import java.util.Calendar;
import android.app.PendingIntent;

public class Settings extends Activity 
{
	//Notification
	ToggleButton notify_button;
	Spinner reminder;
	Button notify_ok;
	private ArrayAdapter<String> listAdapter;
	ArrayList<String> rowList = new ArrayList<String>();
	String dueDate;
	String dueTime;
	
	//Alarm manager
	private PendingIntent pendingIntent;
	ArrayList<String> rowList2 = new ArrayList<String>();
	int size;
	
	//Password protection
	EditText oldPassword;
	EditText newPassword;
	EditText confirm;
	TextView old_pass_lbl;
	CheckBox pass_check;
	String check_password;
	Button btn_reset;
	Button btn_pass_save;
	Button update_csm;
	
	 String newpassword; 
     String oldpassword;
     String confirmpassword;
     
     //Current semester
     Spinner semester_list;
     ArrayList<String> rowList_sem = new ArrayList<String>();
	 ArrayList<String> rowList2_sem = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		final Dialog d = new Dialog(this);
        final TextView tv = new TextView(this);
		
		notify_button = (ToggleButton) findViewById(R.id.tgl_notify);
		pass_check = (CheckBox) findViewById(R.id.chk_pass_en);
		btn_reset = (Button) findViewById(R.id.btn_pass_reset);
		semester_list = (Spinner) findViewById(R.id.spn_current_semester);
		update_csm = (Button) findViewById(R.id.btn_csm_update);
		
		//Check the database for password
		course_dbconfig chk_pass = new course_dbconfig(Settings.this);
		chk_pass.open();
		check_password = chk_pass.getUserIsPassword();
		
		//get the semesters
		int j = chk_pass.getsizeSemester();
		rowList2_sem.addAll(chk_pass.getrowidSemester());
		j=rowList2_sem.size();
		chk_pass.close();
		
		//All the information from database to the list
		for(int k=0;k<j;k++)
		{
			course_dbconfig getCat_info = new course_dbconfig(Settings.this);
        	getCat_info.open();
            rowList_sem.add(getCat_info.getSemsterName(Integer.parseInt(rowList2_sem.get(k))));
            getCat_info.close();
		}
		
		//Add from list to the adapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Settings.this,android.R.layout.simple_spinner_item,rowList_sem);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		semester_list.setAdapter(adapter);
		
		if(check_password.equals("no"))
		{
			pass_check.setChecked(false);
		}
		else
		{ 
			pass_check.setChecked(true); 
			btn_reset.setVisibility(View.VISIBLE);
			
		}
		
		pass_check.setOnClickListener(new OnClickListener() {
			
		@Override
		public void onClick(View v) 
		{
		
			if(pass_check.isChecked())
			{
				//Show the create password dialog
				//set up dialog
		        final Dialog dialog = new Dialog(Settings.this);
		        dialog.setContentView(R.layout.create_password);
		        dialog.setTitle("Create Password");
		        dialog.setCancelable(true);
		        
		        btn_pass_save = (Button) dialog.findViewById(R.id.btn_pass_save);
		        oldPassword = (EditText) dialog.findViewById(R.id.txt_pass_chk);
		        newPassword = (EditText) dialog.findViewById(R.id.txt_pass_new);
		        confirm = (EditText) dialog.findViewById(R.id.txt_pass_confirm);

		        if(check_password.equals("no"))
				{}
				else
				{
					oldPassword.setVisibility(View.VISIBLE); 
					old_pass_lbl.setVisibility(View.VISIBLE);
				}
		        
		        btn_pass_save.setOnClickListener(new OnClickListener() 
				{
					@Override
		            public void onClick(View v) 
		            {
						course_dbconfig store_pass = new course_dbconfig(Settings.this);
						
				        if(check_password.equals("no"))
						{
				        	//Store the password
				        	
				        	
				        	store_pass.open();
				        	
				        	newpassword = newPassword.getText().toString();
				        	confirmpassword = confirm.getText().toString();
				        	
				        	if(newpassword.equals(confirmpassword.toString()))
				        	{
				        		store_pass.updatePassword(newpassword);
				        		store_pass.updateISPassword("yes");
				        	}
				        	else
				        	{
				        		//Show dialog box that password doesnt macth
				        		d.setTitle("User Authentication");
		        				tv.setText("Password doesn't match!");
		        				d.setContentView(tv);
		        				d.show();
				        	}
				        	
				        	
				        	dialog.dismiss();
						}
						else
						{
							//update the password
							//check the old password with the database
							oldpassword = oldPassword.getText().toString();
							if(oldpassword.equals(newpassword))
							{
								//password matched, update that in the database
								store_pass.updatePassword(newpassword);
				        		store_pass.updateISPassword("yes");
							}
							else
							{
								//password doesn't match show the dialog box
								//Show dialog box that password doesnt macth
				        		d.setTitle("User Authentication");
		        				tv.setText("Password doesn't match!");
		        				d.setContentView(tv);
		        				d.show();
							}
							
						}
				        store_pass.close();
		            }
				});
		        
		        dialog.show();
			}
			else
			{
				//Remove the password protection
				course_dbconfig store_pass = new course_dbconfig(Settings.this);
				store_pass.open();
				store_pass.updateISPassword("no");
				store_pass.close();
			}
			
		  }
		});
		
		//Current semester button update
		
		update_csm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				String current_semester = semester_list.getSelectedItem().toString();
				//Update the semster with current selection
				course_dbconfig update_db = new course_dbconfig(Settings.this);
				update_db.open();
				update_db.updatecurrentSemester(current_semester);
				update_db.close();
				
				Toast.makeText(getApplicationContext(),
						"Current Semester updated to " + current_semester,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		
		notify_button.setOnClickListener(new OnClickListener() 
		{
			    @Override
                public void onClick(View v) 
                {
			    	if((notify_button.isChecked()))
			        {
			    		//01. Get the data that mentioned as reminder
			    		//02. put that to the array and send to the alarm manager

			    		final course_dbconfig cCat_count = new course_dbconfig(Settings.this);
			    		cCat_count.open();
			    		rowList2.addAll(cCat_count.getrowidHomeworks_REM());
			    	    size = rowList2.size();
			    		
			    		//Show the dialog box
			    		
			    		//set up dialog
				        Dialog dialog = new Dialog(Settings.this);
				        dialog.setContentView(R.layout.notification);
				        dialog.setTitle("Set Reminder");
				        dialog.setCancelable(true);
				        
				        reminder = (Spinner) dialog.findViewById(R.id.spn_notify_days);
				        notify_ok = (Button)  dialog.findViewById(R.id.btn_notify_ok);
			    		
			    		rowList.add("1 day before");
			    		rowList.add("2 days before");
			    		rowList.add("4 days before");
			    		
			    		ArrayAdapter<String> adapter_hm = new ArrayAdapter<String>(Settings.this,android.R.layout.simple_spinner_item,rowList);
			    		adapter_hm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			    		reminder.setAdapter(adapter_hm);
			    		
			    		notify_ok.setOnClickListener(new OnClickListener() 
			    		{
			    			    @Override
			                    public void onClick(View v) 
			                    {
			    			    	String homeworkId;
			    			    	for(int i=0;i<size;i++)
			    			    	{
			    			    		
			    			    		dueDate = cCat_count.get_homework_DueDate(Integer.parseInt(rowList2.get(i)));
			    			    		dueTime = cCat_count.get_homework_DueTime(Integer.parseInt(rowList2.get(i)));
			    			    	
			    			    		//Parse the date
			    			    		
			    			    		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd");
			    			    		Date date = new Date();
			    			    		try {
											date = format.parse(dueDate);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
			    			    		
			    			    		Calendar c = Calendar.getInstance();
			    			    		c.setTime(date);
			    			    		int year = c.get(Calendar.YEAR);
			    			    		int month = c.get(Calendar.MONTH);
			    			    		int day = c.get(Calendar.DAY_OF_MONTH);
			    			    		
			    			    		//parse the time
			    			    		
			    			    		SimpleDateFormat format2 = new SimpleDateFormat("h:mma");
			    			    		Date time = new Date();
			    			    		try {
											time = format.parse(dueTime);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
			    			    		
			    			    		//Define the date formats
			    			    		Calendar c2 = Calendar.getInstance();
			    			    		c2.setTime(time);
			    			    		int hour = c2.get(Calendar.HOUR);
			    			    		int minute = c2.get(Calendar.MINUTE);
			    			    		int AmorPm = c2.get(Calendar.AM_PM);
			    			    		int interval=0;
			    			    		
			    			    		String item = reminder.getSelectedItem().toString();

			    			    		//Select the interal
			    			    		if(item.equals("1 day before"))
			    			    		{
			    			    			interval = 1;
			    			    		}
			    			    		else if(item.equals("2 day before"))
			    			    		{
			    			    			interval = 2;
			    			    		}
			    			    		else
			    			    		{
			    			    			interval = 4;
			    			    		}
			    			    		
				    			    	//Trigger alarm manager
				    			        Intent myIntent = new Intent(Settings.this, AlarmService.class){
				    			        };
				    			        homeworkId = rowList2.get(i);
				    			        myIntent.putExtra("current_hwid", homeworkId);
				    			        
				    			        //Set the intent
				    			    	pendingIntent = PendingIntent.getService(Settings.this, i, myIntent, 0);
				    			    	AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
				    			    	Calendar calendar = Calendar.getInstance();
				    			        calendar.setTimeInMillis(System.currentTimeMillis());
				    			        
				    			        calendar.set(Calendar.YEAR, year);
				    			        calendar.set(Calendar.MONTH, month);
				    			        calendar.set(Calendar.DAY_OF_MONTH, day-interval);
				    			        calendar.set(Calendar.HOUR_OF_DAY, hour);
				    			        calendar.set(Calendar.MINUTE, minute);
				    			        calendar.set(Calendar.SECOND, 0);
				    			        calendar.set(Calendar.MILLISECOND, 0);
				    			        calendar.set(Calendar.AM_PM, AmorPm); 
				    			        
				    			        //Start the alarm manager
				    			        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
			    			    	}
			    			    	Toast.makeText(Settings.this, "Reminder is set for Selected courses", Toast.LENGTH_LONG).show();
			    			    }
			    		});
			    		
			    		dialog.show();
			        }
			        else
			        {

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
}
