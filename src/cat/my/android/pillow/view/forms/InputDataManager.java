package cat.my.android.pillow.view.forms;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.util.reflection.ValuesTypes.ValueType;
import cat.my.android.pillow.util.reflection.ValuesTypes.ValueTypeClass;
import cat.my.android.pillow.view.forms.inputDatas.CalendarInputData;
import cat.my.android.pillow.view.forms.inputDatas.ColorInput;
import cat.my.android.pillow.view.forms.inputDatas.DateEditTextData;
import cat.my.android.pillow.view.forms.inputDatas.DateInputData;
import cat.my.android.pillow.view.forms.inputDatas.EditTextData;
import cat.my.android.pillow.view.forms.inputDatas.EnumInputData;
import cat.my.android.pillow.view.forms.inputDatas.IdModelAutoCompleteEditTextData;
import cat.my.android.pillow.view.forms.inputDatas.IdentificableModelSpinnerInputData;
import cat.my.android.pillow.view.forms.inputDatas.IntEditTextData;
import cat.my.android.pillow.view.forms.inputDatas.display.CalendarDisplay;
import cat.my.android.pillow.view.forms.inputDatas.display.TextDisplay;
import cat.my.util.exceptions.UnimplementedException;

public class InputDataManager{
	@SuppressWarnings("unchecked")
	public InputData getInputData(Field field, boolean editable){
		Class<?> valueClass = field.getType();
		
		if(!editable){
			if (Calendar.class.isAssignableFrom(valueClass)){
				return new CalendarDisplay();
			} else {
				return new TextDisplay();
			}
			
			
		}
		
		
		ValueType inputTypeAnnotation = (ValueType) field.getAnnotation(ValueType.class);
		if(inputTypeAnnotation!=null){
			ValueTypeClass valueType = inputTypeAnnotation.type();
			if(valueType != ValueTypeClass.DEFAULT){
				//if the input has an especific type defined we check it
				if(valueType == ValueTypeClass.COLOR){
					return new ColorInput();
				}
			} else if(inputTypeAnnotation.belongsTo() !=null){
				//The field is a belogsTo attribute (foreign key)
				Class<? extends IdentificableModel> parentClass = inputTypeAnnotation.belongsTo();
//				return new IdentificableModelSpinnerInputData(parentClass);
				return new IdModelAutoCompleteEditTextData(parentClass);
			}
		}
		
		
		if(String.class.isAssignableFrom(valueClass)){
			return new EditTextData();
		} else if(Integer.class.isAssignableFrom(valueClass) || Integer.TYPE.isAssignableFrom(valueClass)){
			return new IntEditTextData();
		} else if(Enum.class.isAssignableFrom(valueClass)){
			return new EnumInputData(valueClass);
		}  else if (Calendar.class.isAssignableFrom(valueClass)){
			return new CalendarInputData();
		} else if (Date.class.isAssignableFrom(valueClass)){
			return new DateInputData();
		}
		throw new UnimplementedException();
	}
}