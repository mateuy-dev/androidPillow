package com.mateuyabar.android.pillow.data.db.java2db;

import android.database.Cursor;

import com.mateuyabar.android.pillow.data.db.DBUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Deprecated
public class CalendarJava2Db extends BaseSingleClassNullableJava2DbType<Calendar> {
    @Override
    public Class<Calendar> getJavaFiledClass() {
        return Calendar.class;
    }

    @Override
    protected Object javaToDbNotNull(Calendar date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DBUtil.DATE_TIME_STRING_FORMAT);
        return dateFormat.format(date.getTime());
    }



    @Override
    public Calendar dbToJavaNotNull(Cursor cursor, int columnIndex) {
        String date = cursor.getString(columnIndex);
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateJava2Db.dbToDate(date));
        return cal;
    }

    @Override
    public String getDbType() {
        return  DBUtil.CALENDAR_TYPE;
    }
}
