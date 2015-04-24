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

package com.mateuyabar.android.pillow.util.reflection;

import com.mateuyabar.android.pillow.util.reflection.ValuesTypes.Embeddable;
import com.mateuyabar.android.pillow.util.reflection.ValuesTypes.ValueType;
import com.mateuyabar.android.pillow.util.reflection.ValuesTypes.ValueType.NONE;
import com.mateuyabar.util.CaseFormat;
import com.mateuyabar.util.exceptions.BreakFastException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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
