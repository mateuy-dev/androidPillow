package cat.my.android.pillow.test.data;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.test.InstrumentationTestCase;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.PillowError;
import cat.my.android.pillow.data.core.IPillowResult;
import cat.my.android.pillow.data.rest.IRestMapping;
import cat.my.android.pillow.data.rest.RestDataSource;
import cat.my.android.pillow.data.rest.Route;
import cat.my.android.pillow.data.sync.ISynchDataSource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
		Thread.currentThread().sleep(2000);
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
