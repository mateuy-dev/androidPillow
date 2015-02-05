package cat.my.android.pillow;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import cat.my.android.pillow.conf.IModelConfigurations;
import cat.my.android.pillow.conf.ModelConfiguration;
import cat.my.android.pillow.conf.ModelConfigurationFactory;
import cat.my.android.pillow.conf.ModelViewConfiguration;
import cat.my.android.pillow.data.sync.ISynchDataSource;
import cat.my.android.pillow.data.sync.SynchManager;

public class Pillow {
	public static final String PREFERENCES_FILE_KEY = "cat_my_android_pillow";
	public static int xmlFileResId;
//	Map<Class<?>, ISynchDataSource<?>> dataSources;
	PillowConfigXml config;
	
	AbstractDBHelper dbHelper;
	SynchManager synchManager;
	ModelConfigurationFactory modelConfigurationFactory;
	
	private static Pillow pillow;
	public static synchronized Pillow getInstance(Context context){
		if(pillow==null){
			pillow = new Pillow();
			try {
				pillow.init(context, xmlFileResId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pillow;
	}
	
	public static synchronized void setConfigurationFile(int xmlFileResId){
		Pillow.xmlFileResId = xmlFileResId;
	}
	
	public static  Pillow getInstance(){
		//Does not initialize if uninitialized!!
		return pillow;
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
		config = new PillowConfigXml(context, xmlFileResId);
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

	public PillowConfigXml getConfig() {
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
	/**
	 * Shortcut for getModelConfiguration(modelClass).getViewConfiguration();
	 */
	public <T extends IdentificableModel> ModelViewConfiguration<T> getViewConfiguration(Class<T> modelClass){
		return getModelConfiguration(modelClass).getViewConfiguration();
	}
}
