package cat.my.android.pillow.util.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cat.my.android.pillow.IdentificableModel;


public class ValuesTypes {
	public enum ValueTypeClass{DEFAULT, COLOR, DATE, DATETIME}
	public enum BelongsToOnDelete{NO_ACTION, SET_NULL, CASCADE} //RESTRICT
	
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ValueType {
		public interface NONE extends IdentificableModel{};
		
		ValueTypeClass type() default ValueTypeClass.DEFAULT;
		Class<? extends IdentificableModel> belongsTo() default NONE.class;
		BelongsToOnDelete belongsToMode() default BelongsToOnDelete.NO_ACTION;
	}
	
	
	
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface OrderBy{
		public enum OrderType{ASC, DESC, ASC_NO_COLLATE, DESC_NO_COLLATE}
		OrderType type() default OrderType.ASC;
	}
}
