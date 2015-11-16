package com.mateuyabar.android.pillow.view.base;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mateuyabar on 16/06/15.
 */
public interface IModelAdapter<T> {
    public View getView(T model, View convertView, ViewGroup parent);
}
