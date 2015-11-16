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


package com.mateuyabar.android.pillow.conf;

import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.data.db.IDbMapping;
import com.mateuyabar.android.pillow.data.rest.IRestMapping;
import com.mateuyabar.android.pillow.data.validator.IValidator;

import java.lang.reflect.Type;

public interface ModelConfiguration<T extends IdentificableModel> {
	public Class<T> getModelClass();
	
	public Type getCollectionType();

	public IDbMapping<T> getDbMapping();

	public IRestMapping<T> getRestMapping();
	
	public IDataSource<T> getDataSource();
	
	public IValidator<T> getValidator();

}