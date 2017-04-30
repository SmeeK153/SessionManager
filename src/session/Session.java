package session;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import keystore.Keystore;


public final class Session {

	private Keystore keystore = null;
	private HttpsURLConnection connection = null;
	private BasicAuthentication credentials = null;
	
	/**
	 * Create a new session without credentials, and with the defaul keystore.
	 */
	private Session(){
		this.credentials = null;
		this.keystore = Keystore.DEFAULT_KEYSTORE;
	};
	
	/**
	 * Create a new session with the provided credentials and keystore to use.
	 * 
	 * @param credentials
	 * @param keystore
	 */
	protected Session(BasicAuthentication credentials, Keystore keystore) {
		this.credentials = credentials;
		this.keystore = keystore;
	}

	// Add exceptions to throw if the connection needs authentication parameter
	// or an SSL certificate
	public URLConnection getConnection(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		return connection;
	}

	public URLConnection getConnection(String url) throws MalformedURLException, IOException {
		return this.getConnection(new URL(url));
	}

	// Add exceptions to throw if the connection needs authentication parameter
	// or an SSL certificate
	public HttpsURLConnection getSecureConnection(URL url) throws IOException {

		// Make sure that a keystore is selected regardless
		if (this.keystore == null) {
			this.keystore = Keystore.DEFAULT_KEYSTORE;
		}

		HttpsURLConnection connection = (HttpsURLConnection) this.getConnection(url);
		connection.setSSLSocketFactory(this.keystore.getSSLSocketFactory());
		connection.setRequestProperty("Authorization", this.credentials.getAuthenticationParameter());
		return connection;
	}

	public HttpsURLConnection getSecureConnection(String url) throws MalformedURLException, IOException {
		return this.getSecureConnection(new URL(url));
	}
}
