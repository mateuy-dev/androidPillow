package cat.my.android.restvolley.db;

import java.lang.reflect.Field;
import java.util.Date;

import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.exceptions.NotProgrammedException;
import cat.my.android.restvolley.reflection.ReflectionUtil;
import cat.my.util.BreakFastException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReflectionDbMapping<T extends IdentificableModel> implements IDbMapping<T>{
	Class<T> modelClass;
	
	public ReflectionDbMapping(Class<T> modelClass){
		this.modelClass=modelClass;
	}
	
	
	@Override
	public String getTableName() {
		return modelClass.getSimpleName();
	}

	@Override
	public void addRelations(T model) {
	}

	@Override
	public String getDefaultModelOrder() {
		return null;
	}

	@Override
	public void addModelContentValues(T model, ContentValues values) {
		for(Field field: ReflectionUtil.getStoredFields(modelClass)){
			put(values, field.getName(), dbValue(field, model));
		}
	}

	private Object dbValue(Field field, T model) {
		try {
			field.setAccessible(true);
			Object value = field.get(model);
			if(value instanceof Date){
				value = DBUtil.dateToDb((Date)value);
			} else if(value instanceof Enum){
				value = DBUtil.enumToDb((Enum<?>) value);
			}
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public T createModel(Cursor cursor, String id) {
		try {
			T model;
		
			model = modelClass.newInstance();
		
			model.setId(id);
			
			for(Field field: ReflectionUtil.getStoredFields(modelClass)){
				String fieldName = field.getName();
				Class<?> fieldClass = field.getType();
				Object value = null;
				if(String.class.isAssignableFrom(fieldClass)){
					value = cursor.getString(cursor.getColumnIndex(fieldName));
				} else if(Integer.class.isAssignableFrom(fieldClass)){
					value = cursor.getInt(cursor.getColumnIndex(fieldName));
				} else if(Double.class.isAssignableFrom(fieldClass)){
					value = cursor.getDouble(cursor.getColumnIndex(fieldName));
				} else if(Long.class.isAssignableFrom(fieldClass)){
					value = cursor.getLong(cursor.getColumnIndex(fieldName));
				} else if(Date.class.isAssignableFrom(fieldClass)){
					value = DBUtil.getDate(cursor, cursor.getColumnIndex(fieldName));
				} else if(Enum.class.isAssignableFrom(fieldClass)){
					Object[] enumValues = (Object[]) fieldClass.getEnumConstants();//EventType.values()
					value = DBUtil.dbToEnum(cursor, cursor.getColumnIndex(fieldName), enumValues);
				} else {
					throw new NotProgrammedException();
				}
				field.setAccessible(true);
				field.set(model, value);
			}
			return model;
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
	}


	/**
	 * Helper method to allow to put a value of type Object to ContentValues
	 * @param values
	 * @param key
	 * @param value
	 */
	private static void put(ContentValues values, String key, Object value) {
		if(value==null) return;
		
		if(value instanceof String)
			values.put(key, (String) value);
		else if(value instanceof Byte)
			values.put(key, (Byte) value);
		else if(value instanceof Short)
			values.put(key, (Short) value);
		else if(value instanceof Integer) //Integer.TYPE 
			values.put(key, (Integer) value);
		else if(value instanceof Long)
			values.put(key, (Long) value);
		else if(value instanceof Double)
			values.put(key, (Double) value);
		else if(value instanceof Boolean)
			values.put(key, (Boolean) value);
		else if(value instanceof byte[])
			values.put(key, (byte[]) value);
		else{
			throw new NotProgrammedException("can't save value of type" + value.getClass());
		}
	}
	
	String[] projectionAttributes;
	@Override
	public synchronized String[] getModelAttributesForProjection() {
		//TODO refactor and take id out.
		if(projectionAttributes!=null)
			return projectionAttributes;
		Field[] fields = ReflectionUtil.getStoredFields(modelClass);
		String[] result = new String[fields.length-1];
		
		for(int i=0, j=0; i<fields.length; ++i){
			String fieldName = fields[i].getName();
			if(!fieldName.equals("id")){
				result[j]= fieldName;
				j++;
			}
		}
		projectionAttributes = result;
		return projectionAttributes;
	}

	String[][] atts;
	@Override
	public synchronized String[][] getAttributes() {
		if(atts!=null)
			return atts;
		Field[] fields = ReflectionUtil.getStoredFields(modelClass);
		String[][] result = new String[fields.length-1][2];
		for(int i=0, j=0; i<fields.length; ++i){
			String fieldName = fields[i].getName();
			if(!fieldName.equals("id")){
				result[j][0]=fieldName;
				result[j][1]=getDbType(fields[i]);
				j++;
			}
		}
		atts = result;
		return result;
	}


	private String getDbType(Field field) {
		String type;
		Class<?> fieldClass = field.getType();
		if(String.class.isAssignableFrom(fieldClass)){
			type = DBUtil.STRING_TYPE;
		} else if(Integer.class.isAssignableFrom(fieldClass)){
			type = DBUtil.INT_TYPE;
		} else if(Double.class.isAssignableFrom(fieldClass)){
			type = DBUtil.DOUBLE_TYPE;
		} else if(Long.class.isAssignableFrom(fieldClass)){
			throw new NotProgrammedException();
		} else if(Date.class.isAssignableFrom(fieldClass)){
			type = DBUtil.DATE_TYPE;
		} else if(Enum.class.isAssignableFrom(fieldClass)){
			type = DBUtil.ENUM_TYPE;
		} else {
			throw new NotProgrammedException(fieldClass.toString());
		}
		return type;
	}

}
