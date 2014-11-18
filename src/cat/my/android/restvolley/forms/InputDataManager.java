package cat.my.android.restvolley.forms;

import java.lang.reflect.Field;

import cat.my.android.restvolley.forms.InputData.InputDataInfo;
import cat.my.android.restvolley.forms.inputDatas.EditTextData;
import cat.my.android.restvolley.forms.inputDatas.EnumInputData;
import cat.my.android.restvolley.forms.inputDatas.IntEditTextData;

public class InputDataManager{
	@SuppressWarnings("unchecked")
	public InputData getInputData(Field field){
		field.getAnnotations();
		InputDataInfo inputTypeAnnotation = (InputDataInfo) field.getAnnotation(InputDataInfo.class);
		if(inputTypeAnnotation!=null){
			try {
				return (InputData)inputTypeAnnotation.inputClass().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Class<?> valueClass = field.getType();
		if(String.class.isAssignableFrom(valueClass)){
			return new EditTextData();
		} else if(Integer.class.isAssignableFrom(valueClass) || Integer.TYPE.isAssignableFrom(valueClass)){
			return new IntEditTextData();
		} else if(Enum.class.isAssignableFrom(valueClass)){
			return new EnumInputData(valueClass);
		}
		return null;
	}
}