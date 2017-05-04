package session;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;

import keystore.Keystore;

public class SessionFactory {

	/**
	 * This class is meant to generate startSecureSessionions from a static perspective,
	 * therefore, it cannot be instantiated.
	 */
	private SessionFactory() {}

	/**
	 * Start an un-authenticated session.
	 * 
	 * @return Session
	 */
	public static Session startSession() {
		return new Session();
	}
	
	/**
	 * Start a secure session with the default keystore.
	 * 
	 * @return SecureSession
	 */
	public static SecureSession startSecureSession(){
		return startSecureSession(Keystore.DEFAULT_KEYSTORE);
	}
	
	/**
	 * Start a secure session with a provided keystore.
	 * 
	 * @param keystore
	 * @return SecureSession
	 */
	public static SecureSession startSecureSession(Keystore keystore){
		return new SecureSession(keystore);
	}
	
	/**
	 * Start a secure session with a provided keystore.
	 * 
	 * @param keystore
	 * @return SecureSession
	 */
	public static SecureSession startSecureSession(File keystore){
		return startSecureSession(new Keystore(keystore));
	}
	
	/**
	 * Start an authenticated session with username and password with the option of all using the default keystore.
	 * 
	 * @param credentials
	 *            Credential object for authenticating with source
	 * @return 
	 */
	private static SecureSession startSecureSession(BasicAuthentication credentials, Boolean useKeystore) {
		if(useKeystore){
			return startSecureSession(credentials, Keystore.DEFAULT_KEYSTORE);
		} else {
			return new SecureSession(credentials);
		}
	}

	/**
	 * Start an authenticated session with username and password with the option of all using the default keystore.
	 * 
	 * @param password
	 * @return 
	 */
	public static SecureSession startSecureSession(String password, Boolean useKeystore) {
		return startSecureSession(new BasicAuthentication(password), useKeystore);
	}

	/**
	 * Start an authenticated session with username and password with the option of all using the default keystore.
	 * 
	 * @param usernname
	 * @param password
	 * @return 
	 */
	public static SecureSession startSecureSession(String usernname, String password, Boolean useKeystore) {
		return startSecureSession(new BasicAuthentication(usernname, password),useKeystore);
	}
	
	/*
	 * This is the final destination of all startSecureSession() calls.
	 */
	/**
	 * Start an authenticated session with username and password in conjunction with
	 * a specified keystore.
	 * 
	 * @param credentials
	 *            Credential object for authenticating with source
	 * @param keystore
	 * @return 
	 */
	private static SecureSession startSecureSession(BasicAuthentication credentials, Keystore keystore) {
		return new SecureSession(credentials,keystore);
	}

	/**
	 * Start an authenticated session with username and password in conjunction with
	 * a specified keystore.
	 * 
	 * @param username
	 * @param password
	 * @param keystore
	 * @return 
	 */
	public static SecureSession startSecureSession(String username, String password, Keystore keystore) {
		return startSecureSession(new BasicAuthentication(username, password), keystore);
	}

	/**
	 * Start an authenticated session with username and password in conjunction with
	 * a specified keystore.
	 * 
	 * @param password
	 * @param keystore
	 */
	public static SecureSession startSecureSession(String password, Keystore keystore) {
		return startSecureSession(new BasicAuthentication(password), keystore);
	}

	/**
	 * Start an authenticated session with username and password in conjunction with
	 * a specified keystore.
	 * 
	 * @param credentials
	 *            Credential object for authenticating with source
	 * @param keystore
	 */
	private static SecureSession startSecureSession(BasicAuthentication credentials, File keystore) {
		return startSecureSession(credentials, new Keystore(keystore));
	}

	/**
	 * Start an authenticated session with username and password in conjunction with
	 * a specified keystore.
	 * 
	 * @param username
	 * @param password
	 * @param keystore
	 */
	public static SecureSession startSecureSession(String username, String password, File keystore) {
		return startSecureSession(new BasicAuthentication(username, password), keystore);
	}

