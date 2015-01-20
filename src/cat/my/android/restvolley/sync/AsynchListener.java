package cat.my.android.restvolley.sync;

import java.util.concurrent.CountDownLatch;

import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.RestVolleyError;

public class AsynchListener<T> implements Listener<T>, ErrorListener{
	CountDownLatch lock;
	T result; 
	

	RestVolleyError error;
	
	public AsynchListener() {
		lock = new CountDownLatch(1);
	}
	
	@Override
	public void onResponse(T response) {
		this.result = response;
		lock.countDown();
	}

	@Override
	public void onErrorResponse(RestVolleyError error) {
		this.error = error;
		lock.countDown();
	}
	
	public void await() throws InterruptedException{
		lock.await();
	}
	
	public T getResult() throws InterruptedException {
		await();
		return result;
	}

	public RestVolleyError getError() throws InterruptedException {
		await();
		return error;
	}

}
