package cat.my.android.restvolley.forms.inputDatas;

import android.content.Context;
import android.view.View;
import cat.my.android.restvolley.forms.InputData;
import cat.my.android.restvolley.forms.inputs.ColorPicker;

public class ColorInput  implements InputData{
	@Override
	public Object getValue(View view) {
		return ((ColorPicker)view).getColor();
	}

	@Override
	public void setValue(View view, Object value) {
		((ColorPicker)view).setColor((Integer)value);
	}

	@Override
	public View createView(Context context) {
		return new ColorPicker(context);
	}
}