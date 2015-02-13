package cat.my.android.pillow.util.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.view.forms.InputData;


public class ValuesTypes {
	public enum ValueTypeClass{DEFAULT, COLOR, DATE, DATETIME}
	
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ValueType {
		public interface DEFAULT_INPUT extends InputData{};
		public interface NONE extends IdentificableModel{};
		
		ValueTypeClass type() default ValueTypeClass.DEFAULT;
		Class<? extends IdentificableModel> belongsTo() default NONE.class;
	}
	
	
	
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface OrderBy{
		public enum OrderType{ASC, DESC, ASC_NO_COLLATE, DESC_NO_COLLATE}
		OrderType type() default OrderType.ASC;
	}
}
