package com.mateuyabar.android.pillow.view.presenters;

import android.content.Context;

import com.mateuyabar.android.pillow.Listeners;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.sync.CommonListeners;
import com.mateuyabar.util.exceptions.BreakFastException;

/**
 * Created by mateuyabar on 23/11/15.
 */
public class PillowShowPresenter<T extends IdentificableModel> extends PillowModelBasePresenter<T> {
    String modelId;
    ViewRenderer<T> view;
    T model;

    public PillowShowPresenter(Context context, Class < T > modelClass) {
        super(context, modelClass);
    }

    public void initialize(ViewRenderer<T> view){
        this.view = view;
        loadModel();
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }


    public void loadModel(){
        T idModel;
        try {
            idModel = modelClass.newInstance();
        } catch (Exception e) {
            throw new BreakFastException(e);
        }
        idModel.setId(modelId);
        getDataSource().show(idModel).addListeners(new Listeners.ViewListener<T>() {
            @Override
            public void onResponse(T model) {
                PillowShowPresenter.this.model = model;
                view.render(model);
            }
        }, CommonListeners.defaultErrorListener);
    }

    protected T getModel() {
        return model;
    }

    public interface ViewRenderer<T> {
        void render(T model);
    }
}
