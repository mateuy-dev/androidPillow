package cat.my.android.pillow.view.forms;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class FormInputRow{
		InputDataManager inputManager = new InputDataManager();
		Field field;
		View input;
		TextView label;
		List<View> views;
		InputData inputData;
		Context context;
		Object model;
		boolean editable;
		
		public FormInputRow(Context context, Field field, Object model, boolean editable){
			this.field = field;
			this.context = context;
			this.model = model;
			this.editable=editable;
			init();
		}
		public void init(){
			inputData = inputManager.getInputData(field, editable);
			input = (View) inputData.getView(context);
			try {
				inputData.setValue(field.get(model));
			} catch (Exception e) {
				e.printStackTrace();
			}
			createLabelView();
			
		}
		private void createLabelView() {
			label = new TextView(context);
			String labelText = StringResourceUtils.getLabel(context, field);
			label.setText(labelText);
		}
		
		
		public Object getValue(){
			return inputData.getValue();
		}
		
		public View getLabel() {
			return label;
		}
		
		public View getInput() {
			return input;
		}
	}