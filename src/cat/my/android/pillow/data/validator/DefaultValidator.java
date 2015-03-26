package cat.my.android.pillow.data.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cat.my.android.pillow.data.validator.annotations.GreaterThan;
import cat.my.android.pillow.data.validator.annotations.Max;
import cat.my.android.pillow.data.validator.annotations.Min;
import cat.my.android.pillow.data.validator.annotations.NotEmpty;
import cat.my.android.pillow.data.validator.annotations.NotNull;
import cat.my.android.pillow.util.reflection.ReflectionUtil;
import cat.my.util.exceptions.BreakFastException;
import cat.my.util.exceptions.UnimplementedException;

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

