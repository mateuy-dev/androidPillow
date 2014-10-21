/**
 * Copyright Mateu Yábar (http://mateuyabar.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package cat.my.android.restvolley.rest;

import java.io.IOException;
import java.lang.reflect.Type;

import org.atteo.evo.inflector.English;

import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.db.DBUtil;
import cat.my.util.CaseFormat;

import com.android.volley.Request.Method;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class RailsRestMapping<T extends IdentificableModel> implements IRestMapping<T> {
	String prefix;
	Class<T> clazz;
	Type collectionType;
	CaseFormat caseFormat = new CaseFormat();
	
	public RailsRestMapping(String prefix, Class<T> clazz, Type collectionType){
		this.prefix = prefix;
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
		return prefix + "/" + plural + ".json";
	}
	
	protected String getPath(String id){
		String modelName = getModelName();
		String plural = English.plural(modelName);
		return prefix + "/" + plural +"/"+ id +".json";
	}
	
	private String getPath(String id, String operation) {
		String modelName = getModelName();
		String plural = English.plural(modelName);
		String opPath = "/" + operation;
		return prefix + "/" + plural +"/"+ id + opPath +".json";
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
	              String railsValue = in.nextString();
	              String javaValue = railsValue.toUpperCase();
	              Class enumClass =type.getRawType(); 
	              return (TE) Enum.valueOf(enumClass, javaValue);
	         }
	    }

	}

}
