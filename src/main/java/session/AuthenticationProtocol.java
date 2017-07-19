package session;

import java.io.IOException;
import java.net.URL;

import org.json.JSONObject;

import keystore.Keystore;
import session.exception.NotAuthorized401Exception;

public abstract class AuthenticationProtocol {

	protected Keystore keystore = null;
	private Boolean authenticated = false;
	protected Credential credential = null;
	protected JSONObject authenticationCache = new JSONObject();
	protected URL authenticationLocale = null;
	protected SessionConnection authorizationConnection = null;
	
	public AuthenticationProtocol(URL authenticationLocale) {
		this.authenticationLocale = authenticationLocale;
	}
	
	protected Boolean isAuthenticated() {
		return this.authenticated;
	}

	protected void deAuthenticate() {
		this.authenticated = false;
		this.credential = null;
		this.authenticationCache = new JSONObject();
		this.authenticationLocale = null;
		this.useKeystore(false);
		this.authorizationConnection = null;
		System.out.println("Credential and authentication cache have been cleared.");
	}
	
	protected Keystore getKeystore() {
		return this.keystore;
	}
	
	protected JSONObject getAuthenticationCache() {
		return this.authenticationCache;
	}

	protected Boolean connectionAuthorized(URL authorizationLocale, JSONObject authorizationRequestBody,
			JSONObject authorizationRequestHeaders) {
		try {
			this.authorizationConnection = new SessionConnection(authorizationLocale, authorizationRequestBody, authorizationRequestHeaders);
			switch (this.authorizationConnection.getServerResponseCode()) {
			case 200:
				return true;
			case 401:
				throw new NotAuthorized401Exception("Credentials were invalid.");
			default:
				System.err.println("Couldn't retrieve remote resource, received error code "
						+ this.authorizationConnection.getServerResponseVerboseMessage() + ".");
				return false;
			}
		} catch (IOException e) {
			System.err.println("There was a problem with the authorization locale (" + this.authenticationLocale.toString() + ") authorization parameters.");
			e.printStackTrace();
			return false;
		}
	}
	
	public void useKeystore(Boolean useKeystore) {
		if (useKeystore) {
			this.keystore = Keystore.DEFAULT_KEYSTORE;
		} else {
			this.keystore = null;
		}
	}
	
	abstract void authorize(String username, String password, Boolean useKeystore);
}
