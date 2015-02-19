package cat.my.android.pillow.data.sync;

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
			syncManager.synchronize(CommonListeners.dummyListener, CommonListeners.getDefaultThreadedErrorListener(), false);
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