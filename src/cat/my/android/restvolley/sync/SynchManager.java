package cat.my.android.restvolley.sync;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import cat.my.android.restvolley.AbstractDBHelper;
import cat.my.android.restvolley.Listeners.Listener;
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
	
	public SynchManager(Context context, Collection<ISynchDataSource<?>> synchDataSources, AbstractDBHelper dbHelper) {
		super();
		this.synchDataSources = new ArrayList<ISynchDataSource<?>>(synchDataSources);
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

	public void synchronize(Listener<Void> listener, ErrorListener errorListener, boolean force){
		new SynchronizeOperation(listener, errorListener, force).start();
	}
	
	public void sendDirty(Listener<Void> listener, ErrorListener errorListener){
		new SendDirtyOperation(listener, errorListener).start();
	}
	
	private class SynchronizeOperation implements Listener<Void>{
		Listener<Void> listener;
		ErrorListener errorListener;
		boolean force;
		public SynchronizeOperation(Listener<Void> listener, ErrorListener errorListener, boolean force) {
			this.listener = listener;
			this.errorListener = errorListener;
			this.force = force;
		}
		public void start(){
			sendDirty(this, errorListener);
		}
		@Override
		public void onResponse(Void response) {
			download(listener, force);
		}
	}
	
	private class SendDirtyOperation implements Listener<Void>{
		Listener<Void> listener;
		ErrorListener errorListener;
		int i=0;
		public SendDirtyOperation(Listener<Void> listener, ErrorListener errorListener) {
			this.listener = listener;
			this.errorListener = errorListener;
		}
		public void sendNext(){
			if(i<synchDataSources.size()){
				ISynchDataSource<?> dataSource = synchDataSources.get(i);
				++i;
				dataSource.sendDirty(this, errorListener);
			} else {
				listener.onResponse(null);
			}
		}
		@Override
		public void onResponse(Void response) {
			sendNext();
		}
		public void start(){
			sendNext();
		}
	}
	
	private boolean isValidDonwload() {
		return getLastDownload().getTime() + downloadTimeInterval > new Date().getTime();
	}

	private class DownloadTask implements Listener, ErrorListener{
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
