package cat.my.android.pillow.conf;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.PillowConfigXml;

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
