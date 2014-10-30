package cat.my.android.restvolley.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.database.sqlite.SQLiteOpenHelper;
import cat.my.android.restvolley.IDataSource;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.listeners.EventDispatcher;
import cat.my.android.restvolley.listeners.IModelUpdatedListener;
import cat.my.android.restvolley.sync.DeletedEntries;


public class MTDbDataSource<T extends IdentificableModel> implements IDBDataSource<T>{
	ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	IDBDataSource<T> dataSource;

	public MTDbDataSource(IDBDataSource<T> dataSource) {
		super();
		this.dataSource=dataSource;
	}

	private abstract class OperationRunnable<L> implements Runnable{
		L listener;
		ErrorListener errorListener;
		public OperationRunnable(L listener, ErrorListener errorListener) {
			super();
			this.listener = listener;
			this.errorListener = errorListener;
		}
	}
	
	public class SimpleIndexRunnable extends OperationRunnable<Listener<Collection<T>>>{
		public SimpleIndexRunnable(Listener<Collection<T>> listener, ErrorListener errorListener) {
			super(listener, errorListener);
		}
		@Override
		public void run() {
			dataSource.index(listener, errorListener);
		}
	}
	
	@Override
	public void index(Listener<Collection<T>> listener, ErrorListener errorListener) {
		threadPoolExecutor.execute(new SimpleIndexRunnable(listener, errorListener));
	}
	
	public class ComplexIndexRunnable extends OperationRunnable<Listener<Collection<T>>>{
		String selection; String[] selectionArgs; String order;
		public ComplexIndexRunnable(String selection, String[] selectionArgs, String order, Listener<Collection<T>> listener, ErrorListener errorListener) {
			super(listener, errorListener);
			this.selection=selection; this.selectionArgs=selectionArgs; this.order=order;
		}
		@Override
		public void run() {
			dataSource.index(selection, selectionArgs, order, listener, errorListener);
		}
	}
	
	@Override
	public void index(String selection, String[] selectionArgs, String order, Listener<Collection<T>> listener, ErrorListener errorListener) {
		threadPoolExecutor.execute(new ComplexIndexRunnable(selection, selectionArgs, order, listener, errorListener));
	}
	
	public class ShowRunnable extends OperationRunnable<Listener<T>>{
		T model;
		public ShowRunnable(T model, Listener<T> listener, ErrorListener errorListener) {
			super(listener, errorListener);
			this.model = model;
		}
		@Override
		public void run() {
			dataSource.show(model, listener, errorListener);
		}
	}

	@Override
	public void show(T model, Listener<T> listener, ErrorListener errorListener) {
		threadPoolExecutor.execute(new ShowRunnable(model, listener, errorListener));
	}

	public class CreateRunnable extends OperationRunnable<Listener<T>>{
		T model;
		public CreateRunnable(T model, Listener<T> listener, ErrorListener errorListener) {
			super(listener, errorListener);
			this.model = model;
		}
		@Override
		public void run() {
			dataSource.create(model, listener, errorListener);
		}
	}
	
	@Override
	public void create(T model, Listener<T> listener, ErrorListener errorListener) {
		threadPoolExecutor.execute(new CreateRunnable(model, listener, errorListener));
	}
	
	public class UpdateRunnable extends OperationRunnable<Listener<T>>{
		T model;
		public UpdateRunnable(T model, Listener<T> listener, ErrorListener errorListener) {
			super(listener, errorListener);
			this.model = model;
		}
		@Override
		public void run() {
			dataSource.update(model, listener, errorListener);
		}
	}

	@Override
	public void update(T model, Listener<T> listener, ErrorListener errorListener) {
		threadPoolExecutor.execute(new UpdateRunnable(model, listener, errorListener));
	}
	
	public class DestroyRunnable extends OperationRunnable<Listener<Void>>{
		T model;
		public DestroyRunnable(T model, Listener<Void> listener, ErrorListener errorListener) {
			super(listener, errorListener);
			this.model = model;
		}
		@Override
		public void run() {
			dataSource.destroy(model, listener, errorListener);
		}
	}

	@Override
	public void destroy(T model, Listener<Void> listener,
			ErrorListener errorListener) {
		threadPoolExecutor.execute(new DestroyRunnable(model, listener, errorListener));
	}
	
	public DBModelController<T> getDbModelController(){
		return dataSource.getDbModelController();
	}

	public EventDispatcher<T> getEventDispatcher() {
		return dataSource.getEventDispatcher();
	}

}
