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

package com.mateuyabar.android.pillow.view.forms.inputDatas.display;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.mateuyabar.android.pillow.view.forms.inputDatas.AbstractInputData;

public class TextDisplay extends AbstractInputData{
	Object value;
	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
		String text = value!=null ? valueToString() : "";
		getView().setText(text);
	}

	protected String valueToString() {
		return value.toString();
	}

	@Override
	protected View createView(Context context) {
		return new TextView(context);
	}
	
	@Override
	protected TextView getView() {
		return (TextView) super.getView();
	}

}
