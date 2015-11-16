package com.mateuyabar.android.pillow.util.reflection;

import com.mateuyabar.android.pillow.view.reflection.ViewConfig;

import java.lang.reflect.Field;

/**
 * Created by mateuyabar on 16/11/15.
 */
public class ViewReflectionUtil {
    public static boolean isHidden(Field field){
        ViewConfig.ViewType annotation = field.getAnnotation(ViewConfig.ViewType.class);
        return annotation!=null && annotation.hidden();
    }
}
