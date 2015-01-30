package cat.my.android.restvolley.forms.views;

import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.forms.TFormView;
import cat.my.android.restvolley.sync.CommonListeners;
import cat.my.android.restvolley.sync.ISynchDataSource;
import cat.my.android.restvolley.utils.BundleUtils;
import cat.my.util.BreakFastException;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FormFragment<T extends IdentificableModel>  extends Fragment{
	public static final String EDIT_MODE_PARAM = "editMode";

	
	String modelId;
	TFormView<T> formView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		LinearLayout rootView = new LinearLayout(getActivity());
		rootView.setOrientation(LinearLayout.VERTICAL);
		
		
		Bundle bundle = getArguments();
		final String[] atts = BundleUtils.getShownAtts(bundle);
		modelId = BundleUtils.getId(bundle);
		Class<T> clazz = BundleUtils.getModelClass(bundle);
		boolean editMode = bundle.getBoolean(EDIT_MODE_PARAM, true);
		
		formView = new TFormView<T>(getActivity(), editMode);
		rootView.addView(formView);
		
		
		ISynchDataSource<T> dataSource = RestVolley.getInstance(getActivity()).getDataSource(clazz);
		
		try {
			T idModel = null;
			idModel = clazz.newInstance();
			if (modelId != null) {
				//update, we should look for current values
				idModel.setId(modelId);
				dataSource.show(idModel, new Listener<T>() {
					@Override
					public void onResponse(T model) {
						formView.setModel(model, atts);
					}
				}, CommonListeners.dummyErrorListener);
			} else {
				//we check if there is a model in the bundle, other wise we use the empty model
				T bundleModel = BundleUtils.getModel(bundle);
				if(bundleModel!=null){
					idModel = bundleModel;
				}
				formView.setModel(idModel, atts);
			}
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
		
		return rootView;
	}
	
	public T getModel(){
		return formView.getModel();
	}
	
	public String getModelId() {
		return modelId;
	}

}
