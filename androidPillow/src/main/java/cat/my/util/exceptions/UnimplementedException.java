package cat.my.util.exceptions;

public class UnimplementedException extends RuntimeException {

	private static final long serialVersionUID = -67949479340253788L;

	public UnimplementedException() {
		super();
	}

	public UnimplementedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public UnimplementedException(String detailMessage) {
		super(detailMessage);
	}

	public UnimplementedException(Throwable throwable) {
		super(throwable);
	}

}
