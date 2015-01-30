package cat.my.android.restvolley.forms.inputs;

import cat.my.android.restvolley.IDataSource;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.sync.CommonListeners;
import cat.my.android.restvolley.sync.ISynchDataSource;
import android.content.Context;
import android.widget.EditText;

public class ModelDialogInputView<T extends IdentificableModel> extends EditText{
	IDataSource<T> dataSource;
	Class<T> selectedClass;
	T selected;

	public ModelDialogInputView(Context context, Class<T> selectedClass) {
		super(context);
		this.selectedClass = selectedClass;
		dataSource = RestVolley.getInstance(context).getDataSource(selectedClass);
	}

	public void setValue(String modelId){
		T toSearch;
		try {
			toSearch = selectedClass.newInstance();
			toSearch.setId(modelId);
			dataSource.show(toSearch, new Listener<T>() {
				@Override
				public void onResponse(T response) {
					selected = response;
					setText(response.toString());
				}
			}, CommonListeners.silentErrorListener);
			
		} catch (Exception e) {
			//TODO check exception
			e.printStackTrace();
		}
	}
	
	public T getValue(){
		return selected;
	}
}
