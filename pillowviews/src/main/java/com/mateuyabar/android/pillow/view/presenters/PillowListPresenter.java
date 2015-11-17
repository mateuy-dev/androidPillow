package com.mateuyabar.android.pillow.view.presenters;

import android.content.Context;

import com.mateuyabar.android.pillow.Listeners;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.IExtendedDataSource;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.view.CommonViewListeners;

import java.util.Collection;

public class PillowListPresenter<T extends IdentificableModel> extends PillowModelBasePresenter {
    ViewRenderer view;
    T filter;
    boolean dataLoaded = false;


    public PillowListPresenter(Context context, Class modelClass) {
        super(context, modelClass);
    }

    public void initialize(ViewRenderer view){
        this.view = view;
    }

    public void setFilter(T filter) {
        this.filter = filter;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public IPillowResult<Collection<T>> index(){
        IDataSource<T> dataSource =  getDataSource();
        if(filter!=null && dataSource instanceof IExtendedDataSource)
            return ((IExtendedDataSource<T>)dataSource).index(filter);
        else
            return dataSource.index();
    }

    private void loadData(){
        index().addListeners(new Listeners.ViewListener<Collection<T>>() {
            @Override
            public void onResponse(Collection<T> models) {
                view.render(models);
                dataLoaded = true;
            }
        }, CommonViewListeners.defaultErrorListener);
    }

    public void synchronizeModels() {
        getPillow().getSynchManager().download(true).addListeners(new Listeners.ViewListener<Void>() {
            @Override
            public void onResponse(Void response) {
                loadData();
                view.synchronizationFinished();
            }
        }, new Listeners.ViewErrorListener() {
            @Override
            public void onErrorResponse(PillowError error) {
                view.synchronizationFinished();
                new CommonViewListeners.ErrorListenerWithNoConnectionToast(getContext()).onErrorResponse(error);
            }
        });
    }



    public interface ViewRenderer<T>{
        void render(Collection<T> models);
        void synchronizationFinished();
    }
}
