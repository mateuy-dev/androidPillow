package com.mateuyabar.android.pillow.data.keyvalue;

import android.content.SharedPreferences;

import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResult;
import com.mateuyabar.android.pillow.data.db.DBModelController;
import com.mateuyabar.android.pillow.data.db.IDBDataSourceForSynch;
import com.mateuyabar.util.exceptions.BreakFastException;
import com.mateuyabar.util.exceptions.UnimplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mateuyabar on 14/05/15.
 */
public class KeyValueDataSource<T extends IdentificableModel> implements IDBDataSourceForSynch<T> {
    public static final String MODEL_ATT_SUFIX ="model";
    public static final String DIRTY_ATT_SUFIX ="dirty";

    Class<T> modelClass;
    SharedPreferences preferences;
    String modelAtt;
    String dirtyAtt;

    public KeyValueDataSource(Class<T> modelClass, SharedPreferences preferences) {
        this.modelClass = modelClass;
        this.preferences = preferences;

        String modelName = modelClass.getSimpleName();
        modelAtt = modelName+MODEL_ATT_SUFIX;
        dirtyAtt = modelName+DIRTY_ATT_SUFIX;
    }

    @Override
    public IPillowResult<T> setAsNotDirty(T model) {
        preferences.edit().putBoolean(dirtyAtt, false).commit();
        return new PillowResult<>(model);
    }

    @Override
    public IPillowResult<Collection<T>> index() {
        List<T> result = new ArrayList<T>();
        T model = get();
        if(model!=null){
            result.add(model);
        }
        return new PillowResult<Collection<T>>(result);
    }

    @Override
    public IPillowResult<T> show(T idModel) {
        T model = get();
        if(! checkIsCurrentModel(idModel)){
            throw new BreakFastException();
        }
        return new PillowResult<>(model);
    }

    @Override
    public IPillowResult<T> create(T model) {
        if(!isEmpty()){
            throw new BreakFastException("can't create another model");
        }
        return save(model);
    }

    @Override
    public IPillowResult<T> update(T model) {
        T current = get();
        if(! checkIsCurrentModel(model)){
            throw new BreakFastException();
        }
        return save(model);
    }

    @Override
    public IPillowResult<Integer> count(String selection, String[] selectionArgs) {
        int count = getJson()==null ? 0 : 1;
        return new PillowResult<>(count);
    }

    /**
     * @param model model with id
     * @return true if there is an stored model and has the given id.
     */
    public boolean checkIsCurrentModel(T model){
        T current = get();
        return current!=null && current.getId().equals(model.getId());
    }

    public IPillowResult<T> save(T model){
        if(model.getId()==null){
            model.setId(DBModelController.createUUID());
        }
        String json = toJson(model);
        preferences.edit().putString(modelAtt, json).putBoolean(dirtyAtt, true).commit();
        return new PillowResult<>(model);
    }

    public T get(){
        String model = getJson();
        if(model!=null){
            return fromJson(model);
        } else {
            return null;
        }
    }

    public boolean isEmpty(){
        return getJson()==null;
    }

    private String getJson(){
        return preferences.getString(modelAtt, null);
    }

    private T fromJson(String json){
        return null;
    }

    private String toJson(T model){
        return "";
    }

    @Override
    public IPillowResult<Void> destroy(T model) {
        throw new UnimplementedException();
    }

    @Override
    public IPillowResult<Collection<T>> index(String selection, String[] selectionArgs, String order) {
        throw new UnimplementedException();
    }

    @Override
    public IPillowResult<Collection<T>> index(T filter) {
        throw new UnimplementedException();
    }

    @Override
    public DBModelController<T> getDbModelController() {
        throw new UnimplementedException();
    }
}
