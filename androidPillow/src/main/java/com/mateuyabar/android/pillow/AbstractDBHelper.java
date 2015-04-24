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

package com.mateuyabar.android.pillow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.mateuyabar.android.pillow.conf.ModelConfiguration;
import com.mateuyabar.android.pillow.data.db.DBUtil;
import com.mateuyabar.android.pillow.data.db.IDbMapping;
import com.mateuyabar.android.pillow.data.sync.DeletedEntries;

public abstract class AbstractDBHelper extends SQLiteOpenHelper{
	public static final String LOG_ID = Pillow.LOG_ID+" - DB"; 
	Context context;
	public AbstractDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}
	
	List<IDbMapping<?>> mappings;
	public synchronized List<IDbMapping<?>> getMappings(){
		if(mappings==null){
			mappings = new ArrayList<IDbMapping<?>>();
			//We dont need to use sorted order cause no FK defined, and triggers defined after create all tables. In case need modify
			//   Collection<IDataSource<?>> confs = Pillow.getInstance(context).getSortedSynchDataSources();
			Collection<ModelConfiguration<?>> confs = Pillow.getInstance(context).getModelConfigurationFactory().getModelConfigurations().values();
			for(ModelConfiguration<?> conf : confs){
				mappings.add(conf.getDbMapping());
			}
		}
		return mappings;
	}
	
	@Override
	public SQLiteDatabase getReadableDatabase() {
		return super.getReadableDatabase();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	    enableForeignKeys(db);
	}
	
	protected void enableForeignKeys(SQLiteDatabase db) {
		//Not using FK right now
//		if (!db.isReadOnly()) {
//		    db.execSQL("PRAGMA foreign_keys=ON;");
//		}
	}

	public void resetTables(SQLiteDatabase db){
		dropTables(db);
		createTables(db);
	}
	
	public void createTables(SQLiteDatabase db){
		enableForeignKeys(db);
		
		db.execSQL(DeletedEntries.CREATE_TABLE);
		for(IDbMapping<?> mapping: getMappings()){
			String createTableSql = DBUtil.createTable(mapping);
			Log.d(LOG_ID, "creating table: "+createTableSql);
			db.execSQL(createTableSql);
		}
		
		for(IDbMapping<?> mapping: getMappings()){
			List<String> triggers = mapping.getTriggers();
			for(String trigger:triggers){
				Log.d(LOG_ID, "adding trigger: "+trigger);
				db.execSQL(trigger);
			}
		}
	}
	
	public void dropTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS "+DeletedEntries.TABLE);
		for(IDbMapping<?> mapping: getMappings()){
			db.execSQL(DBUtil.dropTable(mapping));
		}
	}
		
}
