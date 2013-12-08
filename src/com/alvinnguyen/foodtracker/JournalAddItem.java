package com.alvinnguyen.foodtracker;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.alvinnguyen.foodtracker.database.FoodDataSource;
import com.alvinnguyen.foodtracker.database.JournalDataSource;
import com.alvinnguyen.foodtracker.database.PantryDataSource;
import com.alvinnguyen.foodtracker.object.FoodItem;

public class JournalAddItem extends ListActivity {

	EditText search;
	ToggleButton pantryToggle, globalToggle;
	TextView empty;
	boolean addFromPantry = true;
	String meal, addDate, savedSearch;
	
	
	PantryDataSource pantry;
	JournalDataSource journal;
	FoodDataSource global;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_journal_add_item);

		initAllFields();
		loadList("");
	}


	public void initAllFields() {
		search = (EditText) findViewById(R.id.journal_add_search);
		pantryToggle = (ToggleButton) findViewById(R.id.journal_add_pantry);
		globalToggle = (ToggleButton) findViewById(R.id.journal_add_global);
		empty = (TextView) findViewById(R.id.journal_add_empty);
		this.getListView().setEmptyView(empty);
		
		meal = getIntent().getStringExtra("meal");
		addDate = getIntent().getStringExtra("add_date");
		
		pantry = new PantryDataSource(this);
		journal = new JournalDataSource(this);
		global = new FoodDataSource(this);
		
		pantryToggle.setChecked(true);
		globalToggle.setChecked(false);
		pantry.open();
		journal.open();
		global.open();
		
		registerForContextMenu(getListView());
	}
	
	public void loadList(String search) {
		List<FoodItem> list = new ArrayList<FoodItem>();
		
		if(addFromPantry) {
			if(search != null && search != "") 
				list = pantry.search(search);
			else
				list = pantry.getAllPantryItems();
		}
		else {
			if(search != null && search != "")
				list = global.search(search);
		}
		
		ArrayAdapter<FoodItem> adapter = new ArrayAdapter<FoodItem>(this,
				R.layout.food_item_list_view, list);
		setListAdapter(adapter);
		
	}
	
	public void toggleFoodSource(View view) {
		if(view.getId() == R.id.journal_add_pantry) {
			addFromPantry = true;
			globalToggle.setChecked(false);
			pantryToggle.setChecked(true);
			empty.setText("No Items In Pantry");
			loadList("");
		} 
		else if(view.getId() == R.id.journal_add_global) {
			addFromPantry = false;
			empty.setText("Search For an Item");
			pantryToggle.setChecked(false);
			globalToggle.setChecked(true);
			loadList("");
		}
		
	}
	
    /** This will be invoked when a menu item is selected */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
 
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        
        ListAdapter adapter = getListAdapter();
        FoodItem selectedItem = (FoodItem) adapter.getItem(info.position);
        
        if(item.getTitle().toString().equals("Add to Journal")) {
        	if(addFromPantry) 
	        	pantry.removeItem(selectedItem);
		    
        	journal.addItem(selectedItem, addDate, meal);
        	loadList(savedSearch);
        	Toast.makeText(this, "Item Added to Journal", Toast.LENGTH_SHORT).show();
        } else {
        	Intent intent = prepareIntent(selectedItem, addFromPantry);
        	startActivity(intent);
        }
        return true;
    }
	
    
    public Intent prepareIntent(FoodItem selected, boolean addFromPantry) {
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
        intent.putExtra("isPantry", addFromPantry);
        
        return intent;
    }
    
	public void searchButtonGo(View view) {
		savedSearch = search.getText().toString();
		loadList(savedSearch);
	}
	
    @Override
    protected void onListItemClick(ListView list, View v, int position, long id) {
     
    FoodItem selected = (FoodItem) list.getItemAtPosition(position);
     Intent intent = prepareIntent(selected, false);
     
     startActivity(intent); 
    }
	
	@Override
	protected void onResume() {
		pantry.open();
		global.open();
		journal.open();
		addFromPantry = true;
		pantryToggle.setChecked(true);
		globalToggle.setChecked(false);
		loadList("");
		super.onResume();
	}

	@Override
	protected void onPause() {
		pantry.close();
		global.close();
		journal.close();
		super.onPause();
	}
	
    /** This will be invoked when an item in the listview is long pressed */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.journal_add_item_menu, menu);
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
