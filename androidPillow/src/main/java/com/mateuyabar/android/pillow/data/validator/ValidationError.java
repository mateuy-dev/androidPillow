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

import java.lang.reflect.Field;

public class ValidationError implements IValidationError{
	Field field;
	Object error;
	public ValidationError(Field field, Object error) {
		super();
		this.field = field;
		this.error = error;
	}
	
	@Override
	public Field getField() {
		return field;
	}
	
	@Override
	public Object getError() {
		return error;
	}
	
	}