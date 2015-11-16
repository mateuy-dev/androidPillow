package com.mateuyabar.android.pillow.androidpillow;

import android.app.Application;

import com.mateuyabar.android.pillow.Pillow;

/**
 * Created by mateuyabar on 16/11/15.
 */
public class PillowDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Pillow.setConfigurationFile(R.xml.android_pillow);
    }
}

