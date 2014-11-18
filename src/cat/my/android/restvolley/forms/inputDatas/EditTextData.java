package cat.my.android.restvolley.forms.inputDatas;

import cat.my.android.restvolley.forms.InputData;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

public class EditTextData implements InputData {
	@Override
	public String getValue(View view) {
		return ((EditText) view).getText().toString();
	}

	@Override
	public void setValue(View view, Object value) {
		((EditText) view).setText((String) value);
	}

	@Override
	public View createView(Context context) {
		return new EditText(context);
	}
}