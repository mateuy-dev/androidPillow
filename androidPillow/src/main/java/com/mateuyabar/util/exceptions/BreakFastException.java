package com.mateuyabar.util.exceptions;

public class BreakFastException extends RuntimeException {

	private static final long serialVersionUID = -5956881676211995975L;

	public BreakFastException() {
		super();
	}

	public BreakFastException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public BreakFastException(String detailMessage) {
		super(detailMessage);
	}

	public BreakFastException(Throwable throwable) {
		super(throwable);
	}

}
