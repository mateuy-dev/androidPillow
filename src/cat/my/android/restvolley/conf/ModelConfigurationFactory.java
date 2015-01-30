package cat.my.android.restvolley.conf;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import cat.my.android.restvolley.IDataSource;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.RestVolleyConfigXml;
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

	public ModelConfigurationFactory(Context context, RestVolleyConfigXml config, IModelConfigurations configuration) {
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
