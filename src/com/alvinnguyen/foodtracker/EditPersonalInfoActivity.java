package com.alvinnguyen.foodtracker;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.annotation.TargetApi;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.alvinnguyen.foodtracker.dialog.DatePickerFragment;


public class EditPersonalInfoActivity extends FragmentActivity {

	EditText name, age, birthday, weight;
	Spinner sex, height_feet, height_inches, activity;
	
	List<String> heightFeetSelections;
	List<String> heightInchesSelections;
	List<String> activitySelections;
	
	String saved_name, saved_birthday, saved_height_feet; 
	String saved_height_inches, saved_sex, saved_activity, birthday_string;
	int saved_age, saved_weight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_personal_info);
		// Show the Up button in the action bar.
		setupActionBar();
		
		initAllFields();
		getSelections();
		readPreviousInfo();
		addListenerForBirthdayChooser();
		setPreviousInfo();

	}

	public void initAllFields() {
		if((sex = (Spinner) findViewById(R.id.edit_sex_spinner))==null) System.out.println("s null");
		if((name = (EditText) findViewById(R.id.edit_edit_name))==null) System.out.println("name null");
		if((age = (EditText) findViewById(R.id.edit_edit_age))==null) System.out.println("age null");
		if((weight = (EditText) findViewById(R.id.edit_edit_weight))==null) System.out.println("w null");
		if((height_feet = (Spinner) findViewById(R.id.edit_edit_height_feet))==null) System.out.println("hf null");
		if((height_inches = (Spinner) findViewById(R.id.edit_edit_height_inches))==null) System.out.println("hi null");
		if((activity = (Spinner) findViewById(R.id.edit_edit_activity))==null) System.out.println("a null");
		if((birthday = (EditText) findViewById(R.id.edit_choose_birthday))==null) System.out.println("bday null");
		
	}
	
	public void getSelections() {
		activitySelections = (List<String>) Arrays.asList(getResources().getStringArray(R.array.activity_array));
		heightFeetSelections = (List<String>) Arrays.asList(getResources().getStringArray(R.array.height_feet_array));
		heightInchesSelections = (List<String>) Arrays.asList(getResources().getStringArray(R.array.height_inches_array));
	}
	
	public void readPreviousInfo() {
		SharedPreferences personalInfo = getSharedPreferences("PersonalInfoPreferences", MODE_PRIVATE);

		saved_name = personalInfo.getString("PERSONALINFO_NAME_STRING", "Not Found");
		saved_age = personalInfo.getInt("PERSONALINFO_AGE_INT", 0);
		saved_weight = personalInfo.getInt("PERSONALINFO_WEIGHT_INT", 0);
		saved_birthday = personalInfo.getString("PERSONALINFO_BIRTHDAY_STRING", "Not Found");
		saved_height_feet = personalInfo.getString("PERSONALINFO_HEIGHTFEET_STRING", "Not Found");
		saved_height_inches = personalInfo.getString("PERSONALINFO_HEIGHTINCHES_STRING", "Not Found");
		saved_activity = personalInfo.getString("PERSONALINFO_ACTIVITY_STRING", "Not Found");
		saved_sex = personalInfo.getString("PERSONALINFO_SEX_STRING", "Not Found");
		birthday_string = saved_birthday;
	}
	
	public void setPreviousInfo() {
		name.setText(saved_name);
		age.setText(""+saved_age);
		weight.setText(""+saved_weight);
		birthday.setText(saved_birthday);
		height_feet.setSelection(heightFeetSelections.indexOf(saved_height_feet));
		height_inches.setSelection(heightInchesSelections.indexOf(saved_height_inches));
		activity.setSelection(activitySelections.indexOf(saved_activity));
		if(saved_sex.equals("Male")) sex.setSelection(0); else sex.setSelection(1);
	}
	
	/*Listener for the Choose Birthday Button */
	private void addListenerForBirthdayChooser() {   
		birthday.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
		    showDatePicker();
		   }
		  });
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
			  
			  birthday_string = new String(monthOfYear+1+"/"+dayOfMonth+"/"+year);
			  birthday.setText(birthday_string);
		  }
	 };
	 
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_personal_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void savePersonalInfo(View view) {
		int nonPrimativeAge = Integer.parseInt(age.getText().toString());
		int nonPrimativeWeight = Integer.parseInt(weight.getText().toString());
		
		SharedPreferences personalInfo = getSharedPreferences("PersonalInfoPreferences", MODE_PRIVATE);
		personalInfo.edit().putString("PERSONALINFO_NAME_STRING", name.getText().toString()).commit();
		personalInfo.edit().putInt("PERSONALINFO_AGE_INT", nonPrimativeAge).commit();
		personalInfo.edit().putInt("PERSONALINFO_WEIGHT_INT", nonPrimativeWeight).commit();
		personalInfo.edit().putString("PERSONALINFO_BIRTHDAY_STRING", birthday_string).commit();
		personalInfo.edit().putString("PERSONALINFO_SEX_STRING", sex.getSelectedItem().toString()).commit();
		personalInfo.edit().putString("PERSONALINFO_HEIGHTFEET_STRING", height_feet.getSelectedItem().toString()).commit();
		personalInfo.edit().putString("PERSONALINFO_HEIGHTINCHES_STRING", height_inches.getSelectedItem().toString()).commit();
		personalInfo.edit().putString("PERSONALINFO_ACTIVITY_STRING", activity.getSelectedItem().toString()).commit();
		
		finish();
	}

}
