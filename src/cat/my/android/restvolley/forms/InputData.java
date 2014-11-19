package cat.my.android.restvolley.forms;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cat.my.android.restvolley.IdentificableModel;
import android.content.Context;
import android.view.View;

public interface InputData {
	
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface InputDataInfo {
		public interface DEFAULT_INPUT extends InputData{};
		public interface NONE extends IdentificableModel{};
		
		Class<? extends InputData> inputClass() default DEFAULT_INPUT.class;
		Class<? extends IdentificableModel> belongsTo() default NONE.class;
		
	}
	
	public Object getValue(View view);

	public void setValue(View view, Object value);

	public View createView(Context context);
}