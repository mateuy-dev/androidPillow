package cat.my.android.restvolley.forms.views;

import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.forms.TFormView;
import cat.my.android.restvolley.sync.CommonListeners;
import cat.my.android.restvolley.sync.ISynchDataSource;
import cat.my.android.restvolley.utils.BundleUtils;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FormFragment<T extends IdentificableModel>  extends Fragment{
	TFormView<T> formView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		LinearLayout rootView = new LinearLayout(getActivity());
		rootView.setOrientation(LinearLayout.VERTICAL);
		
		String id = BundleUtils.getId(getArguments());
		Class<T> clazz = BundleUtils.getModelClass(getArguments());
		formView = new TFormView<T>(getActivity());
		rootView.addView(formView);
		
		ISynchDataSource<T> dataSource = RestVolley.getInstance(getActivity()).getDataSource(clazz);
		
		try {
			T idModel = null;
			idModel = clazz.newInstance();
			if (id != null) {
				//update, we should look for current values
				idModel.setId(id);
				dataSource.show(idModel, new Listener<T>() {
					@Override
					public void onResponse(T model) {
						formView.setModel(model);
					}
				}, CommonListeners.dummyErrorListener);
			} else {
				//new, we just set the empty model
				formView.setModel(idModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rootView;
	}
	
	public T getModel(){
		return formView.getModel();
	}
}
