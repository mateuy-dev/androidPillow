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


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import com.mateuyabar.android.pillow.IDataSource;
import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.Listeners.ViewListener;
import com.mateuyabar.android.pillow.R;
import com.mateuyabar.android.pillow.data.sync.CommonListeners;
import com.mateuyabar.android.pillow.view.base.IModelListAdapter;
import com.mateuyabar.android.pillow.view.forms.inputDatas.EditTextData;
import com.mateuyabar.util.exceptions.BreakFastException;

public class ModelDialogInputView <T extends IdentificableModel> extends EditText{
	IDataSource<T> dataSource;
	Class<T> selectedClass;
	String selectedId;
	T selected;

	public ModelDialogInputView(Context context, Class<T> selectedClass) {
		super(context);
		this.selectedClass = selectedClass;
		init();
	}
	
	protected void init(){
		dataSource = Pillow.getInstance(getContext()).getDataSource(selectedClass);
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
	
	public class SelectDialog extends Dialog{
		IModelListAdapter<T> adapter;
		
		public SelectDialog(Context context) {
			super(context,android.R.style.Theme_Holo_Light_DialogWhenLarge_NoActionBar);
		}
		
		protected void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.select_model_dialog);
		    ListView listView = (ListView)findViewById(R.id.list_view);
		    adapter = Pillow.getInstance(getContext()).getViewConfiguration(selectedClass).getListAdapter(getContext());
		    listView.setOnItemClickListener(new OnItemClickListener() {
		    	@Override
		    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    		setValue(adapter.getItem(position));
					dismiss();
		    	}
			});
		    
		    listView.setAdapter(adapter);
		    adapter.refreshList();
		    
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
