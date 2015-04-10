package cat.my.android.pillow.view.forms.inputDatas;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.view.View;
import cat.my.android.pillow.view.forms.InputData;

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
				listener.onValueChanged(calendar.getTime());
			}
		};
		calendarInput.addOnValueChangedListener(proxyListener);
	}
}