package cat.my.lib.mydata.controllers;

import java.util.Collection;

import cat.my.lib.mydata.SynchDataSource;
import cat.my.lib.restvolley.models.IdentificableModel;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public abstract class SyncModelController<T extends IdentificableModel> extends ModelController<T>{
	public abstract SynchDataSource<T> getDataSource();
	
	public void sendDirty(){
		getDataSource().sendDirty();
	}
	
	public void download(Listener<Collection<T>> listener, ErrorListener errorListener) {
		getDataSource().download(listener, errorListener);
	}
}
