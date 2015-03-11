package cat.my.android.pillow.view.forms;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.Log;
import cat.my.android.pillow.Pillow;

public class StringResourceUtils {
	public static String getLabel(Context context, Field field){
		String stringId = field.getDeclaringClass().getSimpleName()+"_"+field.getName();
		String label = getLabel(context, stringId);
		if(label == null)
			label = field.getName();
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
	
	private static String getLabel(Context context, String stringId){
		int id = context.getResources().getIdentifier(stringId, "string", context.getPackageName());
		String label = null; 
		if(id!=0)
			label = context.getResources().getString(id);
		else{
			Log.e(Pillow.LOG_ID, "The following String needs to be added to string.xml: "+stringId);
		}
		return label;
	}

}
