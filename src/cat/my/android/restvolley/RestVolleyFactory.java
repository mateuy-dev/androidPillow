package cat.my.android.restvolley;

import java.lang.reflect.Type;

import cat.my.android.restvolley.db.IDbMapping;
import cat.my.android.restvolley.db.ReflectionDbMapping;
import cat.my.android.restvolley.forms.views.FormActivity;
import cat.my.android.restvolley.rest.IRestMapping;
import cat.my.android.restvolley.rest.RailsRestMapping;
import cat.my.util.ToImplementException;
import cat.my.util.UnimplementedException;

/**
 * This class will be used to have a factory of the different elements/configurations of the system
 *
 */
public class RestVolleyFactory {
	public <T extends IdentificableModel> IDbMapping<T> getDbMapping(Class<T> modelClass){
		return new ReflectionDbMapping<T>(modelClass);
	}
	
	public <T extends IdentificableModel> IRestMapping<T> getRestMapping(Class<T> modelClass){
		return new RailsRestMapping<T>(getApiUrl(), modelClass, getCollectionType(modelClass));
	}

	public Type getCollectionType(Class<?> modelClass){
		throw new UnimplementedException();
	}
	
	public <T> Class<?> getForm(Class<T> modelClass){
		return FormActivity.class;
	}
	
	private String getApiUrl() {
		throw new ToImplementException();
	}
	
	public class RestVolleyModelConf<T>{
		Class<T> clazz;
		IDbMapping<T> dbMapping;
		IRestMapping<T> restMapping;
		Type collectionType;
		Class<?> formClass;
		
		public RestVolleyModelConf(Class<T> clazz) {
			super();
			this.clazz = clazz;
		}

		public Class<T> getClazz() {
			return clazz;
		}

		public void setClazz(Class<T> clazz) {
			this.clazz = clazz;
		}

		public IDbMapping<T> getDbMapping() {
			return dbMapping;
		}

		public void setDbMapping(IDbMapping<T> dbMapping) {
			this.dbMapping = dbMapping;
		}

		public IRestMapping<T> getRestMapping() {
			return restMapping;
		}

		public void setRestMapping(IRestMapping<T> restMapping) {
			this.restMapping = restMapping;
		}

		public Type getCollectionType() {
			return collectionType;
		}

		public void setCollectionType(Type collectionType) {
			this.collectionType = collectionType;
		}

		public Class<?> getFormClass() {
			return formClass;
		}

		public void setFormClass(Class<?> formClass) {
			this.formClass = formClass;
		}
	}
}
