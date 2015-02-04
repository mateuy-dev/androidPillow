package cat.my.android.restvolley.forms;


import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class TFormView<T> extends GridLayout{
	FormInputs formInputs;
	T model;
	boolean editable;
	


	public TFormView(Context context, boolean editable) {
		super(context);
		this.editable = editable;
		init();
	}
	
	private void init() {
		setColumnCount(2);
//		setOrientation(VERTICAL);
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
		formInputs = new FormInputs(model, getContext(), editable);
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
