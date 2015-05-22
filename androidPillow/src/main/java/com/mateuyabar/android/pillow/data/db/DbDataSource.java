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

package com.mateuyabar.android.pillow.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResult;

import java.util.Collection;
import java.util.List;


public class DbDataSource<T extends IdentificableModel> implements ISynchLocalDbDataSource<T> {
	SQLiteOpenHelper dbHelper;
	IDbMapping<T> dbMapping;
	DBModelController<T> dbModelController;
	Context context;
	Class<T> modelClass;

	public DbDataSource(Class<T> modelClass, Context context, IDbMapping<T> dbMapping) {
		this.modelClass = modelClass;
		this.dbMapping = dbMapping;
		this.dbHelper = Pillow.getInstance(context).getDbHelper();;
		this.context = context;
		dbModelController = new DBModelController<T>(modelClass, dbHelper, dbMapping);
		
	}

	@Override
	public IPillowResult<Collection<T>> index() {
		DBModelController<T> db = getDbModelController();
		return new PillowResult<Collection<T>>(db.index());
	}
	
	@Override
	public IPillowResult<Collection<T>> index(T model) {
		DBModelController<T> db = getDbModelController();
		return new PillowResult<Collection<T>>(db.index(model));
	}
	
	@Override
	public IPillowResult<Collection<T>> index(String selection, String[] selectionArgs, String order) {
		DBModelController<T> db = getDbModelController();
		return new PillowResult<Collection<T>>(db.index(selection, selectionArgs, order));
	}

	@Override
	public IPillowResult<T> show(T model) {
		DBModelController<T> db =getDbModelController();
		T result = db.get(model.getId());
		return new PillowResult<T>( result);
	}

	@Override
	public IPillowResult<T> create(T model) {
		try{
			DBModelController<T> db = getDbModelController();
			db.create(model);
			model = db.get(model.getId());
			return new PillowResult<T>( model);
		} catch (SQLiteException exception){
			return new PillowResult<T>( new PillowError(exception));
		}
	}

	@Override
	public PillowResult<T> update(T model) {
		try{
			DBModelController<T> dbController =getDbModelController();
			dbController.update(model);
			//refresh model
			model = dbController.get(model.getId());
			return new PillowResult<T>(model);
		} catch (SQLiteException exception){
			return new PillowResult<T>(new PillowError(exception));
		}
	}

	@Override
	public PillowResult<Void> destroy(T model) {
		try{
			DBModelController<T> db =getDbModelController();
			db.delete(model);
			return PillowResult.newVoidResult();
		} catch (SQLiteException exception){
			return new PillowResult<Void>(new PillowError(exception));
		}
	}
	
	@Override
	public PillowResult<Integer> count(String selection, String[] selectionArgs) {
		DBModelController<T> db =getDbModelController();
		int result = db.getCount(selection, selectionArgs);
		return new PillowResult<Integer>(result);
	}
	
	@Override
	public IPillowResult<T> setAsNotDirty(T model){
		DBModelController<T> db =getDbModelController();
		db.markAsClean(model);
		model = db.get(model.getId());
		return new PillowResult<T>(model);
	}
	
	public DBModelController<T> getDbModelController(){
		return dbModelController;
	}

	public Context getContext() {
		return context;
	}

	@Override
	public List<T> getDirty(int dirtyType) {
		return getDbModelController().getDirty(dirtyType);
	}

	@Override
	public void cacheAll(List<T> models) {
		getDbModelController().cacheAll(models);
	}

	@Override
	public List<T> getDeletedModelsIds() {
		return getDbModelController().getDeletedEntries().getDeletedModelsIds();
	}

	@Override
	public void setAsDeleted(String id) {
		getDbModelController().getDeletedEntries().setAsDeleted(id);
	}

	public SQLiteOpenHelper getDbHelper() {
		return dbHelper;
	}

	public IDbMapping<T> getDbMapping() {
		return dbMapping;
	}
}
