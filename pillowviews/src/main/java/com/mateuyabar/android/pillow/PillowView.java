package com.mateuyabar.android.pillow;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

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

    public NavigationUtil getNavigation(Fragment fragment){
        return new NavigationUtil(fragment);
    }

    public NavigationUtil getNavigation(FragmentActivity activity, int viewId){
        return new NavigationUtil(activity, viewId);
    }
}
