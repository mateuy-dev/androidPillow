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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.Listeners.ViewListener;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.R;
import com.mateuyabar.android.pillow.data.sync.CommonListeners;
import com.mateuyabar.android.pillow.util.BundleUtils;
import com.mateuyabar.android.pillow.view.NavigationUtil;
import com.mateuyabar.android.pillow.view.forms.TFormView;
import com.mateuyabar.util.exceptions.BreakFastException;

public class PillowShowFragment<T extends IdentificableModel> extends Fragment{
	String modelId;
	TFormView<T> formView;
	IDataSource<T> dataSource;
	Class<T> modelClass;
	String[] atts;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		atts = BundleUtils.getShownAtts(bundle);
		modelId = BundleUtils.getId(bundle);
		modelClass = BundleUtils.getModelClass(bundle);
		
		
		formView = new TFormView<T>(getActivity(), false);
		dataSource = Pillow.getInstance(getActivity()).getDataSource(modelClass);
		
		return formView;
	}
	
	public void loadModel(){
		T idModel;
		try {
			idModel = modelClass.newInstance();
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
		idModel.setId(modelId);
		dataSource.show(idModel).addListeners(new ViewListener<T>() {
            @Override
            public void onResponse(T model) {
                updateView(model);
            }
        }, CommonListeners.defaultErrorListener);
	}
	
	protected void updateView(T model) {
		formView.setModel(model, atts);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadModel();
	}
	
	public T getModel(){
		return formView.getModel();
	}
	
	public String getModelId() {
		return modelId;
	}
	
	public IDataSource<T> getDataSource() {
		return dataSource;
	}
	
	public Class<T> getModelClass() {
		return modelClass;
	}

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	};
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.show_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		if(item.getItemId() == R.id.menu_action_edit){
			editModel();
			return true;
		}
		if(item.getItemId() == R.id.menu_action_delete){
			deleteModel();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void editModel() {
		new NavigationUtil(this).displayEditModel(getModel());
	}
	
	private void deleteModel() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Delete").setMessage("are you sure...?").setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getDataSource().destroy(getModel()).addListeners(new ViewListener<Void>() {
                    @Override
                    public void onResponse(Void response) {
                        new NavigationUtil(PillowShowFragment.this).displayListModel(getModel().getClass());
                    }
                }, CommonListeners.defaultErrorListener);
			}
		}).setNegativeButton("cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}
	
}
