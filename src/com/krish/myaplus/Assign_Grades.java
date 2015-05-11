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

public class Assign_Grades extends Activity  
{
	TextView out_course;
	TextView out_category;
	TextView out_description;
	EditText txt_grade_points;
	EditText txt_grade_outof;
	Button btn_grade_save;
	
	double points;
	double pointsOutof;
	double percPoints;
	
	ArrayList<String> policyList = new ArrayList<String>();
	ArrayList<String> gradeIdList = new ArrayList<String>();
	ArrayList<String> rowList_g = new ArrayList<String>();
	
	public int selectedHWId = 1;
	public int courseID;
	public int policyID;
	String courseName;
	String category;
	String descr;
	int num_of_credits;
	
	float totalPoints_g = 0;
	float totalCredits_g = 0;
	float gpaValue_g = 0;
	public int current_semester_id;
	String letterGrade;
	int credits_g;
	int points_g;
	int gradePoints;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assign_grade);
		
		//Get the homework id from main grades
		Intent intent = getIntent();
		selectedHWId = Integer.parseInt(intent.getExtras().getString("current_hw_id"));
		
		out_course = (TextView)findViewById(R.id.out_gr_course);
		out_category = (TextView)findViewById(R.id.out_gr_cat);
		out_description = (TextView)findViewById(R.id.out_gr_desc);
		txt_grade_points = (EditText)findViewById(R.id.txt_gr_points);
		txt_grade_outof = (EditText)findViewById(R.id.txt_gr_po);
		btn_grade_save = (Button)findViewById(R.id.btn_gr_save);
		final Dialog d = new Dialog(this);
        final TextView tv = new TextView(this);
        
        //Show information in the activity
        
        course_dbconfig getInfo = new course_dbconfig(Assign_Grades.this);
        getInfo.open();
        courseID = getInfo.getCourseIDByHW(selectedHWId);
        courseName = getInfo.getCourseName(courseID);
        category = getInfo.get_homework_category(selectedHWId);
        descr =  getInfo.getHomeWorkDesc(selectedHWId);
        out_course.setText(courseName);
        out_category.setText(category);
        out_description.setText(descr);
        
        //If grades are already assigned then, fill that in the textbox
        try
        {
        	points = getInfo.getPoints(getInfo.getGradeID(selectedHWId));
        	pointsOutof = 100;
        	txt_grade_points.setText( Double.toString(points)  );
        	txt_grade_outof.setText("100");
        
        }
        catch(Exception e)
        {}
        
        getInfo.close();
		btn_grade_save.setOnClickListener(new OnClickListener() 
		{
			    @Override
                public void onClick(View v) 
                {
			    	boolean dbStored = false;
			    	try
	        		{
			    		
			    		points = Double.parseDouble(txt_grade_points.getText().toString());
			    		pointsOutof = Double.parseDouble(txt_grade_outof.getText().toString());
			    		
			    		//Introducing the Database class
			    	    course_dbconfig db_entry = new course_dbconfig(Assign_Grades.this);
			    	    
			    	    //Open the Database
		        		db_entry.open();
		        		
		        		//convert to percentage
		        		percPoints = 100 * (points/pointsOutof);
		        		
		        		//Implementation
		        		//Add information to the course entry
			    		db_entry.createGrades(percPoints);
			    		
			    		//Add information to the relational table
			    		db_entry.getLastIDGrades();
			    		
			    		//Get the policy id by using homework id
			    		policyID = db_entry.getPolicyIdByHW(selectedHWId);

			    		db_entry.create_HOMEWORK_GRADES_Entry(selectedHWId,policyID);
			    		
			    		//Now update the GPA for the course and recalculate everything
			    		updateGPA();
			    		updateGrades();
			    		
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
	 
	/*
	 * Once user update the grade, the function will calculate the grades from the entered points
	 * 
	 * */
	public void updateGPA()
	{
		//Get the database information
		
		course_dbconfig db_gpa_update = new course_dbconfig(Assign_Grades.this);
		db_gpa_update.open();
		policyList.addAll(db_gpa_update.getrowidPolicy(courseID));
		int j;
		int policyId;
		int weight;
		String grade="";
		double points = 0;
		double average = 0;
		double totalPoints = 0;
		double totalWeight = 0;
		double totalGrandPoints = 0;
		
		//Get all the policies
		for(j=0;j<policyList.size();j++)
		{
			policyId = Integer.parseInt(policyList.get(j));
			weight = Integer.parseInt(db_gpa_update.getWeight(policyId));
			gradeIdList.addAll(db_gpa_update.getAllGradesID(policyId));
			
			//If the grade is already entered in the database, then calculate it
			if(gradeIdList.size() >=1)
			{
				for(int k=0;k < gradeIdList.size(); k++)
				{
					//Sum all the points
					points = points + db_gpa_update.getPoints(Integer.parseInt(gradeIdList.get(k)));
				}
				//Get the average of points from all the homeworks
				average = points / gradeIdList.size();
				
			    //Convert to the policy percentage
				totalPoints = totalPoints + ( (average * weight) / 100 ); 
				
				//Get total weight of policy
				totalWeight = totalWeight + weight;
			}	
			points = 0;
			gradeIdList.clear();
		}
		
		totalGrandPoints = (totalPoints * 100) / totalWeight;
				
		//calculate the Grade
		//In future the grading schema will be added, but considered to our university
		///grading policy A- 90 >, B -80 >, C - 70 >, D - 60 >, F < 60 
		
		if(totalGrandPoints >= 90)
		{
			grade="A";
		}
		else if(80 <= totalGrandPoints && totalGrandPoints < 90)
		{
			grade="B";
		}
		else if(70 <= totalGrandPoints && totalGrandPoints < 80)
		{
			grade="C";
		}
		else if(60 <= totalGrandPoints && totalGrandPoints < 70)
		{
			grade="D";
		}
		else
		{
			grade="F";
		}
		
		//Update grades for the course
		db_gpa_update.updateGrades(grade, courseID);
		db_gpa_update.close();	
	}
	
	public void updateGrades()
	{
		//----------------------------------------Calculate the grades-----------------------------------------
				//get the size
			    course_dbconfig course_count = new course_dbconfig(Assign_Grades.this);
			    course_count.open();
			    
			    current_semester_id = course_count.getSemsterIDByCID(courseID);
			    
				int ii = course_count.getsizeCourse(current_semester_id);
				rowList_g.addAll(course_count.getrowidCourse(current_semester_id));
				
				//Every time the list loads, calculate the GPA and get the total points and total credits and store in the 
			    //corresponding semester
				
				for(int k=0;k<rowList_g.size();k++)
				{
					letterGrade = course_count.getLetterGrade( Integer.parseInt(rowList_g.get(k) ) );
					credits_g = course_count.getNumOfCredits( Integer.parseInt(rowList_g.get(k) ) );
					
					if(!letterGrade.equals("x"))
					{
						if(letterGrade.equals("A"))
						{
							gradePoints = 4;
						}
						else if(letterGrade.equals("B"))
						{
							gradePoints = 3;
						}
						else if(letterGrade.equals("C"))
						{
							gradePoints = 2;
						}
						else if(letterGrade.equals("D"))
						{
							gradePoints = 1;
						}
						else
						{
							gradePoints = 0;
						}
						
						points_g = credits_g * gradePoints;
						totalPoints_g = totalPoints_g + points_g;
						totalCredits_g = totalCredits_g + credits_g;
					}
				}
				
				//Add all information to the database
				
					try
					{
						gpaValue_g = totalPoints_g / totalCredits_g;
						
						//update the total points, total credits and gpa
						course_count.updateSemester(current_semester_id, totalPoints_g, totalCredits_g, gpaValue_g);
					}
					catch(Exception e)
					{}
				
				
				//--------------------------------------------------------------------------------------------
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.course_menu, menu);
        return true;
    }
	
}
