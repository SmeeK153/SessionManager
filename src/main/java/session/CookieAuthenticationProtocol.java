package session;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class CookieAuthenticationProtocol extends AuthenticationProtocol {

	public CookieAuthenticationProtocol(String authorizationLocale) throws MalformedURLException {
		this(new URL(authorizationLocale));
	}

	public CookieAuthenticationProtocol(URL authorizationLocale) {
		super(authorizationLocale);
	}

	void authorize(String username, String password, Boolean useKeystore) {
		this.credential = new Credential(username, password);
		if (this.connectionAuthorized(authenticationLocale, this.credential.getCookieCredentialsJSON(),
				(JSONObject) null) != null) {
			this.authenticationCache.put("Cookie", this.authorizationConnection.getCookie());
			System.out.println("Cookie authentication protocol successfully authenticated.");
			this.useKeystore(useKeystore);
		} else {
			System.out.println("Cookie authentication protocol could not be authenticated.");
		}
	}

}
