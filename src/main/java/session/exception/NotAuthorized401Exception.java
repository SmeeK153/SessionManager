package session.exception;

public class NotAuthorized401Exception extends HTTPStatusCodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8919010031557348665L;
	
	public NotAuthorized401Exception(){
		super(401,"Unauthorized");
	}
	
	public NotAuthorized401Exception(String message){
		super(401,"Unauthorized",message);
	}
}
