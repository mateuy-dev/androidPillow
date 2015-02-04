package cat.my.android.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class ScanUtil {
	Activity activity;
	
	public ScanUtil(Activity activity){
		this.activity=activity;
	}

	/**
	 * Calls an intent to read the QR data. In case that no application to read
	 * QR is found, it will redirect to the google market.
	 */
	public void readQrData(int requestId) {
		readFromZxing(requestId, "QR_CODE_MODE");
	}

	public void readBarCode(int requestId) {
		// TODO mode may be ONE_D_MODE more info at:
		// http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/Intents.java
		readFromZxing(requestId, "PRODUCT_MODE"); // ONE_D_MODE
	}

	private void readFromZxing(int requestId, String scanMode) {
		try {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", scanMode);
			activity.startActivityForResult(intent, requestId);
		} catch (Exception e) {
			donwloadZxing();
		}
	}

	private void donwloadZxing() {
		Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
		Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
		activity.startActivity(marketIntent);
	}
}
