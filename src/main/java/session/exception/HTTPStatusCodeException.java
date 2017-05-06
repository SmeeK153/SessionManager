package session.exception;

import java.io.IOException;
import java.io.InputStream;

import core.StreamBuffer;

public class HTTPStatusCodeException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6407976154995770807L;
	
	public HTTPStatusCodeException(){
		super();
	}
	
	public HTTPStatusCodeException(String message){
		super(message);
	}
}
