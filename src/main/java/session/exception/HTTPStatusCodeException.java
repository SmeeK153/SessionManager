package session.exception;

import java.io.IOException;
import java.io.InputStream;

import core.StreamBuffer;

public abstract class HTTPStatusCodeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6407976154995770807L;

	public HTTPStatusCodeException() {
		super();
	}

	public HTTPStatusCodeException(String message) {
		super(message);
	}

	public HTTPStatusCodeException(Integer errorNumber, String errorContext) {
		super("Error Number " + errorNumber + ": " + errorContext);
	}
	
	public HTTPStatusCodeException(Integer errorNumber, String errorContext, String message) {
		super("Error Number " + errorNumber + ": " + errorContext + " - " + message);
	}
}
