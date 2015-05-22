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

package com.mateuyabar.android.pillow.view.forms;

import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.extra.Time;
import com.mateuyabar.android.pillow.util.reflection.ReflectionUtil;
import com.mateuyabar.android.pillow.util.reflection.ValuesTypes.Embeddable;
import com.mateuyabar.android.pillow.util.reflection.ValuesTypes.ValueType;
import com.mateuyabar.android.pillow.util.reflection.ValuesTypes.ValueType.NONE;
import com.mateuyabar.android.pillow.util.reflection.ValuesTypes.ValueTypeClass;
import com.mateuyabar.android.pillow.view.forms.inputDatas.BelongsToDialogInputData;
import com.mateuyabar.android.pillow.view.forms.inputDatas.CalendarInputData;
import com.mateuyabar.android.pillow.view.forms.inputDatas.ColorInput;
import com.mateuyabar.android.pillow.view.forms.inputDatas.DateInputData;
import com.mateuyabar.android.pillow.view.forms.inputDatas.EditTextData;
import com.mateuyabar.android.pillow.view.forms.inputDatas.EnumDialogInputView;
import com.mateuyabar.android.pillow.view.forms.inputDatas.EnumListInput;
import com.mateuyabar.android.pillow.view.forms.inputDatas.IntEditTextData;
import com.mateuyabar.android.pillow.view.forms.inputDatas.TimeInputData;
import com.mateuyabar.android.pillow.view.forms.inputDatas.display.BelongsToTextDisplay;
import com.mateuyabar.android.pillow.view.forms.inputDatas.display.CalendarDisplay;
import com.mateuyabar.android.pillow.view.forms.inputDatas.display.EnumDisplay;
import com.mateuyabar.android.pillow.view.forms.inputDatas.display.TextDisplay;
import com.mateuyabar.android.pillow.view.reflection.ViewConfig.ViewType;
import com.mateuyabar.android.pillow.view.reflection.ViewConfig.ViewType.DEFAULT_INPUT;
import com.mateuyabar.android.pillow.view.reflection.ViewConfig.ViewType.NONE_INPUT;
import com.mateuyabar.util.exceptions.BreakFastException;
import com.mateuyabar.util.exceptions.UnimplementedException;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

public class InputDataManager{
	@SuppressWarnings("unchecked")
	public InputData getInputData(Field field, boolean editable){
		Class<?> valueClass = field.getType();
		ViewType viewType = field.getAnnotation(ViewType.class);
		ValueType inputTypeAnnotation = (ValueType) field.getAnnotation(ValueType.class);
		
		if(!editable){
			if (Calendar.class.isAssignableFrom(valueClass) || Date.class.isAssignableFrom(valueClass)){
				return new CalendarDisplay();
			} else if(ReflectionUtil.isBelongsTo(field)){
				Class<? extends IdentificableModel> parentClass = inputTypeAnnotation.belongsTo();
				BelongsToInputData inputData = new BelongsToTextDisplay<IdentificableModel>();
				inputData.setParentClass(parentClass);
				return inputData;
			} else if(Enum.class.isAssignableFrom(valueClass)){
				return new EnumDisplay();
			} else {
				return new TextDisplay();
			}
			
			
		}
		
		if(viewType!=null && viewType.inputType()!=DEFAULT_INPUT.class){
			Class<? extends InputData> inputType = viewType.inputType();
			if(inputType == NONE_INPUT.class){
				return null;
			} else {
				try {
					InputData inputData = inputType.newInstance();
					if(ReflectionUtil.isBelongsTo(field)){
						Class<? extends IdentificableModel> parentClass = inputTypeAnnotation.belongsTo();
						((BelongsToInputData)inputData).setParentClass(parentClass);
					}
					return inputData;
				} catch (Exception e) {
					throw new BreakFastException(e);
				}
			}
		}
		if(inputTypeAnnotation!=null){
			ValueTypeClass valueType = inputTypeAnnotation.type();
			if(valueType != ValueTypeClass.DEFAULT){
				//if the input has an especific type defined we check it
				if(valueType == ValueTypeClass.COLOR){
					return new ColorInput();
				}
			} else if(ReflectionUtil.isBelongsTo(field)){
				//The field is a belogsTo attribute (foreign key)
				Class<? extends IdentificableModel> parentClass = inputTypeAnnotation.belongsTo();
//				return new IdentificableModelSpinnerInputData(parentClass);
				BelongsToInputData inputData = new BelongsToDialogInputData();
				inputData.setParentClass(parentClass);
				return inputData;
			} else if(inputTypeAnnotation.listType() !=null && inputTypeAnnotation.listType()!=NONE.class){
				//must be an enum!
				return new EnumListInput(inputTypeAnnotation.listType());
			}
		}
		
		Embeddable embeddableAnnotation = valueClass.getAnnotation(Embeddable.class);
		if(embeddableAnnotation!=null){
			return new TextDisplay();
			//throw new ToImplementException("Embedded models can't be displayed yet");
		}
		
		
		if(String.class.isAssignableFrom(valueClass)){
			return new EditTextData();
		} else if(Integer.class.isAssignableFrom(valueClass) || Integer.TYPE.isAssignableFrom(valueClass)){
			return new IntEditTextData();
		} else if(Enum.class.isAssignableFrom(valueClass)){
			return new EnumDialogInputView(valueClass);
		}  else if (Calendar.class.isAssignableFrom(valueClass)){
			return new CalendarInputData();
		} else if (Date.class.isAssignableFrom(valueClass)){
			return new DateInputData();
		} else if(Time.class.isAssignableFrom(valueClass)){
			return new TimeInputData();
		}
		throw new UnimplementedException();
	}
}