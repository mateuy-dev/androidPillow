package cat.my.lib.mydata;

import java.lang.reflect.Type;
import java.util.Collection;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import cat.my.lib.restvolley.models.IdentificableModel;

public abstract class ModelController<T extends IdentificableModel> {
	public abstract Class<T> getModelClass();
	public abstract Type getCollectionType();
	public abstract IDataSource getDataSource();
	
	public void index(Listener<Collection<T>> listener, ErrorListener errorListener) {
		getDataSource().index(getModelClass(), getCollectionType(), listener, errorListener);
	}
	
	public void show(T model, Listener<T> listener, ErrorListener errorListener) {
		getDataSource().show(getModelClass(), model, listener, errorListener);
	}
	
	public void create(T model, Listener<T> listener, ErrorListener errorListener) {
		getDataSource().create(getModelClass(), model, listener, errorListener);
	}
	
	public void update(T model, Listener<T> listener, ErrorListener errorListener) {
		getDataSource().update(getModelClass(), model, listener, errorListener);
	}
	
	public void destroy(T model, Listener<Void> listener, ErrorListener errorListener) {
		getDataSource().destroy(getModelClass(), model, listener, errorListener);
	}

}
