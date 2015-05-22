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

package com.mateuyabar.android.pillow.view.forms.inputDatas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.view.base.PillowBaseListAdapter;
import com.mateuyabar.android.pillow.view.forms.BelongsToInputData;

public class BelongsToAutoCompleteEditTextData<T extends IdentificableModel> extends AbstractInputData implements BelongsToInputData<T> {
	public static final int EMS = 10;
	T selected = null;
	Class<T> parentClass;
    MyAdapter adapter;
	public BelongsToAutoCompleteEditTextData() {
		super();
	}
	
	@Override
	public void setParentClass(Class<T> parentClass) {
		this.parentClass = parentClass;
	}
	
	@Override
	public String getValue() {
		if(selected==null) return null;
        else return selected.getId();
	}

	@Override
	public void setValue(Object value) {
		getView().setText((String) value);
	}

	@Override
	public View createView(Context context) {
		AutoCompleteTextView autoComplete = new AutoCompleteTextView(context);
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = (T) adapter.getItem(position);
            }
        });
        adapter = new MyAdapter(context, parentClass);
		autoComplete.setAdapter(adapter);
		autoComplete.setEms(EMS);
		autoComplete.setThreshold(1);

		return autoComplete;
		
	}
	
	private class MyAdapter extends PillowBaseListAdapter<T> {	
		public MyAdapter(Context context, Class<T> clazz) {
			super(context, clazz);
			refreshList();
		}
		
        @Override
		public View getView(T model, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = new TextView(getContext());
			}
			TextView textView = (TextView) convertView;
			textView.setText(model.toString());
			return textView;
		}
	}
	 
	
	
	@Override
	public EditText getView() {
		return (EditText) super.getView();
	}
	
}