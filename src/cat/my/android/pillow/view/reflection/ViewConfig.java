package cat.my.android.pillow.view.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cat.my.android.pillow.view.forms.InputData;

public interface ViewConfig {
	
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ViewType {
		public interface DEFAULT_INPUT extends InputData{};
		public interface NONE_INPUT extends InputData{};
		Class<? extends InputData> inputType() default DEFAULT_INPUT.class;
		int order() default -1;
	}

}
