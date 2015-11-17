package com.mateuyabar.android.pillow.androidpillow.models;

import com.mateuyabar.android.pillow.data.models.AbstractIdentificableModel;

public class SampleModel extends AbstractIdentificableModel {
    String name;
    Integer integer;
    Boolean booleanAtt;


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

    public Boolean getBooleanAtt() {
        return booleanAtt;
    }

    public void setBooleanAtt(Boolean booleanAtt) {
        this.booleanAtt = booleanAtt;
    }

    @Override
    public String toString() {
        return name;
    }
}
