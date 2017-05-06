package session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import core.StreamBuffer;
import keystore.Keystore;
import session.exception.NotAuthorizedException;

public final class Session {

	private Keystore keystore = null;
	private Credential credential = null;
	private PROTOCOL authenticationProtocol = null;
	private Boolean authenticated = false;
	private JSONObject cache = new JSONObject();

	public enum PROTOCOL {
		BASIC, COOKIE
	}

	/**
	 * Create a new session for connecting to remote resources.
	 */
	public Session() {

	};

	public Boolean isAuthenticated() {
		return (this.authenticationProtocol != null);
	}

	public void deAuthenticate() {
		this.credential = null;
		this.authenticationProtocol = null;
		System.out.println("Removed authentication resources.");
		if(this.cache.remove("Cookie") != null){
			System.out.println("Cookie based authentication credentials removed from cache.");
		}
		if(this.cache.remove("Authorization") != null){
			System.out.println("Basic authoriZation credentials removed from cache.");
		}
	}

	public void setUseKeystore(Boolean useKeystore) {
		if(useKeystore){
			this.keystore = Keystore.DEFAULT_KEYSTORE;
		} else {
			this.keystore = null;
		}
	}

	public Boolean authenticate(String username, String password, String authenticationURL,
			PROTOCOL authenticationProtocol) throws MalformedURLException {
		return this.authenticate(username, password, new URL(authenticationURL), authenticationProtocol);
	}

	public Boolean authenticate(String username, String password, URL authenticationURL,
			PROTOCOL authenticationProtocol) {
		this.deAuthenticate();
		this.authenticationProtocol = authenticationProtocol;
		this.credential = new Credential(username, password);

		switch (this.authenticationProtocol) {
		case BASIC:
			this.authorizeWithBasic(authenticationURL);
			break;
		case COOKIE:
			this.authorizeWithCookie(authenticationURL);
			break;
		}
		return this.authenticated;
	}
	
	private void authorizeWithBasic(URL authorizationResource){
		this.deAuthenticate();
		try {
			SessionConnection authenticationConnection = new SessionConnection(authorizationResource, (JSONObject) null, this.credential.getBasicCredentialsJSON());
			if(this.authenticated = this.canContinueWithConnection(authenticationConnection)){
				this.cache.put("Authorization", this.credential.getBasicCredentialsJSON().get("Authorization"));
				System.out.println("Authenticated with Basic protocol");
			} else {
				System.err.println("Failed to authenticate via Basic protocol: Credentials were not accepted.");
			}
		} catch (IOException e) {
			System.err.println("Failed to authenicate via Basic protocol: Couldn't establish connection.");
			e.printStackTrace();
			return;
		}
	}
	
	private void authorizeWithCookie(URL authorizationResource) {
		try {
			SessionConnection authenticationConnection = new SessionConnection(authorizationResource, this.credential.getOAuth1CredentialsJSON(), (JSONObject) null);
			if(this.authenticated = this.canContinueWithConnection(authenticationConnection)){
				this.cache.put("Cookie", new String(authenticationConnection.getCookie()));
				System.out.println("Authenticated with Cookie protocol");
			} else {
				System.err.println("Failed to authenticate via Cookie protocol: Credentials were not accepted.");
			}
		} catch (IOException e) {
			System.err.println("Failed to authenicate via Cookie protocol: Couldn't establish connection.");
			e.printStackTrace();
			return;
		}
	}

	private Boolean canContinueWithConnection(SessionConnection connection) {
		switch (connection.getServerResponseCode()) {
		case 200:
			return true;
		case 401:
			throw new NotAuthorizedException("Credentials were invalid.");
		default:
			System.err.println("Couldn't retrieve remote resource, received error code "
					+ connection.getServerResponseVerboseMessage() + ".");
			return false;
		}
	}

	// Add exceptions to throw if the connection needs authentication parameter
	// or an SSL certificate
	/**
	 * Provides a connection to the desired resource.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public SessionConnection getConnection(URL url) throws IOException {
		return new SessionConnection(url, (JSONObject) null, this.cache, (Keystore) null);
	}

	/**
	 * Provides a connection to the desired resource.
	 * 
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public SessionConnection getConnection(String url) throws MalformedURLException, IOException {
		return this.getConnection(new URL(url));
	}

}
