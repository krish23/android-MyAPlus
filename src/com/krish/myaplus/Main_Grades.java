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

public class Main_Grades extends Activity 
{
	String[] Homeworks = new String[] {};
	private ListView viewHomeworkList;
	ArrayList<String> HomeworkList = new ArrayList<String>();
	private final Context context = this;
	private ArrayAdapter<String> homeworkAdapater;
	private String selectedItem;
	long listPosition;
	int Lposition;
	ArrayList<String> rowList = new ArrayList<String>();
	static final private int MODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_grades);
		listPosition=1;
		viewHomeworkList = (ListView) findViewById( R.id.lst_gr_hm ); 
		HomeworkList.addAll( Arrays.asList(Homeworks) ); 
		homeworkAdapater = new ArrayAdapter<String>(this, R.layout.display_list, HomeworkList);
		
		//get the size
		course_dbconfig homework_count = new course_dbconfig(Main_Grades.this);
		homework_count.open();
		int i = homework_count.getsizeHomeworks_C();
		rowList.addAll(homework_count.getrowidHomeworks_C());
		homework_count.close();
		String count23="";
		
		//Add all the undone homeworks to the adapter
		for(int j = 0; j<=i-1;j++)
        {
            	count23=count23+rowList.get(j);
            	course_dbconfig db_count1 = new course_dbconfig(this);
                db_count1.open();
                homeworkAdapater.add(db_count1.getHomeworkName_NC((Integer.parseInt(rowList.get(j)))));
                db_count1.close();
        }
		
		homeworkAdapater.notifyDataSetChanged();
	    
	    OnItemClickListener itemListener = new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View arg1, int position,long arg3)
			{
				Lposition = position;
				listPosition=Integer.parseInt(rowList.get(position));
				
				//Once user click, it redirects to the assign grade activity with selected homework id
				Intent assign_grade = new Intent(Main_Grades.this, com.krish.myaplus.Assign_Grades.class) {
				};
				course_dbconfig db_entry = new course_dbconfig(Main_Grades.this);
				db_entry.open();
				assign_grade.putExtra("current_hw_id", Long.toString(listPosition));
				db_entry.close();
				startActivityForResult(assign_grade, MODE);
			}
	    };
	    
	    OnItemLongClickListener itemLongListener = new OnItemLongClickListener(){

	   		 public boolean onItemLongClick(AdapterView<?> parent, View arg1,final int position, long arg3) 
	   		 {
				return false;
	   		 }
	    };
	    
	    viewHomeworkList.setAdapter( homeworkAdapater );
	    viewHomeworkList.setOnItemClickListener(itemListener);
        //viewBookList.setOnItemLongClickListener(itemLongListener);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
