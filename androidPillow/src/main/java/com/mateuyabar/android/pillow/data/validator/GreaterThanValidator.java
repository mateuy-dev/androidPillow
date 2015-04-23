package com.mateuyabar.android.pillow.data.validator;

import java.lang.reflect.Field;

import com.mateuyabar.android.pillow.data.validator.IValidator.IValidationError;
import com.mateuyabar.android.pillow.data.validator.annotations.GreaterThan;

public class GreaterThanValidator<T> extends AbstractFieldValidator<T, GreaterThan> {
	static GenericComparator comparator = new GenericComparator();

	@Override
	public Class<GreaterThan> getAnnotationClass() {
		return GreaterThan.class;
	}

	@Override
	public IValidationError validate(T model, Field field, GreaterThan greatterAnnotation) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
		Class<T> modelClass = (Class<T>) model.getClass();

		Object value = field.get(model);
		String compareAtt = greatterAnnotation.attribute();
		Field compareField = modelClass.getDeclaredField(compareAtt);
		compareField.setAccessible(true);
		Object compareValue = compareField.get(model);
		if(!isValid(value, compareValue, greatterAnnotation)){
			return new ValidationError(field, greatterAnnotation);
		}
		return null;
	}
	
	public static boolean isValid(Object value, Object compareValue, GreaterThan greatterAnnotation){
		int comparizon = comparator.compare(value, compareValue);
		if (comparizon < 0 || (!greatterAnnotation.acceptEqual() && comparizon == 0)) {
			return false;
		}
		return true;
		
	}

}
