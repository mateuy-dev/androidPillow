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

package com.mateuyabar.android.pillow.test.data;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.Pillow;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.rest.IRestMapping;
import com.mateuyabar.android.pillow.data.rest.RestDataSource;
import com.mateuyabar.android.pillow.data.rest.Route;
import com.mateuyabar.android.pillow.data.sync.ISynchDataSource;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

public abstract class AbstractInstrumentationTestCase<T extends IdentificableModel> extends InstrumentationTestCase{
	Class<T> clazz;
	
	public AbstractInstrumentationTestCase(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	
	protected void checkLocalVersionSameAs(T model) throws Exception{
		T showModel = getController().show(model).getResult();
		assertNotNull(showModel);
		assertSame(model, showModel);
	}
	
	protected void checkServerVersionSameAs(T model) throws Exception{
		sendDirty();
		T showModel = getController().getRestDataSource().show(model).getResult();
		assertNotNull(showModel);
		assertSame(model, showModel);
		
//		HttpResponse response = getServerResponse(model);
//		String json_string = EntityUtils.toString(response.getEntity());
//		T serverModel = new Gson().fromJson(json_string, clazz);
//		assertSame(serverModel, model);
	}
	
	protected HttpResponse getServerResponse(T model) throws ClientProtocolException, IOException{
		Type collectionType = new TypeToken<Collection<T>>(){}.getType();
		
		IRestMapping<T> restMapping = getRestMapping();
		Route route = restMapping.getShowPath(model);
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpmodel = new HttpGet(route.getUrl());
		HttpResponse response = httpclient.execute(httpmodel);
		return response;
	}
	
	
	protected void sendDirty() throws Exception{
//		Thread.currentThread().sleep(2000);
		getController().sendDirty().getResult();
	}
	
	protected IPillowResult<Void> synchornize() throws PillowError{
		IPillowResult<Void> result = Pillow.getInstance(getContext()).getSynchManager().synchronize(true);
		result.getResult();
		return result;
	}

	protected <T2 extends IdentificableModel> ISynchDataSource<T2> getController(Class<T2> clazz){
		return (ISynchDataSource<T2>)Pillow.getInstance(getContext()).getDataSource(clazz);
	}
	
	protected IRestMapping<T> getRestMapping(){
		return Pillow.getInstance(getContext()).getModelConfiguration(clazz).getRestMapping();
	}
	
	public Context getContext(){
		return getInstrumentation().getContext();
	}
	
	protected ISynchDataSource<T> getController(){
		return getController(clazz);
	}
	
	protected void assertSame(T model1, T model2){
		assertEquals(model1.getId(), model2.getId());
		String model1Json = new Gson().toJson(model1);
		String model2Json = new Gson().toJson(model2);
//		debug help: model1Json.equals(model2Json)
		assertEquals(model1Json, model2Json);
	}

	public void stopConnection(){
		RestDataSource.SIMULATE_OFFLINE_CONNECTIVITY_ON_TESTING = true;
	}
	
	public void startConnection(){
		RestDataSource.SIMULATE_OFFLINE_CONNECTIVITY_ON_TESTING = false;
	}
	 
}
