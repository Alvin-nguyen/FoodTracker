package com.alvinnguyen.foodtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class ViewItemActivity extends Activity {
	
	EditText name, calories, protein, fat, carbs, fiber, sugar, calcium, 
				iron, sodium, vit_c, vit_a, chol, date, dateLabel;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_item);
		
		initAllFields();
		populateFields();
		
	}
	
	public void initAllFields() {
		name = (EditText) findViewById(R.id.view_item_name);
		calories = (EditText) findViewById(R.id.view_item_calories);
		protein = (EditText) findViewById(R.id.view_item_protein);
		fat = (EditText) findViewById(R.id.view_item_fat);
		carbs = (EditText) findViewById(R.id.view_item_carbs);
		fiber = (EditText) findViewById(R.id.view_item_fiber);
		sugar = (EditText) findViewById(R.id.view_item_sugar);
		calcium = (EditText) findViewById(R.id.view_item_calcium);
		iron = (EditText) findViewById(R.id.view_item_iron);
		sodium = (EditText) findViewById(R.id.view_item_sodium);
		vit_c = (EditText) findViewById(R.id.view_item_vitC);
		vit_a = (EditText) findViewById(R.id.view_item_vitA);
		chol = (EditText) findViewById(R.id.view_item_chol);
		date = (EditText) findViewById(R.id.view_item_date);
		dateLabel = (EditText) findViewById(R.id.view_item_date_label);
	}
	
	
	public void populateFields() {
		Intent intent = this.getIntent();
		boolean isPantry = intent.getBooleanExtra("isPantry", false);
		
		name.setText(intent.getStringExtra("food_item"));
		calories.setText(Integer.toString(intent.getIntExtra("calories", 0)));
		protein.setText(Double.toString(intent.getDoubleExtra("protein", 0)));
		fat.setText(Double.toString(intent.getDoubleExtra("fat", 0)));
		carbs.setText(Double.toString(intent.getDoubleExtra("carbs",0)));
		fiber.setText(Double.toString(intent.getDoubleExtra("fiber",0)));
		sugar.setText(Double.toString(intent.getDoubleExtra("sugar", 0)));
		calcium.setText(Integer.toString(intent.getIntExtra("calcium", 0)));
		iron.setText(Double.toString(intent.getDoubleExtra("iron", 0)));
		sodium.setText(Double.toString(intent.getIntExtra("sodium", 0)));
		vit_c.setText(Double.toString(intent.getDoubleExtra("vit_c", 0)));
		vit_a.setText(Integer.toString(intent.getIntExtra("vit_a", 0)));
		chol.setText(Integer.toString(intent.getIntExtra("chol", 0)));
		if(isPantry) {
			dateLabel.setVisibility(View.VISIBLE);
			date.setVisibility(View.VISIBLE);
			date.setText(intent.getStringExtra("add_date"));
		} else {
			dateLabel.setVisibility(View.GONE);
			date.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_item, menu);
		return true;
	}
	

}
