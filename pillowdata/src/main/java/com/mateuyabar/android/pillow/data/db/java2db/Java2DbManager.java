package com.mateuyabar.android.pillow.data.db.java2db;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Java2DbManager {
    List<Java2DbType> java2DbTypes = new ArrayList<>();

    public Java2DbManager(){
        java2DbTypes.add(new BooleanJava2Db());
        java2DbTypes.add(new CalendarJava2Db());
        java2DbTypes.add(new DateJava2Db());
        java2DbTypes.add(new EnumJava2Db<>());
        java2DbTypes.add(new DateTimeZoneJava2Db());
        java2DbTypes.add(new InstantJava2DB());
    }

    public Java2DbType get(Class<?> clazz){
        for(int i=0; i< java2DbTypes.size(); ++i){
            Java2DbType conversor = java2DbTypes.get(i);
            if(conversor.accepts(clazz))
                return conversor;
        }
        return null;
    }

    public String getDbType(Class<?> clazz){
        return get(clazz).getDbType();
    }

    public Object javaToDb(Object value){
        if(value==null)
            return null;
        Java2DbType java2DbType = get(value.getClass());
        if(java2DbType!=null)
            return java2DbType.javaToDb(value);
        else
            return value;
    }


    public Object dbToJava(Cursor cursor, String columnName,  Class<?> fieldClass){
        Java2DbType java2DbType = get(fieldClass);
        if(java2DbType!=null)
            return java2DbType.dbToJava(cursor, columnName, fieldClass);
        else
            throw new UnsupportedOperationException(fieldClass.toString());
    }

}
