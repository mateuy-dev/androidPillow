package com.mateuyabar.android.pillow.data.db;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResultListener;


public class MultiThreadDbDataSource<T extends IdentificableModel> implements IDBDataSourceForSynch<T>{
	private static ThreadPoolExecutor dbThreadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
//	static ThreadPoolExecutor threadPoolExecutor = new FullStackThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

	IDBDataSourceForSynch<T> dataSource;

	public MultiThreadDbDataSource(IDBDataSourceForSynch<T> dataSource) {
		super();
		this.dataSource=dataSource;
	}

	public abstract static class OperationRunnable<L> implements Runnable{
		PillowResultListener<L> proxyResult;
		public OperationRunnable() {
			this.proxyResult = new PillowResultListener<L>(Pillow.getInstance().getContext());
		}
		public PillowResultListener<L> getProxyResult() {
			return proxyResult;
		}
		public void run(){
			try {
				createMainPillowResult().setListeners(proxyResult, proxyResult);
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
		return execute(new OperationRunnable<Collection<T>>(){
			@Override public IPillowResult<Collection<T>> createMainPillowResult() {
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
		return execute(new OperationRunnable<Integer>(){
			@Override
			protected IPillowResult<Integer> createMainPillowResult() {
				return dataSource.count(selection, selectionArgs);
			}
		});
	}

	
	public DBModelController<T> getDbModelController(){
		return dataSource.getDbModelController();
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

}
