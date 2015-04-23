package com.mateuyabar.android.pillow.view.forms.inputDatas.display;

import java.util.Calendar;
import java.util.Date;

import com.mateuyabar.android.pillow.view.forms.StringResourceUtils;
import com.mateuyabar.util.exceptions.UnimplementedException;

public class CalendarDisplay extends TextDisplay{
	
	@Override
	protected String valueToString() {
		Date date;
		Object value = getValue();
		if(value instanceof Calendar){
			date = ((Calendar)value).getTime();
		} else if(value instanceof Date){
			date = (Date) value;
		} else {
			throw new UnimplementedException();
		}
		return StringResourceUtils.getLabel(getContext(), date, false);
	}

}
