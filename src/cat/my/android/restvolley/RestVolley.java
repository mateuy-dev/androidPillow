package cat.my.android.restvolley;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import cat.my.android.restvolley.sync.ISynchDataSource;
import cat.my.android.restvolley.sync.SynchManager;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteOpenHelper;

public class RestVolley {
	public static final String PREFERENCES_FILE_KEY = "cat_my_android_restvolley";
	
	List<ISynchDataSource<?>> dataSources;
	
	AbstractDBHelper dbHelper;
	SynchManager synchManager;
	
	private static RestVolley restVolley;
	public static synchronized RestVolley getInstance(Context context, int xmlFileResId){
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
	
	public static  RestVolley getInstance(){
		//Does not initialize if uninitialized!!
		return restVolley;
	}
	
	public List<ISynchDataSource<?>> getDataSources() {
		return dataSources;
	}

	public AbstractDBHelper getDbHelper() {
		return dbHelper;
	}

	public SynchManager getSynchManager() {
		return synchManager;
	}
	
	private void init(Context context, int xmlFileResId) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, XmlPullParserException, IOException{
		dataSources = new ArrayList<ISynchDataSource<?>>();
        XmlResourceParser parser = context.getResources().getXml(xmlFileResId);
        //Check http://stackoverflow.com/questions/8378748/create-a-custom-xml-data-type
        int token;
        while ((token = parser.next()) != XmlPullParser.END_DOCUMENT) {
            if (token == XmlPullParser.START_TAG) {
            	String tagName = parser.getName();
                if ("datasource".equals(tagName)) {
                	ISynchDataSource<?> currentModel = (ISynchDataSource<?>) getClassFor(context, parser);
                    dataSources.add(currentModel);
                }
                else if("dbhelper".equals(tagName)){
                	//TODO this should not be an AbstractDBHelper
                	dbHelper = (AbstractDBHelper) getClassFor(context, parser);
                }
            }
        }
        synchManager = new SynchManager(context, dataSources, dbHelper);
	}
	
	private Object getClassFor(Context context, XmlResourceParser parser) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String className = parser.getAttributeValue(0);//("class");
    	Class<?> clazz = Class.forName(className);
    	Constructor<?> constructor = clazz.getConstructor(new Class[] { Context.class});
    	return constructor.newInstance(context);
	}
}
