package com.mateuyabar.android.pillow.androidpillow;


import android.content.Context;
import android.support.v4.app.Fragment;

import com.google.gson.reflect.TypeToken;
import com.mateuyabar.android.pillow.PillowConfigXml;
import com.mateuyabar.android.pillow.androidpillow.models.SampleModel;
import com.mateuyabar.android.pillow.conf.DefaultModelConfiguration;
import com.mateuyabar.android.pillow.conf.DefaultModelViewConfiguration;
import com.mateuyabar.android.pillow.conf.IModelViewConfigurations;
import com.mateuyabar.android.pillow.conf.ModelConfiguration;
import com.mateuyabar.android.pillow.conf.ModelViewConfiguration;
import com.mateuyabar.android.pillow.view.fragments.PillowListFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PillowConfiguration implements IModelViewConfigurations {
    @Override
    public List<ModelConfiguration<?>> getModelConfigurators(Context context, PillowConfigXml config) {
        String url = config.getUrl();

        List<ModelConfiguration<?>> configurations= new ArrayList<ModelConfiguration<?>>();
        DefaultModelConfiguration<SampleModel> sampleModelConfiguration = new DefaultModelConfiguration<>(context, SampleModel.class, new TypeToken<Collection<SampleModel>>(){}, url);
        configurations.add(sampleModelConfiguration);
        return configurations;
    }

    @Override
    public List<ModelViewConfiguration<?>> getModelViewConfigurators(Context context, PillowConfigXml config) {
        List<ModelViewConfiguration<?>> configurations = new ArrayList<>();
        configurations.add(new DefaultModelViewConfiguration<SampleModel>(SampleModel.class){
            @Override
            public Fragment getListFragment() {
                return new PillowListFragment<>();
            }
        });
        return configurations;
    }


}
