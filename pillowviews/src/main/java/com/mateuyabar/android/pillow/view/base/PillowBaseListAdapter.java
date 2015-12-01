/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.mateuyabar.android.pillow.view.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.IExtendedDataSource;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.Listeners.ErrorListener;
import com.mateuyabar.android.pillow.Listeners.ViewListener;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.sync.CommonListeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;





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
	Pillow pillow;

	public PillowBaseListAdapter(Context context, Class<T> clazz) {
		super();
		this.context = context;
		this.pillow  = Pillow.getInstance(context);
		this.dataSource = (IDataSource<T>) pillow.getDataSource(clazz);
	}

	public void refreshList(){
		ViewListener<Collection<T>> listener = new ViewListener<Collection<T>>(){
			@Override
			public void onResponse(Collection<T> postsResponse) {
				onModelsLoaded(postsResponse);
			}
		};
		dataSourceIndex().addListeners(listener, CommonListeners.defaultErrorListener);
	}

	protected void onModelsLoaded(Collection<T> postsResponse) {
		if(postsResponse instanceof List){
			models = (List)postsResponse;
		} else {
			models.clear();
			models.addAll(postsResponse);
		}
		notifyDataSetChanged();
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

	protected Pillow getPillow(){
		return pillow;
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

	public void setModels(List<T> models) {
		this.models = models;
	}
}
