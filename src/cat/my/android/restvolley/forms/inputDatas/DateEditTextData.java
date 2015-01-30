package cat.my.android.restvolley.forms.inputDatas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cat.my.android.restvolley.forms.InputData;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

public class DateEditTextData extends AbstractInputData {
public static final String DATE_STRING_FORMAT = "yyyy-MM-dd";
	
	@Override
	public Date getValue() {
		String stringValue = ((EditText) view).getText().toString();
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
		try {
			return dateFormat.parse(stringValue);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setValue(Object value) {
		Date date = (Date) value;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
		String text = dateFormat.format(date);
		((EditText) view).setText(text);
	}

	@Override
	public View createView(Context context) {
		return new EditText(context);
	}
}