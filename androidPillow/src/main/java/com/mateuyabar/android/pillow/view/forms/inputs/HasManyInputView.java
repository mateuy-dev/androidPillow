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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.R;

import java.util.ArrayList;
import java.util.List;


public class HasManyInputView<T extends IdentificableModel> extends LinearLayout{
	LayoutInflater inflater;
	int items = 0;
	Class<T> selectedClass;
	List<HasManyInputRowView> rows = new ArrayList<HasManyInputRowView>();

	public HasManyInputView(Context context, Class<T> selectedClass) {
		super(context);
		this.selectedClass = selectedClass;
		init();
	}
	
	private void init() {
		inflate(getContext(), R.layout.has_many_input, this);
		
		View addAnotherImtemView = findViewById(R.id.add_another_item);
		addAnotherImtemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createRow();
			}
		});
		setOrientation(VERTICAL);
		createRow();
	}
	
	public List<String> getValue(){
		List<String> result = new ArrayList<String>();
		for(HasManyInputRowView row : rows){
			String value = row.getValue();
			if(value!=null)
				result.add(value);
		}
		return result;
	}
	
	
	
	
	public void createRow(){
		HasManyInputRowView view = new HasManyInputRowView(getContext());
		rows.add(view);
		addView(view, items);
		items++;
	}
	
	public void removeRow(HasManyInputRowView row){
		if(items>1){
			removeView(row);
			rows.remove(row);
			items--;
		} else {
			row.getModelView().setValue((String) null);
		}
	}
	
	/**
	 * Row view
	 */
	public class HasManyInputRowView extends LinearLayout{
		ModelDialogInputView<T> modelView;
		
		public HasManyInputRowView(Context context) {
			super(context);
			init();
		}
		
		private void init() {
			inflate(getContext(), R.layout.has_many_input_line, this);
			ImageButton removeButton = (ImageButton) findViewById(R.id.remove_row_button);
			removeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					removeRow(HasManyInputRowView.this);
				}
			});
			
			modelView = new ModelDialogInputView<T>(getContext(), selectedClass);
			addView(modelView, 0);
			
			
			
/*
			ImageButton imageButton = new ImageButton(getContext());
			imageButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
	        imageButton.setImageResource(R.drawable.ic_clear_grey600_18dp);
			//android:background="@null"/>
	        this.addView(imageButton);*/
			
			setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		
		public ModelDialogInputView<T> getModelView() {
			return modelView;
		}
		
		public String getValue(){
			return modelView.getValue();
		}
		
		public void setValue(String value){
			modelView.setValue(value);
		}
		
		
	}
	
	


	
}
