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
	
	public static class ProxyListener<T, D> implements Listener<D>{
		Listener<T> listener;
		T response;
		public ProxyListener(Listener<T> listener, T response){
			this.listener = listener;
			this.response = response;
		}

		@Override
		public void onResponse(D dummyResponse) {
			listener.onResponse(response);
		}
		
	}

}
