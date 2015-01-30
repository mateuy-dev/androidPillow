package cat.my.android.restvolley.forms.inputDatas;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import cat.my.android.restvolley.forms.InputData;

public class IntEditTextData extends AbstractInputData {
	public View createView(Context context) {
		EditText input = new EditText(context);
		input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
		return input;
	}

	@Override
	public Integer getValue() {
		return Integer.parseInt(getView().getText().toString());
	}

	@Override
	public void setValue(Object value) {
		getView().setText(value.toString());
	}
	
	protected EditText getView(){
		return (EditText) getView();
	}
}