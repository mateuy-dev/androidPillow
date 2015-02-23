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
import cat.my.android.pillow.data.sync.SynchManager;
import cat.my.android.pillow.util.reflection.RelationGraph;

public class Pillow {
	public static final String LOG_ID = "pillow";
	public static final String PREFERENCES_FILE_KEY = "cat_my_android_pillow";
	public static int xmlFileResId;
//	Map<Class<?>, ISynchDataSource<?>> dataSources;
	PillowConfigXml config;
	
	AbstractDBHelper dbHelper;
	SynchManager synchManager;
	ModelConfigurationFactory modelConfigurationFactory;
	RelationGraph relationGraph;
	List<IDataSource<?>> sortedSynchDataSources;
	Context context;
	
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
		this.context=context;
		config = new PillowConfigXml(context, xmlFileResId);
		dbHelper = getClassFor(context, config.getDbHelper());
		Class<IModelConfigurations> modelConfigurationsclazz = (Class<IModelConfigurations>) Class.forName(config.getModelConfigurations());
		IModelConfigurations modelConfigurations = modelConfigurationsclazz.newInstance();
		modelConfigurationFactory = new ModelConfigurationFactory(context, config, modelConfigurations);
		relationGraph = new RelationGraph();
		for(Class<?> modelClass: modelConfigurationFactory.getModelConfigurations().keySet()){
			relationGraph.addClass(modelClass);
		}
		synchManager = new SynchManager(context, dbHelper);
        synchManager.setDownloadTimeInterval(config.getDownloadTimeInterval());
	}
	
	public synchronized List<IDataSource<?>> getSortedSynchDataSources() {
		if(sortedSynchDataSources==null){
			List<Class<?>> order = relationGraph.getSynchOrder();
			sortedSynchDataSources = new ArrayList<IDataSource<?>>();
			for(Class<?> orderItem: order){
				sortedSynchDataSources.add(getDataSource((Class<? extends IdentificableModel>)orderItem));
			}
		}
		return sortedSynchDataSources;
	}
	public Context getContext() {
		return context;
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
