/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.mateuyabar.android.pillow.view.forms;

import android.content.Context;
import android.util.Log;

import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.view.forms.inputDatas.CalendarInputData;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

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
