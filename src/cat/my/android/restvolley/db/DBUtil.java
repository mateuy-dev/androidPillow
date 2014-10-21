package cat.my.android.restvolley.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.database.Cursor;

public class DBUtil {
	public static final String COMMA_SEP = ",";
	public static final int BOOLEAN_TRUE = 1;
	public static final int BOOLEAN_FALSE = 0;
	public static final String STRING_TYPE = " TEXT";
	public static final String BOOLEAN_TYPE = " INTEGER";
	public static final String TIMESTAMP_TYPE = " INTEGER";
	public static final String INT_TYPE = " INTEGER";
	public static final String DECIMAL_TYPE = " INTEGER";
	public static final String DOUBLE_TYPE = " DOUBLE";
	public static final String DATE_TYPE = "  TEXT";

	public static String createTable(IDbMapping<?> mapping){
		return "CREATE TABLE " + mapping.getTableName() 
				+" (" + DBModelController.COMMON_MODEL_ATTRIBUTES + DBUtil.COMMA_SEP + 
				DBModelController.creteAttsString(mapping.getAttributes()) + ");";
	} 
	
	public static String dropTable(IDbMapping<?> mapping){
		return "DROP TABLE IF EXISTS "+mapping.getTableName();
	}

	public static boolean getBoolean(Cursor cursor, int columnIndex){
		int value = cursor.getInt(columnIndex);
		return value==DBUtil.BOOLEAN_TRUE ? true : false;
	}
	
	public static Date getDate(Cursor cursor, int columnIndex){
		String value = cursor.getString(columnIndex);
		return dbToDate(value);
	}
	
	public static Calendar getCalendar(Cursor cursor, int columnIndex){
		String value = cursor.getString(columnIndex);
		return dbToCalendar(value);
	}
	
	private static final String DATE_STRING_FORMAT = "yyyy-MM-dd";
	private static final String DATE_TIME_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static String calendarToDb(Calendar date){
		if(date==null) return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
		return dateFormat.format(date.getTime());
	}
	
	/**
	 * Converts a date to string for json or database storage
	 * @param date
	 * @return
	 */
	public static String dateToDb(Object date){
		if(date==null) return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
		return dateFormat.format(date);
	}
	
	/**
	 * Converts a String obtained from json or DB to date for model usage
	 * @param date
	 * @return
	 */
	public static Date dbToDate(String date){
		if(date==null) return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Converts a String obtained from json or DB to date for model usage
	 * @param date
	 * @return
	 */
	public static Calendar dbToCalendar(String date){
		if(date==null) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dbToDate(date));
		return cal;
	}
	
	public static String dateTimeToDb(Date date){
		if(date==null) return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_STRING_FORMAT);
		return dateFormat.format(date);
	}
	
	public static Date dbToDateTime(String date){
		if(date==null) return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_STRING_FORMAT);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String createUUID() {
		return UUID.randomUUID().toString();
	}
	
}
