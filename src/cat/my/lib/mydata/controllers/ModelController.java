package cat.my.lib.mydata.controllers;

import java.util.Collection;

import cat.my.lib.mydata.IDataSource;
import cat.my.lib.restvolley.models.IdentificableModel;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public abstract class ModelController<T extends IdentificableModel> {
	public abstract IDataSource<T> getDataSource();
	
	public void index(Listener<Collection<T>> listener, ErrorListener errorListener) {
		getDataSource().index(listener, errorListener);
	}
	
	public void show(T model, Listener<T> listener, ErrorListener errorListener) {
		getDataSource().show(model, listener, errorListener);
	}
	
	public void create(T model, Listener<T> listener, ErrorListener errorListener) {
		getDataSource().create(model, listener, errorListener);
	}
	
	public void update(T model, Listener<T> listener, ErrorListener errorListener) {
		getDataSource().update(model, listener, errorListener);
	}
	
	public void destroy(T model, Listener<Void> listener, ErrorListener errorListener) {
		getDataSource().destroy(model, listener, errorListener);
	}

}
