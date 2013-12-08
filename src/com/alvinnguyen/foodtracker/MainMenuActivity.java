package com.alvinnguyen.foodtracker;

import com.alvinnguyen.foodtracker.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
	    if (firstrun){
			Intent intent = new Intent(this, StartActivity.class);
		    startActivity(intent);
		    // Save the state
		    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
		        .edit()
		        .putBoolean("firstrun", false)
		        .commit();
	    } else {
	    	setContentView(R.layout.activity_main_menu);
	    }

	    setContentView(R.layout.activity_main_menu);
	}
	
	/*Do not go back to Welcome screen, prompt for exit instead.*/
	@Override
	public void onBackPressed() {
		doExit();
		
	}
	
	private void doExit() {

	    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

	    alertDialog.setPositiveButton("Yes", new OnClickListener() {

	        public void onClick(DialogInterface dialog, int which) {
	            finish();
	        }
	    });

	    alertDialog.setNegativeButton("No", null);

	    alertDialog.setMessage("Do you want to exit?");
	    alertDialog.setTitle("FoodTracker");
	    alertDialog.show();
	}
	
	public void viewPersonalInfo(View view) {
		Intent intent = new Intent(this, PersonalInfoActivity.class);
	    startActivity(intent);
	}

	public void viewPantry(View view) {
		Intent intent = new Intent(this, PantryActivity.class);
		startActivity(intent);
	}
	
	public void viewJournal(View view) {
		Intent intent = new Intent(this, JournalActivity.class);
		startActivity(intent);
		
	}
	
	public void viewAnalysis(View view) {
		Intent intent = new Intent(this, AnalysisActivity.class);
		startActivity(intent);
	}
	
	public void viewRecommender(View view) {
		Intent intent = new Intent(this, RecommenderActivity.class);
		startActivity(intent);
	}
	
}
