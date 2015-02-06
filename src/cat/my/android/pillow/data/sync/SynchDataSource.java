package cat.my.android.pillow.data.sync;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.data.db.DBModelController;
import cat.my.android.pillow.data.db.DbDataSource;
import cat.my.android.pillow.data.db.IDbMapping;
import cat.my.android.pillow.data.db.MultiThreadDbDataSource;
import cat.my.android.pillow.data.db.MultiThreadDbDataSource.OperationRunnable;
import cat.my.android.pillow.data.rest.IRestMapping;
import cat.my.android.pillow.data.rest.ISessionController;
import cat.my.android.pillow.data.rest.RestDataSource;

public class SynchDataSource<T extends IdentificableModel> implements ISynchDataSource<T>{
	RestDataSource<T> restDataSource;
	ISessionController authenticationData;
	DeletedEntries<T> deletedEntries;
	MultiThreadDbDataSource<T> dbSource;
	DBModelController<T> dbModelController;
	IDbMapping<T> dbFuncs;
	IRestMapping<T> restMap;
	Context context;
	
	public SynchDataSource(IDbMapping<T> dbFuncs, IRestMapping<T> restMap, Context context
, ISessionController authenticationData) {
		this.context=context;
		this.authenticationData=authenticationData;
		this.dbFuncs=dbFuncs;
		restDataSource = new RestDataSource<T>(restMap, context, authenticationData);
		SQLiteOpenHelper dbHelper = Pillow.getInstance(context).getDbHelper();
		deletedEntries = new DeletedEntries<T>(restDataSource, dbHelper);
//		dbSource = new DbDataSource<T>(context, dbFuncs, dbHelper, deletedEntries);
//		if(Pillow.getInstance(context).getConfig().isDbMultiThread()){
		dbSource = new MultiThreadDbDataSource<T>(new DbDataSource<T>(context, dbFuncs, dbHelper, deletedEntries));
		
		dbModelController = dbSource.getDbModelController();
		this.restMap=restMap;
		
	}
	public SynchDataSource(IDbMapping<T> dbFuncs, IRestMapping<T> restMap, Context context) {
		this(dbFuncs, restMap, context, null);
	}
	
	public RestDataSource<T> getRestDataSource() {
		return restDataSource;
	}
	
	@Override
	public void index(Listener<Collection<T>> listener, ErrorListener errorListener) {
		dbSource.index(listener, errorListener);
	}
	
	@Override
	public void index(T model, Listener<Collection<T>> listener, ErrorListener errorListener) {
		dbSource.index(model, listener, errorListener);
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
				restDataSource.create(model, myListener, CommonListeners.volleyErrorListener);
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
				restDataSource.update(model, myListener, CommonListeners.volleyErrorListener);
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
	public void count(String selection, String[] selectionArgs, Listener<Integer> listener,
			ErrorListener errorListener) {
		dbSource.count(selection, selectionArgs, listener, errorListener);
	}
	
	private class SendDirtyRunnable extends OperationRunnable<Listener<Void>>{
		public SendDirtyRunnable(Listener<Void> listener, ErrorListener errorListener) {
			super(listener, errorListener);
		}

		@Override
		public void run() {
			DBModelController<T> db = getDbModelController();
			List<T> createdModels=db.getDirty(DBModelController.DIRTY_STATUS_CREATED);
			for(T model : createdModels){
				restDataSource.create(model, new SetAsNotDirityListener(), CommonListeners.volleyErrorListener);
			}
			List<T> updatedModels=db.getDirty(DBModelController.DIRTY_STATUS_UPDATED);
			for(T model : updatedModels){
				restDataSource.update(model, new SetAsNotDirityListener(), CommonListeners.volleyErrorListener);
			}
			deletedEntries.synchronize();
			
			getListener().onResponse(null);
		}
	}
	
	@Override
	public void sendDirty(Listener<Void> listener, ErrorListener errorListener){
		getThreadPoolExecutor().execute(new SendDirtyRunnable(listener, errorListener));
	}
	
	private ThreadPoolExecutor getThreadPoolExecutor() {
		return dbSource.getThreadPoolExecutor();
	}
	
	private class DownloadRunnable extends OperationRunnable<Listener<Collection<T>>>{
		public DownloadRunnable(Listener<Collection<T>> listener, ErrorListener errorListener) {
			super(listener, errorListener);
		}

		@Override
		public void run() {
			Listener<Collection<T>> fillDatabaseListener = new Listener<Collection<T>>(){
				@Override
				public void onResponse(Collection<T> response) {
					DBModelController<T> db = getDbModelController();
					db.cacheAll(new ArrayList<T>(response));
					getListener().onResponse(response);
				}
			};
			restDataSource.index(fillDatabaseListener, getErrorListener());
		}
	}
	
	@Override
	public void download(final Listener<Collection<T>> listener, ErrorListener errorListener) {
		getThreadPoolExecutor().execute(new DownloadRunnable(listener, errorListener));
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
