package com.mateuyabar.util.exceptions;

/**
 * To use only on develop phase.
 * Indicates that this feature has to be implemented yet.
 */
public class ToImplementException extends UnimplementedException{
	private static final long serialVersionUID = 6570516847121776681L;

	public ToImplementException() {
		super();
	}

	public ToImplementException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ToImplementException(String detailMessage) {
		super(detailMessage);
	}

	public ToImplementException(Throwable throwable) {
		super(throwable);
	}

}
