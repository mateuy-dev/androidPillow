package cat.my.lib.mydata.controllers;

import java.util.Collection;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import cat.my.lib.mydata.SynchDataSource;
import cat.my.lib.restvolley.models.IdentificableModel;

public abstract class SyncModelController<T extends IdentificableModel> extends ModelController<T>{
	public abstract SynchDataSource getDataSource();
	
	public void sendDirty(){
		getDataSource().sendDirty(getModelClass());
	}
	
	public void download(Listener<Collection<T>> listener, ErrorListener errorListener) {
		getDataSource().download(getModelClass(), getCollectionType(), listener, errorListener);
	}
}
