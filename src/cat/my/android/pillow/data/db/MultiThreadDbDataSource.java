package cat.my.android.pillow.data.db;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;

import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.PillowError;
import cat.my.android.pillow.data.core.IPillowResult;
import cat.my.android.pillow.data.core.PillowResult;
import cat.my.android.pillow.data.core.ProxyPillowResult;
import cat.my.android.pillow.util.concurrency.FullStackThreadPoolExecutor;


public class MultiThreadDbDataSource<T extends IdentificableModel> implements IDBDataSourceForSynch<T>{
	private static ThreadPoolExecutor dbThreadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
//	static ThreadPoolExecutor threadPoolExecutor = new FullStackThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

	IDBDataSourceForSynch<T> dataSource;

	public MultiThreadDbDataSource(IDBDataSourceForSynch<T> dataSource) {
		super();
		this.dataSource=dataSource;
	}

	public abstract static class OperationRunnable<L> implements Runnable{
		ProxyPillowResult<L> proxyResult;
		public OperationRunnable() {
			this.proxyResult = new ProxyPillowResult<L>();
		}
		public ProxyPillowResult<L> getProxyResult() {
			return proxyResult;
		}
		public void run(){
			try {
				proxyResult.setMainPillowResult(createMainPillowResult());
			} catch (PillowError e) {
				Context context = Pillow.getInstance().getContext();
				proxyResult.setMainPillowResult(new PillowResult<L>(context, e));
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
