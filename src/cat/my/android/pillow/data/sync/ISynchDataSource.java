package cat.my.android.pillow.data.sync;

import java.util.Collection;

import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.data.db.IDBDataSource;
import cat.my.android.pillow.data.db.IDbMapping;
import cat.my.android.pillow.data.rest.RestDataSource;


public interface ISynchDataSource<T extends IdentificableModel> extends IDBDataSource<T> {
	public void sendDirty(Listener<Void> listener, ErrorListener errorListener);
	public void download(final Listener<Collection<T>> listener, ErrorListener errorListener);
	public Class<T> getModelClass();
	public RestDataSource<T> getRestDataSource();
	public IDbMapping<T> getDbFuncs();
}
