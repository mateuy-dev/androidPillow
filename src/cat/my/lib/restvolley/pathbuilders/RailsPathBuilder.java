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
package cat.my.lib.restvolley.pathbuilders;

import org.atteo.evo.inflector.English;

import com.android.volley.Request.Method;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cat.my.lib.restvolley.models.IdentificableModel;
import cat.my.lib.restvolley.util.CaseFormat;

public class RailsPathBuilder implements IPathBuilder {
	String prefix;
	CaseFormat caseFormat = new CaseFormat();
	
	public RailsPathBuilder(String prefix){
		this.prefix = prefix;
	}
	
	@Override
	public Route getIndexPath(Class<?> clazz){
		return new Route(Method.GET, getPath(clazz));
	}
	
	@Override
	public Route getCreatePath(IdentificableModel model){
		return new Route(Method.POST, getPath(model.getClass()));
	}
	
	@Override
	public Route getShowPath(IdentificableModel model){
		return new Route(Method.GET, getPath(model));
	}
	
	@Override
	public Route getUpdatePath(IdentificableModel model){
		return new Route(Method.PUT, getPath(model));
	}
	
	@Override
	public Route getDestroyPath(IdentificableModel model){
		return new Route(Method.DELETE, getPath(model));
	}
	
	@Override
	public Gson getSerializer(){
		return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
	}
	
	private String getPath(IdentificableModel model){
		return getPath(model.getClass(), model.getId());
	}
	
	protected String getPath(Class<?> clazz, String id){
		String modelName = getModelName(clazz);
		String plural = English.plural(modelName);
		return prefix + "/" + plural +"/"+ id +".json";
	}
	
	protected String getPath(Class<?> clazz){
		String modelName = getModelName(clazz);
		String plural = English.plural(modelName);
		return prefix + "/" + plural + ".json";
	}
	
	public String getModelName(Class<?> clazz){
		return getModelName(clazz.getSimpleName());
	}
	
	protected String getModelName(String modelName){
		return caseFormat.cammelCaseToSnakeCase(modelName);
	}
	
	public class Route{
		int method;
		String url;
		public Route(int method, String url) {
			super();
			this.method = method;
			this.url = url;
		}
		public int getMethod() {
			return method;
		}
		public void setMethod(int method) {
			this.method = method;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
}
