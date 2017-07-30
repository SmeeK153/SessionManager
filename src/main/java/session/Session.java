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
import session.exception.*;
import session.request.ConnectionRequest;

public final class Session implements HTTPConnectionHandler {

	/**
	 * Provides access to create BasicAuthenticationProtocol object from Session
	 * directly
	 * 
	 * @param String
	 *            authorization locale for authentication the Session via the
	 *            protocol
	 * @return Authorization protocol for basic-based authentication
	 * @throws MalformedURLException
	 */
	public static BasicAuthenticationProtocol BasicAuthenticationProtocol(String authorizationLocale)
			throws MalformedURLException {
		return Session.BasicAuthenticationProtocol(new URL(authorizationLocale));
	}

	/**
	 * Provides access to create BasicAuthenticationProtocol object from Session
	 * directly
	 * 
	 * @param String
	 *            authorization locale for authentication the Session via the
	 *            protocol
	 * @return Authorization protocol for basic-based authentication
	 */
	public static BasicAuthenticationProtocol BasicAuthenticationProtocol(URL authorizationLocale) {
		return new BasicAuthenticationProtocol(authorizationLocale);
	}

	/**
	 * Provides access to create CookieAuthenticationProtocol object from Session
	 * directly
	 * 
	 * @param String
	 *            authorization locale for authentication the Session via the
	 *            protocol
	 * @return Authorization protocol for cookie-based authentication
	 * @throws MalformedURLException
	 */
	public static CookieAuthenticationProtocol CookieAuthenticationProtocol(String authorizationLocale)
			throws MalformedURLException {
		return Session.CookieAuthenticationProtocol(new URL(authorizationLocale));
	}

	/**
	 * Provides access to create CookieAuthenticationProtocol object from Session
	 * directly
	 * 
	 * @param String
	 *            authorization locale for authentication the Session via the
	 *            protocol
	 * @return Authorization protocol for cookie-based authentication
	 */
	public static CookieAuthenticationProtocol CookieAuthenticationProtocol(URL authorizationLocale) {
		return new CookieAuthenticationProtocol(authorizationLocale);
	}

	private AuthenticationProtocol authenticationProtocol = null;

	public Boolean isAuthenticated() {
		return (this.authenticationProtocol != null && this.authenticationProtocol.isAuthenticated());
	}

	public void deAuthenticate() {
		this.authenticationProtocol = null;
	}

	public Boolean authorize(AuthenticationProtocol protocol) {
		this.authenticationProtocol = protocol;
		return this.isAuthenticated();
	}

	private JSONObject getRequestHeaders() {
		if (this.authenticationProtocol != null) {
			return this.authenticationProtocol.getAuthenticationCache();
		} else {
			return null;
		}
	}

	private Keystore getRequestKeystore() {
		if (this.authenticationProtocol != null) {
			return this.authenticationProtocol.getKeystore();
		} else {
			return null;
		}
	}
	
	/**
	 * Provides a simplified connection request to the desired resource. Useful for wrapping REST
	 * calls in a more verbose manner.
	 * 
	 * @param method
	 * @param url
	 * @return
	 */
	public ConnectionRequest makeRequest(SessionConnection.REQUEST_METHOD method, URL url) {
		return new ConnectionRequest(this, method, url);
	}

	// Add exceptions to throw if the connection needs authentication parameter
	// or an SSL certificate
	/**
	 * Provides a connection to the desired resource.
	 * 
	 * @param url
	 * @param requestData
	 * @return
	 * @throws IOException
	 */
	public SessionConnection getConnection(URL url, JSONObject requestData) throws IOException {
		return this.getConnection(url, requestData, SessionConnection.REQUEST_METHOD.GET);
	}
	
	/**
	 * Provides a connection to the desired resource.
	 * 
	 * @param url
	 * @param requestData
	 * @param requestMethod
	 * @return
	 * @throws IOException
	 */
	public SessionConnection getConnection(URL url, JSONObject requestData,
			SessionConnection.REQUEST_METHOD requestMethod) throws IOException {
		return new SessionConnection(url, requestData, this.getRequestHeaders(), requestMethod,
				this.getRequestKeystore());
	}
}
