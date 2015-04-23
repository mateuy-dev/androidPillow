package com.mateuyabar.android.pillow.data.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.mateuyabar.android.pillow.util.reflection.ReflectionUtil;
import com.mateuyabar.util.exceptions.BreakFastException;

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

