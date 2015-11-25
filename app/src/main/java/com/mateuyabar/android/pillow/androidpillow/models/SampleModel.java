package com.mateuyabar.android.pillow.androidpillow.models;

import com.mateuyabar.android.pillow.data.models.AbstractIdentificableModel;

public class SampleModel extends AbstractIdentificableModel {
    public enum SampleEnum {option1, option2};


    String name;
    int integer;
    SampleEnum sampleEnum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public SampleEnum getSampleEnum() {
        return sampleEnum;
    }

    public void setSampleEnum(SampleEnum sampleEnum) {
        this.sampleEnum = sampleEnum;
    }

    @Override
    public String toString() {
        return name;
    }
}
