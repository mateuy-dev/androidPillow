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
import android.view.View;

public interface InputData {
	
//	@Target({ElementType.FIELD})
//	@Retention(RetentionPolicy.RUNTIME)
//	public @interface InputDataInfo {
//		public interface DEFAULT_INPUT extends InputData{};
//		public interface NONE extends IdentificableModel{};
//		
//		Class<? extends InputData> inputClass() default DEFAULT_INPUT.class;
//		Class<? extends IdentificableModel> belongsTo() default NONE.class;
//	}
	
	public Object getValue();

	public void setValue(Object value);

	public View getView(Context context);
	
	public void addOnValueChangedListener(ValueChangedListener listener);
	
	public interface ValueChangedListener{
		public void onValueChanged(Object value);
	}
}