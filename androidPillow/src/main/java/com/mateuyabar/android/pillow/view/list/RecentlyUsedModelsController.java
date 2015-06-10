package com.mateuyabar.android.pillow.view.list;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * FIFO of models, used to store the last used models by class.
 * @param <T>
 */
public class RecentlyUsedModelsController<T extends IdentificableModel> {
    private static final Type STRING_ARRAY_TYPE = new TypeToken<Collection<String>>(){}.getType();
    private static final int MAX_SIZE = 5;
    Class<T> modelClass;
    Context context;
    Gson gson;
    SharedPreferences sharedPreferences;
    String preferencesKey;


    public static final String PREF_KEY_PREFIX = "used_model_ids";

    public RecentlyUsedModelsController(Context context, Class<T> modelClass) {
        this.modelClass = modelClass;
        this.context = context;
        this.gson = new Gson();
        this.sharedPreferences = Pillow.getInstance(context).getSharedPreferences();
        this.preferencesKey = PREF_KEY_PREFIX + modelClass.getSimpleName();
    }

    /**
     * Adds the model to the list, or positions it to the first place if allready exits. If size is greatter than MAX_SIZE, it erases an item.
     * @param model
     */
    public void used(T model){
        List<String> used = getRecentlyUsedIds();
        int index = used.indexOf(model.getId());
        if(index == -1){
            used.add(0, model.getId());
            if(used.size()>MAX_SIZE){
                used.remove(MAX_SIZE);
            }
        } else {
            used.remove(index);
            used.add(0, model.getId());
        }
        String value = gson.toJson(used);
        sharedPreferences.edit().putString(preferencesKey, value).commit();
    }

    /**
     * @return list of ids added.
     */
    public List<String> getRecentlyUsedIds(){
        String json = sharedPreferences.getString(preferencesKey, "[]");
        List<String> used =  gson.fromJson(json, STRING_ARRAY_TYPE);
        return used;
    }
}
