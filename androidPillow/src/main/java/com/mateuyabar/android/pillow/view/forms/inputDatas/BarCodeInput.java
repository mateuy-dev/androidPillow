package com.mateuyabar.android.pillow.view.forms.inputDatas;

import android.content.Context;
import android.view.View;
import com.mateuyabar.android.pillow.view.forms.inputs.BarCodeInputView;

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
