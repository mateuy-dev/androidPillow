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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public abstract class AbstractSpinnerInputData<T> extends AbstractInputData {
	@Override
	public View createView(Context context) {
		Spinner spinner = new Spinner(context);
		ArrayAdapter<T> adapter = createAdapter(context, spinner);
		spinner.setAdapter(adapter);
		return spinner;
	}
	
	public abstract ArrayAdapter<T> createAdapter(Context context, Spinner spinner);



	@Override
	public Object getValue() {
		Spinner spinner = ((Spinner)view);
		return spinner.getSelectedItem();
	}

	@Override
	public void setValue(Object value) {
		Spinner spinner = ((Spinner)view);
		int position = ((ArrayAdapter<T>)spinner.getAdapter()).getPosition((T)value);
		spinner.setSelection(position);
		
	}
	
	@Override
	protected Spinner getView() {
		return (Spinner) super.getView();
	}

}
