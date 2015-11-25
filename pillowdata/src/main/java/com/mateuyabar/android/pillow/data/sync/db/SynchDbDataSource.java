package com.mateuyabar.android.pillow.data.sync.db;

import android.content.Context;

import com.mateuyabar.android.pillow.data.IRestDataSource;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.db.DBModelController;
import com.mateuyabar.android.pillow.data.db.IDbMapping;
import com.mateuyabar.android.pillow.data.db.ISynchLocalDbDataSource;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.rest.IAuthenticationController;
import com.mateuyabar.android.pillow.data.rest.IRestMapping;
import com.mateuyabar.android.pillow.data.sync.SynchDataSource;

import java.util.Collection;

/**
 * Created by mateuyabar on 22/05/15.
 */
public class SynchDbDataSource<T extends IdentificableModel> extends SynchDataSource<T> implements ISynchDbDataSource<T> {

    public SynchDbDataSource(Class<T> modelClass,  ISynchLocalDbDataSource<T> dbSource, IRestDataSource<T> restDataSource, Context context, IAuthenticationController authenticationData) {
        super(modelClass, dbSource, restDataSource, context, authenticationData);
    }

    public SynchDbDataSource(Class<T> modelClass, ISynchLocalDbDataSource<T> dbSource, IRestMapping<T> restMap, Context context, IAuthenticationController authenticationData) {
        super(modelClass, dbSource, restMap, context, authenticationData);
    }

    public SynchDbDataSource(Class<T> modelClass, ISynchLocalDbDataSource<T> dbSource, IRestMapping<T> restMap, Context context) {
        super(modelClass, dbSource, restMap, context);
    }

    @Override
    public IPillowResult<Collection<T>> index(T model) {
        return getLocalDataSource().index(model);
    }

    @Override
    public IPillowResult<Collection<T>> index(String selection, String[] selectionArgs, String order) {
        return getLocalDataSource().index(selection, selectionArgs, order);
    }

    @Override
    public IPillowResult<Integer> count(String selection, String[] selectionArgs) {
        return getLocalDataSource().count(selection, selectionArgs);
    }

    @Override
    public IPillowResult<Integer> count(T model) {
        return getLocalDataSource().count(model);
    }

    @Override
    public IDbMapping<T> getDbMapping() {
        return getLocalDataSource().getDbMapping();
    }

    public ISynchLocalDbDataSource<T> getLocalDataSource() {
        return (ISynchLocalDbDataSource<T>) super.getLocalDataSource();
    }

    @Override
    public DBModelController<T> getDbModelController() {
        return getLocalDataSource().getDbModelController();
    }
}
