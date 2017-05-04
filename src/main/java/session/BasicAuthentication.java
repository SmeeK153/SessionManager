package session;

import org.apache.commons.codec.binary.Base64;

public class BasicAuthentication {
	
	private String encodedAuthenticationString = "";
	
	protected BasicAuthentication(String password){
		this(System.getProperty("user.name"),password);
	}

	protected BasicAuthentication(String username, String password){
		String usernamePasswordCombination = username + ":" + password;
		byte[] encodedBytes = Base64.encodeBase64(usernamePasswordCombination.getBytes());
		this.encodedAuthenticationString = new String(encodedBytes);
	}
	
	protected boolean matchesUsername(String username){
		byte[] decodedBytes = Base64.decodeBase64(this.encodedAuthenticationString.getBytes());
		String decodedUsername = new String(decodedBytes);
		return (decodedUsername.split(":")[0].equals(username));
	}
	
	protected String getAuthenticationParameter(){
		return "Basic " + this.encodedAuthenticationString;
	}
	
	public static void main(String...args){
		BasicAuthentication basic = new BasicAuthentication("username","password");
		System.out.println("username:password is encoded correctly: " + basic.getAuthenticationParameter().equals("Basic dXNlcm5hbWU6cGFzc3dvcmQ="));
	}
}
