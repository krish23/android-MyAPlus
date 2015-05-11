package com.krish.myaplus;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

public class Password extends Activity {

	EditText password_txt;
	Button pass_ok;
	String password;
	String check_password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);
		
		final Dialog d = new Dialog(this);
        final TextView tv = new TextView(this);
	
		password_txt = (EditText)findViewById(R.id.txt_pass_chk);
		pass_ok = (Button)findViewById(R.id.btn_pass_ok);
		
		pass_ok.setOnClickListener(new OnClickListener() 
		{
			    @Override
                public void onClick(View v) 
                {
			    	//Check the password from the database and prompt
			    	course_dbconfig chk_pass = new course_dbconfig(Password.this);
			    	chk_pass.open();
			    	check_password = chk_pass.getUserPassword();
			    	password = password_txt.getText().toString();
			    	
			    	if(check_password.equals(password.toString()))
			    	{
			    		//Password is ok , redirect
			    		Intent goToNextActivity = new Intent(getApplicationContext(), Main_Menu.class);
			    		startActivity(goToNextActivity);
			    		finish();
			    	}
			    	else
			    	{
			    		//wrong password show dialogbox
			    		d.setTitle("User Authentication");
        				tv.setText("Wrong Password!");
        				d.setContentView(tv);
        				d.show();
			    	}
                }
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
