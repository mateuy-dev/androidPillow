package com.mateuyabar.android.pillow.view.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.mateuyabar.android.pillow.PillowView;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.view.base.IModelAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mateuyabar on 16/11/15.
 */
public class PillowListAdapter<T extends IdentificableModel> extends BaseAdapter implements Filterable{
    List<T> models = new ArrayList<>();
    //In case that a filter is used, it contains the models before the filtering was executed
    List<T> originalModels;
    BasicFilter basicFilter = new BasicFilter();
    Context context;
    IModelAdapter<T> modelAdapter;
    Class<T> modelClass;

    public PillowListAdapter(Context context, Class<T> modelClass) {
        this.context = context;
        this.modelClass=modelClass;
        this.modelAdapter = PillowView.getInstance(context).getViewConfiguration(modelClass).getModelAdapter(context);
    }

    public void setModels(List<T> models) {
        this.models = models;
        originalModels = null;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public T getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return modelAdapter.getView(getItem(position), convertView, parent);
    }

    @Override
    public Filter getFilter() {
        return basicFilter;
    }

    /**
     * Filters the results using the tostring method.
     */
    private class BasicFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (originalModels == null && !models.isEmpty()) {
                originalModels = new ArrayList<T>(models);
            }

            FilterResults results = new FilterResults();
            if (originalModels == null) {
                results.values = new ArrayList();
                results.count = 0;
            } else if  (constraint == null || constraint.length() == 0) {
                results.values = originalModels;
                results.count = originalModels.size();
            } else {
                List<T> startWith = new ArrayList<T>();
                List<T> contains = new ArrayList<T>();
                for (T model : originalModels) {
                    String modelString = model.toString().toLowerCase();
                    String constrainString = constraint.toString().toLowerCase();
                    if (modelString.startsWith(constrainString)) {
                        startWith.add(model);
                    } else  if(modelString.contains(constrainString)){
                        contains.add(model);
                    }
                }
                List<T> result = new ArrayList<T>(startWith);
                result.addAll(contains);
                results.values = result;
                results.count = result.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (originalModels == null && !models.isEmpty()) {
                originalModels = new ArrayList<T>(models);
            }
            models.clear();
            models.addAll((Collection<? extends T>) results.values);
            notifyDataSetChanged();
        }
    }
}
