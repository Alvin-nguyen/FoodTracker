package com.alvinnguyen.foodtracker.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.alvinnguyen.foodtracker.object.FoodItem;

public class FoodDataSource {
	
	private SQLiteDatabase database;
	private FoodDatabaseHelper dbHelper;
	
	String[] allColumns = {"food_item", "calories", "protein", "fat", "carbs", "fiber",
							"suger", "calcium", "iron", "sodium",
							"vit_c", "vit_a", "cholesterol"};
	
	
	public FoodDataSource(Context context) {
		dbHelper = new FoodDatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void addTestItem() {
		
		ContentValues values = new ContentValues();
		values.put("food_item", "testAdd");
		values.put("protein", "1");
		values.put("carbs", "2");
		values.put("fiber", "3");
		values.put("suger", "4");
		values.put("calcium", "5");
		values.put("iron", "6");
		values.put("sodium", "7");
		values.put("vit_c", "8");
		values.put("vit_a", "9");
		values.put("cholesterol", "10");

		database.insert(FoodDatabaseHelper.TABLE_NAME, null, values);
		
		Cursor cursor = database.query(FoodDatabaseHelper.TABLE_NAME, 
				allColumns, "food_item="+"'testAdd'", null,null,null,null);
		
		cursor.moveToFirst();
		
		if(cursor.getCount() > 0) {
			cursor.close();
		}

	}
	
	public void AddAllItemsToDatabase(Context context, String filename) {
		
		InputStreamReader input;
		String line;
		try {
			input = new InputStreamReader(context.getAssets().open(filename));
			BufferedReader reader = new BufferedReader(input);
			
			database.beginTransaction();
			while((line = reader.readLine()) != null) {
				database.execSQL(line);
			}
			database.setTransactionSuccessful();
			database.endTransaction();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public List<FoodItem> search(String searchable) {
		List<FoodItem> searchResults = new ArrayList<FoodItem>();
		
		Cursor cursor = database.query(FoodDatabaseHelper.TABLE_NAME, allColumns, 
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
				cursor.getInt(cursor.getColumnIndex("cholesterol"))
				);
		return item;
	}
}
