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
import android.widget.EditText;

public class EditTextData extends AbstractInputData {
	public static final int EMS = 10;

	Integer inputType;

	public EditTextData() {
	}

	public EditTextData(int inputType) {
		this.inputType = inputType;
	}

	@Override
	public String getValue() {
		return getView().getText().toString();
	}

	@Override
	public void setValue(Object value) {
		getView().setText((String) value);
	}

	@Override
	public View createView(Context context) {
		EditText editText = new EditText(context);
		editText.setEms(EMS);
		if(inputType!=null){
			editText.setInputType(inputType);
		}
		return editText;
		
	}
	
	@Override
	public EditText getView() {
		return (EditText) super.getView();
	}
}