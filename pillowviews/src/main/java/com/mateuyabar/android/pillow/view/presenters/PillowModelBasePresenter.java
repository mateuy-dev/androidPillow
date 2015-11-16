package com.mateuyabar.android.pillow.view.presenters;

import android.content.Context;

import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;


public abstract class PillowModelBasePresenter<T extends IdentificableModel> extends PillowBasePresenter {
    Class<T> modelClass;

    public PillowModelBasePresenter(Context context, Class<T> modelClass) {
        super(context);
        this.modelClass = modelClass;
    }

    public <T> IDataSource<T> getDataSource() {
        return (IDataSource<T>) super.getDataSource(modelClass);
    }
}
