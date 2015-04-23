package com.mateuyabar.android.pillow.data.rest;

import java.util.HashMap;
import java.util.Map;

import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResult;

/**
 *
 */
public interface IAuthenticationController {
    /**
     * Returns the authentication data need to authenticate the current user to the server. This operation may need to connect to the api server (may not be instant).
     * TODO define what to do if the current user has not been authenticated
     * @return authentication data
     *
     */
	public IPillowResult<AuthenticationData> getAuthentication();

    /**
     * No authentication required.
     */
	public class NullAuthenticationController implements IAuthenticationController {
		@Override
		public IPillowResult<AuthenticationData> getAuthentication() {
			return new PillowResult<AuthenticationData>(Pillow.getInstance().getContext(), new AuthenticationData());
		}
	}
	
	public class AuthenticationData {
		Map<String, Object> data;

		public AuthenticationData(){
			data = new HashMap<String, Object>();
		}
		public AuthenticationData(Map<String, Object> data) {
			this.data = data;
		}
		public Map<String, Object> getData() {
			return data;
		}
        //Should also contain header attributes (like token for oauth)
	}
}
