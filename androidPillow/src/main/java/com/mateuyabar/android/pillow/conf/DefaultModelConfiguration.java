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

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mateuyabar.android.pillow.IDataSource;
import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.data.db.IDbMapping;
import com.mateuyabar.android.pillow.data.db.ReflectionDbMapping;
import com.mateuyabar.android.pillow.data.rest.IRestMapping;
import com.mateuyabar.android.pillow.data.rest.RailsRestMapping;
import com.mateuyabar.android.pillow.data.sync.SynchDataSource;
import com.mateuyabar.android.pillow.data.validator.DefaultValidator;
import com.mateuyabar.android.pillow.data.validator.IValidator;

import java.lang.reflect.Type;
import java.util.Collection;

public class DefaultModelConfiguration<T extends IdentificableModel> implements ModelConfiguration<T>{
	Context context;
	Class<T> modelClass;
	IDbMapping<T> dbMapping;
	IRestMapping<T> restMapping;
	Type collectionType;
	Class<?> formClass;
	IDataSource<T> dataSource;
	ModelViewConfiguration<T> viewConfiguration;
	String url;
	IValidator<T> validator;
	
	public DefaultModelConfiguration(Context context, Class<T> modelClass, TypeToken<Collection<T>> collectionTypeToken, String url) {
		super();
		this.context = context;
		this.modelClass = modelClass;
		this.collectionType = collectionTypeToken.getType();
		this.url = url;
	}

	public void setDbMapping(IDbMapping<T> dbMapping) {
		this.dbMapping = dbMapping;
	}

	public void setRestMapping(IRestMapping<T> restMapping) {
		this.restMapping = restMapping;
	}

	public void setFormClass(Class<?> formClass) {
		this.formClass = formClass;
	}

	public void setDataSource(IDataSource<T> dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setViewConfiguration(ModelViewConfiguration<T> viewConfiguration) {
		this.viewConfiguration = viewConfiguration;
	}
	
	public void setValidator(IValidator<T> validator) {
		this.validator = validator;
	}

	protected IDataSource<T> createDefaultDataSource() {
		return new SynchDataSource<T>(getDbMapping(), getRestMapping(), getContext());
	}

	protected IRestMapping<T> createDefaultRestMapping() {
		return new RailsRestMapping<T>(getUrl(), getModelClass(), getCollectionType());
	}

	protected IDbMapping<T> createDefaultDbMapping() {
		return new ReflectionDbMapping<T>(getModelClass());
	}

	protected ModelViewConfiguration<T> createDefaultViewConfiguration() {
		return new DefaultModelViewConfiguration<T>(modelClass);
	}
	
	protected IValidator<T> createDefaultValidator() {
		return new DefaultValidator<T>(modelClass);
	}
	
	private String getUrl() {
		return url;
	}
	
	@Override
	public Class<T> getModelClass() {
		return modelClass;
	}
	@Override
	public IDbMapping<T> getDbMapping() {
		if(dbMapping==null)
			dbMapping = createDefaultDbMapping();
		return dbMapping;
	}
	@Override
	public IRestMapping<T> getRestMapping() {
		if(restMapping==null){
			restMapping =  createDefaultRestMapping();
		}
		return restMapping;
	}
	@Override
	public Type getCollectionType() {
		return collectionType;
	}
	
	@Override
	public ModelViewConfiguration<T> getViewConfiguration() {
		if(viewConfiguration==null)
			viewConfiguration = createDefaultViewConfiguration();
		return viewConfiguration;
	}
	
	@Override
	public IValidator<T> getValidator() {
		if(validator==null){
			validator = createDefaultValidator();
		}
		return validator;
	}
	
	

	@Override
	public IDataSource<T> getDataSource() {
		if(dataSource==null){
			dataSource = createDefaultDataSource();
		}
		return dataSource;
	}
	
	private Context getContext() {
		return context;
	}
}
