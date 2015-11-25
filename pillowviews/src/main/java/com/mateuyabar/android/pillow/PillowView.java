package com.mateuyabar.android.pillow;

import android.content.Context;

import com.mateuyabar.android.pillow.conf.IModelViewConfigurations;
import com.mateuyabar.android.pillow.conf.ModelViewConfiguration;
import com.mateuyabar.android.pillow.conf.ModelViewConfigurationFactory;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.view.NavigationUtil;

/**
 * Created by mateuyabar on 16/11/15.
 */
public class PillowView {
    private static PillowView pillowView;
    public static synchronized PillowView getInstance(Context context){
        if(pillowView==null){
            pillowView = new PillowView();
            pillowView.init(context);

        }
        return pillowView;
    }

    Pillow pillow;
    Context context;
    ModelViewConfigurationFactory modelViewConfigurationFactory;
    NavigationUtil navigation;

    private void init(Context context) {
        this.context=context;
        pillow = Pillow.getInstance(context);
        modelViewConfigurationFactory = new ModelViewConfigurationFactory(context, pillow.getConfig(), (IModelViewConfigurations) pillow.getModelConfigurations());
    }

    /**
     * Shortcut for getModelConfiguration(modelClass).getViewConfiguration();
     */
    public <T extends IdentificableModel> ModelViewConfiguration<T> getViewConfiguration(Class<T> modelClass){
        return modelViewConfigurationFactory.getModelViewConfiguration(modelClass);
    }

    public void configureNavigation(NavigationUtil navigation){
        this.navigation = navigation;
    }

    public NavigationUtil getNavigation(){
        return navigation;
    }

}
