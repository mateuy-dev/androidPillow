package cat.my.android.pillow.data.validator;

import java.lang.reflect.Field;
import java.util.List;

/**
 * http://beanvalidation.org/
 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html/validation.html
 *
 * @param <T>
 */
public interface IValidator<T> {
	public List<IValidationError> validate(T model);
	
	public interface IValidationError{
		public Field getField();
		public Object getError();
	}
}
