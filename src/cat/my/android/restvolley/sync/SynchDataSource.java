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
import cat.my.android.restvolley.db.DBModelController;
import cat.my.android.restvolley.db.DbDataSource;
import cat.my.android.restvolley.db.IDbMapping;
import cat.my.android.restvolley.rest.IAuthenticationData;
import cat.my.android.restvolley.rest.IRestMapping;
import cat.my.android.restvolley.rest.RestDataSource;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class SynchDataSource<T extends IdentificableModel> implements IDataSource<T>{
	RestDataSource<T> restVolley;
	IAuthenticationData authenticationData;
	DeletedEntries<T> deletedEntries;
	DbDataSource<T> dbSource;
	DBModelController<T> dbModelController;
	IDbMapping<T> dbFuncs;
	IRestMapping<T> restMap;
		
	public SynchDataSource(IDbMapping<T> dbFuncs, IRestMapping<T> restMap, Context context, SQLiteOpenHelper dbHelper, IAuthenticationData authenticationData) {
		this.authenticationData=authenticationData;
		restVolley = new RestDataSource<T>(restMap, context, authenticationData);
		deletedEntries = new DeletedEntries<T>(restVolley, dbHelper);
		dbSource = new DbDataSource<T>(dbFuncs, dbHelper, deletedEntries);
		dbModelController = dbSource.getDbModelController();
	}
	public SynchDataSource(IDbMapping<T> dbFuncs, IRestMapping<T> restMap, Context context, SQLiteOpenHelper dbHelper) {
		this(dbFuncs, restMap, context, dbHelper, null);
	}
	
	public RestDataSource<T> getRestVolley() {
		return restVolley;
	}
	
	public void setServerRequiresAuthentication(boolean serverRequiresAuthentication) {
		this.restVolley.setServerRequiresAuthentication(serverRequiresAuthentication);
	}
	
	@Override
	public void index(Listener<Collection<T>> listener, ErrorListener errorListener) {
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
				restVolley.create(model, myListener, dummyErrorListener);
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
				restVolley.update(model, myListener, dummyErrorListener);
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
		DBModelController<T> db = getDbModelController();
		List<T> createdModels=db.getDirty(DBModelController.DIRTY_STATUS_CREATED);
		for(T model : createdModels){
			restVolley.create(model, new SetAsNotDirityListener(), dummyErrorListener);
			
		}
		List<T> updatedModels=db.getDirty(DBModelController.DIRTY_STATUS_UPDATED);
		for(T model : updatedModels){
			restVolley.update(model, new SetAsNotDirityListener(), dummyErrorListener);
		}
		deletedEntries.synchronize();
	}
	
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
	
	public static ErrorListener dummyErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
		}
	};
	Listener dummyListener = new Listener() {
		@Override
		public void onResponse(Object response) {
		}
	};
	
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
}
