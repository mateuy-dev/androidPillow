package cat.my.android.pillow.data.rest;

import java.util.HashMap;
import java.util.Map;

import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.data.core.IPillowResult;
import cat.my.android.pillow.data.core.PillowResult;

public interface ISessionController {
	public IPillowResult<SessionData> getSession();
	
	public class NullSessionController implements ISessionController{
		@Override
		public IPillowResult<SessionData> getSession() {
			return new PillowResult<ISessionController.SessionData>(Pillow.getInstance().getContext(), new SessionData());
		}
	}
	
	public class SessionData {
		Map<String, Object> data;

		public SessionData(){
			data = new HashMap<String, Object>();
		}
		public SessionData(Map<String, Object> data) {
			this.data = data;
		}
		public Map<String, Object> getData() {
			return data;
		}
	}
}
