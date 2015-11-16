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

package com.mateuyabar.android.pillow.view.forms.inputs;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.mateuyabar.android.pillow.Listeners.ViewListener;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.PillowView;
import com.mateuyabar.android.pillow.views.R;
import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.sync.CommonListeners;
import com.mateuyabar.android.pillow.view.forms.inputDatas.EditTextData;
import com.mateuyabar.android.pillow.view.list.PillowUsedExpandableListAdapter;
import com.mateuyabar.android.pillow.view.list.RecentlyUsedModelsController;
import com.mateuyabar.util.exceptions.BreakFastException;

public class ModelDialogInputView <T extends IdentificableModel> extends EditText{
	IDataSource<T> dataSource;
	Class<T> selectedClass;
	String selectedId;
	T selected;

	public ModelDialogInputView(Context context) {
		super(context);
		init();
	}

	public ModelDialogInputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ModelDialogInputView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ModelDialogInputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public void setModelClass(Class<T> modelClass){
		this.selectedClass = modelClass;
		dataSource = Pillow.getInstance(getContext()).getDataSource(selectedClass);
	}

	protected void init(){
		setEms(EditTextData.EMS);
		setFocusable(false);
		setKeyListener(null);
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				displaySelectDialog();
			}
		});
	}


	
	private void displaySelectDialog() {
		new SelectDialog(getContext()).show();
	}

	public void setValue(String modelId){
		selectedId = modelId;
		if(modelId==null){
			selected = null;
			setText("");
		} else {
			try {
				T toSearch = selectedClass.newInstance();
				toSearch.setId(modelId);
				dataSource.show(toSearch).addListeners(new ViewListener<T>() {
                    @Override
                    public void onResponse(T response) {
                        selected = response;
                        setText(response.toString());
                    }
                }, CommonListeners.defaultErrorListener);
			} catch (Exception e) {
				throw new BreakFastException(e);
			}
		}
	}
	
	protected void setValue(T value){
		selectedId = value.getId();
		selected = value;
		setText(value.toString());
	}
	
	public String getValue(){
		return selectedId;
	}
	
	private class SelectDialog extends Dialog{
		//IModelListAdapter<T> adapter;
		PillowUsedExpandableListAdapter<T> adapter;
		
		public SelectDialog(Context context) {
			super(context,android.R.style.Theme_Holo_Light_DialogWhenLarge_NoActionBar);
		}
		
		protected void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.select_model_dialog);
		    //ListView listView = (ListView)findViewById(R.id.list_view);
			//adapter = Pillow.getInstance(getContext()).getViewConfiguration(selectedClass).getListAdapter(getContext());
			//adapter.refreshList();
			ExpandableListView listView = (ExpandableListView)findViewById(R.id.list_view);
			adapter = PillowView.getInstance(getContext()).getViewConfiguration(selectedClass).getUsedExpandableListAdapter(getContext());
		    /*listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					setValue(adapter.getItem(position));
					dismiss();
				}
			});*/
			listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
				@Override
				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
					T selected = adapter.getChild(groupPosition, childPosition);
					setValue(selected);
					new RecentlyUsedModelsController<T>(getContext(), selectedClass).used(selected);
					dismiss();
					return true;
				}
			});

			listView.setAdapter(adapter);
			for(int i=0; i<adapter.getGroupCount(); ++i){
				listView.expandGroup(i);
			}
		    
		    EditText editText = (EditText) findViewById(R.id.search_edit_text);
		    editText.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					adapter.getFilter().filter(s);
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				
				@Override
				public void afterTextChanged(Editable s) {}
			});
		    
		}
		
	}
}
