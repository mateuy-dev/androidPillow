package com.mateuyabar.android.pillow.data.sync.singleinstance;

import android.content.Context;

import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResult;
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
    public PillowResult<T> get() {
        return getLocalDataSource().get();
    }

    @Override
    public PillowResult<Boolean> exists() {
        return getLocalDataSource().exists();
    }


    @Override
    public IPillowResult<Collection<T>> download() {
        //Only local -> server synch for now.
        Collection<T> list = new ArrayList<>();
        return new PillowResult<Collection<T>>(list);
    }
}
