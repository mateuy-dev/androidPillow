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

import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResultListener;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MultiThreadDbDataSource<T extends IdentificableModel> implements ISynchLocalDbDataSource<T> {
	private static ThreadPoolExecutor dbThreadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
//	static ThreadPoolExecutor threadPoolExecutor = new FullStackThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

	ISynchLocalDbDataSource<T> dataSource;

	public MultiThreadDbDataSource(ISynchLocalDbDataSource<T> dataSource) {
		super();
		this.dataSource=dataSource;
	}

	public abstract static class OperationRunnable<L> implements Runnable{
		PillowResultListener<L> proxyResult;
		public OperationRunnable() {
			this.proxyResult = new PillowResultListener<L>();
		}
		public PillowResultListener<L> getProxyResult() {
			return proxyResult;
		}
		public void run(){
			try {
				createMainPillowResult().addListeners(proxyResult, proxyResult);
			} catch (PillowError e) {
				proxyResult.setError(e);
			}
		}
		protected abstract IPillowResult<L> createMainPillowResult() throws PillowError;
	}
	
	private <K> IPillowResult<K> execute(OperationRunnable<K> runnable){
		dbThreadPool.execute(runnable);
		return runnable.getProxyResult();
	}
	
	
	@Override
	public IPillowResult<Collection<T>> index() {
		return execute(new OperationRunnable<Collection<T>>() {
			@Override
			public IPillowResult<Collection<T>> createMainPillowResult() {
				return dataSource.index();
			}
		});
	}
	
	
	@Override
	public IPillowResult<Collection<T>> index(final T model) {
		return execute(new OperationRunnable<Collection<T>>(){
			@Override public IPillowResult<Collection<T>> createMainPillowResult() {
				return dataSource.index(model);
			}
		});
	}



	@Override
	public IPillowResult<Collection<T>> index(final String selection, final String[] selectionArgs, final String order) {
		return execute(new OperationRunnable<Collection<T>>(){
			@Override
			protected IPillowResult<Collection<T>> createMainPillowResult() {
				return dataSource.index(selection, selectionArgs, order);
			}
			
		});
	}

	@Override
	public IPillowResult<T> show(final T model) {
		return execute(new OperationRunnable<T>() {
			@Override
			protected IPillowResult<T> createMainPillowResult() {
				return dataSource.show(model);
			}
		});
	}
	
	@Override
	public IPillowResult<T> create(final T model) {
		return execute(new OperationRunnable<T>(){
			@Override
			protected IPillowResult<T> createMainPillowResult() {
				return dataSource.create(model);
			}
		});
	}
	
	@Override
	public IPillowResult<T> update(final T model) {
		return execute(new OperationRunnable<T>(){
			@Override
			protected IPillowResult<T> createMainPillowResult() {
				return dataSource.update(model);
			}
		});
	}
	
	@Override
	public IPillowResult<Void> destroy(final T model) {
		return execute(new OperationRunnable<Void>(){
			@Override
			protected IPillowResult<Void> createMainPillowResult() {
				return dataSource.destroy(model);
			}
		});
	}



	@Override
	public IPillowResult<Integer> count(final String selection, final String[] selectionArgs) {
		return execute(new OperationRunnable<Integer>() {
			@Override
			protected IPillowResult<Integer> createMainPillowResult() {
				return dataSource.count(selection, selectionArgs);
			}
		});
	}

	@Override
	public IPillowResult<Integer> count(final T filter) {
		return execute(new OperationRunnable<Integer>() {
			@Override
			protected IPillowResult<Integer> createMainPillowResult() {
				return dataSource.count(filter);
			}
		});
	}


	@Override
	public IPillowResult<T> setAsNotDirty(final T model) {
		return execute(new OperationRunnable<T>() {
			@Override
			protected IPillowResult<T> createMainPillowResult() throws PillowError {
				return dataSource.setAsNotDirty(model);
			}
		});
	}


	@Override
	public List<T> getDirty(int dirtyType) {
		return dataSource.getDirty(dirtyType);
	}

	@Override
	public void cacheAll(List<T> models) {
		dataSource.cacheAll(models);
	}

	@Override
	public List<T> getDeletedModelsIds() {
		return dataSource.getDeletedModelsIds();
	}

	@Override
	public void setAsDeleted(String id) {
		dataSource.setAsDeleted(id);
	}

	@Override
	public IDbMapping<T> getDbMapping() {
		return dataSource.getDbMapping();
	}

	@Override
	public DBModelController<T> getDbModelController() {
		return dataSource.getDbModelController();
	}
}
