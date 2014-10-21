package cat.my.android.restvolley;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;

public class RestVolleyConfig {
	String dbHelper;
	List<String> dataSources = new ArrayList<String>();
	int downloadTimeInterval = 3600000;
	int maxResponseWaitTime = 10000;

	public RestVolleyConfig(Context context, int xmlFileResId){
		try {
			read(context, xmlFileResId);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void read(Context context, int xmlFileResId) throws XmlPullParserException, IOException {
        XmlResourceParser parser = context.getResources().getXml(xmlFileResId);
        //Check http://stackoverflow.com/questions/8378748/create-a-custom-xml-data-type
        int token;
        while ((token = parser.next()) != XmlPullParser.END_DOCUMENT) {
            if (token == XmlPullParser.START_TAG) {
            	String tagName = parser.getName();
                if ("datasource".equals(tagName)) {
                	String clazz = parser.getAttributeValue(0);//("class");
                    dataSources.add(clazz);
                }
                else if("dbhelper".equals(tagName)){
                	String clazz = parser.getAttributeValue(0);//("class");
                	dbHelper = clazz;
                } else if ("downloadTimeInterval".equals(tagName)){
                	downloadTimeInterval = parser.getAttributeIntValue(0, 3600000);//("vale");
                } else if ("maxResponseWaitTime".equals(tagName)){
                	maxResponseWaitTime = parser.getAttributeIntValue(0, 10000);//("vale");
                }
                
            }
        }
	}

	public String getDbHelper() {
		return dbHelper;
	}

	public List<String> getDataSources() {
		return dataSources;
	}

	public int getDownloadTimeInterval() {
		return downloadTimeInterval;
	}

	public int getMaxResponseWaitTime() {
		return maxResponseWaitTime;
	}
}
