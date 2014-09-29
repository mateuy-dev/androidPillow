package cat.my.lib.mydata;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import cat.my.lib.orm.DBModelController;
import cat.my.lib.orm.DBModelControllerManager;
import cat.my.lib.orm.IDBModelControllerManager;
import cat.my.lib.orm.IMapperController;
import cat.my.lib.restvolley.RestVolleyDataSource;
import cat.my.lib.restvolley.models.IdentificableModel;


import com.android.volley.NoConnectionError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class SynchDataSource implements IDataSource{
	RestVolleyDataSource restVolley;
	IDBModelControllerManager dbModelControllerManager;
	DeletedEntries deletedEntries;
		
	public SynchDataSource(RestVolleyDataSource restVolley, IMapperController mapperController, SQLiteOpenHelper dbHelper) {
//		this.dbMapper = dbMapper;
		this.restVolley = restVolley;
		this.deletedEntries = new DeletedEntries(restVolley, dbHelper);
		this.dbModelControllerManager = new DBModelControllerManager(dbHelper, mapperController, deletedEntries);
	}
	
	public <T extends IdentificableModel> void index(Class<T> clazz, Type collectionType, Listener<Collection<T>> listener, ErrorListener errorListener) {
		DBModelController<T> db = getDbModelController(clazz);
		listener.onResponse(db.getAll());
	}
	
	public <T extends IdentificableModel> void sendDirty(Class<T> clazz){
		DBModelController<T> db = getDbModelController(clazz);
		List<T> createdModels=db.getDirty(DBModelController.DIRTY_STATUS_CREATED);
		for(T model : createdModels){
			restVolley.create(clazz, model, new SetAsNotDirityListener<T>(clazz), dummyErrorListener);
			
		}
		List<T> updatedModels=db.getDirty(DBModelController.DIRTY_STATUS_UPDATED);
		for(T model : updatedModels){
			restVolley.update(clazz, model, new SetAsNotDirityListener<T>(clazz), dummyErrorListener);
		}
		deletedEntries.synchronize();
	}
	
	public <T extends IdentificableModel> void download(final Class<T> clazz, Type collectionType, final Listener<Collection<T>> listener, ErrorListener errorListener) {
		Listener<Collection<T>> fillDatabaseListener = new Listener<Collection<T>>(){
			@Override
			public void onResponse(Collection<T> response) {
				DBModelController<T> db = getDbModelController(clazz);
				db.cacheAll(new ArrayList<T>(response));
				listener.onResponse(response);
			}
		};
		restVolley.index(clazz, collectionType, fillDatabaseListener, errorListener);
	}

	public <T extends IdentificableModel> void show(Class<T> clazz, T model, Listener<T> listener, ErrorListener errorListener) {
		DBModelController<T> db =getDbModelController(clazz);
		T result = db.get(model.getId());
		listener.onResponse(result);
	}
	
	public <T extends IdentificableModel> void create(Class<T> clazz, T model, Listener<T> listener, ErrorListener errorListener) {
		DBModelController<T> db =getDbModelController(clazz);
		db.create(model);
		model = db.get(model.getId());
		
		Listener<T> myListener = new SetAsNotDirityListener<T>( clazz);
		
		restVolley.create(clazz, model, myListener, dummyErrorListener);
		listener.onResponse(model);
	}
	
	public class SetAsNotDirityListener<T extends IdentificableModel> implements Listener<T>{
		Class<T> clazz;
		
		public SetAsNotDirityListener(Class<T> clazz) {
			super();
			this.clazz = clazz;
		}

		@Override
		public void onResponse(T response) {
			DBModelController<T> db =getDbModelController(clazz);
			db.markAsClean(response);
			response = db.get(response.getId());
		}
	}
	
	/**
	 * 
	 * @param clazz
	 * @param model
	 * @param listener ATENTION: update operation may return emty result on server. This will result in null T in the listener. Return the T from the server if required
	 * @param errorListener
	 */
	public <T extends IdentificableModel> void update(Class<T> clazz, T model, Listener<T> listener, ErrorListener errorListener) {
		DBModelController<T> db =getDbModelController(clazz);
		db.update(model);
		//refresh model
		model = db.get(model.getId());
		Listener<T> myListener = new SetAsNotDirityListener<T>(clazz);
		restVolley.update(clazz, model, myListener, dummyErrorListener);
		listener.onResponse(model);
	}
	
	public <T extends IdentificableModel> void destroy(Class<T> clazz, T model, Listener<Void> listener, ErrorListener errorListener) {
		DBModelController<T> db =getDbModelController(clazz);
		db.delete(model);
		deletedEntries.setAllreadyDeleted(clazz, model);
		listener.onResponse(null);
		
	}
	
	private <T extends IdentificableModel> DBModelController<T> getDbModelController(Class<T> clazz){
		return dbModelControllerManager.getDBModelController(clazz);
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
