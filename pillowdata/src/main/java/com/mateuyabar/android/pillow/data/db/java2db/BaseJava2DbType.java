package com.mateuyabar.android.pillow.data.db.java2db;


import android.database.Cursor;

public abstract class BaseJava2DbType implements Java2DbType {
    public Object dbToJava(Cursor cursor, String name,  Class<?> fieldClass){
        return dbToJava(cursor, cursor.getColumnIndex(name), fieldClass);
    }

    public abstract Object dbToJava(Cursor cursor, int columnIndex,  Class<?> fieldClass);

}
