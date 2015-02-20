package cat.my.android.pillow.view.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cat.my.android.pillow.IDataSource;
import cat.my.android.pillow.IExtendedDataSource;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.data.sync.CommonListeners;
import cat.my.android.pillow.data.sync.SynchDataSource;





public abstract class PillowBaseListAdapter<T extends IdentificableModel> extends BaseAdapter implements IModelListAdapter<T> {
	T filter;
	Context context;
	protected List<T> models = new ArrayList<T>();
	IDataSource<T> dataSource;
	ErrorListener donwloadErrorListener = CommonListeners.defaultErrorListener;
	ErrorListener refreshListErrorListener = CommonListeners.defaultErrorListener;

	public PillowBaseListAdapter(Context context, Class<T> clazz) {
		super();
		this.context = context;
		this.dataSource = (IDataSource<T>) Pillow.getInstance(context).getDataSource(clazz);
	}

	public void refreshList(){
		Listener<Collection<T>> listener = new Listener<Collection<T>>(){
			@Override
			public void onResponse(Collection<T> postsResponse) {
				models.clear();
				models.addAll(postsResponse);
				notifyDataSetChanged();
			}
		};
		dataSourceIndex(listener, CommonListeners.defaultErrorListener);
	}
	
//	public void downloadData(){
//		Listener<Collection<T>> listener = new Listener<Collection<T>>(){
//			@Override
//			public void onResponse(Collection<T> postsResponse) {
//				refreshList();
//			}
//		};
//		((SynchDataSource<T>)dataSource).download(listener, donwloadErrorListener);
//	}
	
	public void dataSourceIndex(Listener<Collection<T>> listener, ErrorListener errorListener){
		if(filter!=null && dataSource instanceof IExtendedDataSource)
			((IExtendedDataSource<T>)dataSource).index(filter).setViewListeners(listener, errorListener);
		else
			dataSource.index().setViewListeners(listener, errorListener);
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

	
//
//	public void setRefreshListErrorListener(ErrorListener refreshListErrorListener) {
//		this.refreshListErrorListener = refreshListErrorListener;
//	}

	protected Context getContext() {
		return context;
	}

	protected IDataSource<T> getDataSource() {
		return dataSource;
	}
	
	public void setFilter(T filter) {
		this.filter = filter;
	}

}
