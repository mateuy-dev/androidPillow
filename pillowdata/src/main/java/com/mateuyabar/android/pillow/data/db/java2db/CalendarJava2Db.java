package com.mateuyabar.android.pillow.data.db.java2db;

import android.database.Cursor;

import com.mateuyabar.android.pillow.data.db.DBUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Deprecated
public class CalendarJava2Db implements Java2DbType {
    @Override
    public boolean accepts(Class<?> fieldClass) {
        return Calendar.class.isAssignableFrom(fieldClass);
    }

    @Override
    public Object javaToDb(Object date) {
        if(date==null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DBUtil.DATE_TIME_STRING_FORMAT);
        return dateFormat.format(((Calendar)date).getTime());
    }

    @Override
    public Object dbToJava(Cursor cursor, int columnIndex, Class<?> fieldClass) {
        String date = cursor.getString(columnIndex);
        if(date==null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateJava2Db.dbToDate(date));
        return cal;
    }
}
