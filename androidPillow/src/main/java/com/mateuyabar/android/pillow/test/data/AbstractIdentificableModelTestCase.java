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

import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.data.sync.ISynchDataSource;

import org.apache.http.HttpResponse;

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
