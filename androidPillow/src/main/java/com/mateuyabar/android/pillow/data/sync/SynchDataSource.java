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

import android.content.Context;

import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResultListener;
import com.mateuyabar.android.pillow.data.db.DBModelController;
import com.mateuyabar.android.pillow.data.db.DbDataSource;
import com.mateuyabar.android.pillow.data.db.IDBDataSourceForSynch;
import com.mateuyabar.android.pillow.data.db.IDbMapping;
import com.mateuyabar.android.pillow.data.db.MultiThreadDbDataSource;
import com.mateuyabar.android.pillow.data.db.MultiThreadDbDataSource.OperationRunnable;
import com.mateuyabar.android.pillow.data.rest.IAuthenticationController;
import com.mateuyabar.android.pillow.data.rest.IRestMapping;
import com.mateuyabar.android.pillow.data.rest.RestDataSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SynchDataSource<T extends IdentificableModel> implements ISynchDataSource<T>{
	private static ThreadPoolExecutor operationthreadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	
	RestDataSource<T> restDataSource;
	IAuthenticationController authenticationData;
	DeletedEntries<T> deletedEntries;
	IDBDataSourceForSynch<T> dbSource;
	DBModelController<T> dbModelController;
	IDbMapping<T> dbFuncs;
	IRestMapping<T> restMap;
	Context context;
	
	//ensures that no more than one syncrhonization process is beeing done at the same time
	Object synchronizationLock;
	
	public SynchDataSource(IDbMapping<T> dbFuncs, IRestMapping<T> restMap, Context context, IAuthenticationController authenticationData) {
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
				sendDirty().addErrorListener(CommonListeners.defaultErrorListener);
				//Listener<T> myListener = new SetAsNotDirityListener();
				//restDataSource.create(model).addListeners(myListener, CommonListeners.getDefaultThreadedErrorListener());
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
				sendDirty().addErrorListener(CommonListeners.defaultErrorListener);;
				//Listener<T> myListener = new SetAsNotDirityListener();
				//restDataSource.update(model).addListeners(myListener, CommonListeners.getDefaultThreadedErrorListener());
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
				sendDirty().addErrorListener(CommonListeners.defaultErrorListener);;
//				deletedEntries.setAllreadyDeleted(model).addListeners(CommonListeners.dummyListener, CommonListeners.getDefaultThreadedErrorListener());
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
					T created = restDataSource.create(model).get();
					dbSource.setAsNotDirty(created).await();
				}
				List<T> updatedModels=db.getDirty(DBModelController.DIRTY_STATUS_UPDATED);
				for(T model : updatedModels){
					T updated = restDataSource.update(model).get();
					dbSource.setAsNotDirty(updated).await();
				}
				
				deletedEntries.synchronize().await();
				
				return PillowResult.newVoidResult();
			} catch (PillowError e) {
				return new PillowResult<Void>(e);
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
			final PillowResultListener<Collection<T>> result = new PillowResultListener<Collection<T>>();
			
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
			restDataSource.index().addListeners(fillDatabaseListener, result);
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
