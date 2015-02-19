package cat.my.android.pillow.test.data;

import android.content.Context;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.data.sync.AsynchListener;
import cat.my.android.pillow.data.sync.ISynchDataSource;

public class AsynchOps {
	Context context;
	Pillow pillow;
	
	public AsynchOps(Context context){
		this.context = context;
		this.pillow = Pillow.getInstance(context);
	}

	public <T extends IdentificableModel> AsynchListener<T> createModel(T model) throws InterruptedException{
		AsynchListener<T> postListener = new AsynchListener<T>();
		((ISynchDataSource<T>)pillow.getDataSource(model.getClass())).create(model, postListener, postListener);
		postListener.await();
		return postListener;
	}
	
	public <T extends IdentificableModel> AsynchListener<T> updateModel(T model) throws InterruptedException{
		AsynchListener<T> postListener = new AsynchListener<T>();
		((ISynchDataSource<T>)pillow.getDataSource(model.getClass())).update(model, postListener, postListener);
		postListener.await();
		return postListener;
	}
	
	public AsynchListener<Void> synchronize() throws InterruptedException{
		AsynchListener<Void> listener = new AsynchListener<Void>();
		pillow.getSynchManager().synchronize(listener, listener, true);
		listener.await();
		return listener;
	}
}
