//package cat.my.android.restvolley.forms.display;
//
//import java.util.Collection;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.LinearLayout;
//import cat.my.android.restvolley.forms.FormInputs;
//
//public class TDisplayView<T> extends LinearLayout{
//	FormInputs formInputs;
//	T model;
//	
//	
//	public TDisplayView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		init();
//	}
//
//	public TDisplayView(Context context) {
//		super(context);
//		init();
//	}
//	
//	private void init() {
//		setOrientation(VERTICAL);
//	}
//
//	public T getModel() {
//		updateModelFromForm();
//		return model;
//	}
//
//	public void setModel(T model){
//		setModel(model,null);
//	}
//	
//	public void setModel(T model, String[] inputNames) {
//		this.model = model;
//		formInputs = new FormInputs(model, getContext());
//		formInputs.setInputNames(inputNames);
//		generateForm();
//	}
//	
//	private void generateForm() {
//		Collection<View> inputs = formInputs.getInputs();
//		for(View view : inputs){
//			addView(view);
//		}
//	}
//
//	private void updateModelFromForm() {
//		formInputs.updateModelFromForm();
//	}
//
//	
//}