	/**
	 * Start an authenticated session with username and password in conjunction with
	 * a specified keystore.
	 * 
	 * @param password
	 * @param keystore
	 */
	public static SecureSession startSecureSession(String password, File keystore) {
		return startSecureSession(new BasicAuthentication(password), keystore);
	}

	/**
	 * Start an authenticated session with username and password in conjunction with
	 * the default keystore and a new certificate.
	 * 
	 * @param credentials
	 *            Credential object for authenticating with source
	 * @param certificate
	 * @param alias
	 */
	private static SecureSession startSecureSession(BasicAuthentication credentials, File certificate, String alias) {
		return startSecureSession(credentials, Keystore.DEFAULT_KEYSTORE, certificate, alias);
	}

	/**
	 * Start an authenticated session with username and password in conjunction with
	 * the default keystore and a new certificate.
	 * 
	 * @param username
	 * @param password
	 * @param certificate
	 * @param alias
	 */
	public static SecureSession startSecureSession(String username, String password, File certificate, String alias) {
		return startSecureSession(new BasicAuthentication(username, password), certificate, alias);
	}

	/**
	 * Start an authenticated session with username and password in conjunction with
	 * the default keystore and a new certificate.
	 * 
	 * @param password
	 * @param certificate
	 * @param alias
	 */
	public static SecureSession startSecureSession(String password, File certificate, String alias) {
		return startSecureSession(new BasicAuthentication(password), certificate, alias);
	}

	/**
	 * Start an authenticated session with username and password in conjunction with
	 * a specified keystore and new certificate.
	 * 
	 * @param credentials
	 *            Credential object for authenticating with source
	 * @param keystore
	 * @param certificate
	 * @param alias
	 * @return 
	 */
	private static SecureSession startSecureSession(BasicAuthentication credentials, Keystore keystore, File certificate, String alias) {
		try {
			keystore.addCertificate(certificate, alias);
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			System.err.println("The system failed to add the submitted certificate to the desigated alias in the desired keystore. Continuing without it.");
			e.printStackTrace();
		}
		return startSecureSession(credentials, keystore);
	}

	/**
	 * Start an authenticated session with username and password in conjunction with
	 * a specified keystore and new certificate.
	 * 
	 * @param credentials
	 * @param keystore
	 * @param certificate
	 * @param alias
	 */
	public static SecureSession startSecureSession(String username, String password, Keystore keystore, File certificate, String alias) {
		return startSecureSession(new BasicAuthentication(username, password), keystore, certificate, alias);
	}

	/**
	 * Start an authenticated session with username and password in conjunction with
	 * a specified keystore and new certificate.
	 * 
	 * @param credentials
	 * @param keystore
	 * @param certificate
	 * @param alias
	 */
	public static SecureSession startSecureSession(String password, Keystore keystore, File certificate, String alias) {
		return startSecureSession(new BasicAuthentication(password),keystore,certificate,alias);
	}

	/**
	 * Start an authenticated session with username and password in conjunction with
	 * a specified keystore and new certificate.
	 * 
	 * @param credentials
	 *            Credential object for authenticating with source
	 * @param keystore
	 * @param certificate
	 * @param alias
	 */
	private static SecureSession startSecureSession(BasicAuthentication credentials, File keystore, File certificate, String alias) {
		return startSecureSession(credentials, new Keystore(keystore), certificate, alias);
	}
	
	/**
	 * Start an authenticated session with username and password in conjunction with
	 * a specified keystore and new certificate.
	 * 
	 * @param username
	 * @param password
	 * @param keystore
	 * @param certificate
	 * @param alias
	 */
	public static SecureSession startSecureSession(String username, String password, File keystore, File certificate, String alias){
		return startSecureSession(new BasicAuthentication(username,password),keystore,certificate,alias);
	}
	
	/**
	 * Start an authenticated session with username and password in conjunction with
	 * a specified keystore and new certificate.
	 * 
	 * @param password
	 * @param keystore
	 * @param certificate
	 * @param alias
	 */
	public static SecureSession startSecureSession(String password, File keystore, File certificate, String alias){
		return startSecureSession(new BasicAuthentication(password),keystore,certificate,alias);
	}

	
}
