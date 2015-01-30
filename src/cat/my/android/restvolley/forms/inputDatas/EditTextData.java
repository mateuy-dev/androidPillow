package cat.my.android.restvolley.forms.inputDatas;

import cat.my.android.restvolley.forms.InputData;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

public class EditTextData extends AbstractInputData {
	@Override
	public String getValue() {
		return getView().getText().toString();
	}

	@Override
	public void setValue(Object value) {
		getView().setText((String) value);
	}

	@Override
	public View createView(Context context) {
		return new EditText(context);
	}
	
	@Override
	public EditText getView() {
		return (EditText) super.getView();
	}
}