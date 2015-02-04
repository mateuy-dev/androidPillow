package cat.my.android.pillow.view.forms;


import java.util.Collection;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;

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
