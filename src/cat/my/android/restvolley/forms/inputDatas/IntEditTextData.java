package cat.my.android.restvolley.forms.inputDatas;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import cat.my.android.restvolley.forms.InputData;

public class IntEditTextData implements InputData {
	public View createView(Context context) {
		EditText input = new EditText(context);
		input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
		return input;
	}

	@Override
	public Integer getValue(View view) {
		return Integer.parseInt(((EditText) view).getText().toString());
	}

	@Override
	public void setValue(View view, Object value) {
		((EditText) view).setText(value.toString());
	}
}