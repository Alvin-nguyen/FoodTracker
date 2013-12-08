package com.alvinnguyen.foodtracker;

import java.util.Calendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.alvinnguyen.foodtracker.database.FoodDataSource;
import com.alvinnguyen.foodtracker.dialog.DatePickerFragment;


public class StartActivity extends FragmentActivity implements Runnable{

	private final static String DATABASE_FILE_NAME = "ModifiedDB.sql";
	
	private EditText choose_birthday;
	private Spinner sex_spinner,height_feet, height_inches, activity;
	private EditText name, age, weight;
	private String birthday;
	private FoodDataSource db;
	private static ProgressBar progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
	
		addListenerForBirthdayChooser();
		initAllFields();
		
		Thread databaseThread = new Thread(this);
		databaseThread.start();
	}

	/*Listener for the Choose Birthday Button */
	private void addListenerForBirthdayChooser() {   
		choose_birthday = (EditText) findViewById(R.id.choose_birthday);
		
		choose_birthday.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
		    showDatePicker();
		   }
		  });
	}
	
	
	public void initAllFields() {
			sex_spinner = (Spinner) findViewById(R.id.sex_spinner);
			name = (EditText) findViewById(R.id.start_edit_name);
			age = (EditText) findViewById(R.id.start_edit_age);
			weight = (EditText) findViewById(R.id.start_edit_weight);
			height_feet = (Spinner) findViewById(R.id.start_edit_height_feet);
			height_inches = (Spinner) findViewById(R.id.start_edit_height_inches);		
			activity = (Spinner) findViewById(R.id.start_edit_activity);
			progress = (ProgressBar) findViewById(R.id.progressBar1);
			db = new FoodDataSource(this);
			db.open();
	}
	
	public void prepareFoodDatabase() {
		db.AddAllItemsToDatabase(this, DATABASE_FILE_NAME);
	}
	
	private void showDatePicker() {
		  DatePickerFragment date = new DatePickerFragment();
		  /**
		   * Set Up Current Date Into dialog
		   */
		  Calendar calender = Calendar.getInstance();
		  Bundle args = new Bundle();
		  args.putInt("year", calender.get(Calendar.YEAR));
		  args.putInt("month", calender.get(Calendar.MONTH));
		  args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
		  date.setArguments(args);
		  /**
		   * Set Call back to capture selected date
		   */
		  date.setCallBack(ondate);
		  date.show(getSupportFragmentManager(), "Date Picker");
	}
	
	 
	 OnDateSetListener ondate = new OnDateSetListener() {
		  @Override
		  public void onDateSet(DatePicker view, int year, int monthOfYear,
		    int dayOfMonth) {
			  
			  EditText birthdayField = (EditText) findViewById(R.id.choose_birthday);
			  birthday = new String(monthOfYear+1+"/"+dayOfMonth+"/"+year);
			  birthdayField.setText(birthday);
		  }
	 };
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	
	/*Do nothing when back is pressed. user must enter personal information on first launch*/
	@Override
	public void onBackPressed() {
	}
	
	@Override
	public void onResume() {
		db.open();
		super.onResume();
	}
	
	@Override
	public void onPause() {
		db.close();
		super.onPause();
	}
	
	private Double getActivityMultiplier(String level) {
		
		if(level.equals("Sedentary"))
			return 1.2;
		else if(level.equals("Light"))
			return 1.375;
		else if(level.equals("Moderate"))
			return 1.55;
		else if(level.equals("High"))
			return 1.725;
		else
			return 1.9;
	}
	
	private void calculateCalories(int age, String sex, int height, int weight, String activity ) {
		int intCalories;
		double kgWeight = weight * .453592;
		Double multiplier;
		Double constants[] = new Double[4];
		
		multiplier = getActivityMultiplier(activity);
		if(sex.equals("Male")) {
			constants[0]= 66.0; constants[1] = 13.7; constants[2]= 5.0; constants[3]=6.8;
		}
		else {
			constants[0]=655.0; constants[1]=9.6; constants[2]=1.8; constants[3]= 4.7;
		}
		
		intCalories = (int) (constants[0] + (constants[1] *kgWeight) + (constants[2] * height) - (constants[3] * age));
		intCalories = (int) (intCalories * multiplier);
		
		getSharedPreferences("PersonalInfoPreferences", MODE_PRIVATE).edit().putInt("PERSONALINFO_CALORIES_INT", intCalories).commit();
	}
	
	private int calculateHeight(String stringHeight) {
		int feet, inches ;
		String [] split = stringHeight.split("\"|\'");
		feet = Integer.parseInt(split[0]);
		inches = Integer.parseInt(split[1]);
		
		feet = (int) (feet * 30.48);
		inches = (int) (inches * 2.54);
		
		return feet+inches;
	}
	
	public void savePersonalInfo(View view) {
		try {
			if(progress.getVisibility() == View.GONE) {

				int nonPrimativeAge = Integer.parseInt(age.getText().toString());
				int nonPrimativeWeight = Integer.parseInt(weight.getText().toString());
				String sex = sex_spinner.getSelectedItem().toString();
				String HeightF = height_feet.getSelectedItem().toString();
				String HeightI = height_inches.getSelectedItem().toString();
				String activityString = activity.getSelectedItem().toString();
				int intHeight = calculateHeight(HeightF+HeightI);
				
				
				SharedPreferences personalInfo = getSharedPreferences("PersonalInfoPreferences", MODE_PRIVATE);
				personalInfo.edit().putString("PERSONALINFO_NAME_STRING", name.getText().toString()).commit();
				personalInfo.edit().putInt("PERSONALINFO_AGE_INT", nonPrimativeAge).commit();
				personalInfo.edit().putInt("PERSONALINFO_WEIGHT_INT", nonPrimativeWeight).commit();
				personalInfo.edit().putString("PERSONALINFO_BIRTHDAY_STRING", birthday).commit();
				personalInfo.edit().putString("PERSONALINFO_SEX_STRING", sex).commit();
				personalInfo.edit().putString("PERSONALINFO_HEIGHTFEET_STRING", HeightF).commit();
				personalInfo.edit().putString("PERSONALINFO_HEIGHTINCHES_STRING", HeightI).commit();
				personalInfo.edit().putString("PERSONALINFO_ACTIVITY_STRING", activityString).commit();
				calculateCalories(nonPrimativeAge, sex, intHeight, nonPrimativeWeight, activityString);
				
				finish();
			} else {
				Toast.makeText(this, "Still Creating Database", Toast.LENGTH_LONG).show();
			}
			
		} catch(NumberFormatException e) {
			System.out.println("error parsing age or weight");
		}
	}

	@Override
	public void run() {
		prepareFoodDatabase();
		threadHandler.sendEmptyMessage(0);
	}
	
    // Receives Thread's messages, interprets them and acts on the
    // current Activity as needed
    private static Handler threadHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // whenever the Thread notifies this handler we have
            // only this behavior
        	progress.setVisibility(View.GONE);
        }
    };
	
}
