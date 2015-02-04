package cat.my.android.pillow.data.db;

import java.util.Collection;

import cat.my.android.pillow.IDataSource;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;

public interface IDBDataSource<T extends IdentificableModel> extends IDataSource<T>{

	void index(String selection, String[] selectionArgs, String order, Listener<Collection<T>> listener,
			ErrorListener errorListener);
	public DBModelController<T> getDbModelController();
	void count(String selection, String[] selectionArgs, Listener<Integer> listener,
			ErrorListener errorListener);
	

}
