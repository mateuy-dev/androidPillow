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
				NotNull notNull = field.getAnnotation(NotNull.class);
				if(notNull!=null){
					if(field.get(model)==null){
						errors.add(new ValidationError(field, notNull));
						continue;
					}
				}
				
				//NotEmpty validation
				NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
				if(notEmpty!=null){
					boolean notEmptyError = false;
					Object value = field.get(model);
					if(value instanceof Collection<?>){
						Collection<?> collection = (Collection<?>) value;
						notEmptyError = (collection==null || collection.isEmpty());
					} else if(value instanceof String){
						String string = (String) value;
						notEmptyError = string==null || string.length()==0;
					} else {
						throw new UnimplementedException();
					}
					if(notEmptyError){
						errors.add(new ValidationError(field, notEmpty));
						continue;
					}
				}
				
				//Max validation
				Max maxAnnotation = field.getAnnotation(Max.class);
				if(maxAnnotation!=null){
					Object value = field.get(model);
					if(comparator.compare(value, maxAnnotation.value()) >0){
						errors.add(new ValidationError(field, maxAnnotation));
					}
				}
				//Min validation
				Min minAnnotation = field.getAnnotation(Min.class);
				if(minAnnotation!=null){
					Object value = field.get(model);
					if(comparator.compare(value, maxAnnotation.value()) < 0){
						errors.add(new ValidationError(field, minAnnotation));
					}
				}
				
				//GreaterThan validation
				GreaterThan greatterAnnotation = field.getAnnotation(GreaterThan.class);
				if(greatterAnnotation!=null){
					Object value = field.get(model);
					String compareAtt = greatterAnnotation.attribute();
					Field compareField = modelClass.getDeclaredField(compareAtt);
					compareField.setAccessible(true);
					Object compareValue = compareField.get(model);
					int comparizon = comparator.compare(value, compareValue);
					if(comparizon < 0 || (!greatterAnnotation.acceptEqual() && comparizon==0)){
						errors.add(new ValidationError(field, greatterAnnotation));
					}
				}
			}
			return errors;
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
	}

	GenericComparator comparator = new GenericComparator();
	public class GenericComparator implements Comparator<Object>{

		@Override
		public int compare(Object o1, Object o2) {
			if (o1 instanceof Integer){
				Integer i1 = (Integer) o1;
				Integer i2 = (Integer) o2;
				return i1-i2;
			}
			if(o1 instanceof Date){
				Date date1 = (Date) o1;
				Date date2 = (Date) o2;
				return (int) (date1.getTime() - date2.getTime());
			}
			if(o1 instanceof Calendar){
				Calendar cal1 = (Calendar) o1;
				Calendar cal2 = (Calendar) o2;
				return cal1.compareTo(cal2);
			}
			throw new UnimplementedException();
		}
	}
	
	


}

