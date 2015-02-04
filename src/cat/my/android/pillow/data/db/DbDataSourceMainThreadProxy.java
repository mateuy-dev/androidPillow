package cat.my.android.pillow.data.db;

import java.util.Collection;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.data.sync.CommonListeners.ExecuteOnMainThreadProxyListener;
import cat.my.android.pillow.data.sync.DeletedEntries;


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
	
	@Override
	public void count(String selection, String[] selectionArgs, Listener<Integer> listener,
			ErrorListener errorListener) {
		dataSource.count(selection, selectionArgs, new ExecuteOnMainThreadProxyListener<Integer>(getContext(), listener), errorListener);
	}

	public DBModelController<T> getDbModelController() {
		return dataSource.getDbModelController();
	}
	
	public Context getContext() {
		return dataSource.getContext();
	}

	

}
