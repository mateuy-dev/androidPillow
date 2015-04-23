package com.mateuyabar.android.pillow.view.forms.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mateuyabar.android.pillow.IDataSource;
import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.Listeners.ViewListener;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.data.sync.CommonListeners;
import com.mateuyabar.android.pillow.util.BundleUtils;
import com.mateuyabar.android.pillow.view.forms.TFormView;
import com.mateuyabar.util.exceptions.BreakFastException;

public class FormFragment<T extends IdentificableModel>  extends Fragment{
	public static final String EDIT_MODE_PARAM = "editMode";

	String modelId;
	TFormView<T> formView;
	IDataSource<T> dataSource;
	Class<T> modelClass;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		
		
		Bundle bundle = getArguments();
		final String[] atts = BundleUtils.getShownAtts(bundle);
		modelId = BundleUtils.getId(bundle);
		modelClass = BundleUtils.getModelClass(bundle);
		boolean editMode = bundle.getBoolean(EDIT_MODE_PARAM, true);
		
		formView = new TFormView<T>(getActivity(), editMode);
		
		dataSource = Pillow.getInstance(getActivity()).getDataSource(modelClass);
		
		try {
			T idModel = null;
			idModel = modelClass.newInstance();
			if (modelId != null) {
				//update, we should look for current values
				idModel.setId(modelId);
				dataSource.show(idModel).setListeners(new ViewListener<T>() {
					@Override
					public void onResponse(T model) {
						formView.setModel(model, atts);
					}
				}, CommonListeners.defaultErrorListener);
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
		
		return formView;
	}
	
	
	/**
	 * @return The model, or null if the model is invalid
	 */
	public T getModel(){
		return formView.getModel(true);
	}
	
	public String getModelId() {
		return modelId;
	}
	
	public IDataSource<T> getDataSource() {
		return dataSource;
	}

}
