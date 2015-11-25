package com.mateuyabar.android.pillow.data.db.java2db;

import android.database.Cursor;

import com.mateuyabar.android.pillow.data.db.DBUtil;

import org.joda.time.DateTimeZone;

/**
 * Created by mateuyabar on 17/11/15.
 */
public class DateTimeZoneJava2Db extends BaseSingleClassNullableJava2DbType<DateTimeZone> {
    @Override
    public Class<DateTimeZone> getJavaFiledClass() {
        return DateTimeZone.class;
    }

    @Override
    protected Object javaToDbNotNull(DateTimeZone value) {
        return value.getID();
    }

    @Override
    protected DateTimeZone dbToJavaNotNull(Cursor cursor, int columnIndex) {
        return DateTimeZone.forID(cursor.getString(columnIndex));
    }

    @Override
    public String getDbType() {
        return  DBUtil.STRING_TYPE;
    }
}
