package cat.my.android.restvolley.forms.inputDatas;

import cat.my.android.restvolley.forms.InputData;
import cat.my.android.restvolley.forms.inputs.BarCodeInputView;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

public class BarCodeInput extends AbstractInputData{

	@Override
	public Object getValue() {
		return ((BarCodeInputView)view).getValue();
	}

	@Override
	public void setValue(Object value) {
		((BarCodeInputView)view).setValue((String)value);
	}

	@Override
	public View createView(Context context) {
		return new BarCodeInputView(context);
	}

}
