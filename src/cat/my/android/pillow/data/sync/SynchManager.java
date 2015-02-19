package cat.my.android.pillow.data.sync;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cat.my.android.pillow.AbstractDBHelper;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.IDataSource;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.PillowError;
import cat.my.android.pillow.data.db.DBUtil;

import cat.my.android.pillow.util.reflection.RelationGraph;


public class SynchManager {
	public static final String LOG_ID = Pillow.LOG_ID +" SynchManager";
	Context context;
	SharedPreferences sharedPref;
	AbstractDBHelper dbHelper;
	long downloadTimeInterval = 3600000; //1 hour
	private static final String LAST_DOWNLOAD_DATE = "LAST_DOWNLOAD_DATE";
	Executor executor = Executors.newSingleThreadExecutor();
	
	
	
	public SynchManager(Context context, AbstractDBHelper dbHelper) {
		super();
		this.dbHelper = dbHelper;
		this.context = context;
		sharedPref = context.getSharedPreferences(Pillow.PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
	}
	
	List<ISynchDataSource<?>> sortedSynchDataSources;
	private synchronized List<ISynchDataSource<?>> getSortedSynchDataSources() {
		if(sortedSynchDataSources==null){
			sortedSynchDataSources = new ArrayList<ISynchDataSource<?>>();
			List<IDataSource<?>> dataSources = Pillow.getInstance(context).getSortedSynchDataSources();
			for(IDataSource<?> dataSource : dataSources){
				if(dataSource instanceof ISynchDataSource){
					sortedSynchDataSources.add((ISynchDataSource<?>) dataSource);
				}
			}
		}
		return sortedSynchDataSources;
	}
	
	private void resetTables() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		dbHelper.resetTables(db);
		db.close();
	}
	
	public void reloadData(Listener<Void> listener, ErrorListener errorListener){
		resetTables();
		download(listener, errorListener, true);
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
		if(!force && getLastDownload()!=null && isValidDonwload()){
			executeOperation(new Operation(Type.DOWNLOAD, listener, errorListener));
		} else {
			executeOperation(new Operation(Type.FULL_SYNC, listener, errorListener));
		}
	}

	public void sendDirty(Listener<Void> listener, ErrorListener errorListener){
		executeOperation(new Operation(Type.SEND_DIRTY, listener, errorListener));
	}
	
	public void download(Listener<Void> listener, ErrorListener errorListener, boolean force){
		if(!force && getLastDownload()!=null && isValidDonwload()){
			listener.onResponse(null);
		} else {
			executeOperation(new Operation(Type.DOWNLOAD, listener, errorListener));
		}
	}
	
	private synchronized void executeOperation(Operation operation) {
		executor.execute(operation);
	}
	
	public void realSendDirty(Listener<Void> listener, ErrorListener errorListener){
		for(ISynchDataSource<?> dataSource : getSortedSynchDataSources()){
			AsynchListener asyncListener = new AsynchListener<Void>();
			Log.d(LOG_ID, "Sending Dirty "+dataSource.getClass().getSimpleName());
			dataSource.sendDirty(asyncListener, asyncListener);
			
				asyncListener.await();
				if(asyncListener.getError()!=null){
					errorListener.onErrorResponse(asyncListener.getError());
					return;
				}
				Log.d(LOG_ID, "Sent Dirty "+dataSource.getClass().getSimpleName());
			
		}
		listener.onResponse(null);
	}
	
	public void realDownload(Listener<Void> listener, ErrorListener errorListener){
		Date date = new Date();
			for(ISynchDataSource<?> dataSource : getSortedSynchDataSources()){
				AsynchListener asyncListener = new AsynchListener<Void>();
				dataSource.download(asyncListener,asyncListener);
				
					asyncListener.await();
					if(asyncListener.getError()!=null){
						errorListener.onErrorResponse(asyncListener.getError());
						return;
					}
				
			}
			setLastDownload(new Date());
			listener.onResponse(null);
	}
	
	public void realSynchronize(Listener<Void> listener, ErrorListener errorListener){
		AsynchListener asyncListener = new AsynchListener<Void>();
		realSendDirty(asyncListener, asyncListener);
		
			asyncListener.await();
			if(asyncListener.getError()!=null){
				errorListener.onErrorResponse(asyncListener.getError());
				return;
			}
		
		realDownload(listener, errorListener);
	}

	private boolean isValidDonwload() {
		return getLastDownload().getTime() + downloadTimeInterval > new Date().getTime();
	}
	
	enum Type{FULL_SYNC, SEND_DIRTY, DOWNLOAD};
	protected class Operation implements Runnable{
		Type type;
		Listener listener;
		ErrorListener errorListener;
		public Operation(Type type, Listener listener, ErrorListener errorListener) {
			super();
			this.type = type;
			this.listener = listener;
			this.errorListener = errorListener;
		}
		@Override
		public void run() {
			switch(type){
			case SEND_DIRTY:
				realSendDirty(listener, errorListener);
				break;
			case DOWNLOAD:
				realDownload(listener, errorListener);
				break;
			case FULL_SYNC:
				realSynchronize(listener, errorListener);
				break;
			}
		}
	}
}
