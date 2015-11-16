/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.mateuyabar.android.pillow.data.rest;

import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResult;

import java.util.HashMap;
import java.util.Map;

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
	 * Ressets current authetication. Called, for example, the current authentication does not work
	 */
	public IPillowResult<Void> resetAuthentication();


    /**
     * No authentication required.
     */
	public class NullAuthenticationController implements IAuthenticationController {
		@Override
		public IPillowResult<AuthenticationData> getAuthentication() {
			return new PillowResult<AuthenticationData>(new AuthenticationData());
		}

		@Override
		public IPillowResult<Void> resetAuthentication() {
			return PillowResult.newVoidResult();
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
