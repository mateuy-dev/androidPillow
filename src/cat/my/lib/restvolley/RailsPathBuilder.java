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
package cat.my.lib.restvolley;

import org.atteo.evo.inflector.English;

import com.android.volley.Request.Method;

import cat.my.lib.restvolley.models.IdentificableModel;
import cat.my.lib.restvolley.util.CaseFormat;

public class RailsPathBuilder {
	String prefix;
	CaseFormat caseFormat = new CaseFormat();
	
	public RailsPathBuilder(String prefix){
		this.prefix = prefix;
	}
	
	public Route getIndexPath(Class<?> clazz){
		return new Route(Method.GET, getPath(clazz));
	}
	
	public Route getCreatePath(IdentificableModel model){
		return new Route(Method.POST, getPath(model.getClass()));
	}
	
	public Route getShowPath(IdentificableModel model){
		return new Route(Method.GET, getPath(model));
	}
	
	public Route getUpdatePath(IdentificableModel model){
		return new Route(Method.PUT, getPath(model));
	}
	
	public Route getDestroyPath(IdentificableModel model){
		return new Route(Method.DELETE, getPath(model));
	}
	
	private String getPath(IdentificableModel model){
		return getPath(model.getClass(), model.getId());
	}
	
	private String getPath(Class<?> clazz, String id){
		String modelName = getModelName(clazz);
		String plural = English.plural(modelName);
		return prefix + "/" + plural +"/"+ id +".json";
	}
	
	private String getPath(Class<?> clazz){
		String modelName = getModelName(clazz);
		String plural = English.plural(modelName);
		return prefix + "/" + plural + ".json";
	}
	
	private String getModelName(Class<?> clazz){
		return caseFormat.cammelCaseToSnakeCase(clazz.getSimpleName());
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
