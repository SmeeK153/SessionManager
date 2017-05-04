package session;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import keystore.Keystore;

public final class SecureSession {

	private Keystore keystore = null;
	private BasicAuthentication credentials = null;

	/**
	 * Create a new session with the default keystore.
	 * 
	 */
	protected SecureSession() {
		this((BasicAuthentication) null, (Keystore) null);
	};

	/**
	 * Create a new session with the provided credentials and the default
	 * keystore.
	 * 
	 * @param credentials
	 */
	protected SecureSession(BasicAuthentication credentials) {
		this(credentials, (Keystore) null);
	}
	
	/**
	 * Create a new session with the provided keystore.
	 * 
	 * @param keystore
	 */
	protected SecureSession(Keystore keystore){
		this((BasicAuthentication) null, keystore);
	}
	
	/**
	 * Provides a connection to the desired resource with the provided
	 * credentials and keystore.
	 * 
	 * @param credentials
	 * @param keystore
	 */
	protected SecureSession(BasicAuthentication credentials, Keystore keystore) {
		this.credentials = credentials;
		this.keystore = keystore;
	}

	// Add exceptions to throw if the connection needs authentication parameter
	// or an SSL certificate
	/**
	 * Provides a connection to the desired resource with the provided
	 * credentials and keystore.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws SSLHandshakeException
	 */
	public SecureSessionConnection getSecureConnection(URL url) throws IOException, SSLHandshakeException {
		return new SecureSessionConnection(url, this.credentials, this.keystore);
	}

	/**
	 * Provides a connection with the associated security resources.
	 * 
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SSLHandshakeException
	 */
	public SecureSessionConnection getSecureConnection(String url)
			throws MalformedURLException, IOException, SSLHandshakeException {
		return getSecureConnection(new URL(url));
	}
}
