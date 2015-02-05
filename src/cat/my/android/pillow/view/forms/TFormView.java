package cat.my.android.pillow.view.forms;


import java.util.Collection;

import cat.my.android.pillow.R;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Space;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;


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
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL);
		int marginHorizontal = getContext().getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
		int marginVertical = getContext().getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
		setPadding(marginHorizontal, marginVertical, marginHorizontal, marginVertical);
		setLayoutParams(params);
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
		removeAllViews();
		Collection<FormInputRow> inputs = formInputs.getInputs();
		for(FormInputRow rowInput : inputs){
			View label = rowInput.getLabel();
			GridLayout.LayoutParams labelParams = new GridLayout.LayoutParams();
			labelParams.setGravity(Gravity.RIGHT);
			label.setLayoutParams(labelParams);
			int rightPadding = getContext().getResources().getDimensionPixelSize(R.dimen.form_label_right_padding);
			label.setPadding(label.getPaddingLeft(), label.getPaddingTop(), rightPadding, label.getPaddingBottom());
			
			View input = rowInput.getInput();
			GridLayout.LayoutParams inputParams = new GridLayout.LayoutParams();
			inputParams.setGravity(Gravity.LEFT);
			input.setLayoutParams(inputParams);
			
			addView(label);
			addView(input);
			
		}
	}

	private void updateModelFromForm() {
		formInputs.updateModelFromForm();
	}

	
}
