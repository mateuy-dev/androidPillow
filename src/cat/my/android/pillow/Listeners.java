package cat.my.android.pillow;

import com.android.volley.VolleyError;

public class Listeners{
	public interface Listener<T> extends com.android.volley.Response.Listener<T>{}
	public interface ViewListener<T> extends Listener<T>{}
	public interface ViewErrorListener extends ErrorListener{}
	public interface ErrorListener {
		public void onErrorResponse(PillowError error);
	};
	
	public static class VolleyErrorListener implements com.android.volley.Response.ErrorListener{
		ErrorListener listener;
		public VolleyErrorListener(ErrorListener listener) {
			this.listener = listener;
		}
		@Override
		public void onErrorResponse(VolleyError error) {
			listener.onErrorResponse(new PillowError(error));
		}
	}
}
