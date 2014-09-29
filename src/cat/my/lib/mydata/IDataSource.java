package cat.my.lib.mydata;

import java.lang.reflect.Type;
import java.util.Collection;

import cat.my.lib.restvolley.models.IdentificableModel;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public interface IDataSource {
	public <T extends IdentificableModel> void index(Class<T> clazz, Type collectionType, Listener<Collection<T>> listener, ErrorListener errorListener);

	public <T extends IdentificableModel> void show(Class<T> clazz, T model, Listener<T> listener, ErrorListener errorListener);
	
	public <T extends IdentificableModel> void create(Class<T> clazz, T model, Listener<T> listener, ErrorListener errorListener);
	
	public <T extends IdentificableModel> void update(Class<T> clazz, T model, Listener<T> listener, ErrorListener errorListener);
	
	public <T extends IdentificableModel> void destroy(Class<T> clazz, T model, Listener<Void> listener, ErrorListener errorListener);
}
