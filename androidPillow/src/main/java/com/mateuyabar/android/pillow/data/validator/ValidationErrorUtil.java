/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.mateuyabar.android.pillow.data.validator;

import android.content.Context;

import com.mateuyabar.android.pillow.R;
import com.mateuyabar.android.pillow.data.validator.IValidator.IValidationError;
import com.mateuyabar.android.pillow.data.validator.annotations.GreaterThan;
import com.mateuyabar.android.pillow.data.validator.annotations.Max;
import com.mateuyabar.android.pillow.data.validator.annotations.Min;
import com.mateuyabar.android.pillow.data.validator.annotations.NotEmpty;
import com.mateuyabar.android.pillow.data.validator.annotations.NotNull;
import com.mateuyabar.android.pillow.view.forms.StringResourceUtils;
import com.mateuyabar.util.exceptions.UnimplementedException;

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
