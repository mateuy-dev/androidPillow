package com.mateuyabar.android.pillow.view.forms.inputDatas;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TimePicker;
import com.mateuyabar.android.pillow.data.extra.Time;


public class TimeInputData extends AbstractInputData {
	Class<? extends Time> timeClass;

	@Override
	public Time getValue() {
		String stringValue = ((EditText) view).getText().toString();
		if(stringValue.equals(""))
			return null;
		String[] values = stringValue.split(":");
		int hour = Integer.parseInt(values[0]);
		int minute = Integer.parseInt(values[1]);
		Time time = new Time(hour, minute);
		return time;
	}

	@Override
	public void setValue(Object value) {
        if(value==null){
            ((EditText) view).setText("");
        } else {
        	Time time = (Time) value;
        	String text = String.format("%02d:%02d", time.getHour(), time.getMinute());
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
				Time value = getValue();
				
				if(value==null){
					value = new Time();
				}
				int hour = value.getHour();
				int minute = value.getMinute();
				new TimePickerDialog(context,  new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						Time time = new Time(hourOfDay, minute);
						setValue(time);
					}
				}, hour, minute, true).show();
			}
		});

		return editText;
	}
	
	
	
	
}

