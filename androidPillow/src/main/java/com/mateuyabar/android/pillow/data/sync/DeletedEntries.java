/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.mateuyabar.android.pillow.data.sync;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.db.DBUtil;
import com.mateuyabar.android.util.CursorUtil;
import com.mateuyabar.util.exceptions.BreakFastException;

import java.util.ArrayList;
import java.util.List;


/**
 * Keeps track of entries deleted on the database but no yet in the server.
 * Only use on synchronized items
 *
 */
public class DeletedEntries<T extends IdentificableModel> {
	//TODO this class may be reorganized!!
	public static final String TABLE = "deleted_instances";
	public static final String ID_COLUMN = "id";
	public static final String CLASS_COLUMN = "class";
	
	
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE
			+ " (" + ID_COLUMN + DBUtil.STRING_TYPE+ DBUtil.COMMA_SEP +
					CLASS_COLUMN + DBUtil.STRING_TYPE +
			");";

	SQLiteOpenHelper dbHelper;
	Class<T> modelClass;


	public static final String WHERE_ID_SELECTION = ID_COLUMN + " == ?";

	public DeletedEntries(Class<T> modelClass, SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
		this.modelClass = modelClass;

	}

	public <T extends IdentificableModel> void setToDelete(SQLiteDatabase db, T model) {
		ContentValues values = new ContentValues();
		values.put(ID_COLUMN, model.getId());
		values.put(CLASS_COLUMN, model.getClass().getName());
		db.insert(TABLE, null, values);
		
	}

	public void setAsDeleted(String id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		setAsDeleted(db, id);
		db.close();
	}
	
	public void setAsDeleted(SQLiteDatabase db, String id) {
		String[] selectionArgs = { id };
		db.delete(TABLE, WHERE_ID_SELECTION, selectionArgs);
	}

	public List<T> getDeletedModelsIds(){
		List<T> result = new ArrayList<>();
		Cursor cursor = getCursor();
		while(cursor.moveToNext()){
			try {
				String id = CursorUtil.getString(cursor, ID_COLUMN);
				Class<T> clazz = getModelClass();
				T model = clazz.newInstance();
				model.setId(id);
				result.add(model);
			} catch (Exception e) {
				throw new BreakFastException(e);
			}
		}
		return result;
	}
	
	
	
	private Cursor getCursor(){
		//TODO order may be important!!!
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = {ID_COLUMN, CLASS_COLUMN};
		String selection = CLASS_COLUMN +" == ?";
		String[] values = {getModelClass().getName()};
		Cursor cursor = db.query(TABLE,	projection, selection, values, null, null,  null);
		return cursor;
	}
	
	public Class<T> getModelClass(){
		return modelClass;
	}

	public boolean isDeleted(String id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = {ID_COLUMN, CLASS_COLUMN};
		String[] selectionArgs = { id };
		Cursor cursor = db.query(TABLE,	projection, WHERE_ID_SELECTION, selectionArgs, null, null,  null);
		return cursor.moveToNext();
	}

}
