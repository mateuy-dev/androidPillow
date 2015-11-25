package com.mateuyabar.android.pillow.data.db.java2db;

import android.database.Cursor;

/**
 * Created by mateuyabar on 17/11/15.
 */
public interface Java2DbType {
    boolean accepts(Class<?> fieldClass);
    Object javaToDb(Object value);
    Object dbToJava(Cursor cursor, String name,  Class<?> fieldClass);
    String getDbType();
}
