package cat.my.android.restvolley.forms.inputDatas;

import java.util.Collection;

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
	public ArrayAdapter<T> createAdapter(Context context) {
		adapter = new ArrayAdapter<T>(context, android.R.layout.simple_spinner_item);
		loadData(context, onLoadListener, CommonListeners.dummyErrorListener);
		return adapter;
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


}
