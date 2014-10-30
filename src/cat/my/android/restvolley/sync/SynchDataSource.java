package cat.my.android.restvolley.sync;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import cat.my.android.restvolley.IDataSource;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.RestVolleyError;
import cat.my.android.restvolley.db.DBModelController;
import cat.my.android.restvolley.db.DbDataSource;
import cat.my.android.restvolley.db.IDBDataSource;
import cat.my.android.restvolley.db.IDbMapping;
import cat.my.android.restvolley.db.MTDbDataSource;
import cat.my.android.restvolley.listeners.EventDispatcher;
import cat.my.android.restvolley.rest.ISessionController;
import cat.my.android.restvolley.rest.IRestMapping;
import cat.my.android.restvolley.rest.RestDataSource;


import cat.my.android.restvolley.Listeners.Listener;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

public class SynchDataSource<T extends IdentificableModel> implements ISynchDataSource<T>{
	RestDataSource<T> restVolley;
	ISessionController authenticationData;
	DeletedEntries<T> deletedEntries;
	IDBDataSource<T> dbSource;
	DBModelController<T> dbModelController;
	IDbMapping<T> dbFuncs;
	IRestMapping<T> restMap;
	Context context;
	
	public SynchDataSource(IDbMapping<T> dbFuncs, IRestMapping<T> restMap, Context context
, ISessionController authenticationData) {
		this.context=context;
		this.authenticationData=authenticationData;
		this.dbFuncs=dbFuncs;
		restVolley = new RestDataSource<T>(restMap, context, authenticationData);
		SQLiteOpenHelper dbHelper = RestVolley.getInstance(context).getDbHelper();
		deletedEntries = new DeletedEntries<T>(restVolley, dbHelper);
		dbSource = new DbDataSource<T>(context, dbFuncs, dbHelper, deletedEntries);
		if(RestVolley.getInstance(context).getConfig().isDbMultiThread())
			dbSource = new MTDbDataSource<T>(dbSource);
		
		dbModelController = dbSource.getDbModelController();
		this.restMap=restMap;
		
	}
	public SynchDataSource(IDbMapping<T> dbFuncs, IRestMapping<T> restMap, Context context) {
		this(dbFuncs, restMap, context, null);
	}
	
	public RestDataSource<T> getRestVolley() {
		return restVolley;
	}
	
	@Override
	public void index(Listener<Collection<T>> listener, ErrorListener errorListener) {
		dbSource.index(listener, errorListener);
	}
	
	@Override
	public void index(String selection, String[] selectionArgs, String order, Listener<Collection<T>> listener, ErrorListener errorListener) {
		dbSource.index(selection, selectionArgs, order, listener, errorListener);
	}
	
	@Override
	public void show(T model, Listener<T> listener, ErrorListener errorListener) {
		dbSource.show(model, listener, errorListener);
	}
	
	@Override
	public void create(final T model, final Listener<T> listener, ErrorListener errorListener) {
		Listener<T> createdOnDbListener = new Listener<T>(){
			@Override
			public void onResponse(T response) {
				Listener<T> myListener = new SetAsNotDirityListener();
				restVolley.create(model, myListener, volleyErrorListener);
				listener.onResponse(model);
			}
		};
		dbSource.create(model, createdOnDbListener, errorListener);
	}
	
	@Override
	public void update(final T model, final Listener<T> listener, ErrorListener errorListener) {
		//@param listener ATENTION: update operation may return empty result on server. This will result in null T in the listener. Return the T from the server if required
		Listener<T> updatedOnDbListener = new Listener<T>(){
			@Override
			public void onResponse(T response) {
				Listener<T> myListener = new SetAsNotDirityListener();
				restVolley.update(model, myListener, volleyErrorListener);
				listener.onResponse(model);
			}
		};
		dbSource.update(model, updatedOnDbListener, errorListener);
	}
	
	@Override
	public void destroy(final T model, final Listener<Void> listener, ErrorListener errorListener) {
		Listener<Void> deletedOnDbListener = new Listener<Void>(){
			@Override
			public void onResponse(Void response) {
				deletedEntries.setAllreadyDeleted(model);
				listener.onResponse(response);
			}
		};
		dbSource.destroy(model, deletedOnDbListener, errorListener);
	}
	
	@Override
	public void sendDirty(){
		//TODO this should work with listeners!!!
		DBModelController<T> db = getDbModelController();
		List<T> createdModels=db.getDirty(DBModelController.DIRTY_STATUS_CREATED);
		for(T model : createdModels){
			restVolley.create(model, new SetAsNotDirityListener(), volleyErrorListener);
			
		}
		List<T> updatedModels=db.getDirty(DBModelController.DIRTY_STATUS_UPDATED);
		for(T model : updatedModels){
			restVolley.update(model, new SetAsNotDirityListener(), volleyErrorListener);
		}
		deletedEntries.synchronize();
	}
	
	@Override
	public void download(final Listener<Collection<T>> listener, ErrorListener errorListener) {
		Listener<Collection<T>> fillDatabaseListener = new Listener<Collection<T>>(){
			@Override
			public void onResponse(Collection<T> response) {
				DBModelController<T> db = getDbModelController();
				db.cacheAll(new ArrayList<T>(response));
				listener.onResponse(response);
			}
		};
		restVolley.index(fillDatabaseListener, errorListener);
	}

	@Override
	public EventDispatcher<T> getEventDispatcher(){
		return dbSource.getEventDispatcher();
	}
	
	public class SetAsNotDirityListener implements Listener<T>{
		
		public SetAsNotDirityListener() {
			super();
		}

		@Override
		public void onResponse(T response) {
			DBModelController<T> db =getDbModelController();
			db.markAsClean(response);
			response = db.get(response.getId());
		}
	}
	
	@Override
	public DBModelController<T> getDbModelController(){
		return dbModelController;
	}
	
	public ModelInfo getModelInfo(){
		ModelInfo result = new ModelInfo();
		DBModelController<T> db = getDbModelController();
		
		List<T> createdModels=db.getDirty(DBModelController.DIRTY_STATUS_CREATED);
		result.setDirtyCreatedNum(createdModels.size());
		
		List<T> updatedModels=db.getDirty(DBModelController.DIRTY_STATUS_UPDATED);
		result.setDirtyUpdatedNum(updatedModels.size());
		return result;
	}
	
	public static class ModelInfo{
		int dirtyCreatedNum, dirtyUpdatedNum;

		public int getDirtyCreatedNum() {
			return dirtyCreatedNum;
		}

		public void setDirtyCreatedNum(int dirtyCreatedNum) {
			this.dirtyCreatedNum = dirtyCreatedNum;
		}

		public int getDirtyUpdatedNum() {
			return dirtyUpdatedNum;
		}

		public void setDirtyUpdatedNum(int dirtyUpdatedNum) {
			this.dirtyUpdatedNum = dirtyUpdatedNum;
		}
		
	}

	private static final VolleyErrorListener volleyErrorListener= new VolleyErrorListener();
	private static class VolleyErrorListener implements ErrorListener{
		@Override
		public void onErrorResponse(RestVolleyError error) {
			if(error.getVolleyError() instanceof NoConnectionError){
				Log.i("RestVolley", error.getMessage());
			} else {
				error.printStackTrace();
			}
		}
		
	}

	public IDbMapping<T> getDbFuncs() {
		return dbFuncs;
	}
	@Override
	public Class<T> getModelClass() {
		return restMap.getModelClass();
	}
	public Context getContext() {
		return context;
	}
}
