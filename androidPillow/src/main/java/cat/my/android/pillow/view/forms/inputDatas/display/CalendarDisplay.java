package cat.my.android.pillow.view.forms.inputDatas.display;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cat.my.android.pillow.view.forms.StringResourceUtils;
import cat.my.android.pillow.view.forms.inputDatas.CalendarInputData;
import cat.my.util.exceptions.UnimplementedException;

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
