package cat.my.android.pillow.view.forms;

import android.content.Context;
import android.view.View;

public interface InputData {
	
//	@Target({ElementType.FIELD})
//	@Retention(RetentionPolicy.RUNTIME)
//	public @interface InputDataInfo {
//		public interface DEFAULT_INPUT extends InputData{};
//		public interface NONE extends IdentificableModel{};
//		
//		Class<? extends InputData> inputClass() default DEFAULT_INPUT.class;
//		Class<? extends IdentificableModel> belongsTo() default NONE.class;
//	}
	
	public Object getValue();

	public void setValue(Object value);

	public View getView(Context context);
}