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

package com.mateuyabar.android.pillow.view.forms;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.mateuyabar.android.pillow.R;
import com.mateuyabar.android.pillow.view.reflection.ViewConfig.ViewType;
import com.mateuyabar.util.exceptions.BreakFastException;

import java.lang.reflect.Field;
import java.util.List;

public class FormInputRow{
		InputDataManager inputManager = new InputDataManager();
		Field field;
		View input;
		TextView label;
		List<View> views;
		InputData inputData;
		Context context;
		Object model;
		boolean editable;
		int order;
		
		public FormInputRow(Context context, Field field, Object model, boolean editable){
			this.field = field;
			this.context = context;
			this.model = model;
			this.editable=editable;
			init();
		}
		public void init(){
			inputData = inputManager.getInputData(field, editable);
			input = (View) inputData.getView(context);
			try {
				inputData.setValue(field.get(model));
			} catch (Exception e) {
				throw new BreakFastException(e);
			}
			createLabelView();
			ViewType viewType = field.getAnnotation(ViewType.class);
			if(viewType!=null){
				order = viewType.order();
			}
			
		}
		private void createLabelView() {
			label = new TextView(context);
			String labelText = StringResourceUtils.getLabel(context, field);
			label.setText(labelText);
            label.setTypeface(null, Typeface.BOLD);


            TypedValue value = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorPrimary,value, false);
            int color = value.data;
            label.setTextColor(context.getResources().getColor(color));
		}
		
		
		public Object getValue(){
			return inputData.getValue();
		}
		
		public View getLabel() {
			return label;
		}
		
		public View getInput() {
			return input;
		}
		
		public int getOrder() {
			return order;
		}
		
		public InputData getInputData() {
			return inputData;
		}
	}