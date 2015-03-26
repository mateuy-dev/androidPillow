package cat.my.android.pillow.data.validator;

import java.lang.reflect.Field;
import java.util.Collection;

import cat.my.android.pillow.data.validator.IValidator.IValidationError;
import cat.my.android.pillow.data.validator.annotations.Max;
import cat.my.android.pillow.data.validator.annotations.NotEmpty;
import cat.my.android.pillow.data.validator.annotations.NotNull;
import cat.my.util.exceptions.BreakFastException;
import cat.my.util.exceptions.UnimplementedException;

public class NotEmptyValidator<T> extends AbstractFieldValidator<T, NotEmpty> {
	@Override
	public Class<NotEmpty> getAnnotationClass() {
		return NotEmpty.class;
	}
	
	@Override
	public IValidationError validate(T model, Field field, NotEmpty notEmpty) throws IllegalAccessException, IllegalArgumentException {

		boolean notEmptyError = false;
		Object value = field.get(model);
		if (value instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) value;
			notEmptyError = (collection == null || collection.isEmpty());
		} else if (value instanceof String) {
			String string = (String) value;
			notEmptyError = string == null || string.length() == 0;
		} else {
			throw new UnimplementedException();
		}
		if (notEmptyError) {
			return new ValidationError(field, notEmpty);
		}

		return null;

	}

}
