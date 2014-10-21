package cat.my.android.restvolley.sync;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import cat.my.android.restvolley.IDataSource;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.Listeners.CollectionListener;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.db.DBModelController;
import cat.my.android.restvolley.db.DbDataSource;
import cat.my.android.restvolley.db.IDbMapping;
import cat.my.android.restvolley.rest.ISessionController;
import cat.my.android.restvolley.rest.IRestMapping;
import cat.my.android.restvolley.rest.RestDataSource;


import cat.my.android.restvolley.Listeners.Listener;
import com.android.volley.VolleyError;

public class SynchDataSource<T extends IdentificableModel> implements IDataSource<T>, ISynchDataSource<T>{
	RestDataSource<T> restVolley;
	ISessionController authenticationData;
	DeletedEntries<T> deletedEntries;
	DbDataSource<T> dbSource;
	DBModelController<T> dbModelController;
	IDbMapping<T> dbFuncs;
	IRestMapping<T> restMap;
	
	public SynchDataSource(IDbMapping<T> dbFuncs, IRestMapping<T> restMap, Context context
, ISessionController authenticationData) {
		this.authenticationData=authenticationData;
		this.dbFuncs=dbFuncs;
		restVolley = new RestDataSource<T>(restMap, context, authenticationData);
		SQLiteOpenHelper dbHelper = RestVolley.getInstance(context).getDbHelper();
		deletedEntries = new DeletedEntries<T>(restVolley, dbHelper);
		dbSource = new DbDataSource<T>(dbFuncs, dbHelper, deletedEntries);
		dbModelController = dbSource.getDbModelController();
	}
	public SynchDataSource(IDbMapping<T> dbFuncs, IRestMapping<T> restMap, Context context) {
		this(dbFuncs, restMap, context, null);
	}
	
	public RestDataSource<T> getRestVolley() {
		return restVolley;
	}
	
	@Override
	public void index(CollectionListener<T> listener, ErrorListener errorListener) {
		dbSource.index(listener, errorListener);
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
				restVolley.create(model, myListener, DummyListeners.dummyErrorListener);
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
				restVolley.update(model, myListener, DummyListeners.dummyErrorListener);
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
	
	public void sendDirty(){
		//TODO this should work with listeners!!!
		DBModelController<T> db = getDbModelController();
		List<T> createdModels=db.getDirty(DBModelController.DIRTY_STATUS_CREATED);
		for(T model : createdModels){
			restVolley.create(model, new SetAsNotDirityListener(), DummyListeners.dummyErrorListener);
			
		}
		List<T> updatedModels=db.getDirty(DBModelController.DIRTY_STATUS_UPDATED);
		for(T model : updatedModels){
			restVolley.update(model, new SetAsNotDirityListener(), DummyListeners.dummyErrorListener);
		}
		deletedEntries.synchronize();
	}
	
	public void download(final CollectionListener<T> listener, ErrorListener errorListener) {
		CollectionListener<T> fillDatabaseListener = new CollectionListener<T>(){
			@Override
			public void onResponse(Collection<T> response) {
				DBModelController<T> db = getDbModelController();
				db.cacheAll(new ArrayList<T>(response));
				listener.onResponse(response);
			}
		};
		restVolley.index(fillDatabaseListener, errorListener);
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
	
	private DBModelController<T> getDbModelController(){
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
//	private ErrorListener adapt(ErrorListener errorListener) {
//		return new MyDataErrorListener(errorListener);
//	}
//	
//	private class MyDataErrorListener implements ErrorListener{
//		ErrorListener mainListener;
//
//		public MyDataErrorListener(ErrorListener mainListener) {
//			super();
//			this.mainListener = mainListener;
//		}
//
//		@Override
//		public void onErrorResponse(VolleyError error) {
//			if(error instanceof NoConnectionError){
//				//No connection
//			} else {
//				mainListener.onErrorResponse(error);
//			}
//		}
//		
//	}

	public IDbMapping<T> getDbFuncs() {
		return dbFuncs;
	}
}
