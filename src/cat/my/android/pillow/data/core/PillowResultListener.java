package cat.my.android.pillow.data.core;

import android.content.Context;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.PillowError;

public class PillowResultListener<T> extends PillowResult<T> implements Listener<T>, ErrorListener{

	@Override
	public void onResponse(T response) {
		setResult(response);
	}

	@Override
	public void onErrorResponse(PillowError error) {
		setError(error);
	}

	public PillowResultListener(Context context, Exception exception) {
		super(context, exception);

	}

	public PillowResultListener(Context context, PillowError error) {
		super(context, error);

	}

	public PillowResultListener(Context context, T result) {
		super(context, result);

	}

	public PillowResultListener(Context context) {
		super(context);

	}

	
	

}
