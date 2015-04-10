package cat.my.android.pillow.data.validator;

import java.lang.reflect.Field;

import cat.my.android.pillow.data.validator.IValidator.IValidationError;
import cat.my.android.pillow.data.validator.annotations.GreaterThan;
import cat.my.android.pillow.data.validator.annotations.Max;
import cat.my.android.pillow.data.validator.annotations.NotNull;
import cat.my.util.exceptions.BreakFastException;

public class MaxValidator<T> extends AbstractFieldValidator<T, Max> {
	GenericComparator comparator = new GenericComparator();
	
	@Override
	public Class<Max> getAnnotationClass() {
		return Max.class;
	}

	@Override
	public IValidationError validate(T model, Field field, Max maxAnnotation) throws IllegalAccessException, IllegalArgumentException {
		Object value = field.get(model);
		if (comparator.compare(value, maxAnnotation.value()) > 0) {
			return new ValidationError(field, maxAnnotation);
		}
		return null;
	}

}
