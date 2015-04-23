package com.mateuyabar.android.pillow.view.forms.inputDatas;

import java.util.Collection;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.mateuyabar.android.pillow.IDataSource;
import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.Listeners.ErrorListener;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.Listeners.ViewListener;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.data.sync.CommonListeners;
import com.mateuyabar.android.pillow.view.forms.BelongsToInputData;

public class IdentificableModelSpinnerInputData<T extends IdentificableModel> extends AbstractSpinnerInputData<T> implements BelongsToInputData<T>{
	Class<T> parentClass;
	ArrayAdapter<T> adapter;
	
	public IdentificableModelSpinnerInputData() {
		
	}
	
	@Override
	public void setParentClass(Class<T> parentClass) {
		this.parentClass = parentClass;	
	}

	@Override
	public ArrayAdapter<T> createAdapter(Context context, Spinner spinner) {
		adapter = new CustomArrayAdapter<T>(spinner, context, android.R.layout.simple_spinner_item);
		loadData(context, onLoadListener, CommonListeners.defaultErrorListener);
		return adapter;
	}
	
	@Override
	public void setValue(Object value) {
		String id = (String) value;
		T model;
		try {
			model = parentClass.newInstance();
			model.setId(id);
			Spinner spinner = (getView());
			((CustomArrayAdapter<T>)spinner.getAdapter()).setSelection(model);
		} catch (Exception e) {
			//TODO
			e.printStackTrace();
		}
	}
	
	@Override
	public Object getValue() {
		return ((T)super.getValue()).getId();
	}
	
	/**
	 * May be overwritten if needed
	 * @param context
	 * @param listener
	 * @param errorListener
	 */
	protected void loadData(Context context, Listener<Collection<T>> listener, ErrorListener errorListener) {
		IDataSource<T> dataSource = Pillow.getInstance(context).getDataSource(parentClass);
		dataSource.index().setListeners(listener, errorListener);
	}

	ViewListener<Collection<T>> onLoadListener = new ViewListener<Collection<T>>(){
		@Override
		public void onResponse(Collection<T> items) {
			for(T item:items){
				adapter.add(item);
			}
			adapter.notifyDataSetChanged();
		}
		
	};
	
	private class CustomArrayAdapter<T> extends ArrayAdapter<T>{
		T selection;
		Spinner spinner;
		public CustomArrayAdapter(Spinner spinner, Context context, int resource) {
			super(context, resource);
			this.spinner = spinner;
		}
		
		public void setSelection(T model){
			selection = model;
			updateSelected();
		}

		private void updateSelected() {
			int position = getPosition(selection);
			if(position!=-1){
				spinner.setSelection(position);
			}
		}
		
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			updateSelected();
		}
	}


}
