package com.alvinnguyen.foodtracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PantryDatabaseHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "pantry_database.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "pantry_table";
	
	private static final String DATABASE_CREATE =
			"CREATE TABLE " + TABLE_NAME
			+ "(" 
			+ "id INTEGER PRIMARY KEY,"
			+ "food_item TEXT NOT NULL, " 
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
			+ "cholesterol INTEGER NOT NULL, "
			+ "add_date TEXT"
			+ ");";
	
	
	public PantryDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(PantryDatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
		
	}

}
