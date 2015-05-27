package com.mateuyabar.android.pillow.data.sync.singleinstance;

import android.content.Context;

import com.mateuyabar.android.pillow.Listeners;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResultListener;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.rest.IAuthenticationController;
import com.mateuyabar.android.pillow.data.rest.IRestMapping;
import com.mateuyabar.android.pillow.data.singleinstance.ISynchLocalSingleInstanceDataSource;
import com.mateuyabar.android.pillow.data.sync.SynchDataSource;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by mateuyabar on 22/05/15.
 */
public class SynchSingleInstanceDataSource<T extends IdentificableModel> extends SynchDataSource<T> implements ISynchSingleInstanceDataSource<T> {

    public SynchSingleInstanceDataSource(Class<T> modelClass, ISynchLocalSingleInstanceDataSource<T> dbSource, IRestMapping<T> restMap, Context context, IAuthenticationController authenticationData) {
        super(modelClass, dbSource, restMap, context, authenticationData);
    }

    public SynchSingleInstanceDataSource(Class<T> modelClass, ISynchLocalSingleInstanceDataSource<T> dbSource, IRestMapping<T> restMap, Context context) {
        super(modelClass, dbSource, restMap, context);
    }

    @Override
    public ISynchLocalSingleInstanceDataSource<T> getLocalDataSource() {
        return (ISynchLocalSingleInstanceDataSource<T>) super.getLocalDataSource();
    }

    @Override
    public IPillowResult<T> get() {
        return getLocalDataSource().get();
    }

    @Override
    public IPillowResult<T> set(final T model) {
        final PillowResultListener<T> result = new PillowResultListener<>();
        get().addListeners(new Listeners.Listener<T>() {
            @Override
            public void onResponse(T response) {
                if(response !=null){
                    if(model.getId()==null){
                        model.setId(response.getId());
                    }
                    update(model).addListeners(result,result);
                } else {
                    create(model);
                }
            }
        }, result);
        return result;
    }

    @Override
    public IPillowResult<Boolean> exists() {
        return getLocalDataSource().exists();
    }


    @Override
    public IPillowResult<Collection<T>> download() {
        //Only local -> server synch for now.
        Collection<T> list = new ArrayList<>();
        return new PillowResult<Collection<T>>(list);
    }
}
