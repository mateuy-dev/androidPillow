package cat.my.android.restvolley.forms;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cat.my.android.restvolley.IdentificableModel;

import android.content.Context;
import android.view.View;

public class FormInputs {
	InputDataManager inputManager = new InputDataManager();
	
	//Data
	Map<Field, FormInput> inputViewMap = null;
	Object model;
	Context context;
	
	//Filters
	String[] inputNames;
	
	public FormInputs(Object model, Context context) {
		super();
		this.model = model;
		this.context = context;
	}
	
	private void initInputs() {
		if(inputViewMap!=null)
			return;
		
		inputViewMap = new HashMap<Field, FormInput>();
		for(Field field: model.getClass().getDeclaredFields()){
			try {
				if(!field.isSynthetic() && acceptInput(field) && !isTransient(field)){
					field.setAccessible(true);
					inputViewMap.put(field, new FormInput(context, field, model));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setInputNames(String[] inputNames) {
		this.inputNames = inputNames;
	}
	
	public Collection<View> getInputs(){
		initInputs();
		Collection<View> result = new ArrayList<View>();
		if(inputNames==null){
			for(FormInput row : inputViewMap.values()){
				result.add(row.getRootView());
			}
		} else {
			for(String inputName: inputNames){
				result.add(getInput(inputName));
			}
		}
		return result;
	}
	
	private View getInput(String fieldName){
		initInputs();
		try {
			Field field = model.getClass().getDeclaredField(fieldName);
			return inputViewMap.get(field).getRootView();
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	public void updateModelFromForm() {
		for(Entry<Field, FormInput> entries : inputViewMap.entrySet()){
			Field field= entries.getKey();
			Object value = entries.getValue().getValue();
			try {
				field.set(model, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public boolean acceptInput(Field field){
		String fieldName = field.getName();
		if(model instanceof IdentificableModel && fieldName.equals("id")){
			return false;
		}
		if(inputNames==null) return true;
		for(String inputName:inputNames){
			if(fieldName.equals(inputName)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean isTransient(Field field){
		return Modifier.isTransient(field.getModifiers());
	}

}
