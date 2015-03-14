package cat.my.android.pillow.view.forms.inputDatas;

import cat.my.util.StringUtil;
import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class IntEditTextData extends AbstractInputData {
	public View createView(Context context) {
		EditText input = new EditText(context);
		input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
		input.setEms(EditTextData.EMS);
		input.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus && getValue()==0){
					setValue("");
				} else if(!hasFocus && StringUtil.isBlanck(getView().getText().toString())){
					setValue(0);
				}
			}
		});
		return input;
	}

	@Override
	public Integer getValue() {
		//TODO does never return null integers
		String value = getView().getText().toString();
		if(StringUtil.isBlanck(value)){
			return 0;
		} else {
			return Integer.parseInt(value);
		}
	}

	@Override
	public void setValue(Object value) {
		getView().setText(value.toString());
	}
	
	protected EditText getView(){
		return (EditText) super.getView();
	}
}