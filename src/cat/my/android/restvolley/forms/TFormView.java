package cat.my.android.restvolley.forms;


import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class TFormView<T> extends LinearLayout{
	FormInputs formInputs;
	T model;
	
	
	public TFormView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TFormView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		setOrientation(VERTICAL);
	}

	public T getModel() {
		updateModelFromForm();
		return model;
	}

	public void setModel(T model){
		setModel(model,null);
	}
	
	public void setModel(T model, String[] inputNames) {
		this.model = model;
		formInputs = new FormInputs(model, getContext());
		formInputs.setInputNames(inputNames);
		generateForm();
	}
	
	private void generateForm() {
		Collection<View> inputs = formInputs.getInputs();
		for(View view : inputs){
			addView(view);
		}
	}

	private void updateModelFromForm() {
		formInputs.updateModelFromForm();
	}

	
}
