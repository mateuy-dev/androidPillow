package cat.my.android.util;

import android.database.Cursor;

public class CursorUtil {
	public static int getInt(Cursor cursor, String columnName){
		return cursor.getInt(cursor.getColumnIndex(columnName));
	}
	public static String getString(Cursor cursor, String columnName){
		return cursor.getString(cursor.getColumnIndex(columnName));
	}
	public static Long getLong(Cursor cursor, String columnName){
		return cursor.getLong(cursor.getColumnIndex(columnName));
	}
}
