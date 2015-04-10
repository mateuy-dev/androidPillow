package cat.my.android.pillow.view.forms.inputDatas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cat.my.android.pillow.view.forms.StringResourceUtils;


public class EnumDialogInputView extends AbstractInputData {
	Class<?> valueClass;
	Enum<?> value;
	EditText editText;
	
	public EnumDialogInputView(Class<?> valueClass) {
		this.valueClass = valueClass;
	}

	@Override
	public Object getValue() {
		return value;
	}
	
	@Override
	public void setValue(Object value) {
		Enum<?> enumValue = (Enum<?>) value;
		this.value = enumValue;
		String text = StringResourceUtils.getLabel(getContext(), enumValue);
		editText.setText(text);
	}

	@Override
	protected View createView(Context context) {
		editText = new EditText(context);
		editText.setEms(EditTextData.EMS);
		editText.setFocusable(false);
		editText.setKeyListener(null);
		editText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createDialog().show();
			}
		});
		return editText;
	}

	public AlertDialog createDialog(){
		final Map<String, Enum<?>> values = new HashMap<String, Enum<?>>();
        final List<String> stringValues = new ArrayList<String>();
		for (Object constant : valueClass.getEnumConstants()) {
			Enum<?> value = (Enum<?>) constant;
			String label = StringResourceUtils.getLabel(getContext(), value);
			values.put(label, value);
            stringValues.add(label);
		}


		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder// .setTitle("no title for now")
		.setItems(stringValues.toArray(new String[]{}), new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String selectedString = stringValues.get(which);
				Enum<?> value = values.get(selectedString);
				setValue(value);
			}
		});
		return builder.create();
	}
	    
	    
	
	
}
