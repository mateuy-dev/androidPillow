package com.mateuyabar.android.pillow.androidpillow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mateuyabar.android.pillow.PillowView;
import com.mateuyabar.android.pillow.androidpillow.models.SampleModel;
import com.mateuyabar.android.pillow.view.NavigationUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PillowView.getInstance(this).configureNavigation(new NavigationUtil(this, R.id.container));

        if (savedInstanceState == null) {
            PillowView.getInstance(this).getNavigation().displayListModel(SampleModel.class);
        }
    }
}
