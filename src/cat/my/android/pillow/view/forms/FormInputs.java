package cat.my.android.pillow.view.forms;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.util.reflection.ReflectionUtil;
import cat.my.util.exceptions.BreakFastException;

public class FormInputs {
	InputDataManager inputManager = new InputDataManager();
	
	//Data
	Map<Field, FormInputRow> inputViewMap = null;
	Object model;
	Context context;
	boolean editable;
	
	//Filters
	String[] inputNames;
	
	public FormInputs(Object model, Context context, boolean editable) {
		super();
		this.model = model;
		this.context = context;
		this.editable = editable;
	}
	
	private void initInputs() {
		if(inputViewMap!=null)
			return;
		
		inputViewMap = new HashMap<Field, FormInputRow>();
		for(Field field: ReflectionUtil.getStoredFields(model.getClass())){
			try {
				if(acceptInput(field)){
					field.setAccessible(true);
					inputViewMap.put(field, new FormInputRow(context, field, model, editable));
				}
			} catch (Exception e) {
				throw new BreakFastException(e);
			}
		}
	}
	
	public void setInputNames(String[] inputNames) {
		this.inputNames = inputNames;
	}
	
	public Collection<FormInputRow> getInputs(){
		initInputs();
		List<FormInputRow> result = new ArrayList<FormInputRow>();
		if(inputNames==null){
			for(FormInputRow row : inputViewMap.values()){
				result.add(row);
			}
			//sort
			Collections.sort(result, new Comparator<FormInputRow>() {
				public int compare(FormInputRow lhs, FormInputRow rhs) {
					return FormInputs.compare(lhs.getOrder(), rhs.getOrder());
				};
			});
		} else {
			for(String inputName: inputNames){
				result.add(getInput(inputName));
			}
		}
		return result;
	}
	
	 public static int compare(int lhs, int rhs) {
		 return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
	 }
	
	private FormInputRow getInput(String fieldName){
		initInputs();
		try {
			Field field = model.getClass().getDeclaredField(fieldName);
			return inputViewMap.get(field);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	public void updateModelFromForm() {
		for(Entry<Field, FormInputRow> entries : inputViewMap.entrySet()){
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
	
	

}
