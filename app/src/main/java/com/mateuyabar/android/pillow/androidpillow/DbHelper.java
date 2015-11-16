package com.mateuyabar.android.pillow.androidpillow;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mateuyabar.android.pillow.AbstractDBHelper;


public class DbHelper extends AbstractDBHelper {
	// If you change the database schema, you must increment the database version.
    public static int DATABASE_VERSION = 1;
	public static String DATABASE_NAME = "pillow_sample.db";
	
	
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		resetTables(db);
	}

	
	

}
