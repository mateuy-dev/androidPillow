package com.mateuyabar.android.pillow.data.validator;

import java.lang.reflect.Field;

import com.mateuyabar.android.pillow.data.validator.IValidator.IValidationError;
import com.mateuyabar.android.pillow.data.validator.annotations.NotNull;

public class NotNullValidator<T> extends AbstractFieldValidator<T, NotNull> {

	@Override
	public Class<NotNull> getAnnotationClass() {
		return NotNull.class;
	}

	@Override
	public IValidationError validate(T model, Field field, NotNull notNull) throws IllegalAccessException, IllegalArgumentException {
		if (field.get(model) == null) {
			return new ValidationError(field, notNull);
		}
		return null;

	}

}
