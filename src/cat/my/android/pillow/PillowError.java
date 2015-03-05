package cat.my.android.pillow;

import cat.my.util.exceptions.BreakFastException;


@SuppressWarnings("serial")
public class PillowError extends Exception{

	public PillowError() {
		super();
	}

	public PillowError(String detailMessage, Throwable throwable) {
		super(detailMessage, getRealException(throwable));
	}

	public PillowError(String detailMessage) {
		super(detailMessage);
	}

	public PillowError(Throwable throwable) {
		super(getRealException(throwable));
	}
	
	private static Throwable getRealException(Throwable throwable){
		//We don't want to encapsulate exceptions in more than one lever of PillowError
		if(throwable instanceof PillowError){
			new BreakFastException("CHECK THIS");
		}
		return throwable;
	}
	
}
