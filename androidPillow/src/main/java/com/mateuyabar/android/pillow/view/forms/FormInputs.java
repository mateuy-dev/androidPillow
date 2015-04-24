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

import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.data.validator.GreaterThanValidator;
import com.mateuyabar.android.pillow.data.validator.annotations.GreaterThan;
import com.mateuyabar.android.pillow.util.reflection.ReflectionUtil;
import com.mateuyabar.android.pillow.view.forms.InputData.ValueChangedListener;
import com.mateuyabar.util.exceptions.BreakFastException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FormInputs {
	InputDataManager inputManager = new InputDataManager();
	
	//Data
	Map<Field, FormInputRow> inputViewMap = null;
	Object model;
	Context context;
	boolean editable;
	
	//Filters
	String[] inputNames;
	
	public FormInputs(Object model, Context context, boolean editable) {
		super();
		this.model = model;
		this.context = context;
		this.editable = editable;
	}
	
	private void initInputs() {
		try {
			Class<?> modelClass = model.getClass();
			Map<Field, GreaterThan> greaterThans= new HashMap<Field, GreaterThan>();
			if(inputViewMap!=null)
				return;
			inputViewMap = new HashMap<Field, FormInputRow>();
			for(Field field: ReflectionUtil.getStoredFields(modelClass)){
					if(acceptInput(field)){
						field.setAccessible(true);
						inputViewMap.put(field, new FormInputRow(context, field, model, editable));
						
						GreaterThan greaterThan = field.getAnnotation(GreaterThan.class);
						if(greaterThan!=null){
							greaterThans.put(field, greaterThan);
						}
					}
				
			}
			
			//Greater than fires and listeners: update value if null when greater than is set
			for(Entry<Field, GreaterThan> entry:greaterThans.entrySet()){
				FormInputRow originInputRow = inputViewMap.get(entry.getKey());
				final InputData originInputData = originInputRow.getInputData();
				final GreaterThan greaterThan = entry.getValue();
				String greaterThanAtt = greaterThan.attribute();
				Field greaterThanField = modelClass.getDeclaredField(greaterThanAtt);
				FormInputRow greaterThanInputRow = inputViewMap.get(greaterThanField);
				if(greaterThanInputRow!=null){
					greaterThanInputRow.getInputData().addOnValueChangedListener(new ValueChangedListener() {
						@Override
						public void onValueChanged(Object value) {
							Object originValue = originInputData.getValue();
							if(originValue==null || !GreaterThanValidator.isValid(originValue, value, greaterThan))
								originInputData.setValue(value);
						}
					});
				}
			}
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
	}
	
	public void setInputNames(String[] inputNames) {
		this.inputNames = inputNames;
	}
	
	public Collection<FormInputRow> getInputs(){
		initInputs();
		List<FormInputRow> result = new ArrayList<FormInputRow>();
		if(inputNames==null){
			for(FormInputRow row : inputViewMap.values()){
				result.add(row);
			}
			//sort
			Collections.sort(result, new Comparator<FormInputRow>() {
				public int compare(FormInputRow lhs, FormInputRow rhs) {
					return FormInputs.compare(lhs.getOrder(), rhs.getOrder());
				};
			});
		} else {
			for(String inputName: inputNames){
				result.add(getInput(inputName));
			}
		}
		return result;
	}
	
	 public static int compare(int lhs, int rhs) {
		 return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
	 }
	
	private FormInputRow getInput(String fieldName){
		initInputs();
		try {
			Field field = model.getClass().getDeclaredField(fieldName);
			return inputViewMap.get(field);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	public void updateModelFromForm() {
		for(Entry<Field, FormInputRow> entries : inputViewMap.entrySet()){
			Field field= entries.getKey();
			Object value = entries.getValue().getValue();
			try {
				field.set(model, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public boolean acceptInput(Field field){
		String fieldName = field.getName();
		if(model instanceof IdentificableModel && fieldName.equals("id")){
			return false;
		}
		if(inputNames==null) return true;
		for(String inputName:inputNames){
			if(fieldName.equals(inputName)){
				return true;
			}
		}
		return false;
	}
	
	

}
