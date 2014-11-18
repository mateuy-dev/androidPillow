package cat.my.android.restvolley.forms;

import java.lang.reflect.Field;
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
	Map<Field, View> inputViewMap = null;
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
		
		inputViewMap = new HashMap<Field, View>();
		for(Field field: model.getClass().getDeclaredFields()){
			try {
				if(!field.isSynthetic() && acceptInput(field)){
					field.setAccessible(true);
					InputData inputData = inputManager.getInputData(field);
					View input = (View) inputData.createView(context);
					inputData.setValue(input, field.get(model));
					inputViewMap.put(field, input);
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
		if(inputNames==null)
			return inputViewMap.values();
		
		Collection<View> result = new ArrayList<View>();
		for(String inputName: inputNames){
			result.add(getInput(inputName));
		}
		return result;
	}
	
	private View getInput(String fieldName){
		initInputs();
		try {
			Field field = model.getClass().getDeclaredField(fieldName);
			return inputViewMap.get(field);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	public void updateModelFromForm() {
		for(Entry<Field, View> entries : inputViewMap.entrySet()){
			Field field= entries.getKey();
			View view = entries.getValue();
			InputData inputData = inputManager.getInputData(field);
			Object value = inputData.getValue(view);
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
	
	public class FormInputRow{
		Field field;
		View input;
		View rootView;
		InputData inputData;
		public FormInputRow(Field field){
			this.field = field;
			init();
		}
		public void init(){
			field.setAccessible(true);
			
			inputData = inputManager.getInputData(field);
			input = (View) inputData.createView(context);
			try {
				inputData.setValue(input, field.get(model));
			} catch (Exception e) {
				e.printStackTrace();
			}
			rootView = input;
		}
		
	}

}
