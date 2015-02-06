package cat.my.android.pillow.data.sync;

import java.util.Collection;

import android.content.Context;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.data.db.DBModelController;
import cat.my.android.pillow.data.db.IDbMapping;
import cat.my.android.pillow.data.rest.IRestMapping;
import cat.my.android.pillow.data.rest.ISessionController;
import cat.my.android.pillow.data.rest.RestDataSource;
import cat.my.android.pillow.data.sync.CommonListeners.ExecuteOnMainThreadProxyListener;
import cat.my.android.pillow.data.sync.SynchDataSource.ModelInfo;

public class SynchDataSourceMainThreadProxy<T extends IdentificableModel> implements ISynchDataSource<T>{
	SynchDataSource<T>  dataSource;

	public SynchDataSourceMainThreadProxy(SynchDataSource<T> dataSource) {
		this.dataSource = dataSource;
	}

	public SynchDataSourceMainThreadProxy(IDbMapping<T> dbFuncs, IRestMapping<T> restMap, Context context,
			ISessionController authenticationData) {
		dataSource = new SynchDataSource<T>(dbFuncs, restMap, context, authenticationData);
	}

	public SynchDataSourceMainThreadProxy(IDbMapping<T> dbFuncs, IRestMapping<T> restMap, Context context) {
		dataSource = new SynchDataSource<T>(dbFuncs, restMap, context);
	}

	public SynchDataSource<T> getDataSource() {
		return dataSource;
	}

	public void index(Listener<Collection<T>> listener, ErrorListener errorListener) {
		dataSource.index(new ExecuteOnMainThreadProxyListener<Collection<T>>(getContext(),listener), errorListener);
	}
	
	public void index(T filter, Listener<Collection<T>> listener, ErrorListener errorListener) {
		dataSource.index(filter, new ExecuteOnMainThreadProxyListener<Collection<T>>(getContext(),listener), errorListener);
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
		dataSource.count(selection, selectionArgs, new ExecuteOnMainThreadProxyListener<Integer>(getContext(),listener), errorListener);
	}

	public void sendDirty(Listener<Void> listener, ErrorListener errorListener) {
		dataSource.sendDirty(listener, errorListener);
	}

	public void download(Listener<Collection<T>> listener, ErrorListener errorListener) {
		dataSource.download(listener, errorListener);
	}


	public ModelInfo getModelInfo() {
		return dataSource.getModelInfo();
	}

	public Class<T> getModelClass() {
		return dataSource.getModelClass();
	}

	public RestDataSource<T> getRestDataSource() {
		return dataSource.getRestDataSource();
	}

	public DBModelController<T> getDbModelController() {
		return dataSource.getDbModelController();
	}

	public IDbMapping<T> getDbFuncs() {
		return dataSource.getDbFuncs();
	}

	public Context getContext() {
		return dataSource.getContext();
	}
	
	

	
}
