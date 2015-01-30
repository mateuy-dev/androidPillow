package cat.my.android.restvolley;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import cat.my.android.restvolley.conf.IModelConfigurations;
import cat.my.android.restvolley.conf.ModelConfiguration;
import cat.my.android.restvolley.conf.ModelConfigurationFactory;
import cat.my.android.restvolley.sync.ISynchDataSource;
import cat.my.android.restvolley.sync.SynchManager;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteOpenHelper;

public class RestVolley {
	public static final String PREFERENCES_FILE_KEY = "cat_my_android_restvolley";
	public static int xmlFileResId;
//	Map<Class<?>, ISynchDataSource<?>> dataSources;
	RestVolleyConfigXml config;
	
	AbstractDBHelper dbHelper;
	SynchManager synchManager;
	ModelConfigurationFactory modelConfigurationFactory;
	
	private static RestVolley restVolley;
	public static synchronized RestVolley getInstance(Context context){
		if(restVolley==null){
			restVolley = new RestVolley();
			try {
				restVolley.init(context, xmlFileResId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return restVolley;
	}
	
	public static synchronized void setConfigurationFile(int xmlFileResId){
		RestVolley.xmlFileResId = xmlFileResId;
	}
	
	public static  RestVolley getInstance(){
		//Does not initialize if uninitialized!!
		return restVolley;
	}
	
//	public Collection<ISynchDataSource<?>> getDataSources() {
//		return dataSources.values();
//	}

	public AbstractDBHelper getDbHelper() {
		return dbHelper;
	}

	public SynchManager getSynchManager() {
		return synchManager;
	}
	
	private void init(Context context, int xmlFileResId) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, XmlPullParserException, IOException{
		config = new RestVolleyConfigXml(context, xmlFileResId);
		dbHelper = getClassFor(context, config.getDbHelper());
		Class<IModelConfigurations> modelConfigurationsclazz = (Class<IModelConfigurations>) Class.forName(config.getModelConfigurations());
		IModelConfigurations modelConfigurations = modelConfigurationsclazz.newInstance();
		modelConfigurationFactory = new ModelConfigurationFactory(context, config, modelConfigurations);
		List<ISynchDataSource<?>> synchedSources = new ArrayList<ISynchDataSource<?>>();
		for(ModelConfiguration<?> modelConf : modelConfigurationFactory.getModelConfigurations().values()){
			IDataSource<?> dataSource = modelConf.getDataSource();
			if(dataSource instanceof ISynchDataSource<?>){
				synchedSources.add((ISynchDataSource<?>) dataSource);
			}
		}
		
        synchManager = new SynchManager(context, synchedSources, dbHelper);
        synchManager.setDownloadTimeInterval(config.getDownloadTimeInterval());
	}
	
	private <T> T  getClassFor(Context context, String className) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
    	Class<T> clazz = (Class<T>) Class.forName(className);
    	Constructor<T> constructor = clazz.getConstructor(new Class[] { Context.class});
    	return constructor.newInstance(context);
	}

	public RestVolleyConfigXml getConfig() {
		return config;
	}
	
	/**
	 * Shortcut for getModelConfiguration(modelClass).getDataSource();
	 */
	public <T extends IdentificableModel> IDataSource<T> getDataSource(Class<T> modelClass){
		return getModelConfiguration(modelClass).getDataSource();
	}
	
	public <T extends IdentificableModel> ModelConfiguration<T> getModelConfiguration(Class<T> modelClass){
		return modelConfigurationFactory.getModelConfiguration(modelClass);
	}
	
	public ModelConfigurationFactory getModelConfigurationFactory() {
		return modelConfigurationFactory;
	}
}
