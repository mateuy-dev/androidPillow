package com.mateuyabar.android.pillow.data.validator;

import java.lang.reflect.Field;

import com.mateuyabar.android.pillow.data.validator.IValidator.IValidationError;
import com.mateuyabar.android.pillow.data.validator.annotations.Min;

public class MinValidator<T> extends AbstractFieldValidator<T, Min> {
	GenericComparator comparator = new GenericComparator();
	
	@Override
	public Class<Min> getAnnotationClass() {
		return Min.class;
	}

	@Override
	public IValidationError validate(T model, Field field, Min minAnnotation) throws IllegalAccessException, IllegalArgumentException {
		Object value = field.get(model);
		if (comparator.compare(value, minAnnotation.value()) < 0) {
			return new ValidationError(field, minAnnotation);
		}
		return null;
	}

}
