package cat.my.android.restvolley.conf;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.db.IDbMapping;
import cat.my.android.restvolley.db.ReflectionDbMapping;
import cat.my.android.restvolley.forms.views.FormActivity;
import cat.my.android.restvolley.rest.IRestMapping;
import cat.my.android.restvolley.rest.RailsRestMapping;
import cat.my.util.ToImplementException;
import cat.my.util.UnimplementedException;

/**
 * Contains configurations and resources (datasource, dbMapping, resmapping) for model classes.
 *
 */
public class ModelConfigurationFactory {
	Map<Class<?>, ModelConfiguration<?>> modelConfigurations = new HashMap<Class<?>, ModelConfiguration<?>>();
	
	public void addModelConfiguration(ModelConfiguration<?> modelConfiguration){
		modelConfigurations.put(modelConfiguration.getModelClass(), modelConfiguration);
	}
	
	public <T extends IdentificableModel> IDbMapping<T> getDbMapping(Class<T> modelClass){
		IDbMapping<T> result = getModelConfiguration(modelClass).getDbMapping();
		if(result==null){
			//if not set, we add generic one
			result = new ReflectionDbMapping<T>(modelClass);
			getModelConfiguration(modelClass).setDbMapping(result);
		}
		return result;
	}
	
	public <T extends IdentificableModel> IRestMapping<T> getRestMapping(Class<T> modelClass){
		IRestMapping<T> result = getModelConfiguration(modelClass).getRestMapping();
		if(result==null){
			result = new RailsRestMapping<T>(getApiUrl(), modelClass, getCollectionType(modelClass));
			getModelConfiguration(modelClass).setRestMapping(result);
		}
		return result;
	}

	public Type getCollectionType(Class<?> modelClass){
		return getModelConfiguration(modelClass).getCollectionType();
	}
	
	public <T> Class<?> getForm(Class<T> modelClass){
		Class<?> result = getModelConfiguration(modelClass).getFormClass();
		if(result==null){
			result = FormActivity.class;
			getModelConfiguration(modelClass).setFormClass(result);
		}
		return result;
	}
	
	private String getApiUrl() {
		throw new ToImplementException();
	}
	
	@SuppressWarnings("unchecked")
	private <T> ModelConfiguration<T> getModelConfiguration(Class<T> modelClass){
		return (ModelConfiguration<T>) modelConfigurations.get(modelClass);
	}
}
