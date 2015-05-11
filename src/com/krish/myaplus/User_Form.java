package com.krish.myaplus;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class User_Form extends Activity 
{	
	EditText username;
	EditText password;
	String get_username;
	String get_password;
	
	Button login;
	String result = null;
    InputStream is = null;
    StringBuilder sb=null;
    String user_login;
    String user_password;
    
    //Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    
    // Progress Dialog
    private ProgressDialog pDialog;
    
    //url to get all products list
    private static String url_username = "http://and.tool-monks.us/user_login.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NAME = "fullname";
 
    // products JSONArray
    JSONArray users = null;
    
	@Override
	 public void onCreate(Bundle savedInstanceState) 
	{
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.user_form);
	     username = (EditText)findViewById(R.id.txt_username);
	     password = (EditText)findViewById(R.id.txt_password);
	     login = (Button)findViewById(R.id.btn_login);
	     
	   //Initialize the database
	  		Startup_DB db_entry = new Startup_DB(User_Form.this);
	  	    db_entry.open();
	  	    db_entry.close();
	     
	     course_dbconfig read_db = new course_dbconfig(User_Form.this);
	     read_db.open();
	     user_login = read_db.getUserLogin();
	     
	     //Check the password status, if user enabled password then
	     user_password = read_db.getUserIsPassword();
	    		 
	     if(user_password.equals("yes"))
	     {
	    	 //Show the password activity
	    	 Intent intent = new Intent(User_Form.this,Password.class);
			 startActivity(intent);
			 finish();
	     }
	     else{
	    	 
	     }
	    	 
	     if( user_login.equals("yes") && user_password.equals("no"))
	     {
	    	 read_db.close();
	    	//Redirect to the main menu
	    	Intent intent = new Intent(User_Form.this,Main_Menu.class);
			startActivity(intent);
			finish();
	     }
	     else
	     { 
	 		
	     }
	     read_db.close();
	     
	        // button click event
	        login.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View view) {
	            	new Thread(new doNetwork()).start();
	            }
	        });
	}
	
	 /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadUser extends AsyncTask<String, String, String> 
    {
    	 @Override
         protected void onPreExecute() 
    	 {
             super.onPreExecute();  
             pDialog = new ProgressDialog(User_Form.this);
             pDialog.setMessage("Creating Product..");
             pDialog.setIndeterminate(false);
             pDialog.setCancelable(true);
             pDialog.show();
    	 }
    	 
    	 protected String doInBackground(String... args) 
    	 {
    		 final Dialog d = new Dialog(User_Form.this);
    		 final TextView tv = new TextView(User_Form.this);
    		 
    		 get_username = username.getText().toString();
    		 get_password = password.getText().toString();
    		 
    		// Building Parameters
    		 List<NameValuePair> params = new ArrayList<NameValuePair>();
             params.add(new BasicNameValuePair("username", get_username));
             params.add(new BasicNameValuePair("password", get_password));
             
             JSONObject json = jParser.makeHttpRequest(url_username,
                     "POST", params);
             
          // check log cat fro response
             Log.d("Create Response", json.toString());
             
             //check for success tag
             try {
                 int success = json.getInt(TAG_SUCCESS);
  
                 if (success == 1) {
                	 
                	 //Create the database
                	//update the database
                 	
                	 
                	 
                     // successfully created product
                     Intent i = new Intent(getApplicationContext(), Main_Menu.class);
                     startActivity(i);
  
                     // closing this screen
                     finish();
                 } else {
                     // failed to create product
                	d.setTitle("FAILEDDDD");
     				tv.setText("ERROR");
     				d.setContentView(tv);
     				d.show();
                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
    		 
    		 return null;
    	 }
    	 
    	 /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }
    
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }

    };
    
    class doNetwork  implements Runnable {

     	public void run() {
     	     		
     	 get_username = username.getText().toString();
   		 get_password = password.getText().toString();
   		 
   		// Building Parameters
   		 List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", get_username));
            params.add(new BasicNameValuePair("password", get_password));
            
            JSONObject json = jParser.makeHttpRequest(url_username,
                    "POST", params);
            
            //check log cat for response
            //Log.d("Create Response", json.toString());
            
            //check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                	
                	course_dbconfig insert_db = new course_dbconfig(User_Form.this);
                 	insert_db.open();
                 	insert_db.createUser("-1", "0.0", "", "yes","no");
                 	insert_db.close();
                	
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), Main_Menu.class);
                    startActivity(i);
 
                    // closing this screen
                    finish();
                } 
                else {
                	 Log.d("USER NOT FOUND", json.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
     	}
     }
    
    
}
