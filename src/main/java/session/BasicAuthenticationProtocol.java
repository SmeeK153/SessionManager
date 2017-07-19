package session;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class BasicAuthenticationProtocol extends AuthenticationProtocol {
	
	public BasicAuthenticationProtocol(String authorizationLocale) throws MalformedURLException {
		this(new URL(authorizationLocale));
	}
	
	public BasicAuthenticationProtocol(URL authorizationLocale) {
		super(authorizationLocale);
	}

	public void authorize(String username, String password, Boolean useKeystore) {
		this.credential = new Credential(username, password);
		if (this.connectionAuthorized(authenticationLocale, (JSONObject) null,
				this.credential.getBasicCredentialsJSON())) {
			this.authenticationCache.put("Authorization",
					this.credential.getBasicCredentialsJSON().get("Authorization"));
			System.out.println("Basic authentication protocol successfully authenticated.");
			this.useKeystore(useKeystore);
		} else {
			System.out.println("Basic authentication protocol could not be authenticated.");
		}
	}
}