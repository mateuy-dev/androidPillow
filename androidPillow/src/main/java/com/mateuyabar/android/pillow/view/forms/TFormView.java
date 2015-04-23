package com.mateuyabar.android.pillow.view.forms;


import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Collection;
import java.util.List;

import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.R;
import com.mateuyabar.android.pillow.conf.ModelConfiguration;
import com.mateuyabar.android.pillow.data.validator.DefaultValidator;
import com.mateuyabar.android.pillow.data.validator.IValidator;
import com.mateuyabar.android.pillow.data.validator.IValidator.IValidationError;
import com.mateuyabar.android.pillow.data.validator.ValidationErrorUtil;
import com.mateuyabar.android.util.MetricUtil;


public class TFormView<T> extends GridLayout{
	FormInputs formInputs;
	T model;
	boolean editable;
    boolean singleColumn;
    IValidator<T> validator;


	public TFormView(Context context, boolean editable) {
		super(context);
		this.editable = editable;
		init();
	}
	
	private void init() {
        //assuming singleCloumn == false
		setSingleColumn(singleColumn);
		setFocusable(true);
	    setFocusableInTouchMode(true);
        configureLayoutParams();
        setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_background));
	}

    private void configureLayoutParams(){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
        int margin = MetricUtil.dipToPixels(getContext(), 20);
        params.topMargin= margin;
        int marginHorizontal = getContext().getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        int marginVertical = getContext().getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        setPadding(marginHorizontal, marginVertical, marginHorizontal, marginVertical);
        setLayoutParams(params);

    }

    public void setSingleColumn(boolean singleColumn) {
        this.singleColumn = singleColumn;
        if(singleColumn){
            setColumnCount(1);
        } else {
            setColumnCount(2);
        }

    }

    /**
     * @return validator used. If not specified with a set, the pillow configuration is used or Default validator by default
     */
    public IValidator<T> getValidator() {
        if(validator==null){
            Class modelClass = model.getClass();
            ModelConfiguration modelConf = Pillow.getInstance(getContext()).getModelConfiguration(modelClass);
            if(modelConf!=null)
                validator = modelConf.getValidator();
            else
                validator = new DefaultValidator<T>(modelClass);
        }
        return validator;
    }

    /**
     * Specify an specific validator for the form
     * @param validator
     */
    public void setValidator(IValidator<T> validator) {
        this.validator = validator;
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

			
			List<IValidationError> errors = getValidator().validate(model);
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
		boolean first = true;
		for(FormInputRow rowInput : inputs){
			View label = rowInput.getLabel();
			GridLayout.LayoutParams labelParams = new GridLayout.LayoutParams();
            int rightPadding = getContext().getResources().getDimensionPixelSize(R.dimen.form_label_right_padding);
            int topPadding = label.getPaddingTop();
            if(singleColumn) {
                labelParams.setGravity(Gravity.LEFT);
                if(!first)
                	topPadding = getContext().getResources().getDimensionPixelSize(R.dimen.form_label_single_column_top_padding);
            } else {
                labelParams.setGravity(Gravity.RIGHT);
            }
			label.setLayoutParams(labelParams);
			label.setPadding(label.getPaddingLeft(), topPadding, rightPadding, label.getPaddingBottom());
			
			View input = rowInput.getInput();
			GridLayout.LayoutParams inputParams = new GridLayout.LayoutParams();
            inputParams.setGravity(Gravity.LEFT);
			input.setLayoutParams(inputParams);
			
			addView(label);
			addView(input);
			first = false;
		}
	}

	private void updateModelFromForm() {
		formInputs.updateModelFromForm();
	}


	
}
