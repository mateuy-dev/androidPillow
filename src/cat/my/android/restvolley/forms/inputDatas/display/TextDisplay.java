package cat.my.android.restvolley.forms.inputDatas.display;

import cat.my.android.restvolley.forms.inputDatas.AbstractInputData;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class TextDisplay extends AbstractInputData{
	Object value;
	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
		String text = value!=null ? value.toString() : "";
		getView().setText(text);
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
