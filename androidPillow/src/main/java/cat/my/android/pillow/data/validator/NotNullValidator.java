package cat.my.android.pillow.data.validator;

import java.lang.reflect.Field;

import cat.my.android.pillow.data.validator.IValidator.IValidationError;
import cat.my.android.pillow.data.validator.annotations.NotEmpty;
import cat.my.android.pillow.data.validator.annotations.NotNull;
import cat.my.util.exceptions.BreakFastException;

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
