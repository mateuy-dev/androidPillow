package com.mateuyabar.android.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class MetricUtil {
	public static int dipToPixels(Context context, float dipValue) {
	    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
	}
}
