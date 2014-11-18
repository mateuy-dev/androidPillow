package cat.my.android.restvolley.forms;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import android.content.Context;
import android.view.View;

public interface InputData {
	
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface InputDataInfo {
		Class<? extends InputData> inputClass();
	}
	
	public Object getValue(View view);

	public void setValue(View view, Object value);

	public View createView(Context context);
}