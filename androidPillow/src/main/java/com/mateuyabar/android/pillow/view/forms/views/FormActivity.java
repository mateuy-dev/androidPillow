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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.Listeners.Listener;
import com.mateuyabar.android.pillow.Listeners.ViewListener;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.R;
import com.mateuyabar.android.pillow.data.sync.CommonListeners;
import com.mateuyabar.android.pillow.util.BundleUtils;
import com.mateuyabar.util.StringUtil;

public class FormActivity<T extends IdentificableModel>  extends ActionBarActivity  {
	FormFragment<T> formFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addCustomActionBar();
	    setContentView(R.layout.form_activity);
		if (savedInstanceState == null) {
			formFragment = new FormFragment<T>();
			formFragment.setArguments(BundleUtils.copyBundle(getIntent().getExtras()));
			getSupportFragmentManager().beginTransaction().add(R.id.container, formFragment).commit();
		}
	}

	private void addCustomActionBar() {
		// Inflate your custom layout
	    final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.form_action_bar, null);
	    // Set up your ActionBar
	    final ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayShowHomeEnabled(false);
	    actionBar.setDisplayShowTitleEnabled(false);
	    actionBar.setDisplayShowCustomEnabled(true);
	    actionBar.setCustomView(actionBarLayout);
	   
//	    final int actionBarColor = getResources().getColor(R.color.action_bar);
//	    actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));
	    
	    Button saveButton = (Button) findViewById(R.id.action_bar_save);
	    OnClickListener okClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				T model = formFragment.getModel();
				if(model==null){
					//validation errors displayed. Do nothing.
					return;
				}
                IDataSource<T> dataSource = (IDataSource<T>) Pillow.getInstance(FormActivity.this).getDataSource(model.getClass());
				if(StringUtil.isBlanck(model.getId())){
					dataSource.create(model).addListeners(getOnSaveListener(), CommonListeners.defaultErrorListener);
				} else {
					dataSource.update(model).addListeners(getOnSaveListener(), CommonListeners.defaultErrorListener);
				}
			}
			
		};
		saveButton.setOnClickListener(okClickListener);
	}
	
	public Listener<T> getOnSaveListener(){
		return closeOnSaveListener;
	}
	
	private ViewListener<T> closeOnSaveListener = new ViewListener<T>() {
		@Override
		public void onResponse(T response) {
			finish();
		}
	};
	

	
}
