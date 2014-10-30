package cat.my.android.restvolley;

import java.io.PrintStream;
import java.io.PrintWriter;

import com.android.volley.VolleyError;

public class RestVolleyError {
	VolleyError volleyError;

	public RestVolleyError(VolleyError volleyError) {
		super();
		this.volleyError = volleyError;
	}

	public Throwable fillInStackTrace() {
		return volleyError.fillInStackTrace();
	}

	public String getMessage() {
		return volleyError.getMessage();
	}

	public String getLocalizedMessage() {
		return volleyError.getLocalizedMessage();
	}

	public StackTraceElement[] getStackTrace() {
		return volleyError.getStackTrace();
	}

	public void printStackTrace() {
		volleyError.printStackTrace();
	}

	public void printStackTrace(PrintStream err) {
		volleyError.printStackTrace(err);
	}

	public void printStackTrace(PrintWriter err) {
		volleyError.printStackTrace(err);
	}

	public String toString() {
		return volleyError.toString();
	}

	public Throwable getCause() {
		return volleyError.getCause();
	}

	public VolleyError getVolleyError() {
		return volleyError;
	}
}
