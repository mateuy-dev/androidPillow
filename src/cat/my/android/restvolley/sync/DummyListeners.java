package cat.my.android.restvolley.sync;

import android.content.Context;
import android.widget.Toast;

import cat.my.android.restvolley.Listeners.CollectionListener;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.RestVolleyError;

import com.android.volley.VolleyError;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DummyListeners {
	public static ErrorListener dummyErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(RestVolleyError error) {
			error.printStackTrace();
		}
	};
	public static Listener dummyListener = new Listener() {
		@Override
		public void onResponse(Object response) {
		}
	};
	
	public static class DummyToastListener<T> implements Listener<T>{
		String text;
		Context context;
		public DummyToastListener(Context context, String text) {
			this.context = context;
			this.text= text;
		}
		@Override
		public void onResponse(T response) {
			Toast.makeText(context, text, Toast.LENGTH_LONG).show();
		}
	}
		
	public static CollectionListener dummyLogCollectionListener = new CollectionListener(){
		@Override
		public void onResponse(Object response) {
			System.out.println(new Gson().toJson(response).toString());
		}
	};
	
	
	public static class ProxyListener<T, D> implements Listener<D>{
		Listener<T> listener;
		T response;
		public ProxyListener(Listener<T> listener, T response){
			this.listener = listener;
			this.response = response;
		}

		@Override
		public void onResponse(D dummyResponse) {
			listener.onResponse(response);
		}
		
	}

}
