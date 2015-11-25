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

import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.data.IRestDataSource;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.MultiTaskVoidResult;
import com.mateuyabar.android.pillow.data.core.PillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResultListener;
import com.mateuyabar.android.pillow.data.db.MultiThreadDbDataSource.OperationRunnable;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
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
	IRestDataSource<T> restDataSource;


	IAuthenticationController authenticationData;
	//DeletedEntries<T> deletedEntries;
	ISynchLocalDataSource<T> localDataSource;
	//ILocalSynchDataSource<T> dbModelController;

//	IRestMapping<T> restMap;
	Context context;
	Class<T> modelClass;
	
	//ensures that no more than one syncrhonization process is beeing done at the same time
	Object synchronizationLock;

	public SynchDataSource(Class<T> modelClass, ISynchLocalDataSource<T> localDataSource, IRestDataSource<T> restDataSource, Context context, IAuthenticationController authenticationData) {
		this.modelClass=modelClass;
		this.context=context;
		this.authenticationData=authenticationData;
		this.restDataSource = restDataSource;


//		localDataSource = new DbDataSource<T>(context, dbFuncs, dbHelper, deletedEntries);
//		if(Pillow.getInstance(context).getConfig().isDbMultiThread()){
		this.localDataSource = localDataSource;

		//dbModelController = localDataSource.getDbModelController();

	}

	public SynchDataSource(Class<T> modelClass, ISynchLocalDataSource<T> localDataSource, IRestMapping<T> restMap, Context context, IAuthenticationController authenticationData) {
		this(modelClass, localDataSource, new RestDataSource<T>(restMap, context, authenticationData), context, authenticationData);

	}
	public SynchDataSource(Class<T> modelClass, ISynchLocalDataSource<T> localDataSource, IRestMapping<T> restMap, Context context) {
		this(modelClass, localDataSource, restMap, context, null);
	}
	
	public IRestDataSource<T> getRestDataSource() {
		return restDataSource;
	}
	
	@Override
	public IPillowResult<Collection<T>> index() {
		return localDataSource.index();
	}
	

	
	@Override
	public IPillowResult<T> show(T model) {
		return localDataSource.show(model);
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
		return localDataSource.create(model).addListener(createdOnDbListener);
	}

	public IPillowResult<T> createNow(final T model){
		final PillowResultListener<T> result = new PillowResultListener<>();
		Listener<T> onCreatedListener = new Listener<T>() {
			@Override
			public void onResponse(T response) {
				cache(response);
				result.setResult(response);
			}
		};
		restDataSource.create(model).addListeners(onCreatedListener, result);
		return result;
	}

	protected void cache(T model){
		ArrayList<T> data = new ArrayList<>();
		data.add(model);
		localDataSource.cacheAll(data);
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
		return localDataSource.update(model).addListener(updatedOnDbListener);
	}
	
	@Override
	public IPillowResult<Void> destroy(final T model) {
		//The Pillow result is finished when result created in DB not in server.
		
		Listener<Void> deletedOnDbListener = new Listener<Void>(){
			@Override
			public void onResponse(Void response) {
				sendDirty().addErrorListener(CommonListeners.defaultErrorListener);;
			}
		};
		return localDataSource.destroy(model).addListener(deletedOnDbListener);
		
	}
	
	
	
	private class SendDirtyRunnable extends OperationRunnable<Void>{
		@Override
		protected IPillowResult<Void> createMainPillowResult() {
			try {

				List<T> createdModels= localDataSource.getDirty(ISynchLocalDataSource.DIRTY_STATUS_CREATED);
				for(T model : createdModels){
					T created = null;
					try {
						created = restDataSource.create(model).get();
					}catch (final PillowError createError){
						//Maybe the item has allready been created but the app did not recieve the response.
						//TODO only do this if 500 error
						try {
							T existing = restDataSource.show(model).get();
							//Existing is !=null
							localDataSource.setAsNotDirty(existing);
						}catch (PillowError showError){
							//THe item is not there, we send the original error
							throw createError;
						}
					}
					if(created !=null) {
						localDataSource.setAsNotDirty(created).await();
					}

				}
				List<T> updatedModels= localDataSource.getDirty(ISynchLocalDataSource.DIRTY_STATUS_UPDATED);
				for(T model : updatedModels){
					T updated = restDataSource.update(model).get();
					localDataSource.setAsNotDirty(updated).await();
				}

				//Synchronize deleted entries (delete on server the ones deleted on device)
				MultiTaskVoidResult result = new MultiTaskVoidResult();
				for(T model: localDataSource.getDeletedModelsIds()){
					final String id = model.getId();
					final PillowResultListener<Void> subOperation = new PillowResultListener<Void>();
					Listener<Void> onServerDeletedListener = new Listener<Void>(){
						@Override
						public void onResponse(Void response) {
							localDataSource.setAsDeleted(id);
							subOperation.setResult(null);
						}
					};
					restDataSource.destroy(model).addListeners(onServerDeletedListener, result);
					result.addOperation(subOperation);
				}
				result.setLastOperationAdded();
				result.await();
				
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

						localDataSource.cacheAll(new ArrayList<T>(response));
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
			localDataSource.setAsNotDirty(response);
		}
	}
	
	/*private void setAsNotDirty(T model) {
		DBModelController<T> db =getDbModelController();
		db.markAsClean(model);
		model = db.get(model.getId());
	}*/
	
	/*@Override
	public DBModelController<T> getDbModelController(){
		return dbModelController;
	}*/
	
	public ModelInfo getModelInfo(){
		ModelInfo result = new ModelInfo();

		
		List<T> createdModels= localDataSource.getDirty(ISynchLocalDataSource.DIRTY_STATUS_CREATED);
		result.setDirtyCreatedNum(createdModels.size());
		
		List<T> updatedModels= localDataSource.getDirty(ISynchLocalDataSource.DIRTY_STATUS_UPDATED);
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

	public ISynchLocalDataSource<T> getLocalDataSource() {
		return localDataSource;
	}

	@Override
	public Class<T> getModelClass() {
		return modelClass;
	}
	public Context getContext() {
		return context;
	}

}
