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

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

import com.mateuyabar.util.exceptions.BreakFastException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarInputData extends AbstractInputData {
	public static final String DATE_STRING_FORMAT = "dd-MM-yyyy";

	@Override
	public Calendar getValue() {
		String stringValue = ((EditText) view).getText().toString();
		if(stringValue.equals(""))
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
		try {
			Date date = dateFormat.parse(stringValue);
			Calendar result = new GregorianCalendar();
			result.setTime(date);
			return result;
		} catch (ParseException e) {
			new BreakFastException(e);
		}
		return null;
	}

	@Override
	public void setValue(Object value) {
        if(value==null){
            ((EditText) view).setText("");
        } else {
            Calendar calendar = (Calendar) value;
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
            String text = dateFormat.format(calendar.getTime());
            ((EditText) view).setText(text);
        }
	}

	@Override
	public View createView(final Context context) {
		EditText editText = new EditText(context);
		editText.setEms(EditTextData.EMS);
		editText.setFocusable(false);
		editText.setKeyListener(null);
		
		editText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar value = getValue();
				if(value==null){
					value = new GregorianCalendar();
				}
				int year =	value.get(Calendar.YEAR);
				int monthOfYear = value.get(Calendar.MONTH);
				int dayOfMonth = value.get(Calendar.DAY_OF_MONTH);
				new DatePickerDialog(context, new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
						setValue(calendar);
					}
				}, year, monthOfYear, dayOfMonth).show();
			}
		});
		return editText;
	}
	
	@Override
	protected EditText getView() {
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