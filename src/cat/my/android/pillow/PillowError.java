package cat.my.android.pillow;

import java.io.PrintStream;
import java.io.PrintWriter;

import com.android.volley.VolleyError;

@SuppressWarnings("serial")
public class PillowError extends Exception{

	public PillowError() {
		super();
	}

	public PillowError(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public PillowError(String detailMessage) {
		super(detailMessage);
	}

	public PillowError(Throwable throwable) {
		super(throwable);
	}
	
}
