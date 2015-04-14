package cat.my.android.pillow.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import cat.my.android.pillow.util.reflection.ValuesTypes.Embeddable;
import cat.my.android.pillow.util.reflection.ValuesTypes.ValueType;
import cat.my.android.pillow.util.reflection.ValuesTypes.ValueType.NONE;
import cat.my.util.CaseFormat;
import cat.my.util.exceptions.BreakFastException;

public class ReflectionUtil {
	
	/**
	 * Returns the declared fields including fields form parent classes that are not transient neither synthetic
	 * @param type
	 * @return
	 */
	public static Field[] getStoredFields(Class type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            Field[] innerFields = c.getDeclaredFields();
            for(Field field:innerFields){
            	if(!field.isSynthetic() &&  !isTransient(field)){
            		fields.add(field);
            	}
            }
        }
        return fields.toArray(new Field[]{});
    }

	public static boolean isTransient(Field field){
		return Modifier.isTransient(field.getModifiers());
	}
	
	public static boolean isBelongsTo(Field field){
		ValueType valueType = field.getAnnotation(ValueType.class);
		return valueType!=null && valueType.belongsTo()!=null && valueType.belongsTo()!=NONE.class;
	}
	
	public static List<Class<?>> getBelongsToClasses(Class<?> modelClass){
		List<Class<?>> result = new ArrayList<Class<?>>();
		for(Field field : getBelongsToFields(modelClass)){
			ValueType valueType = field.getAnnotation(ValueType.class);
			result.add(valueType.belongsTo());
		}
		return result;
	}
	
	public static List<Field> getBelongsToFields(Class<?> modelClass){
		List<Field> result = new ArrayList<Field>();
		for(Field field: getStoredFields(modelClass)){
			if(ReflectionUtil.isBelongsTo(field)){
				result.add(field);
			}
		}
		return result;
	}
	
	public static void setReferenceId(Object model, Class<?> referencedClass, String id){
		String attribute = referencedClass.getSimpleName() +"Id";
		attribute = new CaseFormat().firstLetterToLowerCase(attribute);
		try {
			Field field = model.getClass().getDeclaredField(attribute);
			field.setAccessible(true);
			field.set(model, id);
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
	}
	
	public static boolean isEmbeddable(Class<?> clazz){
		return clazz.getAnnotation(Embeddable.class)!=null;
	}
	
	public static boolean isNull(Field field, Object model) {
		field.setAccessible(true);
		try {
			Object value = field.get(model);
			if(value==null)
				return true;
			Class<?> fieldClass = field.getType();
			if(int.class.isAssignableFrom(fieldClass)){
				//TODO check this: we define 0 as a null value for int
				int intValue = (Integer) value;
				return intValue==0;
			}
			return false;
			
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
	}

	public static boolean isInt(Class<?> fieldClass){
		return Integer.class.isAssignableFrom(fieldClass) || int.class.isAssignableFrom(fieldClass);
	}
	
	public static boolean isBoolean(Class<?> fieldClass){
		return Boolean.class.isAssignableFrom(fieldClass) || boolean.class.isAssignableFrom(fieldClass);
	}

    public static void setValue(Object model, String attribute, Object value)  {
        try {
            Field field = model.getClass().getField(attribute);
            field.setAccessible(true);
            field.set(model, value);
        } catch (Exception e){
            throw new BreakFastException(e);
        }
    }
}
