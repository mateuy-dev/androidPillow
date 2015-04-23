package com.mateuyabar.android.pillow.view.forms.inputDatas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

import com.mateuyabar.util.exceptions.BreakFastException;

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