package com.mateuyabar.android.pillow.data.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.util.reflection.ReflectionUtil;
import com.mateuyabar.android.pillow.util.reflection.ValuesTypes.BelongsToOnDelete;
import com.mateuyabar.android.pillow.util.reflection.ValuesTypes.OrderBy;
import com.mateuyabar.android.pillow.util.reflection.ValuesTypes.ValueType;
import com.mateuyabar.util.exceptions.BreakFastException;
import com.mateuyabar.util.exceptions.UnimplementedException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReflectionDbMapping<T extends IdentificableModel> implements IDbMapping<T>{
	private static final String EMBEDDED_MODEL_ATTRIBUTE_SEPARATOR = "_";
	
	Class<T> modelClass;
	
	String[] projectionAttributes;
	String[][] atts;
	boolean orderByLoaded=false;
	String orderBy;
	
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
		if(!orderByLoaded){
			Field[] fields = ReflectionUtil.getStoredFields(modelClass);
			for(Field field: fields){
				OrderBy orderByAnnotation = (OrderBy) field.getAnnotation(OrderBy.class);
				if(orderByAnnotation!=null){
					orderBy = field.getName();
					switch(orderByAnnotation.type()){
					case ASC:
						break;
					case DESC:
						orderBy += " DESC";
						break;
					case ASC_NO_COLLATE:
						orderBy += " COLLATE NOCASE";
						break;
					case DESC_NO_COLLATE:
						orderBy += " COLLATE NOCASE DESC";
						break;
					}
					
				}
				
			}
			orderByLoaded=true;
		}
		return orderBy;
	}

	@Override
	public void addModelContentValues(T model, ContentValues values) {
        addModelContentValues(model, values, "");
	}

    public void addModelContentValues(Object model, ContentValues values, String prefix) {
        for(Field field: ReflectionUtil.getStoredFields(model.getClass())){
            String key = prefix+field.getName();
            Object value  = dbValue(field, model);

            if(ReflectionUtil.isEmbeddable(value.getClass())){
                addModelContentValues(value, values, key + EMBEDDED_MODEL_ATTRIBUTE_SEPARATOR);
            } else {
                DBUtil.put(values, key, value);
            }
        }
    }



    private Object dbValue(Field field, Object model) {
        try {
            field.setAccessible(true);
            Object value = field.get(model);
            return DBUtil.dbValue(value);
        } catch (Exception e) {
            throw new BreakFastException(e);
        }
    }

	
	@Override
	public T createModel(Cursor cursor, String id) {
		try {
			T model;
			model = modelClass.newInstance();
			model.setId(id);
			fillModel(cursor, model);
			return model;
		} catch (Exception e) {
			throw new BreakFastException(e);
		}
	}
	
	private <K> K fillModel(Cursor cursor, K model){
		return fillModel(cursor, model, "");
		
	}


    public <T> T getValue(Cursor cursor, String fieldName, Class<T> fieldClass){
        Object value = null;
        if(String.class.isAssignableFrom(fieldClass)){
            value = cursor.getString(cursor.getColumnIndex(fieldName));
        } else if(ReflectionUtil.isInt(fieldClass)){
            value = cursor.getInt(cursor.getColumnIndex(fieldName));
        }else  if(ReflectionUtil.isBoolean(fieldClass)){
            value = DBUtil.getBoolean(cursor, cursor.getColumnIndex(fieldName));
        } else if(Double.class.isAssignableFrom(fieldClass)){
            value = cursor.getDouble(cursor.getColumnIndex(fieldName));
        } else if(Long.class.isAssignableFrom(fieldClass)){
            value = cursor.getLong(cursor.getColumnIndex(fieldName));
        } else if(Calendar.class.isAssignableFrom(fieldClass)){
            value = DBUtil.getCalendar(cursor, cursor.getColumnIndex(fieldName));
        } else if(Date.class.isAssignableFrom(fieldClass)){
            value = DBUtil.getDate(cursor, cursor.getColumnIndex(fieldName));
        } else if(Enum.class.isAssignableFrom(fieldClass)){
            Object[] enumValues = (Object[]) fieldClass.getEnumConstants();//EventType.values()
            value = DBUtil.dbToEnum(cursor, cursor.getColumnIndex(fieldName), enumValues);
        } else if(ReflectionUtil.isEmbeddable(fieldClass)){
            //Embeddable model
            Object embedModel;
            try {
                embedModel = fieldClass.newInstance();
            } catch (Exception e) {
                throw new BreakFastException();
            }
            fillModel(cursor, embedModel, fieldName+EMBEDDED_MODEL_ATTRIBUTE_SEPARATOR);
            value = embedModel;
        } else {
            throw new UnimplementedException();
        }
        return (T) value;
    }
	
	private <K> K fillModel(Cursor cursor, K model, String field_prefix){
		for(Field field: ReflectionUtil.getStoredFields(model.getClass())){
			String fieldName = field_prefix + field.getName();
			Class<?> fieldClass = field.getType();
			Object value = getValue(cursor, fieldName, fieldClass);
			field.setAccessible(true);
			try {
				field.set(model, value);
			} catch (Exception e) {
				throw new BreakFastException();
			}
		}
		return model;
	}
	
	

	public synchronized String[] getModelAttributesForProjection() {
		//TODO refactor and take id out.
		if(projectionAttributes!=null)
			return projectionAttributes;
		
		List<String> result = getModelAttributesForProjection(modelClass, "");
		projectionAttributes = result.toArray(new String[]{});
		return projectionAttributes;
	}
	
	private List<String> getModelAttributesForProjection(Class<?> modelClass, String prefix) {
		Field[] fields = ReflectionUtil.getStoredFields(modelClass);
		List<String> result = new ArrayList<String>();
		
		for(int i=0, j=0; i<fields.length; ++i){
			Field field = fields[i];
			String fieldName = field.getName();
			if(!fieldName.equals("id")){
				if(ReflectionUtil.isEmbeddable(field.getType())){
					result.addAll(getModelAttributesForProjection(field.getType(), prefix+fieldName+EMBEDDED_MODEL_ATTRIBUTE_SEPARATOR));
				} else {
					result.add(prefix+fieldName);
					j++;
				}
			}
		}
		return result;
	}

	

	public synchronized String[][] getAttributes() {
		if(atts!=null)
			return atts;
		
		List<String[]> result = getAttributes(modelClass, "");
		atts = result.toArray(new String[][]{});
		return atts;
	}
	
	private List<String[]> getAttributes(Class<?> modelClass, String prefix) {
		Field[] fields = ReflectionUtil.getStoredFields(modelClass);
		List<String[]> result = new ArrayList<String[]>();
		for(int i=0, j=0; i<fields.length; ++i){
			Field field = fields[i];
			String fieldName = field.getName();
			if(!fieldName.equals("id")){
				if(ReflectionUtil.isEmbeddable(field.getType())){
					result.addAll(getAttributes(field.getType(), prefix+fieldName+EMBEDDED_MODEL_ATTRIBUTE_SEPARATOR));
				} else {
					String[] map = new String[2];
					map[0]=prefix+fieldName;
					map[1]=getDbType(field);
					result.add(map);
					j++;
				}
			}
		}
		
		return result;
	}


	private String getDbType(Field field) {
		String type;
		Class<?> fieldClass = field.getType();
		if(String.class.isAssignableFrom(fieldClass)){
			type = DBUtil.STRING_TYPE;
		} else if(ReflectionUtil.isInt(fieldClass)){
			type = DBUtil.INT_TYPE;
		} else if(ReflectionUtil.isBoolean(fieldClass)){
			type = DBUtil.BOOLEAN_TYPE;
		} else if(Double.class.isAssignableFrom(fieldClass)){
			type = DBUtil.DOUBLE_TYPE;
		} else if(Long.class.isAssignableFrom(fieldClass)){
			throw new UnimplementedException();
		} else if(Calendar.class.isAssignableFrom(fieldClass)){
			type = DBUtil.CALENDAR_TYPE;
		} else if(Date.class.isAssignableFrom(fieldClass)){
			type = DBUtil.DATE_TYPE;
		} else if(Enum.class.isAssignableFrom(fieldClass)){
			type = DBUtil.ENUM_TYPE;
		} else {
			throw new UnimplementedException(fieldClass.toString());
		}
		return type;
	}


	@Override
	public IDBSelection getSelection(T filter) {
		//TODO add embedd models in search filter
		
		List<String> selectionList = new ArrayList<String>();
		List<String> args = new ArrayList<String>();
		
		Field[] fields = ReflectionUtil.getStoredFields(modelClass);
		for(Field field:fields){
			Class<?> fieldClass = field.getType();
			if(!ReflectionUtil.isNull(field, filter)){
				Object dbValue = dbValue(field, filter);
				String name = field.getName();
				selectionList.add(name + " == ?");
//				if(dbValue instanceof String){
//					args.add(dbValue.toString());
//				} else {
					args.add(dbValue.toString());
//				}
				
			}
		}
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(String selectionItem:selectionList){
			if(first)
				first = false;
			else
				builder.append(" AND ");
			builder.append(selectionItem);
		}
		String selection = builder.toString();
		
		return new DBSelection(selection, args.toArray(new String[]{}));
	}
	
	@Override
	public List<String> getTriggers() {
		List<String> result = new ArrayList<String>();
		Field[] fields = ReflectionUtil.getStoredFields(modelClass);
		for(Field field:fields){
			ValueType valueType = field.getAnnotation(ValueType.class);
			if(ReflectionUtil.isBelongsTo(field) && valueType.belongsToMode()!=BelongsToOnDelete.NO_ACTION){
				Class<? extends IdentificableModel> referencedClass = valueType.belongsTo();
				BelongsToOnDelete onDelete = valueType.belongsToMode();
				IDbMapping<?> referencedDbMapping = Pillow.getInstance().getModelConfiguration(referencedClass).getDbMapping();
				String parentTable = referencedDbMapping.getTableName();
				String childTable = getTableName();
				String foreignId = field.getName();
				
				String triggerName = "fkd_"+parentTable+"_"+childTable;
				String trigger = null;
				if(onDelete == BelongsToOnDelete.SET_NULL){
					trigger = "CREATE TRIGGER "+triggerName+" BEFORE DELETE ON "+parentTable+" FOR EACH ROW BEGIN UPDATE "+childTable+" SET "+foreignId+"=NULL WHERE "+foreignId+" = OLD.id; END";
				}else if (onDelete == BelongsToOnDelete.CASCADE){
					trigger = "CREATE TRIGGER "+triggerName+" BEFORE DELETE ON "+parentTable+" FOR EACH ROW BEGIN DELETE FROM "+childTable+" WHERE "+foreignId+" = OLD.id; END";
				} else{
					throw new UnimplementedException();
				}
				result.add(trigger);
			}
		}
		return result;
	}
	
