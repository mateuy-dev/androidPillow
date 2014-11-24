package cat.my.android.restvolley.forms.inputDatas;

import cat.my.android.restvolley.forms.InputData;
import cat.my.android.restvolley.forms.inputs.BarCodeInputView;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

public class BarCodeInput implements InputData{

	@Override
	public Object getValue(View view) {
		return ((BarCodeInputView)view).getValue();
	}

	@Override
	public void setValue(View view, Object value) {
		((BarCodeInputView)view).setValue((String)value);
	}

	@Override
	public View createView(Context context) {
		return new BarCodeInputView(context);
	}

}
