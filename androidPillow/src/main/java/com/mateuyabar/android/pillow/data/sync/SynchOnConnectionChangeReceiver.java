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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * BroadcastReciever that checks for internet and inits users + donwloads if necessary....
 * 
 * Call register to make it work.
 */
public class SynchOnConnectionChangeReceiver extends BroadcastReceiver {
	SynchManager syncManager;
	
	public void register(Context context, SynchManager syncManager){
		this.syncManager = syncManager;
		context.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if(connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnected()){
			syncManager.synchronize(false).setListeners(null, CommonListeners.getDefaultThreadedErrorListener());
		}
		
		
//		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
//		NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//		if (activeNetInfo != null) {
//			Toast.makeText(context, "Active Network Type : " + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT).show();
//		}
//		if (mobNetInfo != null) {
//			Toast.makeText(context, "Mobile Network Type : " + mobNetInfo.getTypeName(), Toast.LENGTH_SHORT).show();
//		}
	}
}