package com.krish.myaplus;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
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

public class Main_Homeworks extends Activity
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
	int courseId;
	Button btn_doneHomework;
	String homework;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_homeworks);
		listPosition=1;
		viewHomeworkList = (ListView) findViewById( R.id.lst_view_hm ); 
		HomeworkList.addAll( Arrays.asList(Homeworks) ); 
		homeworkAdapater = new ArrayAdapter<String>(this, R.layout.display_list, HomeworkList);
		btn_doneHomework = (Button) findViewById( R.id.btn_hm_done ); 
		btn_doneHomework.setVisibility(View.GONE);
		
		//get the size
		course_dbconfig homework_count = new course_dbconfig(Main_Homeworks.this);
		homework_count.open();
		int i = homework_count.getsizeHomeworks_NC();
	    rowList.addAll(homework_count.getrowidHomeworks_NC());
	    homework_count.close();
	    String count23="";
	    
	    //Get all the homeworks and put into the adapater
	    for(int j = 0; j<=i-1;j++)
        {
            	count23=count23+rowList.get(j);
            	course_dbconfig db_count1 = new course_dbconfig(this);
                db_count1.open();
                courseId = db_count1.getCourseIDByHW(Integer.parseInt(rowList.get(j)));
                homeworkAdapater.add( db_count1.getHomeworkName_NC((Integer.parseInt(rowList.get(j)))) + "     -   "  + db_count1.getCourseName(courseId) );
                db_count1.close();
        }
	    
	    homeworkAdapater.notifyDataSetChanged();
	    
	    OnItemClickListener itemListener = new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View arg1, int position,long arg3)
			{
				//Once the user clicks an entry, it will show the information in a toast
				btn_doneHomework.setVisibility(1);
				//get the position that user touched
				Lposition = position;
				listPosition=Integer.parseInt(rowList.get(position));
				course_dbconfig db_entry = new course_dbconfig(Main_Homeworks.this);
		        db_entry.open();
		        
		        //Showing the toast
				Toast.makeText(getApplicationContext(),
						"Category : " + db_entry.get_homework_category(listPosition)+ "\n" + "Priority : " +db_entry.get_homework_priority(listPosition) + "\n" + "Due Date : " +db_entry.get_homework_DueDate(listPosition) + "\n" + "Time : " +db_entry.get_homework_DueTime(listPosition),
						Toast.LENGTH_SHORT).show();
				db_entry.close();
				btn_doneHomework.setText(parent.getItemAtPosition(position).toString()+ " is done ");
			}
	    };
	    
	    //Setup the done button, so if user press done,
	    //Then remove the homework from the list, and mark that homework as done
	    
	    btn_doneHomework.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) 
			{
				//Mark that homework as done
				course_dbconfig update_db_entry = new course_dbconfig(Main_Homeworks.this);
				update_db_entry.open();
				update_db_entry.homeIsDone(listPosition);
			
				update_db_entry.close();
				
				Intent goToNextActivity = new Intent(getApplicationContext(), Main_Homeworks.class);
				startActivity(goToNextActivity);
				finish();
			}
		});
	    
	    OnItemLongClickListener itemLongListener = new OnItemLongClickListener(){

	   		 public boolean onItemLongClick(AdapterView<?> parent, View arg1,final int position, long arg3) 
	   		 {
	   			 //If user clicks the item for a couple of seconds, the delete dialog will prompt
	   			 
	   			Lposition = position;
				listPosition=Integer.parseInt(rowList.get(position));
				homework = homeworkAdapater.getItem(Lposition);
	   			 
				//Dialog box
	   			AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Do you really want to remove " + homework + "?");
				builder.setCancelable(false);
				builder.setPositiveButton("yes", new DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						//Remove the homework from the list
						homeworkAdapater.remove(homework);
						homeworkAdapater.notifyDataSetChanged();
						rowList.remove(position);
						
						//Refresh the list
						viewHomeworkList.invalidate();

						//Remove the homework from the database
						course_dbconfig del = new course_dbconfig(Main_Homeworks.this);
						del.open();
						del.deleteEntryHomeworks(listPosition);
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
	    
	    viewHomeworkList.setAdapter( homeworkAdapater );
	    viewHomeworkList.setOnItemClickListener(itemListener);
	    viewHomeworkList.setOnItemLongClickListener(itemLongListener);
	}
	
	public void addHomeworks(View v)
	{
		Intent goToNextActivity = new Intent(getApplicationContext(), Add_Homework.class);
		startActivity(goToNextActivity);
		finish();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
