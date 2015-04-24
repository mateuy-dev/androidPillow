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
package com.mateuyabar.android.pillow.data.rest;

import com.android.volley.Request.Method;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.data.db.DBUtil;
import com.mateuyabar.util.CaseFormat;

import org.atteo.evo.inflector.English;

import java.io.IOException;
import java.lang.reflect.Type;

public class RailsRestMapping<T extends IdentificableModel> implements IRestMapping<T> {
	String url;
	Class<T> clazz;
	Type collectionType;
	CaseFormat caseFormat = new CaseFormat();
	
	public RailsRestMapping(String url, Class<T> clazz, Type collectionType){
		this.url = url;
		this.clazz = clazz;
		this.collectionType = collectionType;
	}
	
	@Override
	public Route getCollectionRoute(int method, String operation){
		return new Route(method, getPath(operation));
	}
	
	@Override
	public Route getMemberRoute(T model, int method, String operation){
		return new Route(method, getPath(model.getId(), operation));
	}

	@Override
	public Route getIndexPath(){
		return new Route(Method.GET, getPath());
	}
	
	@Override
	public Route getCreatePath(T model){
		return new Route(Method.POST, getPath());
	}
	
	@Override
	public Route getShowPath(T model){
		return new Route(Method.GET, getPath(model));
	}
	
	@Override
	public Route getUpdatePath(T model){
		return new Route(Method.PUT, getPath(model));
	}
	
	@Override
	public Route getDestroyPath(T model){
		return new Route(Method.DELETE, getPath(model));
	}
	
	@Override
	public Gson getSerializer(){
		GsonBuilder builder = new GsonBuilder();
		builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
		builder.registerTypeAdapterFactory(new RailsEnumAdapterFactory());
		builder.setDateFormat(DBUtil.DATE_STRING_FORMAT);
		return builder.create();
	}
	
	private String getPath(T model){
		return getPath(model.getId());
	}
	
	protected String getPath(){
		String modelName = getModelName();
		String plural = English.plural(modelName);
		return url + "/" + plural + ".json";
	}
	
	protected String getPath(String id){
		String modelName = getModelName();
		String plural = English.plural(modelName);
		return url + "/" + plural +"/"+ id +".json";
	}
	
	private String getPath(String id, String operation) {
		String modelName = getModelName();
		String plural = English.plural(modelName);
		String opPath = "/" + operation;
		return url + "/" + plural +"/"+ id + opPath +".json";
	}
	
	public String getModelName(){
		return getModelName(clazz.getSimpleName());
	}
	
	protected String getModelName(String modelName){
		return caseFormat.cammelCaseToSnakeCase(modelName);
	}

	@Override
	public Class<T> getModelClass() {
		return clazz;
	}

	@Override
	public Type getCollectionType() {
		return collectionType;
	}
	
	/**
	 * Enums values are expresed in minus in Rails and capital leters in Java
	 */
	private class RailsEnumAdapterFactory implements TypeAdapterFactory {

	    @Override
	    public <TE> TypeAdapter<TE> create(final Gson gson, final TypeToken<TE> type) {
	            Class<? super TE> rawType = type.getRawType();
	            if (Enum.class.isAssignableFrom(rawType)) {
	                return new RailsEnumTypeAdapter(type);
	            }
	            return null;
	    }

	    private class RailsEnumTypeAdapter<TE> extends TypeAdapter<TE> {
	    	TypeToken<TE> type;
	        public RailsEnumTypeAdapter(TypeToken<TE> type) {
				this.type = type;
			}

			public void write(JsonWriter out, TE value) throws IOException {
	              if (value == null) {
	                   out.nullValue();
	                   return;
	              }
	              Enum<?> enumValue = (Enum<?>) value;
	              String javaValue = enumValue.toString();
	              String railsValue = javaValue.toLowerCase();
	              out.value(railsValue);
	              
	         }

			public TE read(JsonReader in) throws IOException {
				if (in.peek() == JsonToken.NULL) {
					in.nextNull();
					return null;
				} else {
					String railsValue = in.nextString();
					String javaValue = railsValue.toUpperCase();
					Class enumClass = type.getRawType();
					return (TE) Enum.valueOf(enumClass, javaValue);
				}
			}
		}

	}

}
