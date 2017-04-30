package session;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import keystore.Keystore;


public final class Session {

	/**
	 * Create a session without credentials or a keystore.
	 */
	protected Session(){};

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
		return new SessionConnection(url);
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
