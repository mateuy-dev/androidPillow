package com.mateuyabar.android.pillow.data.db.java2db;

import android.database.Cursor;

import com.mateuyabar.android.pillow.data.db.DBUtil;

import org.joda.time.Instant;

/**
 * Created by mateuyabar on 17/11/15.
 */
public class InstantJava2DB extends BaseSingleClassNullableJava2DbType<Instant> {
    @Override
    public Class<Instant> getJavaFiledClass() {
        return Instant.class;
    }

    @Override
    protected Object javaToDbNotNull(Instant value) {
        return value.getMillis();
    }

    @Override
    protected Instant dbToJavaNotNull(Cursor cursor, int columnIndex) {
        Long millis = cursor.getLong(columnIndex);
        return new Instant(millis);
    }

    @Override
    public String getDbType() {
        return  DBUtil.LONG_TYPE;
    }
}
