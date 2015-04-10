package cat.my.android.pillow.data.validator;

import java.lang.reflect.Field;
import java.util.List;

import cat.my.android.pillow.data.validator.IValidator.IValidationError;

public interface IFieldValidator<T> {
	public IValidationError validate(T model, Field field);

}