//	Not used. Not using Foreign keys now (Check DBUtil)
//	public List<String> getForeignKeys(){
//		
//		List<String> result = new ArrayList<String>();
//		Field[] fields = ReflectionUtil.getStoredFields(modelClass);
//		for(Field field:fields){
//			ValueType valueType = field.getAnnotation(ValueType.class);
//			if(valueType!=null && valueType.belongsTo()!=null && valueType.belongsTo()!=NONE.class){
//				Class<? extends IdentificableModel> referencedClass = valueType.belongsTo();
//				BelongsToOnDelete onDelete = valueType.belongsToMode();
//				
//				IDbMapping<?> referencedDbMapping = Pillow.getInstance().getModelConfiguration(referencedClass).getDbMapping();
//				String referencedTable = referencedDbMapping.getTableName();
//				
//				String key = "FOREIGN KEY("+field.getName()+") REFERENCES "+referencedTable+"(id)" + getString(onDelete);
//				
//				result.add(key);
//			}
//		}
//		return result;
//	}
//	
//	private String getString(BelongsToOnDelete onDelete) {
//		switch (onDelete) {
//		case SET_NULL:
//			return " ON DELETE SET NULL";
//		case CASCADE:
//			return  "ON DELETE CASCADE";
//		case RESTRICT:
//			return "";
//		}
//		throw new UnimplementedException();
//	}


	

}
