package cat.my.android.restvolley.forms.inputDatas;

import java.util.Collection;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.forms.InputData;
import cat.my.android.restvolley.sync.CommonListeners;
import cat.my.android.restvolley.sync.ISynchDataSource;

public abstract class AbstractSpinnerInputData<T> implements InputData {
	@Override
	public View createView(Context context) {
		Spinner spinner = new Spinner(context);
		ArrayAdapter<T> adapter = createAdapter(context);
		spinner.setAdapter(adapter);
		return spinner;
	}
	
	public abstract ArrayAdapter<T> createAdapter(Context context);



	@Override
	public Object getValue(View view) {
		Spinner spinner = ((Spinner)view);
		return spinner.getSelectedItem();
	}

	@Override
	public void setValue(View view, Object value) {
		Spinner spinner = ((Spinner)view);
		int position = ((ArrayAdapter<T>)spinner.getAdapter()).getPosition((T)value);
		spinner.setSelection(position);
		
	}

}
