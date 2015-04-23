package com.mateuyabar.android.pillow.view.forms.inputDatas;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class DefaultTextWatcher implements TextWatcher {
	@Override
	public void afterTextChanged(Editable s) {}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
}

