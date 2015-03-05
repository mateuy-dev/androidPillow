package cat.my.android.pillow.view.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import cat.my.android.pillow.IDataSource;
import cat.my.android.pillow.IExtendedDataSource;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.Listeners.ViewListener;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.data.core.IPillowResult;
import cat.my.android.pillow.data.sync.CommonListeners;





public abstract class PillowBaseListAdapter<T extends IdentificableModel> extends BaseAdapter implements IModelListAdapter<T>, Filterable {
	T filter;
	Context context;
	List<T> models = new ArrayList<T>();
	//In case that a filter is used, it contains the models before the filtering was executed
	List<T> originalModels;
	IDataSource<T> dataSource;
	ErrorListener donwloadErrorListener = CommonListeners.defaultErrorListener;
	ErrorListener refreshListErrorListener = CommonListeners.defaultErrorListener;
	BasicFilter basicFilter = new BasicFilter();

	public PillowBaseListAdapter(Context context, Class<T> clazz) {
		super();
		this.context = context;
		this.dataSource = (IDataSource<T>) Pillow.getInstance(context).getDataSource(clazz);
	}

	public void refreshList(){
		ViewListener<Collection<T>> listener = new ViewListener<Collection<T>>(){
			@Override
			public void onResponse(Collection<T> postsResponse) {
				models.clear();
				models.addAll(postsResponse);
				notifyDataSetChanged();
			}
		};
		dataSourceIndex().setListeners(listener, CommonListeners.defaultErrorListener);
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
	
	public IPillowResult<Collection<T>> dataSourceIndex(){
		if(filter!=null && dataSource instanceof IExtendedDataSource)
			return ((IExtendedDataSource<T>)dataSource).index(filter);
		else
			return dataSource.index();
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

	@Override
	public Filter getFilter() {
		return basicFilter;
	}
	
	/**
	 * Filters the results using the tostring method.
	 */
	private class BasicFilter extends Filter{
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			if (originalModels == null && !models.isEmpty()) {
				originalModels = new ArrayList<T>(models);
			}
			
			FilterResults results = new FilterResults();
			if (originalModels == null) {
				results.values = new ArrayList();
				results.count = 0;
			} else if  (constraint == null || constraint.length() == 0) {
				results.values = originalModels;
				results.count = originalModels.size();
			} else {
				List<T> startWith = new ArrayList<T>();
				List<T> contains = new ArrayList<T>();
				for (T model : originalModels) {
					String modelString = model.toString().toLowerCase();
					String constrainString = constraint.toString().toLowerCase();
					if (modelString.startsWith(constrainString)) {
						startWith.add(model);
					} else  if(modelString.contains(constrainString)){
						contains.add(model);
					}
				}
				List<T> result = new ArrayList<T>(startWith);
				result.addAll(contains);
				results.values = result;
				results.count = result.size();

			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			if (originalModels == null && !models.isEmpty()) {
				originalModels = new ArrayList<T>(models);
			}
			models.clear();
			models.addAll((Collection<? extends T>) results.values);
			notifyDataSetChanged();
		}
	}
}
