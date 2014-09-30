package cat.my.android.restvolley;

import java.util.Collection;


import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public interface IDataSource <T extends IdentificableModel>{
	public void index(Listener<Collection<T>> listener, ErrorListener errorListener);

	public void show(T model, Listener<T> listener, ErrorListener errorListener);
	
	public void create(T model, Listener<T> listener, ErrorListener errorListener);
	
	public void update(T model, Listener<T> listener, ErrorListener errorListener);
	
	public void destroy(T model, Listener<Void> listener, ErrorListener errorListener);
}
