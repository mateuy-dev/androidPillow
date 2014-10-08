package cat.my.android.restvolley.sync;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import cat.my.android.restvolley.AbstractDBHelper;
import cat.my.android.restvolley.users.AbstractUserController;

import com.android.volley.Response.Listener;

public class SynchManager {
	List<ISynchDataSource<?>> synchDataSources= new ArrayList<ISynchDataSource<?>>();


	AbstractDBHelper dbHelper;
	Date lastDownload = null;
	long downloadTimePeriod = 3600000;
	
	public SynchManager(List<ISynchDataSource<?>> synchDataSources, AbstractDBHelper dbHelper) {
		super();
		this.synchDataSources = synchDataSources;
		this.dbHelper = dbHelper;
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
		if(!force && lastDownload!=null && isValidDonwload()){
			listener.onResponse(null);
		} else {
			//TODO if download does not work lastDownload should not change!
			lastDownload=new Date();
			new DownloadTask(listener).downloadCurrent();
		}
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
		return lastDownload.getTime() + downloadTimePeriod > new Date().getTime();
	}

	private class DownloadTask implements Listener{
		int i=0;
		Listener<Void> listener;
		public DownloadTask(Listener<Void> listener) {
			this.listener = listener;
		}

		@Override
		public void onResponse(Object response) {
			if(synchDataSources.size()<i){
				i++;
				downloadCurrent();
			} else {
				listener.onResponse(null);
			}
		}
		
		private void downloadCurrent(){
			synchDataSources.get(i).download(this, DummyListeners.dummyErrorListener);
		}
	}
}
