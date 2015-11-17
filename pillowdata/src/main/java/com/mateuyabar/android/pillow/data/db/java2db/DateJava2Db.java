package com.mateuyabar.android.pillow.data.db.java2db;

import android.database.Cursor;

import com.mateuyabar.android.pillow.data.db.DBUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Deprecated
public class DateJava2Db implements Java2DbType {
    @Override
    public boolean accepts(Class<?> fieldClass) {
        return Date.class.isAssignableFrom(fieldClass);
    }

    @Override
    public String javaToDb(Object value) {
        if(value==null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DBUtil.DATE_TIME_STRING_FORMAT);
        return dateFormat.format(value);
    }

    @Override
    public Date dbToJava(Cursor cursor, int columnIndex, Class<?> fieldClass) {
        String date = cursor.getString(columnIndex);
        return dbToDate(date);
    }

    public static Date dbToDate(String date){
        if(date==null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DBUtil.DATE_TIME_STRING_FORMAT);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
