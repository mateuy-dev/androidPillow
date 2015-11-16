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

import com.mateuyabar.android.pillow.PillowConfigXml;
import com.mateuyabar.android.pillow.data.models.IdentificableModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains configurations and resources (datasource, dbMapping, resmapping) for model classes.
 *
 */
public class ModelViewConfigurationFactory {
	Map<Class<?>, ModelViewConfiguration<?>> modelViewConfigurations = new HashMap<Class<?>, ModelViewConfiguration<?>>();

	public ModelViewConfigurationFactory(Context context, PillowConfigXml config, IModelViewConfigurations configuration) {
		for(ModelViewConfiguration<?> conf : configuration.getModelViewConfigurators(context, config)){
			modelViewConfigurations.put(conf.getModelClass(), conf);
		}
	}
	public Map<Class<?>, ModelViewConfiguration<?>> getModelConfigurations() {
		return modelViewConfigurations;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IdentificableModel> ModelViewConfiguration<T> getModelViewConfiguration(Class<T> modelClass){
		return (ModelViewConfiguration<T>) modelViewConfigurations.get(modelClass);
	}
}
