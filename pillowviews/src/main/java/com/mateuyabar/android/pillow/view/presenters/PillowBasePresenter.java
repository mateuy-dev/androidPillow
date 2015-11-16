package com.mateuyabar.android.pillow.view.presenters;

import android.content.Context;

import com.mateuyabar.android.cleanbase.BaseComponent;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;


public abstract class PillowBasePresenter extends BaseComponent {
    public PillowBasePresenter(Context context) {
        super(context);
    }

    protected Pillow getPillow(){
        return Pillow.getInstance(getContext());
    }

    public <T extends IdentificableModel> IDataSource<T> getDataSource(Class<T> modelclass){
        return getPillow().getDataSource(modelclass);
    }
}
