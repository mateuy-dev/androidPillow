/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.mateuyabar.android.pillow.data.validator;

import com.mateuyabar.android.pillow.data.validator.IValidator.IValidationError;
import com.mateuyabar.android.pillow.data.validator.annotations.GreaterThan;

import java.lang.reflect.Field;

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
