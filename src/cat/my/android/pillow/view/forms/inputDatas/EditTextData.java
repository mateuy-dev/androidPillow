package cat.my.android.pillow.view.forms.inputDatas;

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
		EditText editText = new EditText(context);
		editText.setEms(10);
		return editText;
		
	}
	
	@Override
	public EditText getView() {
		return (EditText) super.getView();
	}
}