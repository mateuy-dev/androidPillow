package com.mateuyabar.util;

/**
 * Boolea util functions
 */
public class BooleanUtil {

    public static boolean isTrue(Boolean bool){
        return bool!=null && bool;
    }

    public static boolean isFalse(Boolean bool){
        return bool!=null && !bool;
    }
}
