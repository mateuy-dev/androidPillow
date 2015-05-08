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
				dataSource.show(idModel).addListeners(new ViewListener<T>() {
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

        //return inflater.inflate(R.layout.form_sample, null);
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
