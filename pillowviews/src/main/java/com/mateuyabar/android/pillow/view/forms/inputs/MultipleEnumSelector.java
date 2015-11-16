/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.mateuyabar.android.pillow.view.forms.inputs;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.mateuyabar.android.pillow.view.forms.StringResourceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
