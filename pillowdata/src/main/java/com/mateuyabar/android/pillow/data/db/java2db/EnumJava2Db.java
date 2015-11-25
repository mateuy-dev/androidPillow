package com.mateuyabar.android.pillow.data.db.java2db;

import android.database.Cursor;

import com.mateuyabar.android.pillow.data.db.DBUtil;
import com.mateuyabar.android.pillow.data.models.IdentificableEnum;
import com.mateuyabar.util.exceptions.BreakFastException;

/**
 * Created by mateuyabar on 17/11/15.
 */
public class EnumJava2Db<T> extends BaseJava2DbType {
    @Override
    public boolean accepts(Class<?> fieldClass) {
        return Enum.class.isAssignableFrom(fieldClass);
    }

    @Override
    public Integer javaToDb(Object value) {
        if(value==null)
            return null;
        if(value instanceof IdentificableEnum){
            ((IdentificableEnum)value).getId();
        }
        return ((Enum<?>)value).ordinal();
    }

    @Override
    public  T dbToJava(Cursor cursor, int columnIndex,  Class<?> enumClass) {
        if (cursor.isNull (columnIndex))
            return null;
        int id = cursor.getInt(columnIndex);
        Object[] values = enumClass.getEnumConstants();
        if(enumClass.isAssignableFrom(IdentificableEnum.class)){
            for(Object value:values){
                if(((IdentificableEnum)value).getId() == id){
                    return (T) value;
                }
            }
        } else {
            return (T) values[id];
        }
        throw new BreakFastException();
    }

    @Override
    public String getDbType() {
        return  DBUtil.ENUM_TYPE;
    }
}
