package cat.my.android.pillow.view.forms.inputDatas.display;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cat.my.android.pillow.view.forms.StringResourceUtils;
import cat.my.android.pillow.view.forms.inputDatas.CalendarInputData;
import cat.my.util.exceptions.UnimplementedException;

public class EnumDisplay extends TextDisplay{
	
	@Override
	protected String valueToString() {
		return StringResourceUtils.getLabel(getContext(), (Enum<?>)getValue());
	}

}
