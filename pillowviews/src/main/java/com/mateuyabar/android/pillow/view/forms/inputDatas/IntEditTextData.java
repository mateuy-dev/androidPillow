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
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.mateuyabar.util.StringUtil;

public class IntEditTextData extends AbstractInputData {
	public View createView(Context context) {
		EditText input = new EditText(context);
		input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
		input.setEms(EditTextData.EMS);
		input.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus && getValue()==0){
					setValue("");
				} else if(!hasFocus && StringUtil.isBlanck(getView().getText().toString())){
					setValue(0);
				}
			}
		});
		return input;
	}

	@Override
	public Integer getValue() {
		//TODO does never return null integers
		String value = getView().getText().toString();
		if(StringUtil.isBlanck(value)){
			return 0;
		} else {
			return Integer.parseInt(value);
		}
	}

	@Override
	public void setValue(Object value) {
		getView().setText(value.toString());
	}
	
	protected EditText getView(){
		return (EditText) super.getView();
	}
	
	@Override
	public void addOnValueChangedListener(final ValueChangedListener listener) {
		getView().addTextChangedListener(new DefaultTextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				listener.onValueChanged(getValue());
			}
		});
	}
}