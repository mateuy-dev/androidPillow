package cat.my.android.restvolley;

import java.util.Collection;

import com.android.volley.VolleyError;

public class Listeners{
	public interface CollectionListener<T> extends Listener<Collection<T>>{}
	public interface Listener<T> extends com.android.volley.Response.Listener<T>{}
	public interface ErrorListener {
		public void onErrorResponse(RestVolleyError error);
	};
	
	public static class VolleyErrorListener implements com.android.volley.Response.ErrorListener{
		ErrorListener listener;
		public VolleyErrorListener(ErrorListener listener) {
			this.listener = listener;
		}
		@Override
		public void onErrorResponse(VolleyError error) {
			listener.onErrorResponse(new RestVolleyError(error));
		}
	}
}
