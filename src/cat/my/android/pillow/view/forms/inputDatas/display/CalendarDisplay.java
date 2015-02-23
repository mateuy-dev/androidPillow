package cat.my.android.pillow.view.forms.inputDatas.display;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cat.my.android.pillow.view.forms.inputDatas.CalendarInputData;

public class CalendarDisplay extends TextDisplay{
	
	@Override
	protected String valueToString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(CalendarInputData.DATE_STRING_FORMAT);
		return dateFormat.format(getValue().getTime());
	}

	@Override
	public Calendar getValue() {
		return (Calendar) value;
	}
	

}
