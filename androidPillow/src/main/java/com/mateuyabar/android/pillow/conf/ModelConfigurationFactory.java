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

import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.PillowConfigXml;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains configurations and resources (datasource, dbMapping, resmapping) for model classes.
 *
 */
public class ModelConfigurationFactory {
	Map<Class<?>, ModelConfiguration<?>> modelConfigurations = new HashMap<Class<?>, ModelConfiguration<?>>();

	public ModelConfigurationFactory(Context context, PillowConfigXml config, IModelConfigurations configuration) {
		for(ModelConfiguration<?> conf : configuration.getModelConfigurators(context, config)){
			modelConfigurations.put(conf.getModelClass(), conf);
		}
	}
	
//	public <T extends IdentificableModel> IDbMapping<T> getDbMapping(Class<T> modelClass){
//		return getModelConfiguration(modelClass).getDbMapping();
//	}
//	
//	public <T extends IdentificableModel> IRestMapping<T> getRestMapping(Class<T> modelClass){
//		return getModelConfiguration(modelClass).getRestMapping();
//	}
//
//	public <T extends IdentificableModel> Type getCollectionType(Class<T> modelClass){
//		return getModelConfiguration(modelClass).getCollectionType();
//	}
//	
//	public <T  extends IdentificableModel> Class<?> getForm(Class<T> modelClass){
//		return getModelConfiguration(modelClass).getFormClass();
//	}
//	
	public Map<Class<?>, ModelConfiguration<?>> getModelConfigurations() {
		return modelConfigurations;
	}
//	
//	public <T  extends IdentificableModel> IDataSource<T> getDataSource(Class<T> modelClass){
//		return getModelConfiguration(modelClass).getDataSource();
//	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T extends IdentificableModel> ModelConfiguration<T> getModelConfiguration(Class<T> modelClass){
		return (ModelConfiguration<T>) modelConfigurations.get(modelClass);
	}
}
