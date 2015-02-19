package cat.my.android.pillow;

import java.util.Collection;

import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.data.core.IPillowResult;



public interface IDataSource <T extends IdentificableModel>{
	/**
	 * Returns all the instances
	 * @param listener to be executed when the instances have been retrieved
	 * @param errorListener to be executed in case of error
	 */
	public IPillowResult<Collection<T>> index();

	public IPillowResult<T> show(T model);
	
	public IPillowResult<T> create(T model);
	
	public IPillowResult<T> update(T model);
	
	public IPillowResult<Void> destroy(T model);
}
