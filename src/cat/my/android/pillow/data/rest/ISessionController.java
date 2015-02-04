package cat.my.android.pillow.data.rest;

import java.util.HashMap;
import java.util.Map;

import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;

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
