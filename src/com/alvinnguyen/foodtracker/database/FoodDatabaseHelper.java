package com.alvinnguyen.foodtracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class FoodDatabaseHelper extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME = "food_database.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "food_table";
	
	private static final String DATABASE_CREATE = 
			"CREATE TABLE "+ TABLE_NAME
			+ "(" 
			+ "food_item TEXT PRIMARY KEY, " 
			+ "calories INTEGER NOT NULL, " 
			+ "protein REAL NOT NULL, "
			+ "fat REAL NOT NULL, " 
			+ "carbs REAL NOT NULL, "
			+ "fiber REAL NOT NULL, "
			+ "suger REAL NOT NULL, "
			+ "calcium INTEGER NOT NULL, "
			+ "iron REAL NOT NULL, "
			+ "sodium INTEGER NOT NULL, "
			+ "vit_c REAL NOT NULL, "
			+ "vit_a INTEGER NOT NULL, "
			+ "cholesterol INTEGER NOT NULL "
			+ ");";
	
	public FoodDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(FoodDatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);
	}

	
}
