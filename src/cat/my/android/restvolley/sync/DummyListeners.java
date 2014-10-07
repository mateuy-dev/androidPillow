package cat.my.android.restvolley.sync;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public class DummyListeners {
	public static ErrorListener dummyErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
		}
	};
	public static Listener dummyListener = new Listener() {
		@Override
		public void onResponse(Object response) {
		}
	};

}
