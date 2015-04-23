package com.mateuyabar.android.pillow.view.forms.inputDatas.display;

import com.mateuyabar.android.pillow.view.forms.StringResourceUtils;

public class EnumDisplay extends TextDisplay{
	
	@Override
	protected String valueToString() {
		return StringResourceUtils.getLabel(getContext(), (Enum<?>)getValue());
	}

}
