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

import com.mateuyabar.android.pillow.util.reflection.ReflectionUtil;
import com.mateuyabar.util.exceptions.BreakFastException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DefaultValidator<T> implements IValidator<T>{
	Class<T> modelClass;
	GenericComparator comparator = new GenericComparator();
	
	GreaterThanValidator<T> greaterThanValidator  = new GreaterThanValidator<T>();
	NotNullValidator<T> notNullValidator = new NotNullValidator<T>();
	NotEmptyValidator<T> notEmptyValidator = new NotEmptyValidator<T>();
	MaxValidator<T> maxValidator = new MaxValidator<T>();
	MinValidator<T> minValidator = new MinValidator<T>();
	
	
	public DefaultValidator(Class<T> modelClass) {
		this.modelClass = modelClass;
	}

	@Override
	public List<IValidationError> validate(T model) {
		try {
			List<IValidationError> errors = new ArrayList<IValidationError>();
			for(Field field :ReflectionUtil.getStoredFields(modelClass)){
				field.setAccessible(true);
				
				//NotNull validation
				IValidationError notNullError = notNullValidator.validate(model, field);
				if(notNullError!=null){
					errors.add(notNullError);
					continue;
				}
				
				//NotEmpty validation
				IValidationError notEmptyError = notEmptyValidator.validate(model, field);
				if(notEmptyError!=null){
					errors.add(notEmptyError);
					continue;
				}
				
				//Max validation
				IValidationError error = maxValidator.validate(model, field);
				addIfNotNull(errors, error);
				
				//Min validation
				error = minValidator.validate(model, field);
				addIfNotNull(errors, error);
				
				//GreaterThan validation
				error = greaterThanValidator.validate(model, field);
				addIfNotNull(errors,error);
			}
			return errors;
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
	}

	
	public static <Q> void addIfNotNull(List<Q> list, Q item){
		if(item!=null){
			list.add(item);
		}
	}
	
	


}

