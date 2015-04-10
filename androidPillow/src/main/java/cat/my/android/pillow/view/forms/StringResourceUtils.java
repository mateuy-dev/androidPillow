package cat.my.android.pillow.view.forms;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.Log;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.view.forms.inputDatas.CalendarInputData;

public class StringResourceUtils {
	public static String getLabel(Context context, Field field){
        Class modelClass = field.getDeclaringClass();
        String fieldName = field.getName();
		return getLabel(context, modelClass, fieldName);
	}
    
    public static String getLabel(Context context, Class modelClass, String fieldName){
        String stringId = modelClass.getSimpleName()+"_"+fieldName;
        String label = getLabel(context, stringId);
        if(label == null)
            label = fieldName;
        return label;
    }
	
	public static String getLabel(Context context, Enum<?> value){
		if(value==null) return "";
		String stringId = value.getClass().getSimpleName()+"_"+value.name();
		String label = getLabel(context, stringId);
		if(label == null)
			label = value.name();
		return label;
	}
	
	public static String getLabel(Context context, String stringId){
		int id = context.getResources().getIdentifier(stringId, "string", context.getPackageName());
		String label = null; 
		if(id!=0)
			label = context.getResources().getString(id);
		else{
			Log.e(Pillow.LOG_ID, "The following String needs to be added to string.xml: "+stringId);
		}
		return label;
	}

	public static String getLabel(Context context, Date date, boolean dateTime){
		SimpleDateFormat dateFormat = new SimpleDateFormat(CalendarInputData.DATE_STRING_FORMAT);
		return dateFormat.format(date);
	}
}
