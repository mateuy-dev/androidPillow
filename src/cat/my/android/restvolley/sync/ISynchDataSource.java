package cat.my.android.restvolley.sync;

import java.util.Collection;
import java.util.List;


import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.db.IDBDataSource;
import cat.my.android.restvolley.sync.SynchDataSource.SetAsNotDirityListener;

import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.Listeners.Listener;

public interface ISynchDataSource<T extends IdentificableModel> extends IDBDataSource<T> {
	public void sendDirty();
	public void download(final Listener<Collection<T>> listener, ErrorListener errorListener);
	public Class<T> getModelClass();
}
