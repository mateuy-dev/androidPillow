package cat.my.android.restvolley.rest;

import java.util.HashMap;
import java.util.Map;

import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.Listeners.Listener;

public interface ISessionController {
	public Map<String, Object> getSession();

	public void init(Listener<Void> listener, ErrorListener errorListener);
	
	public class NullSessionController implements ISessionController{

		@Override
		public Map<String, Object> getSession() {
			return new HashMap<String, Object>();
		}


		@Override
		public void init(Listener<Void> listener, ErrorListener errorListener) {
			listener.onResponse(null);
		}
		
	}
}
