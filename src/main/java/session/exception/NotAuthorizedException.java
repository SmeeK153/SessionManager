package session.exception;

public class NotAuthorizedException extends HTTPStatusCodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8919010031557348665L;
	
	public NotAuthorizedException(){
		super("Error Code 401");
	}
	
	public NotAuthorizedException(String message){
		super("Error Code 401: " + message);
	}
}
