package cat.my.android.pillow;

import java.io.PrintStream;
import java.io.PrintWriter;

import com.android.volley.VolleyError;

public class PillowError {
	
	Exception exception;
	
	public PillowError(Exception exception){
		this.exception = exception;
	}

	/*public PillowError(VolleyError volleyError) {
		super();
		this.volleyError = volleyError;
	}*/

	public Throwable fillInStackTrace() {
		return exception.fillInStackTrace();
	}

	public String getMessage() {
		return exception.getMessage();
	}

	public String getLocalizedMessage() {
		return exception.getLocalizedMessage();
	}

	public StackTraceElement[] getStackTrace() {
		return exception.getStackTrace();
	}

	public void printStackTrace() {
		exception.printStackTrace();
	}

	public void printStackTrace(PrintStream err) {
		exception.printStackTrace(err);
	}

	public void printStackTrace(PrintWriter err) {
		exception.printStackTrace(err);
	}

	public String toString() {
		return exception.toString();
	}

	public Throwable getCause() {
		return exception.getCause();
	}

	public Exception getException() {
		return exception;
	}
}
