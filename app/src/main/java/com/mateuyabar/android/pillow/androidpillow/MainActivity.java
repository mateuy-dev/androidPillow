package com.mateuyabar.android.pillow.androidpillow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mateuyabar.android.pillow.PillowView;
import com.mateuyabar.android.pillow.androidpillow.models.SampleModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            PillowView.getInstance(this).getNavigation(this, R.id.container).displayListModel(SampleModel.class);
        }
    }
}
