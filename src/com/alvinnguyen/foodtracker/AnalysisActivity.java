package com.alvinnguyen.foodtracker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.alvinnguyen.foodtracker.database.JournalDataSource;
import com.alvinnguyen.foodtracker.dialog.DatePickerFragment;
import com.alvinnguyen.foodtracker.object.FoodItem;
import com.alvinnguyen.foodtracker.object.HealthInfo;

public class AnalysisActivity extends FragmentActivity {

	String date, sex;
	long time;
	EditText date_field;
	Button choose_date;
	TextView i_calories, i_fat, i_chol, i_sodium, i_carbs, i_fiber, i_sugar, i_protein, i_vita, i_vitc, i_calcium, i_iron;  
	TextView calories, fat, chol, sodium, carbs, fiber, sugar, protein, vita, vitc, calcium, iron; 
	TextView t_calories, t_fat, t_chol, t_sodium, t_carbs, t_fiber, t_sugar, t_protein, t_vita, t_vitc, t_calcium, t_iron; 
	int age, weight, cal;
	HealthInfo info;
	
	JournalDataSource datasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analysis);

		initAllFields();
		addListenerDateChooser();
		populateToday(date);
		populateTotal();
	}

	
	public void initAllFields() {
		date_field = (EditText) findViewById(R.id.analysis_date);
		choose_date = (Button) findViewById(R.id.analysis_choose_date);
		i_calories = (TextView) findViewById(R.id.analysis_calories_indicator);
		i_fat =  (TextView) findViewById(R.id.analysis_fat_indicator);
		i_chol = (TextView) findViewById(R.id.analysis_chol_indicator);
		i_sodium = (TextView) findViewById(R.id.analysis_sodium_indicator);
		i_carbs  = (TextView) findViewById(R.id.analysis_carbs_indicator);
		i_fiber = (TextView) findViewById(R.id.analysis_fiber_indicator);
		i_sugar =  (TextView) findViewById(R.id.analysis_sugar_indicator);
		i_protein = (TextView) findViewById(R.id.analysis_protein_indicator);
		i_vita = (TextView) findViewById(R.id.analysis_vita_indicator);
		i_vitc = (TextView) findViewById(R.id.analysis_vitc_indicator);
		i_calcium = (TextView) findViewById(R.id.analysis_calcium_indicator);
		i_iron = (TextView) findViewById(R.id.analysis_iron_indicator);
		
		calories = (TextView) findViewById(R.id.analysis_calories_today);
		fat =  (TextView) findViewById(R.id.analysis_fat_today);
		chol = (TextView) findViewById(R.id.analysis_chol_today);
		sodium = (TextView) findViewById(R.id.analysis_sodium_today);
		carbs  = (TextView) findViewById(R.id.analysis_carbs_today);
		fiber = (TextView) findViewById(R.id.analysis_fiber_today);
		sugar =  (TextView) findViewById(R.id.analysis_sugar_today);
		protein = (TextView) findViewById(R.id.analysis_protein_today);
		vita = (TextView) findViewById(R.id.analysis_vita_today);
		vitc = (TextView) findViewById(R.id.analysis_vitc_today);
		calcium = (TextView) findViewById(R.id.analysis_calcium_today);
		iron = (TextView) findViewById(R.id.analysis_iron_today);
		
		t_calories = (TextView) findViewById(R.id.analysis_calories_total);
		t_fat =  (TextView) findViewById(R.id.analysis_fat_total);
		t_chol = (TextView) findViewById(R.id.analysis_chol_total);
		t_sodium = (TextView) findViewById(R.id.analysis_sodium_total);
		t_carbs  = (TextView) findViewById(R.id.analysis_carbs_total);
		t_fiber = (TextView) findViewById(R.id.analysis_fiber_total);
		t_sugar =  (TextView) findViewById(R.id.analysis_sugar_total);
		t_protein = (TextView) findViewById(R.id.analysis_protein_total);
		t_vita = (TextView) findViewById(R.id.analysis_vita_total);
		t_vitc = (TextView) findViewById(R.id.analysis_vitc_total);
		t_calcium = (TextView) findViewById(R.id.analysis_calcium_total);
		t_iron = (TextView) findViewById(R.id.analysis_iron_total);
		
		datasource = new JournalDataSource(this);
		datasource.open();
		date = getTodaysDate();
		date_field.setText("Today");
		
		age = getSharedPreferences("PersonalInfoPreferences", MODE_PRIVATE).getInt("PERSONALINFO_AGE_INT", -1);
		sex = getSharedPreferences("PersonalInfoPreferences", MODE_PRIVATE).getString("PERSONALINFO_SEX_STRING", "");
		weight = getSharedPreferences("PersonalInfoPreferences", MODE_PRIVATE).getInt("PERSONALINFO_WEIGHT_INT", -1);
		cal = getSharedPreferences("PersonalInfoPreferences", MODE_PRIVATE).getInt("PERSONALINFO_CALORIES_INT", -1);
		info = new HealthInfo(age, sex, weight, cal);
	}
	
	public void populateToday(String date) {
		int today_calories=0,  today_chol=0, today_sodium=0, today_vita=0,today_calcium=0;
		Double today_fat=0.0, today_carbs=0.0, today_fiber=0.0, today_sugar=0.0, today_protein=0.0;
		Double today_vitc=0.0,  today_iron=0.0;
		
		FoodItem currentItem;
		
		ArrayList<FoodItem> items = (ArrayList<FoodItem>) datasource.getAllItemsAtDate(date);
		
		for(int i=0; i<items.size(); i++) {
			currentItem = items.get(i);
			today_calories += currentItem.getCalories();
			today_chol += currentItem.getChol();
			today_sodium += currentItem.getSodium();
			today_vita += currentItem.getVitA();
			today_calcium += currentItem.getCalcium();
			today_fat += currentItem.getFat();
			today_carbs += currentItem.getCarbs();
			today_fiber += currentItem.getFiber();
			today_sugar += currentItem.getSuger();
			today_protein += currentItem.getProtein();
			today_vitc += currentItem.getVitC();
			today_iron += currentItem.getIron();	
		}
		
		//Set Todays numbers
		setToday(calories, Double.valueOf(today_calories));
		setToday(chol, Double.valueOf(today_chol));
		setToday(sodium, Double.valueOf(today_sodium));
		setToday(vita, Double.valueOf(today_vita));
		setToday(calcium, Double.valueOf(today_calcium));
		setToday(fat, today_fat);
		setToday(carbs, today_carbs);
		setToday(fiber, today_fiber);
		setToday(sugar, today_sugar);
		setToday(protein, today_protein);
		setToday(vitc, today_vitc);
		setToday(iron, today_iron);
		
		//Set Todays indicators
		setIndicator(i_calories, info.getCaloriesIndicator(today_calories));
		setIndicator(i_chol, info.getCholIndicator(today_chol));
		setIndicator(i_sodium, info.getSodiumIndicator(today_sodium));
		setIndicator(i_vita, info.getVitAIndicator(today_vita));
		setIndicator(i_calcium, info.getCalciumIndicator(today_calcium));
		setIndicator(i_fat, info.getFatIndicator(today_fat));
		setIndicator(i_carbs, info.getCarbsIndicator(today_carbs));
		setIndicator(i_fiber, info.getFiberIndicator(today_fiber));
		setIndicator(i_sugar, info.getSugarIndicator(today_sugar));
		setIndicator(i_protein, info.getProteinIndicator(today_protein));
		setIndicator(i_vitc, info.getVitCIndicator(today_vitc));
		setIndicator(i_iron, info.getIronIndicator(today_iron));
	}
	
	public void setIndicator(TextView label, int color) {

		if(color == HealthInfo.GREEN)
			label.setBackgroundColor(Color.GREEN);
		else if(color==HealthInfo.YELLOW)
			label.setBackgroundColor(Color.YELLOW);
		else if(color==HealthInfo.RED)
			label.setBackgroundColor(Color.RED);
		else
			label.setBackgroundColor(Color.BLUE);
	}
	
	public void setToday(TextView label, Double value) {
		DecimalFormat nearestTenth = new DecimalFormat("##.#");
		label.setText(nearestTenth.format(value));
	}
	
	public void populateTotal() {
		t_calories.setText(""+info.getCalories());
		t_fat.setText(""+info.getRDAFat());
		t_chol.setText(""+info.getRDAChol());
		t_sodium.setText(""+info.getRDASodium());
		t_carbs.setText(""+info.getRDACarbs());
		t_fiber.setText(""+info.getRDAFiber());
		t_sugar.setText(""+info.getRDASugar());
		t_protein.setText(""+info.getRDAProtein());
		t_vita.setText(""+info.getRDAVitA());
		t_vitc.setText(""+info.getRDAVitC());
		t_calcium.setText(""+info.getRDACalcium());
		t_iron.setText(""+info.getRDAIron());
		
		
	}
	
	public String getTodaysDate() {
		
		Calendar c = Calendar.getInstance();
		time = c.getTimeInMillis();
		return c.get(Calendar.MONTH)+1+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.YEAR);
	}
	

	
	private void addListenerDateChooser() {   

		choose_date.setOnClickListener(new OnClickListener() {
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
		date.show(getSupportFragmentManager(), "Date");
	}


	OnDateSetListener ondate = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			date = new String(monthOfYear+1+"/"+dayOfMonth+"/"+year);
			Calendar c = Calendar.getInstance();
			GregorianCalendar storedDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
			time = storedDate.getTimeInMillis();
			
			if(c.get(Calendar.DAY_OF_MONTH)== dayOfMonth && 
					c.get(Calendar.MONTH)== monthOfYear && c.get(Calendar.YEAR)== year)
				date_field.setText("Today");
			else
				date_field.setText(date);
			
			populateToday(date);
			populateTotal();
		}
	};

	public void viewTrends(View view) {
		Intent intent = new Intent(this, AnalysisTrendsActivity.class);
		
		intent.putExtra("endDate", time);
		startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
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

}
