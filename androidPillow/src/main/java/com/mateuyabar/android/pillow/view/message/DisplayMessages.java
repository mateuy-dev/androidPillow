package com.mateuyabar.android.pillow.view.message;

import android.content.Context;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

/**
 * Created by mateuyabar on 26/05/15.
 */
public class DisplayMessages {
    private static final long ERROR_DURATION = 5000;

    public static void info(Context context, int resId){
        SnackbarManager.show(Snackbar.with(context).text(resId));
    }
    public static void info(Context context, String text){
        SnackbarManager.show(Snackbar.with(context).text(text));
    }

    public static void error(Context context, String text){
        SnackbarManager.show(Snackbar.with(context).text(text).duration(ERROR_DURATION));
    }

    public static void error(Context context, int resId){
        SnackbarManager.show(Snackbar.with(context).text(resId).duration(ERROR_DURATION));
    }
}
