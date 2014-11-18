package cat.my.android.restvolley.forms;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FormInput{
	InputDataManager inputManager = new InputDataManager();
		Field field;
		View input;
		LinearLayout rootView;
		InputData inputData;
		Context context;
		Object model;
		public FormInput(Context context, Field field, Object model){
			this.field = field;
			this.context = context;
			this.model = model;
			init();
		}
		public void init(){
			inputData = inputManager.getInputData(field);
			input = (View) inputData.createView(context);
			try {
				inputData.setValue(input, field.get(model));
			} catch (Exception e) {
				e.printStackTrace();
			}
			createRootView();
			
		}
		private void createRootView() {
			rootView = new LinearLayout(context);
			TextView label = new TextView(context);
			label.setText(field.getName());
			rootView.addView(label);
			rootView.addView(input);
		}
		public View getRootView() {
			return rootView;
		}
		
		public Object getValue(){
			return inputData.getValue(input);
		}
	}