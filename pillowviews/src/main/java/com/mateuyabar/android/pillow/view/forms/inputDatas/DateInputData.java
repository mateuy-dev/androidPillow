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

import com.mateuyabar.android.pillow.view.forms.InputData;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateInputData implements InputData{
	CalendarInputData calendarInput = new CalendarInputData();

	@Override
	public Date getValue() {
		if(calendarInput.getValue() == null)
			return null;
		else
			return calendarInput.getValue().getTime();
	}

	@Override
	public void setValue(Object value) {
		if(value==null){
			calendarInput.setValue(null);
		} else {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime((Date)value);
			
			calendarInput.setValue(calendar);
		}
	}

	@Override
	public View getView(Context context) {
		return calendarInput.getView(context);
	}
	
	@Override
	public void addOnValueChangedListener(final ValueChangedListener listener) {
		ValueChangedListener proxyListener = new ValueChangedListener() {
			@Override
			public void onValueChanged(Object value) {
				Calendar calendar = (Calendar) value;
				Date time = calendar!=null ? calendar.getTime() : null;
				listener.onValueChanged(time);
			}
		};
		calendarInput.addOnValueChangedListener(proxyListener);
	}
}