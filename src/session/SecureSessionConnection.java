package session;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import keystore.Keystore;

public class SecureSessionConnection extends SessionConnection {
	
	private BasicAuthentication credentials;
	private Keystore keystore;
	
	protected SecureSessionConnection(URL url, BasicAuthentication credentials, Keystore keystore) {
		super(url);
		this.credentials = credentials;
		this.keystore = keystore;
	}
	
	/**
	 * Creates an authenticated HTTPS connection with the provided keystore
	 * 
	 * @return
	 * @throws IOException
	 */
	private HttpsURLConnection getNewConnection() throws IOException{
		HttpsURLConnection connection = (HttpsURLConnection) this.url.openConnection();
		
		connection.setRequestProperty("Authorization", credentials.getAuthenticationParameter());
		connection.setSSLSocketFactory(keystore.getSSLSocketFactory());
		
		return connection;
	}
}
