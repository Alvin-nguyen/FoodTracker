package com.alvinnguyen.foodtracker;

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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

import com.alvinnguyen.foodtracker.database.FoodDataSource;
import com.alvinnguyen.foodtracker.database.PantryDataSource;
import com.alvinnguyen.foodtracker.object.FoodItem;

public class PantryAddItemActivity extends ListActivity {

	PantryDataSource pantrySource;
	FoodDataSource globalSource;
	
	EditText search;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pantry_add_item);

		initAllFields();
		
		//globalSource.addTestItem();
	}

	public void initAllFields() {
		search = (EditText) findViewById(R.id.pantry_additem_search);
		
		pantrySource = new PantryDataSource(this);
		pantrySource.open();
		
		globalSource = new FoodDataSource(this);
		globalSource.open();
		
		ListView globalList = this.getListView();
		globalList.setEmptyView(findViewById(R.id.pantry_additem_empty));
		TextView empty = (TextView) getListView().getEmptyView();
		empty.setText("Please Search For An Item");
		
		
		registerForContextMenu(getListView());
	}
	
	public void searchGlobalDatabase(View view) {
		
		List<FoodItem> searchItems = globalSource.search(search.getText().toString());
		
		if(searchItems.size() == 0) {
			TextView empty = (TextView) getListView().getEmptyView();
			empty.setText("Search Found Nothing");
		}
		
		ArrayAdapter<FoodItem> adapter = new ArrayAdapter<FoodItem>(this,
			R.layout.food_item_list_view, searchItems);
		setListAdapter(adapter);
	}

	@Override
	protected void onResume() {
		pantrySource.open();
		globalSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		pantrySource.close();
		globalSource.close();
		super.onPause();
	}
	
    /** This will be invoked when a menu item is selected */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
 
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        
        ListAdapter adapter = getListAdapter();
        FoodItem selectedItem = (FoodItem) adapter.getItem(info.position);
        
        if(item.getTitle().toString().equals("Add to Pantry")) {
        	pantrySource.addItem(selectedItem);
        	Toast.makeText(this, "Item Added", Toast.LENGTH_LONG).show();

        } 
        else {
            Intent intent = prepareIntent(selectedItem);
            startActivity(intent); 
        }
        return true;
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
        intent.putExtra("isPantry", false);
        
        return intent;
    }
    
    @Override
    protected void onListItemClick(ListView list, View v, int position, long id) {
     
    FoodItem selected = (FoodItem) list.getItemAtPosition(position);
     Intent intent = prepareIntent(selected);
     
     startActivity(intent); 
    }
    
    
    /** This will be invoked when an item in the listview is long pressed */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.global_item_menu, menu);
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
