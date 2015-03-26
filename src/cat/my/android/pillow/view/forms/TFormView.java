package cat.my.android.pillow.view.forms;


import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.R;
import cat.my.android.pillow.data.validator.IValidator;
import cat.my.android.pillow.data.validator.ValidationErrorUtil;
import cat.my.android.pillow.data.validator.IValidator.IValidationError;
import cat.my.android.util.MetricUtil;


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
		setFocusable(true);
	    setFocusableInTouchMode(true);
	    
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
		int margin = MetricUtil.dipToPixels(getContext(), 20);
		params.topMargin= margin;
		int marginHorizontal = getContext().getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
		int marginVertical = getContext().getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
		setPadding(marginHorizontal, marginVertical, marginHorizontal, marginVertical);
		setLayoutParams(params);
		
		
		
		setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_background));
		
		
//		setOrientation(VERTICAL);
	}

	/**
	 * Returns the model displayed in the fragment.
	 * If validate is set to true, it will check for model validation. If valid will be returned. If not valid null will be returned and errors will be displayed
	 * @param validate if the model must be validated
	 * @return the model or null if validate is true and invalid
	 */
	public T getModel(boolean validate){
		T model = getModel();
		if(validate){
			Class modelClass = model.getClass();
			IValidator<T> validator = Pillow.getInstance(getContext()).getModelConfiguration(modelClass).getValidator();
			List<IValidationError> errors = validator.validate(model);
			if(!errors.isEmpty()){
	            //Error found for now we toast the first one. This could be improved
	            IValidator.IValidationError error = errors.get(0);
	            String string = ValidationErrorUtil.getStringError(getContext(), modelClass, error);
	            Toast.makeText(getContext(), string, Toast.LENGTH_LONG).show();
	            return null;
	        }
		}
		return model;
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
