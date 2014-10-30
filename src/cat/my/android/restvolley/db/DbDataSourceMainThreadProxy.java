package cat.my.android.restvolley.db;

import java.util.Collection;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.listeners.EventDispatcher;
import cat.my.android.restvolley.sync.DeletedEntries;
import cat.my.android.restvolley.sync.CommonListeners.ExecuteOnMainThreadProxyListener;

public class DbDataSourceMainThreadProxy <T extends IdentificableModel> implements IDBDataSource<T>{
	DbDataSource<T> dataSource;
	
	public DbDataSourceMainThreadProxy(DbDataSource<T> dataSource) {
		this.dataSource = dataSource;
	}

	public DbDataSourceMainThreadProxy(Context context, IDbMapping<T> funcs, SQLiteOpenHelper dbHelper, DeletedEntries<T> deletedEntries) {
		this(new DbDataSource<T>(context, funcs, dbHelper, deletedEntries));
	}

	public DbDataSource<T> getDataSource() {
		return dataSource;
	}

	public void index(Listener<Collection<T>> listener, ErrorListener errorListener) {
		dataSource.index(new ExecuteOnMainThreadProxyListener<Collection<T>>(getContext(),listener) , errorListener);
	}

	public void index(String selection, String[] selectionArgs, String order, Listener<Collection<T>> listener,
			ErrorListener errorListener) {
		dataSource.index(selection, selectionArgs, order, new ExecuteOnMainThreadProxyListener<Collection<T>>(getContext(),listener), errorListener);
	}

	public void show(T model, Listener<T> listener, ErrorListener errorListener) {
		dataSource.show(model, new ExecuteOnMainThreadProxyListener<T>(getContext(),listener), errorListener);
	}

	public void create(T model, Listener<T> listener, ErrorListener errorListener) {
		dataSource.create(model, new ExecuteOnMainThreadProxyListener<T>(getContext(),listener), errorListener);
	}

	public void update(T model, Listener<T> listener, ErrorListener errorListener) {
		dataSource.update(model, new ExecuteOnMainThreadProxyListener<T>(getContext(),listener), errorListener);
	}

	public void destroy(T model, Listener<Void> listener, ErrorListener errorListener) {
		dataSource.destroy(model, new ExecuteOnMainThreadProxyListener<Void>(getContext(),listener), errorListener);
	}

	public DBModelController<T> getDbModelController() {
		return dataSource.getDbModelController();
	}

	public EventDispatcher<T> getEventDispatcher() {
		return dataSource.getEventDispatcher();
	}
	
	public Context getContext() {
		return dataSource.getContext();
	}

}
