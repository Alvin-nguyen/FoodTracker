package com.alvinnguyen.foodtracker.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.alvinnguyen.foodtracker.object.FoodItem;

public class JournalDataSource {

	private SQLiteDatabase database;
	private JournalDatabaseHelper dbHelper;

	private String[] allColumns = {"food_item","calories", "protein", "fat", "carbs", "fiber",
			"suger", "calcium", "iron", "sodium",
			"vit_c", "vit_a", "cholesterol", "add_date", "meal"};

	public JournalDataSource(Context context) {
		dbHelper = new JournalDatabaseHelper(context);

	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public List<FoodItem> getBreakfastList(String date) {
		List<FoodItem> breakfastItems = new ArrayList<FoodItem>();

		Cursor cursor = database.query(JournalDatabaseHelper.TABLE_NAME, allColumns, 
				"meal='breakfast' AND add_date="+"'"+date+"'", null, null, null, null);

		cursor.moveToFirst();
		if(cursor.getCount() >0) {
			while(!cursor.isAfterLast()) {
				FoodItem item = cursorToFoodItem(cursor);
				breakfastItems.add(item);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return breakfastItems;
	}
	
	public List<FoodItem> getLunchList(String date) {
		List<FoodItem> lunchItems = new ArrayList<FoodItem>();

		Cursor cursor = database.query(JournalDatabaseHelper.TABLE_NAME, allColumns, 
				"meal='lunch' AND add_date="+"'"+date+"'", null, null, null, null);

		cursor.moveToFirst();
		if(cursor.getCount() >0) {
			while(!cursor.isAfterLast()) {
				FoodItem item = cursorToFoodItem(cursor);
				lunchItems.add(item);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return lunchItems;
	}
	
	public List<FoodItem> getDinnerList(String date) {
		List<FoodItem> dinnerItems = new ArrayList<FoodItem>();

		Cursor cursor = database.query(JournalDatabaseHelper.TABLE_NAME, allColumns, 
				"meal='dinner' AND add_date="+"'"+date+"'", null, null, null, null);

		cursor.moveToFirst();
		if(cursor.getCount() >0) {
			while(!cursor.isAfterLast()) {
				FoodItem item = cursorToFoodItem(cursor);
				dinnerItems.add(item);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return dinnerItems;
	}
	
	public List<FoodItem> getAllItemsAtDate(String date) {
		List<FoodItem> items = new ArrayList<FoodItem>();
		
		Cursor cursor = database.query(JournalDatabaseHelper.TABLE_NAME, allColumns,
				"add_date="+ "'"+date+"'", null, null, null, null);
		
		cursor.moveToFirst();
		if(cursor.getCount()>0) {
			while(!cursor.isAfterLast()) {
				FoodItem item = cursorToFoodItem(cursor);
				items.add(item);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return items;
	}
	
	public FoodItem addItem(FoodItem item, String date, String meal) {

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
		values.put("add_date", date);
		values.put("meal", meal);

		long insertID = database.insert(JournalDatabaseHelper.TABLE_NAME, null, values);

		Cursor cursor = database.query(JournalDatabaseHelper.TABLE_NAME, 
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
	
	public String fixName(String food_item) {
		return food_item.replace("'", "''");
	}
	
	public boolean removeItem(FoodItem item) {

		Cursor removeID = database.query(JournalDatabaseHelper.TABLE_NAME, 
				new String[] {"id"}, "food_item=\'"+fixName(item.getName())+"\'" +
						" AND add_date=\'"+item.getDate()+"\'" +
						" AND meal=\'"+item.getMeal()+"\'", null, null, null, null, "1");
		removeID.moveToFirst();

		if(removeID.getCount() ==1) {
			int removed = database.delete(JournalDatabaseHelper.TABLE_NAME,
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
				cursor.getString(cursor.getColumnIndex("add_date")),
				cursor.getString(cursor.getColumnIndex("meal"))
				);
		return item;
	}
}
