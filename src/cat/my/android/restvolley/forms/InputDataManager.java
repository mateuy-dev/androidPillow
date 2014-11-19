package cat.my.android.restvolley.forms;

import java.lang.reflect.Field;
import java.util.Date;

import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.forms.InputData.InputDataInfo;
import cat.my.android.restvolley.forms.InputData.InputDataInfo.DEFAULT_INPUT;
import cat.my.android.restvolley.forms.inputDatas.DateEditTextData;
import cat.my.android.restvolley.forms.inputDatas.EditTextData;
import cat.my.android.restvolley.forms.inputDatas.EnumInputData;
import cat.my.android.restvolley.forms.inputDatas.IdentificableModelInputData;
import cat.my.android.restvolley.forms.inputDatas.IntEditTextData;

public class InputDataManager{
	@SuppressWarnings("unchecked")
	public InputData getInputData(Field field){
		field.getAnnotations();
		InputDataInfo inputTypeAnnotation = (InputDataInfo) field.getAnnotation(InputDataInfo.class);
		if(inputTypeAnnotation!=null){
			if(inputTypeAnnotation.inputClass() != DEFAULT_INPUT.class){
				try {
					return (InputData)inputTypeAnnotation.inputClass().newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if(inputTypeAnnotation.belongsTo() !=null){
				Class<? extends IdentificableModel> parentClass = inputTypeAnnotation.belongsTo();
				return new IdentificableModelInputData(parentClass);
			}
		}
		
		Class<?> valueClass = field.getType();
		if(String.class.isAssignableFrom(valueClass)){
			return new EditTextData();
		} else if(Integer.class.isAssignableFrom(valueClass) || Integer.TYPE.isAssignableFrom(valueClass)){
			return new IntEditTextData();
		} else if(Enum.class.isAssignableFrom(valueClass)){
			return new EnumInputData(valueClass);
		} else if (Date.class.isAssignableFrom(valueClass)){
			return new DateEditTextData();
		}
		return null;
	}
}