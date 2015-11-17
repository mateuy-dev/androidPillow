package com.mateuyabar.android.pillow.data.db.java2db;

import android.database.Cursor;

import com.mateuyabar.android.pillow.data.db.DBUtil;
import com.mateuyabar.android.pillow.util.reflection.ReflectionUtil;


public class BooleanJava2Db implements Java2DbType {
    @Override
    public boolean accepts(Class<?> value) {
        return ReflectionUtil.isBoolean(value);
    }

    @Override
    public Integer javaToDb(Object value) {
        if(value==null) return null;
        return ((Boolean)value) ? DBUtil.BOOLEAN_TRUE : DBUtil.BOOLEAN_FALSE;
    }

    @Override
    public Boolean dbToJava(Cursor cursor, int columnIndex, Class<?> javaClass) {
       int value = cursor.getInt(columnIndex);
        return value== DBUtil.BOOLEAN_TRUE ? Boolean.TRUE : Boolean.FALSE;
    }


}
