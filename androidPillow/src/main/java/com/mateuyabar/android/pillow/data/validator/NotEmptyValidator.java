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
import com.mateuyabar.android.pillow.data.validator.annotations.NotEmpty;
import com.mateuyabar.util.exceptions.UnimplementedException;

import java.lang.reflect.Field;
import java.util.Collection;

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
