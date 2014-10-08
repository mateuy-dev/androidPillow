package cat.my.android.restvolley.sync;

import java.util.Collection;
import java.util.List;

import cat.my.android.restvolley.db.DBModelController;
import cat.my.android.restvolley.sync.SynchDataSource.SetAsNotDirityListener;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public interface ISynchDataSource<T> {
	public void sendDirty();
	public void download(final Listener<Collection<T>> listener, ErrorListener errorListener);

}
