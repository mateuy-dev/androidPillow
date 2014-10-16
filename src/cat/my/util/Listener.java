package cat.my.util;

/**
 * General porpose Listener
 */
public interface Listener<T> {
	/** Called when a response is received. */
	public void onResponse(T response);
}
