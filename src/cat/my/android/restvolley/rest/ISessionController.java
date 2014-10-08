package cat.my.android.restvolley.rest;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

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
