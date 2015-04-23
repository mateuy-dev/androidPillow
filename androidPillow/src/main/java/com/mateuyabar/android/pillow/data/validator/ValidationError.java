package com.mateuyabar.android.pillow.data.validator;



import java.lang.reflect.Field;

import com.mateuyabar.android.pillow.data.validator.IValidator.IValidationError;

public class ValidationError implements IValidationError{
	Field field;
	Object error;
	public ValidationError(Field field, Object error) {
		super();
		this.field = field;
		this.error = error;
	}
	
	@Override
	public Field getField() {
		return field;
	}
	
	@Override
	public Object getError() {
		return error;
	}
	
	}