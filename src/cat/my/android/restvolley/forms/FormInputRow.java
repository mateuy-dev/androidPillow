package cat.my.android.restvolley.forms;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FormInputRow{
		InputDataManager inputManager = new InputDataManager();
		Field field;
		View input;
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
			createRootView();
			
		}
		private void createRootView() {
			TextView label = new TextView(context);
			String labelText = StringResourceUtils.getLabel(context, field);
			label.setText(labelText);
			views = new ArrayList<View>();
			views.add(label);
			views.add(input);
		}
		public List<View> getViews() {
			return views;
		}
		
		public Object getValue(){
			return inputData.getValue();
		}
	}