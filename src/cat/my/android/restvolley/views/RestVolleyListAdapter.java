package cat.my.android.restvolley.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cat.my.android.restvolley.IDataSource;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.Listeners.CollectionListener;
import cat.my.android.restvolley.RestVolley;

import cat.my.android.restvolley.sync.DummyListeners;
import cat.my.android.restvolley.sync.SynchDataSource;

import cat.my.android.restvolley.Listeners.ErrorListener;



public abstract class RestVolleyListAdapter<T extends IdentificableModel> extends BaseAdapter {
	Context context;
	List<T> models = new ArrayList<T>();
	IDataSource<T> dataSource;
	ErrorListener donwloadErrorListener = DummyListeners.dummyErrorListener;
	ErrorListener refreshListErrorListener = DummyListeners.dummyErrorListener;

	public RestVolleyListAdapter(Context context, Class<T> clazz) {
		super();
		this.context = context;
		this.dataSource = (IDataSource<T>) RestVolley.getInstance(context).getDataSource(clazz);
	}

	public void refreshList(){
		CollectionListener<T> listener = new CollectionListener<T>(){
			@Override
			public void onResponse(Collection<T> postsResponse) {
				models.clear();
				models.addAll(postsResponse);
				notifyDataSetChanged();
			}
		};
		dataSource.index(listener, refreshListErrorListener);
	}
	
	public void downloadData(){
		CollectionListener<T> listener = new CollectionListener<T>(){
			@Override
			public void onResponse(Collection<T> postsResponse) {
				refreshList();
			}
		};
		
		((SynchDataSource<T>)dataSource).download(listener, donwloadErrorListener);
	}

	@Override
	public int getCount() {
		return models.size();
	}

	@Override
	public T getItem(int position) {
		return models.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getView(getItem(position), convertView, parent);
	}
	
	public abstract View getView(T model, View convertView, ViewGroup parent);

	public void setOps(IDataSource<T> ops) {
		this.dataSource = ops;
	}

	public void setRefreshListErrorListener(ErrorListener refreshListErrorListener) {
		this.refreshListErrorListener = refreshListErrorListener;
	}

	public Context getContext() {
		return context;
	}

	public IDataSource<T> getDataSource() {
		return dataSource;
	}

}
