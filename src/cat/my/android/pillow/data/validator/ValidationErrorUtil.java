package cat.my.android.pillow.data.validator;

import android.content.Context;

import cat.my.android.pillow.R;
import cat.my.android.pillow.data.validator.IValidator.IValidationError;
import cat.my.android.pillow.data.validator.annotations.GreaterThan;
import cat.my.android.pillow.data.validator.annotations.Max;
import cat.my.android.pillow.data.validator.annotations.Min;
import cat.my.android.pillow.data.validator.annotations.NotEmpty;
import cat.my.android.pillow.data.validator.annotations.NotNull;
import cat.my.android.pillow.view.forms.StringResourceUtils;
import cat.my.util.exceptions.UnimplementedException;

public class ValidationErrorUtil {

    public static String getStringError(Context context, Class<?> modelClass, IValidationError validationError){
        String field="";
        if(validationError.getField()!=null) {
            field = StringResourceUtils.getLabel(context, validationError.getField());
        }
        Object error = validationError.getError();
        if(error instanceof NotNull){
            return context.getString(R.string.error_not_null, field);
        } else if(error instanceof NotEmpty){
            return context.getString(R.string.error_not_empty, field);
        } else if (error instanceof Max){
            long maxValue = ((Max) error).value();
            return context.getString(R.string.error_max, field, maxValue);
        } else if (error instanceof Min){
            long minValue = ((Min) error).value();
            return context.getString(R.string.error_min, field, minValue);
        } else if(error instanceof GreaterThan){
            String att = ((GreaterThan) error).attribute();
            String attName = StringResourceUtils.getLabel(context, modelClass, att);
            return context.getString(R.string.error_greater_than, field, attName);
        } else if(error instanceof Integer){
            return context.getString((Integer) error);
        } else if(error instanceof String){
            return (String) error;
        }
        throw new UnimplementedException();
    }
}
