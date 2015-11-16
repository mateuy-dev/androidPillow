package com.mateuyabar.android.pillow.androidpillow.models;

import com.mateuyabar.android.pillow.data.models.AbstractIdentificableModel;

public class SampleModel extends AbstractIdentificableModel {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
