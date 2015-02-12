package cat.my.android.pillow;

import java.util.Collection;

import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;

public interface IExtendedDataSource<T extends IdentificableModel> extends IDataSource<T>{
	public void index(T filter, Listener<Collection<T>> listener, ErrorListener errorListener);

}
