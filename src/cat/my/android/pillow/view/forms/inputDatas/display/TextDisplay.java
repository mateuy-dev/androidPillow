package cat.my.android.pillow.view.forms.inputDatas.display;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import cat.my.android.pillow.view.forms.inputDatas.AbstractInputData;

public class TextDisplay extends AbstractInputData{
	Object value;
	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
		String text = value!=null ? valueToString() : "";
		getView().setText(text);
	}

	protected String valueToString() {
		return value.toString();
	}

	@Override
	protected View createView(Context context) {
		return new TextView(context);
	}
	
	@Override
	protected TextView getView() {
		return (TextView) super.getView();
	}

}
