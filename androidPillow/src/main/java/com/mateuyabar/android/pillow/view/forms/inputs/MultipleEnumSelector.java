package com.mateuyabar.android.pillow.view.forms.inputs;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mateuyabar.android.pillow.view.forms.StringResourceUtils;

/**
 * Created by mateuyabar on 25/02/15.
 */
public class MultipleEnumSelector extends LinearLayout {
    Class<?> enumeration;
    Map<CheckBox, Enum<?>> checkboxes = new HashMap<CheckBox, Enum<?>>();
    public MultipleEnumSelector(Context context, Class<?> enumeration) {
        super(context);
        this.enumeration = enumeration;
        init();
    }

    public void init(){
        setOrientation(LinearLayout.VERTICAL);
        for (Object constant : enumeration.getEnumConstants()) {
            Enum<?> value = (Enum<?>) constant;
            String label = StringResourceUtils.getLabel(getContext(), value);
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(label);
            addView(checkBox);
            checkboxes.put(checkBox, value);
        }
    }

    public List<Enum<?>> getSelectedEnums(){
        List<Enum<?>> result = new ArrayList<Enum<?>>();
        for(CheckBox checkBox:checkboxes.keySet()){
            if(checkBox.isChecked()){
                result.add(checkboxes.get(checkBox));
            }
        }
        return result;
    }

    public void setSelectedEnums(List<Enum<?>> selectedEnums) {
    	if(selectedEnums==null)
    		return;
        for(CheckBox checkBox:checkboxes.keySet()){
            Enum<?> value = checkboxes.get(checkBox);
            if(selectedEnums.contains(value)){
                checkBox.setSelected(true);
            }
        }
    }



}
