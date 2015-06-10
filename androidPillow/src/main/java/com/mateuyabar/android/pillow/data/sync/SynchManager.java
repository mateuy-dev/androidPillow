/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.mateuyabar.android.pillow.data.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mateuyabar.android.pillow.AbstractDBHelper;
import com.mateuyabar.android.pillow.data.IDataSource;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResultListener;
import com.mateuyabar.android.pillow.data.db.DBUtil;
import com.mateuyabar.util.exceptions.UnimplementedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SynchManager {
	public static final String LOG_ID = Pillow.LOG_ID + " SynchManager";
	Context context;
	SharedPreferences sharedPref;
	AbstractDBHelper dbHelper;
	long downloadTimeInterval = 3600000; // 1 hour
	private static final String LAST_DOWNLOAD_DATE = "LAST_DOWNLOAD_DATE";
	Executor executor = Executors.newSingleThreadExecutor();

	public SynchManager(Context context, AbstractDBHelper dbHelper) {
		super();
		this.dbHelper = dbHelper;
		this.context = context;
		sharedPref = Pillow.getInstance(context).getSharedPreferences();
	}

	List<ISynchDataSource<?>> sortedSynchDataSources;

	private synchronized List<ISynchDataSource<?>> getSortedSynchDataSources() {
		if (sortedSynchDataSources == null) {
			sortedSynchDataSources = new ArrayList<ISynchDataSource<?>>();
			List<IDataSource<?>> dataSources = Pillow.getInstance(context).getSortedSynchDataSources();
			for (IDataSource<?> dataSource : dataSources) {
				if (dataSource instanceof ISynchDataSource) {
					sortedSynchDataSources.add((ISynchDataSource<?>) dataSource);
				}
			}
		}
		return sortedSynchDataSources;
	}

	private void resetTables() {
		if(dbHelper!=null) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			dbHelper.resetTables(db);
			db.close();
		}
		//TODO Key-pair values should also be deleted!
		Log.w("PILLOW", "Key-pair values should also be deleted!");

}

	/**
	 * Used after user loged in
	 */
	public IPillowResult<Void> reloadData() {
		resetTables();
		return download(true);
	}

	private void setLastDownload(Date date) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(LAST_DOWNLOAD_DATE, DBUtil.dateTimeToDb(date));
		editor.commit();
	}

	public Date getLastDownload() {
		String value = sharedPref.getString(LAST_DOWNLOAD_DATE, null);
		return DBUtil.dbToDateTime(value);
	}

	public long getDownloadTimeInterval() {
		return downloadTimeInterval;
	}

	/**
	 * downloadTimeInterval indicates the accepted interval (in milliseconds)
	 * between two downloads (note that is not used on force=true)
	 * 
	 * @param downloadTimeInterval
	 */
	public void setDownloadTimeInterval(long downloadTimeInterval) {
		this.downloadTimeInterval = downloadTimeInterval;
	}

	public IPillowResult<Void> synchronize(boolean force) {
		if (!force && getLastDownload() != null && isValidDonwload()) {
			return executeOperation(new Operation(Type.DOWNLOAD));
		} else {
			return executeOperation(new Operation(Type.FULL_SYNC));
		}
	}

	public IPillowResult<Void> sendDirty() {
		return executeOperation(new Operation(Type.SEND_DIRTY));
	}

	public IPillowResult<Void> download(boolean force) {
		if (!force && getLastDownload() != null && isValidDonwload()) {
			return PillowResult.newVoidResult();
		} else {
			return executeOperation(new Operation(Type.DOWNLOAD));
		}
	}

	private synchronized IPillowResult<Void> executeOperation(Operation operation) {
		executor.execute(operation);
		return operation.getResult();
	}

	public IPillowResult<Void> realSendDirty() {
		try {
			for (ISynchDataSource<?> dataSource : getSortedSynchDataSources()) {
				Log.d(LOG_ID, "Sending Dirty " + dataSource.getClass().getSimpleName());
				dataSource.sendDirty().get();
				Log.d(LOG_ID, "Sent Dirty " + dataSource.getClass().getSimpleName());
			}
			return PillowResult.newVoidResult();
		} catch (PillowError e) {
			return new PillowResult<Void>(e);
		}
	}

	public IPillowResult<Void> realDownload() {
		try {
			Date date = new Date();
			for (ISynchDataSource<?> dataSource : getSortedSynchDataSources()) {
				dataSource.download().get();
			}
			setLastDownload(new Date());
			return PillowResult.newVoidResult();
		} catch (PillowError e) {
			return new PillowResult<Void>(e);
		}
	}

	public IPillowResult<Void> realSynchronize() {
		try {
			realSendDirty().get();
			realDownload().get();
			return PillowResult.newVoidResult();
		} catch (PillowError e) {
			return new PillowResult<Void>(e);
		}

	}

	private boolean isValidDonwload() {
		return getLastDownload().getTime() + downloadTimeInterval > new Date().getTime();
	}

	enum Type {
		FULL_SYNC, SEND_DIRTY, DOWNLOAD
	};

	protected class Operation implements Runnable {
		Type type;
		PillowResultListener<Void> result;
		public Operation(Type type) {
			super();
			this.type = type;
			this.result = new PillowResultListener<Void>();
		}
		
		public PillowResultListener<Void> getResult() {
			return result;
		}

		@Override
		public void run() {
			IPillowResult<Void> mainPillowResult;
			switch (type) {
			case SEND_DIRTY:
				mainPillowResult = realSendDirty();
				break;
			case DOWNLOAD:
				mainPillowResult = realDownload();
				break;
			case FULL_SYNC:
				mainPillowResult = realSynchronize();
				break;
			default :
				throw new UnimplementedException();
			}
			mainPillowResult.addListeners(result, result);
		}
	}
}
