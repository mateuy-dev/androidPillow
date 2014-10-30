package cat.my.android.restvolley.db;

import java.util.Collection;

import cat.my.android.restvolley.IDataSource;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.listeners.EventDispatcher;

public interface IDBDataSource<T extends IdentificableModel> extends IDataSource<T>{

	void index(String selection, String[] selectionArgs, String order, Listener<Collection<T>> listener,
			ErrorListener errorListener);
	public DBModelController<T> getDbModelController();
	public EventDispatcher<T> getEventDispatcher();
	

}
