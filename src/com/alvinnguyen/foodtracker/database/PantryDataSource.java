package com.alvinnguyen.foodtracker.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.alvinnguyen.foodtracker.object.FoodItem;

public class PantryDataSource {


	private SQLiteDatabase database;
	private PantryDatabaseHelper dbHelper;

	private String[] allColumns = {"food_item","calories", "protein", "fat", "carbs", "fiber",
			"suger", "calcium", "iron", "sodium",
			"vit_c", "vit_a", "cholesterol", "add_date"};

	public PantryDataSource(Context context) {
		dbHelper = new PantryDatabaseHelper(context);

	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public String fixName(String food_item) {
		return food_item.replace("'", "''");
	}
	

	public FoodItem addItem(FoodItem item) {
		Calendar c = Calendar.getInstance();

		ContentValues values = new ContentValues();
		values.put("food_item", item.getName());
		values.put("calories", item.getCalories());
		values.put("protein", item.getProtein());
		values.put("fat", item.getFat());
		values.put("carbs", item.getCarbs());
		values.put("fiber", item.getFiber());
		values.put("suger", item.getSuger());
		values.put("calcium", item.getCalcium());
		values.put("iron", item.getIron());
		values.put("sodium", item.getSodium());
		values.put("vit_c", item.getVitC());
		values.put("vit_a", item.getVitA());
		values.put("cholesterol", item.getChol());
		values.put("add_date", c.get(Calendar.MONTH)+1+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.YEAR));

		long insertID = database.insert(PantryDatabaseHelper.TABLE_NAME, null, values);

		Cursor cursor = database.query(PantryDatabaseHelper.TABLE_NAME, 
				allColumns, "id="+insertID, null,null,null,null);

		cursor.moveToFirst();
		if(cursor.getCount() > 0) {
			FoodItem addedItem = cursorToFoodItem(cursor);
			cursor.close();
			return addedItem;
		}
		else
			return null;
	}

	public boolean removeItem(FoodItem item) {

		Cursor removeID = database.query(PantryDatabaseHelper.TABLE_NAME, 
				new String[] {"id"}, "food_item=\'"+fixName(item.getName())+"\'" +
						" AND add_date=\'"+item.getDate()+"\'", null, null, null, null, "1");
		removeID.moveToFirst();

		if(removeID.getCount() ==1) {
			int removed = database.delete(PantryDatabaseHelper.TABLE_NAME,
					"id="+removeID.getInt(removeID.getColumnIndex("id")), null);

			removeID.close();
			if(removed ==1) {
				return true;				
			}
			else
				return false;
		}
		else {
			removeID.close();
			return false;
		}
	}

	public List<FoodItem> getAllPantryItems() {
		List<FoodItem> allItems = new ArrayList<FoodItem>();

		Cursor cursor = database.query(PantryDatabaseHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		if(cursor.getCount() >0) {
			while(!cursor.isAfterLast()) {
				FoodItem item = cursorToFoodItem(cursor);
				allItems.add(item);
				cursor.moveToNext();
			}
		}

		cursor.close();
		return allItems;
	}

	public List<FoodItem> search(String searchable) {
		List<FoodItem> searchResults = new ArrayList<FoodItem>();

		Cursor cursor = database.query(PantryDatabaseHelper.TABLE_NAME, allColumns, 
				"food_item like " + "'%"+searchable+"%'", null, null, null, null);

		cursor.moveToFirst();
		if(cursor.getCount() >0) {
			while(!cursor.isAfterLast()) {
				FoodItem item = cursorToFoodItem(cursor);
				searchResults.add(item);
				cursor.moveToNext();
			}
		}

		cursor.close();
		return searchResults;
	}

	public FoodItem cursorToFoodItem(Cursor cursor) {
		FoodItem item;
		item = new FoodItem(cursor.getString(cursor.getColumnIndex("food_item")),
				cursor.getInt(cursor.getColumnIndex("calories")),
				cursor.getDouble(cursor.getColumnIndex("protein")),
				cursor.getDouble(cursor.getColumnIndex("fat")),
				cursor.getDouble(cursor.getColumnIndex("carbs")),
				cursor.getDouble(cursor.getColumnIndex("fiber")),
				cursor.getDouble(cursor.getColumnIndex("suger")),
				cursor.getInt(cursor.getColumnIndex("calcium")),
				cursor.getDouble(cursor.getColumnIndex("iron")),
				cursor.getInt(cursor.getColumnIndex("sodium")),
				cursor.getDouble(cursor.getColumnIndex("vit_c")),
				cursor.getInt(cursor.getColumnIndex("vit_a")),
				cursor.getInt(cursor.getColumnIndex("cholesterol")),
				cursor.getString(cursor.getColumnIndex("add_date"))
				);
		return item;
	}

}
