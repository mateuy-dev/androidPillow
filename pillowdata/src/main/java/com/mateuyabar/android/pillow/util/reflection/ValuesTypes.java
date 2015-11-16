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

package com.mateuyabar.android.pillow.util.reflection;

import com.mateuyabar.android.pillow.data.models.IdentificableModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


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
		Class<?> listType() default NONE.class;
	}
	
	
	
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface OrderBy{
		public enum OrderType{ASC, DESC, ASC_NO_COLLATE, DESC_NO_COLLATE}
		OrderType type() default OrderType.ASC;
	}
	
	@Target({ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Embeddable{}
}
