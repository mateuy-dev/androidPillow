package cat.my.android.restvolley.sync;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import cat.my.android.restvolley.AbstractDBHelper;
import cat.my.android.restvolley.Listeners.CollectionListener;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.RestVolleyError;
import cat.my.android.restvolley.db.DBUtil;
import cat.my.android.restvolley.users.AbstractUserController;

import cat.my.android.restvolley.Listeners.Listener;

public class SynchManager {
	
	
	List<ISynchDataSource<?>> synchDataSources= new ArrayList<ISynchDataSource<?>>();

	SharedPreferences sharedPref;
	AbstractDBHelper dbHelper;
	long downloadTimeInterval = 3600000; //1 hour
	private static final String LAST_DOWNLOAD_DATE = "LAST_DOWNLOAD_DATE";
	
	public SynchManager(Context context, List<ISynchDataSource<?>> synchDataSources, AbstractDBHelper dbHelper) {
		super();
		this.synchDataSources = synchDataSources;
		this.dbHelper = dbHelper;
		sharedPref = context.getSharedPreferences(RestVolley.PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
	}
	
	public List<ISynchDataSource<?>> getSynchDataSources() {
		return synchDataSources;
	}
	
	private void resetTables() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		dbHelper.resetTables(db);
		db.close();
	}
	
	public void reloadData(Listener<Void> listener){
		resetTables();
		download(listener, true);
	}
	
	public void download(Listener<Void> listener, boolean force){
		if(!force && getLastDownload()!=null && isValidDonwload()){
			listener.onResponse(null);
		} else {
			new DownloadTask(listener).start();
		}
	}
	
	private void setLastDownload(Date date) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(LAST_DOWNLOAD_DATE, DBUtil.dateTimeToDb(date));
		editor.commit();
	}

	public Date getLastDownload() {
		String value =  sharedPref.getString(LAST_DOWNLOAD_DATE, null);
		return DBUtil.dbToDateTime(value);
	}

	
	public long getDownloadTimeInterval() {
		return downloadTimeInterval;
	}

	/**
	 * downloadTimeInterval indicates the accepted interval (in milliseconds) between two downloads (note that is not used on force=true) 
	 * @param downloadTimeInterval
	 */
	public void setDownloadTimeInterval(long downloadTimeInterval) {
		this.downloadTimeInterval = downloadTimeInterval;
	}

	public void synchronize(Listener<Void> listener, boolean force){
		upload();
		download(listener, force);
	}
	
	public void upload(){
		//This should work with listeners!!!!!!!!!!!!!!!!!!!!!
		for(ISynchDataSource<?> dataSource:synchDataSources){
			dataSource.sendDirty();
		}
	}
	
	private boolean isValidDonwload() {
		return getLastDownload().getTime() + downloadTimeInterval > new Date().getTime();
	}

	private class DownloadTask implements CollectionListener, ErrorListener{
		int i=0;
		Listener<Void> listener;
		boolean errorFound = false;
		public DownloadTask(Listener<Void> listener) {
			this.listener = listener;
		}

		@Override
		public void onResponse(Object response) {
			i++;
			if(synchDataSources.size()>i){
				downloadCurrent();
			} else {
				if(!errorFound){
					setLastDownload(new Date());
					listener.onResponse(null);
				}
			}
		}
		
		private void downloadCurrent(){
			synchDataSources.get(i).download(this, this);
		}
		
		public void start(){
			downloadCurrent();
		}

		@Override
		public void onErrorResponse(RestVolleyError error) {
			errorFound = true;
			error.printStackTrace();
		}
	}
}
