package com.alvinnguyen.foodtracker;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alvinnguyen.foodtracker.database.PantryDataSource;
import com.alvinnguyen.foodtracker.object.FoodItem;


//http://wptrafficanalyzer.in/blog/creating-a-floating-contextual-menu-in-android/
//http://www.vogella.com/articles/AndroidSQLite/article.html

public class PantryActivity extends ListActivity {

	EditText search;
	PantryDataSource datasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pantry);
		
		initAllFields();
		loadPantry();
	}

	
	public void initAllFields(){
		search = (EditText) findViewById(R.id.pantry_search);
		datasource = new PantryDataSource(this);
		datasource.open();
		
		ListView pantryList = this.getListView();
		pantryList.setEmptyView(findViewById(R.id.empty));
		
		registerForContextMenu(getListView());
	}


	public void loadPantry() {
		List<FoodItem> allItems = datasource.getAllPantryItems();
		
		if(allItems.size() == 0) {
			TextView empty = (TextView) getListView().getEmptyView();
			empty.setText("No Items Found");
		}
		
		ArrayAdapter<FoodItem> adapter = new ArrayAdapter<FoodItem>(this,
				R.layout.food_item_list_view, allItems);
		    setListAdapter(adapter);
	}
	
	
	public void searchDatabase(View view) {
		
		List<FoodItem> searchItems = datasource.search(search.getText().toString());
		
		if(searchItems.size() == 0) {
			TextView empty = (TextView) getListView().getEmptyView();
			empty.setText("Search Found Nothing");
		}
		ArrayAdapter<FoodItem> adapter = new ArrayAdapter<FoodItem>(this,
			R.layout.food_item_list_view, searchItems);
		setListAdapter(adapter);
	}
	
	public void addToPantry(View view) {
		Intent intent = new Intent(this, PantryAddItemActivity.class);
		startActivity(intent);
	}
	
	public void removeFromPantry(FoodItem item) {
		datasource.removeItem(item);
		loadPantry();
	}
	
	@Override
	protected void onResume() {
		datasource.open();
		loadPantry();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
	
    /** This will be invoked when a menu item is selected */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
 
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        
        ListAdapter adapter = getListAdapter();
        FoodItem selectedItem = (FoodItem) adapter.getItem(info.position);
        
        if(item.getTitle().toString().equals("Remove Item")) {
        	removeFromPantry(selectedItem);
        	Toast.makeText(this, "Item Removed", Toast.LENGTH_SHORT).show();
        } else {
        	Intent intent = prepareIntent(selectedItem);
        	startActivity(intent);
        }
        return true;
    }
    
    @Override
    protected void onListItemClick(ListView list, View v, int position, long id) {
     
    FoodItem selected = (FoodItem) list.getItemAtPosition(position);
     Intent intent = prepareIntent(selected);
     
     startActivity(intent); 
    }
    
	
    public Intent prepareIntent(FoodItem selected) {
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
        intent.putExtra("isPantry", true);
        
        return intent;
    }
    
    /** This will be invoked when an item in the listview is long pressed */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.pantry_item_menu, menu);
    }
}
