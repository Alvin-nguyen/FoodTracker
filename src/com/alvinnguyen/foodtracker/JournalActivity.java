package com.alvinnguyen.foodtracker;

import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alvinnguyen.foodtracker.database.JournalDataSource;
import com.alvinnguyen.foodtracker.dialog.DatePickerFragment;
import com.alvinnguyen.foodtracker.object.FoodItem;

public class JournalActivity extends FragmentActivity {

	Button choose_date;
	ListView breakfast, lunch, dinner;
	EditText date_field;
	String date;
	JournalDataSource datasource;
	int listPressed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_journal);


		initAllFields();
		addListenerDateChooser();
		loadJournal(date);
	}


	public void initAllFields() {
		choose_date = (Button) findViewById(R.id.journal_choose_date);
		breakfast = (ListView) findViewById(R.id.journal_breakfast_list);
		lunch = (ListView) findViewById(R.id.journal_lunch_list);
		dinner = (ListView) findViewById(R.id.journal_dinner_list);
		date_field = (EditText) findViewById(R.id.journal_date);
		
		breakfast.setEmptyView(findViewById(R.id.journal_breakfast_empty));
		lunch.setEmptyView(findViewById(R.id.journal_lunch_empty));
		dinner.setEmptyView(findViewById(R.id.journal_dinner_empty));
		
		datasource = new JournalDataSource(this);
		datasource.open();
		date = getTodaysDate();
		date_field.setText("Today");

		registerForContextMenu(breakfast);
		registerForContextMenu(lunch);
		registerForContextMenu(dinner);
		
		registerOnClicksForLists();
	}

	public void loadJournal(String date) {
		List<FoodItem> bList = datasource.getBreakfastList(date);
		List<FoodItem> lList = datasource.getLunchList(date);
		List<FoodItem> dList = datasource.getDinnerList(date);
		
		ArrayAdapter<FoodItem> adapter = new ArrayAdapter<FoodItem>(this,
		        R.layout.food_item_list_view, bList);
		breakfast.setAdapter(adapter);
		
		adapter = new ArrayAdapter<FoodItem>(this,
				R.layout.food_item_list_view, lList);
		lunch.setAdapter(adapter);
		
		adapter = new ArrayAdapter<FoodItem>(this,
				R.layout.food_item_list_view, dList);
		dinner.setAdapter(adapter);
	}
	
	public String getTodaysDate() {
		
		Calendar c = Calendar.getInstance();
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
		date.show(getSupportFragmentManager(), "Journal Date");
	}


	OnDateSetListener ondate = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			date = new String(monthOfYear+1+"/"+dayOfMonth+"/"+year);
			Calendar c = Calendar.getInstance();
			if(c.get(Calendar.DAY_OF_MONTH)== dayOfMonth && 
					c.get(Calendar.MONTH)== monthOfYear && c.get(Calendar.YEAR)== year)
				date_field.setText("Today");
			else
				date_field.setText(date);
			loadJournal(date);
		}
	};
	
	@Override
	protected void onResume() {
		datasource.open();
		loadJournal(date);
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
	
	public void addToJournal(View view) {
		
		Intent intent = new Intent(this, JournalAddItem.class);
		intent.putExtra("add_date", date);
		
		if(view.getId() == R.id.journal_add_breakfast) {
			intent.putExtra("meal", "breakfast");
		}
		else if(view.getId() == R.id.journal_add_lunch) {
			intent.putExtra("meal", "lunch");
		}
		else {
			intent.putExtra("meal", "dinner");
		}
		startActivity(intent);
	}
	
    /** This will be invoked when a menu item is selected */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	ListAdapter adapter;
    	
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	
        
        if(listPressed == breakfast.getId()) {
        	adapter = breakfast.getAdapter();
        }
        else if(listPressed == lunch.getId()) {
        	adapter = lunch.getAdapter();
        }
        else {
        	adapter = dinner.getAdapter();
  
        }
        
        FoodItem selectedItem = (FoodItem) adapter.getItem(info.position);
        
        if(item.getTitle().toString().equals("Remove Item")) {
        	datasource.removeItem(selectedItem);
        	loadJournal(date);
        	Toast.makeText(this, "Item Removed", Toast.LENGTH_SHORT).show();
        } else {
        	Intent intent = prepareIntent(selectedItem, false);
        	startActivity(intent);
        }
        return true;
    }
	
    
    public Intent prepareIntent(FoodItem selected, boolean isPantry) {
        Intent intent = new Intent(this, ViewItemActivity.class);
        intent.putExtra("food_item", selected.getName());
        intent.putExtra("calories", selected.getCalories());
        intent.putExtra("protein", selected.getProtein());
        intent.putExtra("fat", selected.getFat());
        intent.putExtra("carbs", selected.getCarbs());
        intent.putExtra("fiber", selected.getFiber());
        intent.putExtra("suger",selected.getSuger());
        intent.putExtra("calcium",selected.getCalcium());
        intent.putExtra("iron",selected.getIron());
        intent.putExtra("sodium",selected.getSodium());
        intent.putExtra("vit_c",selected.getVitC());
        intent.putExtra("vit_a", selected.getVitA());
        intent.putExtra("chol", selected.getChol());
        intent.putExtra("add_date", selected.getDate());
        intent.putExtra("isPantry", isPantry);
        
        return intent;
    }
	
    public void registerOnClicksForLists() {
		breakfast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			    FoodItem selected = (FoodItem) parent.getItemAtPosition(position);
			     Intent intent = prepareIntent(selected, false);   
			     startActivity(intent);            
			}});
    
		lunch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			    FoodItem selected = (FoodItem) parent.getItemAtPosition(position);
			     Intent intent = prepareIntent(selected, false);   
			     startActivity(intent);            
			}});
		
		dinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			    FoodItem selected = (FoodItem) parent.getItemAtPosition(position);
			     Intent intent = prepareIntent(selected, false);   
			     startActivity(intent);            
			}});
    }
    
    /** This will be invoked when an item in the listview is long pressed */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        listPressed = v.getId();
        getMenuInflater().inflate(R.menu.journal_item_menu, menu);
    }
	


}
