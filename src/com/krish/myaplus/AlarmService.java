package com.krish.myaplus;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
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

public class AlarmService extends Service 
{
	  private NotificationManager mNotificationManager;
	  private int YOURAPP_NOTIFICATION_ID=1;
	  String homework_id;
	
	  //All the homework infos
	  String homework_name;
	  String homework_cat;
	  String homework_course;
	  
	@Override
	public void onCreate()
	{
		Toast.makeText(this, "AlarmService.onCreate()", Toast.LENGTH_LONG).show();
	}
	
	@Override

	public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
		
	return null;
	}
	
	@Override

	public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	//Toast.makeText(this, "AlarmService.onDestroy()", Toast.LENGTH_LONG).show();
	}



	@Override

	public void onStart(Intent intent, int startId) {
	// TODO Auto-generated method stub
	super.onStart(intent, startId);
	
	//Get the homework id from the alarm manager
	homework_id = intent.getExtras().getString("current_hwid");
	
    //Get the homework information from the database using the home work id
	
	course_dbconfig hw_db = new course_dbconfig(AlarmService.this);
	hw_db.open();
	homework_name = hw_db.getHomeworkName_NC(Long.parseLong(homework_id));
	int courseId = hw_db.getCourseIDByHW(Integer.parseInt(homework_id));
	homework_course = hw_db.getCourseName(courseId);
	hw_db.close();
	
	//Set the notification
	// Get the notification manager service.
    mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    
    //Trigger the notification
    	this.showNotification();
	}
    
	public void showNotification()
	{
		//create and display a status Bar Notification
        int icon = R.drawable.study;
        CharSequence tickerText = homework_name + " - " + homework_course;
        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, tickerText, when);
        
        //Activate the sound and light when homework notification triggered.
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        
        //Show the notification
        Context context = getApplicationContext();
        CharSequence contentTitle = homework_name; 
        CharSequence contentText = homework_course;
        Intent notificationIntent = new Intent(this, AlarmService.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        mNotificationManager.notify(YOURAPP_NOTIFICATION_ID, notification);
	}


	@Override

	public boolean onUnbind(Intent intent) {
	// TODO Auto-generated method stub
	Toast.makeText(this, "AlarmService.onUnbind()", Toast.LENGTH_LONG).show();
	return super.onUnbind(intent);

	}
}
