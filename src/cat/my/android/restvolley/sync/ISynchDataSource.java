package cat.my.android.restvolley.sync;

import java.util.Collection;
import java.util.List;

import cat.my.android.restvolley.db.DBModelController;
import cat.my.android.restvolley.sync.SynchDataSource.SetAsNotDirityListener;

import cat.my.android.restvolley.Listeners.CollectionListener;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.Listeners.Listener;

public interface ISynchDataSource<T> {
	public void sendDirty();
	public void download(final CollectionListener<T> listener, ErrorListener errorListener);
	public Class<T> getModelClass();
}
