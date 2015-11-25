package com.mateuyabar.android.pillow.data.db.java2db;

import android.database.Cursor;

import com.mateuyabar.android.pillow.data.db.DBUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Deprecated
public class DateJava2Db extends BaseSingleClassNullableJava2DbType<Date> {
    @Override
    public Class<Date> getJavaFiledClass() {
        return Date.class;
    }

    @Override
    protected Object javaToDbNotNull(Date value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DBUtil.DATE_TIME_STRING_FORMAT);
        return dateFormat.format(value);
    }

    @Override
    protected Date dbToJavaNotNull(Cursor cursor, int columnIndex) {
        String date = cursor.getString(columnIndex);
        return dbToDate(date);
    }

    @Override
    public String getDbType() {
        return  DBUtil.CALENDAR_TYPE;
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
