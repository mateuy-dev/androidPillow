package cat.my.android.pillow.data.sync;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.PillowError;
import cat.my.android.pillow.data.core.IPillowResult;
import cat.my.android.pillow.data.core.PillowResult;
import cat.my.android.pillow.data.core.PillowResultListener;
import cat.my.android.pillow.data.db.DBModelController;
import cat.my.android.pillow.data.db.DbDataSource;
import cat.my.android.pillow.data.db.IDBDataSourceForSynch;
import cat.my.android.pillow.data.db.IDbMapping;
import cat.my.android.pillow.data.db.MultiThreadDbDataSource;
import cat.my.android.pillow.data.db.MultiThreadDbDataSource.OperationRunnable;
import cat.my.android.pillow.data.rest.IRestMapping;
import cat.my.android.pillow.data.rest.ISessionController;
import cat.my.android.pillow.data.rest.RestDataSource;

public class SynchDataSource<T extends IdentificableModel> implements ISynchDataSource<T>{
	private static ThreadPoolExecutor operationthreadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	
	RestDataSource<T> restDataSource;
	ISessionController authenticationData;
	DeletedEntries<T> deletedEntries;
	IDBDataSourceForSynch<T> dbSource;
	DBModelController<T> dbModelController;
	IDbMapping<T> dbFuncs;
	IRestMapping<T> restMap;
	Context context;
	
	//ensures that no more than one syncrhonization process is beeing done at the same time
	Object synchronizationLock;
	
	public SynchDataSource(IDbMapping<T> dbFuncs, IRestMapping<T> restMap, Context context, ISessionController authenticationData) {
		this.context=context;
		this.authenticationData=authenticationData;
		this.dbFuncs=dbFuncs;
		restDataSource = new RestDataSource<T>(restMap, context, authenticationData);
		deletedEntries = new DeletedEntries<T>(context, restDataSource);
//		dbSource = new DbDataSource<T>(context, dbFuncs, dbHelper, deletedEntries);
//		if(Pillow.getInstance(context).getConfig().isDbMultiThread()){
		dbSource = new MultiThreadDbDataSource<T>(new DbDataSource<T>(context, dbFuncs, deletedEntries));
		
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
	public IPillowResult<Collection<T>> index() {
		return dbSource.index();
	}
	
	@Override
	public IPillowResult<Collection<T>> index(T model) {
		return dbSource.index(model);
	}
	
	@Override
	public IPillowResult<Collection<T>> index(String selection, String[] selectionArgs, String order) {
		return dbSource.index(selection, selectionArgs, order);
	}
	
	@Override
	public IPillowResult<T> show(T model) {
		return dbSource.show(model);
	}
	
	@Override
	public IPillowResult<Integer> count(String selection, String[] selectionArgs) {
		return dbSource.count(selection, selectionArgs);
	}
	
	@Override
	public IPillowResult<T> create(final T model) {
		//The Pillow result is finished when result created in DB not in server.
		Listener<T> createdOnDbListener = new Listener<T>(){
			@Override
			public void onResponse(T response) {
				sendDirty();
				//Listener<T> myListener = new SetAsNotDirityListener();
				//restDataSource.create(model).setListeners(myListener, CommonListeners.getDefaultThreadedErrorListener());
			}
		};
		
		return dbSource.create(model).addListener(createdOnDbListener);
	}
	
	
	
	@Override
	public IPillowResult<T> update(final T model) {
		//@param listener ATENTION: update operation may return empty result on server. This will result in null T in the listener. Return the T from the server if required
		
		//The Pillow result is finished when result created in DB not in server.
		Listener<T> updatedOnDbListener = new Listener<T>(){
			@Override
			public void onResponse(T response) {
				sendDirty();
				//Listener<T> myListener = new SetAsNotDirityListener();
				//restDataSource.update(model).setListeners(myListener, CommonListeners.getDefaultThreadedErrorListener());
			}
		};
		return dbSource.update(model).addListener(updatedOnDbListener);
	}
	
	@Override
	public IPillowResult<Void> destroy(final T model) {
		//The Pillow result is finished when result created in DB not in server.
		
		Listener<Void> deletedOnDbListener = new Listener<Void>(){
			@Override
			public void onResponse(Void response) {
				sendDirty();
//				deletedEntries.setAllreadyDeleted(model).setListeners(CommonListeners.dummyListener, CommonListeners.getDefaultThreadedErrorListener());
			}
		};
		return dbSource.destroy(model).addListener(deletedOnDbListener);
		
	}
	
	
	
	private class SendDirtyRunnable extends OperationRunnable<Void>{
		@Override
		protected IPillowResult<Void> createMainPillowResult() {
			try {
				DBModelController<T> db = getDbModelController();
				List<T> createdModels=db.getDirty(DBModelController.DIRTY_STATUS_CREATED);
				for(T model : createdModels){
					T created = restDataSource.create(model).getResult();
					dbSource.setAsNotDirty(created).await();
				}
				List<T> updatedModels=db.getDirty(DBModelController.DIRTY_STATUS_UPDATED);
				for(T model : updatedModels){
					T updated = restDataSource.update(model).getResult();
					dbSource.setAsNotDirty(updated).await();
				}
				
				deletedEntries.synchronize().await();
				
				return PillowResult.newVoidResult(context);
			} catch (PillowError e) {
				return new PillowResult<Void>(context, e);
			}
		}
		
	}
	
	@Override
	public IPillowResult<Void> sendDirty(){
		return execute(new SendDirtyRunnable());
	}
	
	protected <K> IPillowResult<K> execute(OperationRunnable<K> runnable){
		operationthreadPool.execute(runnable);
		return runnable.getProxyResult();
	}
	
	private class DownloadRunnable extends OperationRunnable<Collection<T>>{
		@Override
		protected IPillowResult<Collection<T>> createMainPillowResult() {
			final PillowResultListener<Collection<T>> result = new PillowResultListener<Collection<T>>(context);
			
			Listener<Collection<T>> fillDatabaseListener = new Listener<Collection<T>>(){
				@Override
				public void onResponse(Collection<T> response) {
					try{
						DBModelController<T> db = getDbModelController();
						db.cacheAll(new ArrayList<T>(response));
						result.setResult(response);
					} catch(Exception e){
						result.setError(new PillowError(e));
					}
				}
			};
			restDataSource.index().setListeners(fillDatabaseListener, result);
			return result;
		}
	}
	
	@Override
	public IPillowResult<Collection<T>> download() {
		return execute(new DownloadRunnable());
	}

	
	public class SetAsNotDirityListener implements Listener<T>{

		@Override
		public void onResponse(T response) {
			dbSource.setAsNotDirty(response);
		}
	}
	
	/*private void setAsNotDirty(T model) {
		DBModelController<T> db =getDbModelController();
		db.markAsClean(model);
		model = db.get(model.getId());
	}*/
	
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
