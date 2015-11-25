package com.mateuyabar.android.pillow.data.db.java2db;

import android.database.Cursor;


public abstract class BaseSingleClassJava2DbType<JT> extends BaseJava2DbType {
    public abstract Class<JT> getJavaFiledClass();

    @Override
    public boolean accepts(Class<?> fieldClass) {
        return getJavaFiledClass().isAssignableFrom(fieldClass);
    }


    public JT dbToJava(Cursor cursor, int columnIndex){
        return (JT) dbToJava(cursor, columnIndex, getJavaFiledClass());
    }


}
