package com.mateuyabar.android.pillow.data.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.mateuyabar.android.pillow.data.validator.IValidator.IValidationError;
import com.mateuyabar.util.exceptions.BreakFastException;

public abstract class AbstractFieldValidator<T, K extends Annotation> implements IFieldValidator<T>{
	
	public abstract Class<K> getAnnotationClass();
	
	@Override
	public IValidationError validate(T model, Field field) {
		try{
			K annotation = field.getAnnotation(getAnnotationClass());
			if(annotation!=null){
				return validate(model, field, annotation);
			}
			return null;
		} catch(Exception e){
			throw new BreakFastException();
		}
	}

	public abstract IValidationError validate(T model, Field field, K annotation) throws Exception;

}
