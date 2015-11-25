package com.mateuyabar.android.pillow.data.db.java2db;

import android.database.Cursor;

/**
 * Created by mateuyabar on 17/11/15.
 */
public abstract class BaseSingleClassNullableJava2DbType<JT> extends BaseSingleClassJava2DbType<JT> {


    @Override
    public Object javaToDb(Object value){
        if(value==null)
            return null;
        return javaToDbNotNull((JT) value);
    }

    protected abstract Object javaToDbNotNull(JT value);

    @Override
    public JT dbToJava(Cursor cursor, int columnIndex,  Class<?> fieldClass){
        if(cursor.isNull(columnIndex)){
            return null;
        }
        return dbToJavaNotNull(cursor, columnIndex);
    }

    protected abstract JT dbToJavaNotNull(Cursor cursor, int columnIndex);
}
