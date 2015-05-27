package com.mateuyabar.android.pillow.data.singleinstance;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResult;
import com.mateuyabar.android.pillow.data.db.DBModelController;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.sync.ISynchLocalDataSource;
import com.mateuyabar.util.exceptions.BreakFastException;
import com.mateuyabar.util.exceptions.UnimplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by mateuyabar on 14/05/15.
 */
public class SingleInstanceKeyValueDataSource<T extends IdentificableModel> implements ISynchLocalSingleInstanceDataSource<T> {
    public static final String MODEL_ATT_SUFIX ="model";
    public static final String DIRTY_ATT_SUFIX ="dirty";

    Class<T> modelClass;
    SharedPreferences preferences;
    String modelAtt;
    String dirtyAtt;
    Gson gson = new Gson();

    public SingleInstanceKeyValueDataSource(Class<T> modelClass, SharedPreferences preferences) {
        this.modelClass = modelClass;
        this.preferences = preferences;

        String modelName = modelClass.getSimpleName();
        modelAtt = modelName+MODEL_ATT_SUFIX;
        dirtyAtt = modelName+DIRTY_ATT_SUFIX;
    }

    @Override
    public IPillowResult<T> setAsNotDirty(T model) {
        preferences.edit().putInt(dirtyAtt, ISynchLocalDataSource.DIRTY_STATUS_CLEAN).commit();
        return new PillowResult<>(model);
    }

    @Override
    public IPillowResult<Collection<T>> index() {
        return new PillowResult<Collection<T>>(getAsList());
    }



    @Override
    public IPillowResult<T> show(T idModel) {
        T model = synchronGet();
        if(! checkIsCurrentModel(idModel)){
            throw new BreakFastException();
        }
        return new PillowResult<>(model);
    }

    @Override
    public IPillowResult<T> create(T model) {
        if(synchronExists()){
            throw new BreakFastException("can't create another model");
        }
        return save(model, ISynchLocalDataSource.DIRTY_STATUS_CREATED);
    }

    @Override
    public IPillowResult<T> update(T model) {
        T current = synchronGet();
        if(! checkIsCurrentModel(model)){
            throw new BreakFastException();
        }
        return save(model, ISynchLocalDataSource.DIRTY_STATUS_UPDATED);
    }



    @Override
    public PillowResult<T> get() {
        return new PillowResult<>(synchronGet());
    }

    @Override
    public IPillowResult<T> set(T model) {
        if(synchronExists()){
            if(model.getId()==null){
                model.setId(synchronGet().getId());
            }
            return update(model);
        } else {
            return create(model);
        }
    }

    @Override
    public PillowResult<Boolean> exists() {
        return new PillowResult<>(synchronExists());
    }

    /**
     * @param model model with id
     * @return true if there is an stored model and has the given id.
     */
    public boolean checkIsCurrentModel(T model){
        T current = synchronGet();
        return current!=null && current.getId().equals(model.getId());
    }

    public IPillowResult<T> save(T model, int dirtyStatus){
        if(model.getId()==null){
            model.setId(DBModelController.createUUID());
        }
        if(dirtyStatus==DIRTY_STATUS_UPDATED && getDirtyType()==DIRTY_STATUS_CREATED){
            //Updating a model not created on the server yet.
            dirtyStatus = DIRTY_STATUS_CREATED;
        }
        String json = toJson(model);
        preferences.edit().putString(modelAtt, json).putInt(dirtyAtt, dirtyStatus).commit();
        return new PillowResult<>(model);
    }

    /**
     * @return the stored element or null
     */
    public T synchronGet(){
        String model = getJson();
        if(model!=null){
            return fromJson(model);
        } else {
            return null;
        }
    }

    /**
     * @return A list with the stored element or an empty list
     */
    private List<T> getAsList(){
        List<T> result = new ArrayList<T>();
        T model = synchronGet();
        if(model!=null){
            result.add(model);
        }
        return result;
    }

    public boolean synchronExists(){
        return getJson()!=null;
    }

    private String getJson(){
        return preferences.getString(modelAtt, null);
    }

    private T fromJson(String json){
        return gson.fromJson(json, modelClass);
    }

    private String toJson(T model){
        return gson.toJson(model);
    }

    private int getDirtyType(){
        return preferences.getInt(dirtyAtt, DIRTY_STATUS_CLEAN);
    }

    @Override
    public IPillowResult<Void> destroy(T model) {
        throw new UnimplementedException();
    }


    @Override
    public List<T> getDirty(int dirtyType) {
        int currentDirtyType = getDirtyType();
        if(dirtyType == currentDirtyType){
            return getAsList();
        } else {
            return new ArrayList<>();
        }
    }


    @Override
    public void cacheAll(List<T> models) {
        if(models.size()!=1)
            throw new UnimplementedException();
        save(models.get(0), DIRTY_STATUS_CLEAN);

    }

    @Override
    public List<T> getDeletedModelsIds() {
        return Collections.emptyList();
    }

    @Override
    public void setAsDeleted(String id) {}
}
