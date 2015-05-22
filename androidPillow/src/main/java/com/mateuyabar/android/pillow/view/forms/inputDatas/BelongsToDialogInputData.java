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

import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.view.forms.BelongsToInputData;
import com.mateuyabar.android.pillow.view.forms.inputs.ModelDialogInputView;

public class BelongsToDialogInputData<T extends IdentificableModel> extends AbstractInputData implements BelongsToInputData<T>{

	
	Class<T> parentClass;
	
	@Override
	public Object getValue() {
		return getView().getValue();
	}

	@Override
	public void setValue(Object value) {
		getView().setValue((String) value);
	}

	@Override
	protected View createView(Context context) {
		return new ModelDialogInputView<T>(context, parentClass);
	}
	
	@Override
	public ModelDialogInputView<T> getView() {
		return (ModelDialogInputView<T>) super.getView();
	} 

	@Override
	public void setParentClass(Class<T> parentClass) {
		this.parentClass = parentClass;
	}

}
