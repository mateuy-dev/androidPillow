package cat.my.android.pillow.test.data;

import org.apache.http.HttpResponse;

import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.data.sync.ISynchDataSource;

public abstract class  AbstractIdentificableModelTestCase<T extends IdentificableModel> extends AbstractInstrumentationTestCase<T>{
	int a;
	
	public AbstractIdentificableModelTestCase(Class<T> clazz) {
		super(clazz);
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
		
		updateModel(createdPost);
//		createdPost.setTitle("new Title");
		T updatedPost = controller.update(createdPost).getResult();
		assertSame(createdPost, updatedPost);
		
		//check update
		checkLocalVersionSameAs(updatedPost);
		checkServerVersionSameAs(updatedPost);

		//delete post
		controller.destroy(createdPost).getResult();
		
		//check deleted locally
		T showPost = controller.show(createdPost).getResult();
		assertNull(showPost);
		
		//check deleted remotelly
		sendDirty();
		HttpResponse response = getServerResponse(createdPost);
		assertEquals(response.getStatusLine().getStatusCode(), 404);
	}
	
	protected abstract void updateModel(T toUpdateModel);
	protected abstract T createDBModel() throws Exception;

}
