package session;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;

import keystore.Keystore;

public class SessionFactory {

	/**
	 * This class is meant to generate startSessionions from a static perspective,
	 * therefore, it cannot be instantiated.
	 */
	private SessionFactory() {}

	/**
	 * startSession an un-authenticated session.
	 * @return 
	 */
	public static Session startSession() {
		return new Session();
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * the default keystore.
	 * 
	 * @param credentials
	 *            Credential object for authenticating with source
	 * @return 
	 */
	private static SecureSession startSession(BasicAuthentication credentials) {
		return startSession(credentials, Keystore.DEFAULT_KEYSTORE);
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * the default keystore.
	 * 
	 * @param password
	 * @return 
	 */
	public static SecureSession startSession(String password) {
		return startSession(new BasicAuthentication(password));
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * the default keystore.
	 * 
	 * @param usernname
	 * @param password
	 * @return 
	 */
	public static SecureSession startSession(String usernname, String password) {
		return startSession(new BasicAuthentication(usernname, password));
	}
	
	/*
	 * This is the final destination of all startSession() calls.
	 */
	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * a specified keystore.
	 * 
	 * @param credentials
	 *            Credential object for authenticating with source
	 * @param keystore
	 * @return 
	 */
	private static SecureSession startSession(BasicAuthentication credentials, Keystore keystore) {
		return new SecureSession(credentials,keystore);
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * a specified keystore.
	 * 
	 * @param username
	 * @param password
	 * @param keystore
	 * @return 
	 */
	public static SecureSession startSession(String username, String password, Keystore keystore) {
		return startSession(new BasicAuthentication(username, password), keystore);
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * a specified keystore.
	 * 
	 * @param password
	 * @param keystore
	 */
	public static SecureSession startSession(String password, Keystore keystore) {
		return startSession(new BasicAuthentication(password), keystore);
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * a specified keystore.
	 * 
	 * @param credentials
	 *            Credential object for authenticating with source
	 * @param keystore
	 */
	private static SecureSession startSession(BasicAuthentication credentials, File keystore) {
		return startSession(credentials, new Keystore(keystore));
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * a specified keystore.
	 * 
	 * @param username
	 * @param password
	 * @param keystore
	 */
	public static SecureSession startSession(String username, String password, File keystore) {
		return startSession(new BasicAuthentication(username, password), keystore);
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * a specified keystore.
	 * 
	 * @param password
	 * @param keystore
	 */
	public static SecureSession startSession(String password, File keystore) {
		return startSession(new BasicAuthentication(password), keystore);
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * the default keystore and a new certificate.
	 * 
	 * @param credentials
	 *            Credential object for authenticating with source
	 * @param certificate
	 * @param alias
	 */
	private static SecureSession startSession(BasicAuthentication credentials, File certificate, String alias) {
		return startSession(credentials, Keystore.DEFAULT_KEYSTORE, certificate, alias);
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * the default keystore and a new certificate.
	 * 
	 * @param username
	 * @param password
	 * @param certificate
	 * @param alias
	 */
	public static SecureSession startSession(String username, String password, File certificate, String alias) {
		return startSession(new BasicAuthentication(username, password), certificate, alias);
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * the default keystore and a new certificate.
	 * 
	 * @param password
	 * @param certificate
	 * @param alias
	 */
	public static SecureSession startSession(String password, File certificate, String alias) {
		return startSession(new BasicAuthentication(password), certificate, alias);
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * a specified keystore and new certificate.
	 * 
	 * @param credentials
	 *            Credential object for authenticating with source
	 * @param keystore
	 * @param certificate
	 * @param alias
	 * @return 
	 */
	private static SecureSession startSession(BasicAuthentication credentials, Keystore keystore, File certificate, String alias) {
		try {
			keystore.addCertificate(certificate, alias);
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			System.err.println("The system failed to add the submitted certificate to the desigated alias in the desired keystore. Continuing without it.");
			e.printStackTrace();
		}
		return startSession(credentials, keystore);
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * a specified keystore and new certificate.
	 * 
	 * @param credentials
	 * @param keystore
	 * @param certificate
	 * @param alias
	 */
	public static SecureSession startSession(String username, String password, Keystore keystore, File certificate, String alias) {
		return startSession(new BasicAuthentication(username, password), keystore, certificate, alias);
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * a specified keystore and new certificate.
	 * 
	 * @param credentials
	 * @param keystore
	 * @param certificate
	 * @param alias
	 */
	public static SecureSession startSession(String password, Keystore keystore, File certificate, String alias) {
		return startSession(new BasicAuthentication(password),keystore,certificate,alias);
	}

	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * a specified keystore and new certificate.
	 * 
	 * @param credentials
	 *            Credential object for authenticating with source
	 * @param keystore
	 * @param certificate
	 * @param alias
	 */
	private static SecureSession startSession(BasicAuthentication credentials, File keystore, File certificate, String alias) {
		return startSession(credentials, new Keystore(keystore), certificate, alias);
	}
	
	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * a specified keystore and new certificate.
	 * 
	 * @param username
	 * @param password
	 * @param keystore
	 * @param certificate
	 * @param alias
	 */
	public static SecureSession startSession(String username, String password, File keystore, File certificate, String alias){
		return startSession(new BasicAuthentication(username,password),keystore,certificate,alias);
	}
	
	/**
	 * startSession an authenticated session with username and password in conjunction with
	 * a specified keystore and new certificate.
	 * 
	 * @param password
	 * @param keystore
	 * @param certificate
	 * @param alias
	 */
	public static SecureSession startSession(String password, File keystore, File certificate, String alias){
		return startSession(new BasicAuthentication(password),keystore,certificate,alias);
	}

	public static void main(String...args) throws MalformedURLException, IOException{
		Session session = SessionFactory.startSession();
		SessionConnection conn = session.getConnection("https://jira.atlassian.com/rest/api/latest/issue/JRACLOUD-62622");
		conn.printResponse();
	}
}
