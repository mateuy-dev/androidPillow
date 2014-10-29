package cat.my.android.restvolley.listeners;

import java.util.HashSet;
import java.util.Set;

public class EventDispatcher<T> {
	Set<IModelUpdatedListener<T>> updateListeners = new HashSet<IModelUpdatedListener<T>>();
	
	public void addModelUpdatedLister(IModelUpdatedListener<T> listener){
		updateListeners.add(listener);
	}
	
	public void removeModelUpdatedListener(IModelUpdatedListener<T> listener){
		updateListeners.remove(listener);
	}
	
	public void notifyModelUpdate(T model){
		for(IModelUpdatedListener<T> listener: updateListeners){
			listener.onModelUpdated(model);
		}
	}
}
