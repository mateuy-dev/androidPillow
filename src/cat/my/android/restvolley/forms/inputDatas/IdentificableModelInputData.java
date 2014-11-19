package cat.my.android.restvolley.forms.inputDatas;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.Listeners;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.forms.InputData;
import cat.my.android.restvolley.sync.CommonListeners;
import cat.my.android.restvolley.sync.ISynchDataSource;

public class IdentificableModelInputData<T extends IdentificableModel> extends AbstractSpinnerInputData<T>{
	Class<T> parentClass;
	ArrayAdapter<T> adapter;
	
	public IdentificableModelInputData(Class<T> parentClass) {
		this.parentClass = parentClass;
	}

	@Override
	public ArrayAdapter<T> createAdapter(Context context, Spinner spinner) {
		adapter = new CustomArrayAdapter<T>(spinner, context, android.R.layout.simple_spinner_item);
		loadData(context, onLoadListener, CommonListeners.dummyErrorListener);
		return adapter;
	}
	
	@Override
	public void setValue(View view, Object value) {
		String id = (String) value;
		T model;
		try {
			model = parentClass.newInstance();
			model.setId(id);
			Spinner spinner = ((Spinner)view);
			((CustomArrayAdapter<T>)spinner.getAdapter()).setSelection(model);
		} catch (Exception e) {
			//TODO
			e.printStackTrace();
		}
	}
	
	@Override
	public Object getValue(View view) {
		return ((T)super.getValue(view)).getId();
	}
	
	/**
	 * May be overwritten if needed
	 * @param context
	 * @param listener
	 * @param errorListener
	 */
	protected void loadData(Context context, Listener<Collection<T>> listener, ErrorListener errorListener) {
		ISynchDataSource<T> dataSource = RestVolley.getInstance(context).getDataSource(parentClass);
		dataSource.index(listener, errorListener);
	}

	Listener<Collection<T>> onLoadListener = new Listener<Collection<T>>(){
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
