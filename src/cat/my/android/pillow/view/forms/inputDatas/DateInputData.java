package cat.my.android.pillow.view.forms.inputDatas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cat.my.android.pillow.view.forms.InputData;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class DateInputData implements InputData {
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
	
}