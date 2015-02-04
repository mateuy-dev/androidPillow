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
import cat.my.android.pillow.data.rest.IRestMapping;
import cat.my.android.pillow.data.rest.RailsRestMapping;
import cat.my.android.pillow.data.rest.Route;
import cat.my.android.pillow.data.sync.AsynchListener;
import cat.my.android.pillow.data.sync.ISynchDataSource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public abstract class AbstractInstrumentationTestCase<T extends IdentificableModel> extends InstrumentationTestCase{
	Class<T> clazz;
	
	public AbstractInstrumentationTestCase(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}
	
	public void testController(){
		assertNotNull(getController());
	}
	
	/**
	 * Test Create, show and delete operation local and remote
	 */
	public void testBasicOperations() throws Throwable {
		//create post
		ISynchDataSource<T> controller = getController();
		
		T createdPost = createDBModel();
		assertNotNull(createdPost);
		assertNotNull(createdPost.getId());
		
		//check create
		checkLocalVersionSameAs(createdPost);
		checkServerVersionSameAs(createdPost);
		
		//update
		AsynchListener<T> listener = new AsynchListener<T>();
		updateModel(createdPost);
//		createdPost.setTitle("new Title");
		controller.update(createdPost, listener, listener);
		T updatedPost = listener.getResult();
		assertSame(createdPost, updatedPost);
		
		//check update
		checkLocalVersionSameAs(updatedPost);
		checkServerVersionSameAs(updatedPost);

		//delete post
		AsynchListener<Void> deleteListener = new AsynchListener<Void>();
		controller.destroy(createdPost, deleteListener, deleteListener);
		deleteListener.await();
		
		//check deleted locally
		listener = new AsynchListener<T>();
		controller.show(createdPost, listener, listener);
		T showPost = listener.getResult();
		assertNull(showPost);
		
		//check deleted remotelly
		sendDirty();
		HttpResponse response = getServerResponse(createdPost);
		assertEquals(response.getStatusLine().getStatusCode(), 404);
	}

	protected abstract void updateModel(T toUpdateModel);
	protected abstract T createDBModel() throws Exception;

	protected void checkLocalVersionSameAs(T model) throws Exception{
		AsynchListener<T> listener = new AsynchListener<T>();
		getController().show(model, listener, listener);
		T showModel = listener.getResult();
		assertNotNull(showModel);
		assertSame(model, showModel);
	}
	
	protected void checkServerVersionSameAs(T model) throws Exception{
		sendDirty();
		AsynchListener<T> listener = new AsynchListener<T>();
		getController().getRestDataSource().show(model, listener, listener);
		T showModel = listener.getResult();
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
	
	
	protected void sendDirty() throws InterruptedException{
		AsynchListener<Void> dirtyListener = new AsynchListener<Void>();
		getController().sendDirty(dirtyListener, dirtyListener);
		dirtyListener.await();
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

	
}
