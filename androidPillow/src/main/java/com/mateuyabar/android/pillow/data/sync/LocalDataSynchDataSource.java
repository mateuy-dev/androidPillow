package com.mateuyabar.android.pillow.data.sync;

import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.db.DBModelController;
import com.mateuyabar.android.pillow.data.rest.RestDataSource;

import java.util.Collection;

/**
 * Used for data that is stored in the device and send to the server but is not downloaded from the server.
 * For example device information.
 */
public class LocalDataSynchDataSource<T extends IdentificableModel> implements ISynchDataSource<T> {
    @Override
    public IPillowResult<Void> sendDirty() {
        return null;
    }

    @Override
    public IPillowResult<Collection<T>> download() {
        return null;
    }

    @Override
    public Class<T> getModelClass() {
        return null;
    }

    @Override
    public RestDataSource<T> getRestDataSource() {
        return null;
    }

    /*@Override
    public IDbMapping<T> getDbFuncs() {
        return null;
    }*/

    @Override
    public IPillowResult<Collection<T>> index(String selection, String[] selectionArgs, String order) {
        return null;
    }

    @Override
    public DBModelController<T> getDbModelController() {
        return null;
    }

    @Override
    public IPillowResult<Integer> count(String selection, String[] selectionArgs) {
        return null;
    }

    @Override
    public IPillowResult<Collection<T>> index(T filter) {
        return null;
    }

    @Override
    public IPillowResult<Collection<T>> index() {
        return null;
    }

    @Override
    public IPillowResult<T> show(T model) {
        return null;
    }

    @Override
    public IPillowResult<T> create(T model) {
        return null;
    }

    @Override
    public IPillowResult<T> update(T model) {
        return null;
    }

    @Override
    public IPillowResult<Void> destroy(T model) {
        return null;
    }
}
