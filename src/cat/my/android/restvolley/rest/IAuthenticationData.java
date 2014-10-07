package cat.my.android.restvolley.rest;

import java.util.HashMap;
import java.util.Map;

public interface IAuthenticationData {
	public Map<String, Object> getSession();
	public boolean isAuthenticated();
	
	public class NullAuthenticationData implements IAuthenticationData{

		@Override
		public Map<String, Object> getSession() {
			return new HashMap<String, Object>();
		}

		@Override
		public boolean isAuthenticated() {
			return false;
		}
		
	}
}
