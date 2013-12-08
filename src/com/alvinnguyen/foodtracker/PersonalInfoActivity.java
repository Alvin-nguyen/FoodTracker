package com.alvinnguyen.foodtracker;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class PersonalInfoActivity extends Activity {

	EditText name, age, birthday, sex, height, weight, activity, calories;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		setupActionBar();
		setContentView(R.layout.activity_personal_info);
		
		initAllFields();
		setPersonalInfo();
	}

	
	private void initAllFields() {
		if((name = (EditText) findViewById(R.id.edit_name)) == null) System.out.println("name null");
		if((age = (EditText) findViewById(R.id.edit_age)) == null) System.out.println("age null");
		if((birthday = (EditText) findViewById(R.id.edit_birthday)) == null)System.out.println("bd null");
		if((sex = (EditText) findViewById(R.id.edit_sex)) == null)System.out.println("s null");
		if((height = (EditText) findViewById(R.id.edit_height)) == null)System.out.println("h null");
		if((weight = (EditText) findViewById(R.id.edit_weight)) == null)System.out.println("w null");
		if((activity = (EditText) findViewById(R.id.edit_activitylevel)) == null)System.out.println("a null");
		if((calories = (EditText) findViewById(R.id.edit_calories)) == null)System.out.println("c null");
	}
	
	private void setPersonalInfo() {
		SharedPreferences personalInfo = getSharedPreferences("PersonalInfoPreferences", MODE_PRIVATE);
		
		String concatHeight = personalInfo.getString("PERSONALINFO_HEIGHTFEET_STRING", "0\'")
				+ personalInfo.getString("PERSONALINFO_HEIGHTINCHES_STRING", "0\"");
		String stringSex = personalInfo.getString("PERSONALINFO_SEX_STRING", "Not found");
		String stringActivity = personalInfo.getString("PERSONALINFO_ACTIVITY_STRING", "Not found");
		int intWeight = personalInfo.getInt("PERSONALINFO_WEIGHT_INT",0);
		int intAge = personalInfo.getInt("PERSONALINFO_AGE_INT", 0);
		int intHeight = calculateHeight(concatHeight);		
		
		name.setText(personalInfo.getString("PERSONALINFO_NAME_STRING", "Not found"));
		birthday.setText(personalInfo.getString("PERSONALINFO_BIRTHDAY_STRING", "Not found"));
		age.setText(""+intAge);
		weight.setText(""+intWeight);
		sex.setText(stringSex);
		height.setText(concatHeight);
		activity.setText(stringActivity);
		
		calculateCalories(intAge, stringSex, intHeight, intWeight, stringActivity);
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
		calories.setText(""+intCalories);
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
	protected void onResume() {
		setPersonalInfo();
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
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
	
	
	public void editPersonalInfo(View view) {

		Intent intent = new Intent(this, EditPersonalInfoActivity.class);
		
	    startActivity(intent);

	}

}
