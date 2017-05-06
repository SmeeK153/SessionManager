package keystore;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Keystore {

	private static File DEFAULT_KEYSTORE_FILE = new File(
			System.getProperty("user.home") + "/.ssl/.credentials/keystore.jks");
	public static Keystore DEFAULT_KEYSTORE = new Keystore(true);
	private static KeyStore SYSTEM_KEYSTORE = null;

	static {
		try {
			SYSTEM_KEYSTORE = KeyStore.getInstance(KeyStore.getDefaultType());
		} catch (KeyStoreException e) {
			System.err.println(
					"Keystore could not be accessed. The credentials may be incorrect or changed, or the keystore may've been corrupted.");
			e.printStackTrace();
		}
	}

	private File keystore = null;
	private char[] keystorePassword = "password".toCharArray();
	private KeyStore systemKeystore;
	private Boolean initialized = false;
	
	/**
	 * Creates a keystore at the default path
	 */
	private static void createKeystore() {
		Keystore.createKeystore(DEFAULT_KEYSTORE_FILE);
	}

	/**
	 * Creates a keystore at the specified path
	 * 
	 * @param file
	 *            Keystore to be created
	 * @return
	 */
	private static Boolean createKeystore(File file) {

		// Create the folder structure if it doesn't already exist
		if (file.exists()) {
			return true;
		} else {
			file.getParentFile().mkdirs();
		}

		// Attempt to create the actual file, if possible
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("The desired keystore file could not be created.");
				e.printStackTrace();
			}
		}

		return file.exists();
	}

	/**
	 * Provides user interface to select a new certificate file
	 * 
	 * @return Returns the selected certificate file
	 */
	private static File getNewCertificateFile() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		java.awt.FileDialog fd = new java.awt.FileDialog((java.awt.Frame) null);
		fd.setAlwaysOnTop(true);
		fd.setTitle("Please select the appropriate access certificate.");
		fd.setDirectory(System.getProperty("user.home"));
		fd.setVisible(true);
		File file = new File(fd.getDirectory(), fd.getFile());
		return file;
	}

	/**
	 * Creates the default keystore, can only be used by the class static
	 * reference
	 */
	private Keystore(Boolean bool) {
		this(Keystore.DEFAULT_KEYSTORE_FILE);
	}

	/**
	 * Creates a specialized keystore in order to create separate containers for
	 * different certificates by allowing the user to select their certificate
	 */
	private Keystore() {
		this(Keystore.getNewCertificateFile());
	}

	/**
	 * Creates a specialized keystore in order to create separate containers for
	 * different certificates
	 * 
	 * @param keystore
	 */
	public Keystore(File keystore) {
		this.keystore = keystore;
		this.createKeystore(this.keystore);
		try {
			this.initializeKeyStore();
		} catch (FileNotFoundException e) {
			System.err.println("The keystore " + keystore.getAbsolutePath() + " couldn't be found.");
			e.printStackTrace();
		}
	}

	/**
	 * Opens a new input stream to the keystore
	 * 
	 * @return Returns a new FileInputStream
	 * @throws FileNotFoundException
	 */
	private FileInputStream getKeystoreInputStream() throws FileNotFoundException {
		return new FileInputStream(this.keystore);
	}

	/**
	 * Opens a new output stream to the keystore
	 * 
	 * @return Returns a new FileOutputStream
	 * @throws FileNotFoundException
	 */
	private FileOutputStream getKeystoreOutputStream() throws FileNotFoundException {
		return new FileOutputStream(this.keystore);
	}
	
	/**
	 * Retrieves and initializes the TrustManagerFactory
	 * 
	 * @return TrustManagerFactory with the Default Algorithm
	 */
	private TrustManagerFactory getSystemTrustManagerFactory(){
		TrustManagerFactory factory = null;
		
		try {
			factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		} catch (NoSuchAlgorithmException e) {
			System.err.println("The default TrustManagerFactory algorithm failed.");
			e.printStackTrace();
		}
		try {
			factory.init(this.systemKeystore);
		} catch (KeyStoreException e) {
			System.err.println("The TrustManagerFactory could not be initialized.");
			e.printStackTrace();
		}
		return factory;
	}
	
	/**
	 * Retrieves and initializes the SSLContext
	 * 
	 * @param protocol Desired protocol to be used
	 * @return SSLContext with the desired protocol
	 */
	private SSLContext getSSLContext(String protocol){
		SSLContext context = null;
		try {
			context = SSLContext.getInstance(protocol);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("The desired SSLContext '" + protocol + "' was invalid, substituted SSL instead.");
			try {
				context = SSLContext.getInstance("SSL");
			} catch (NoSuchAlgorithmException e1) {
				System.err.println("The SSL substitution failed...");
				e1.printStackTrace();
			}
		}
		try {
			context.init(null, this.getSystemTrustManagerFactory().getTrustManagers(), null);
		} catch (KeyManagementException e) {
			System.err.println("The SSLContext could not be initialized.");
			e.printStackTrace();
		}
		return context;
	}
	
	/**
	 * Retrieves and initializes the SSLContext
	 * 
	 * @return SSLContext with the 'SSL' protocol
	 */
	private SSLContext getSSLContext(){
		return this.getSSLContext("SSL");
	}
	
	/**
	 * Retrieves the SSL Socket Factory from the initialized SSL Context
	 * 
	 * @return SSLSocketFactory with the 'SSL' protocol
	 */
	public SSLSocketFactory getSSLSocketFactory(){
		return this.getSSLSocketFactory("SSL");
	}
	
	/**
	 * Retrieves the SSL Socket Factory from the initialized SSL Context
	 * 
	 * @param protocol Desired protocol to be used
	 * @return SSLSocketFactory with the desired protocol
	 */
	private SSLSocketFactory getSSLSocketFactory(String protocol){
		return this.getSSLContext(protocol).getSocketFactory();
	}
	
	/**
	 * Loads the desired keystore into memory for manipulation
	 * 
	 * @return Boolean on whether keystore was loaded or not
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void initializeKeyStore() throws FileNotFoundException {
		
		if(initialized){
			System.out.println("Keystore is already initialized.");
			return;
		}
		
		try {
			this.systemKeystore = KeyStore.getInstance(KeyStore.getDefaultType());
		} catch (KeyStoreException e1) {
			e1.printStackTrace();
		}
		
		try {
			try {
				this.systemKeystore.load(this.getKeystoreInputStream(), this.keystorePassword);
				System.out.println("Existing keystore was loaded.");
				System.out.println("Keystore has been successfully initialized.");
				this.initialized = true;
			} catch (EOFException e) {
				this.systemKeystore.load(null, this.keystorePassword);
				System.out.println("Blank keystore was loaded.");
				System.out.println("Keystore has been successfully initialized.");
				this.initialized = true;
			} 
		} catch (IOException e) {
			System.err.println("The keystore password has changed or the keystore is corrupt. The system cannot initialize the keystore.");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static X509Certificate extractX509FromFile(File certificate) throws FileNotFoundException{
		FileInputStream certificateInputStream = new FileInputStream(certificate);
		try {
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			return (X509Certificate) factory.generateCertificate(certificateInputStream);
		} catch (CertificateException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void addCertificate(String host) throws NoSuchAlgorithmException, CertificateException, IOException{
		if(!this.initialized){
			System.err.println("Keystore has not been initialized, the system cannot continue.");
			return;
		}
		
		this.addCertificate(Keystore.getNewCertificateFile(),host);
	}
	
	public void addCertificate(File certificate, String host) throws NoSuchAlgorithmException, CertificateException, IOException{
		if(!this.initialized){
			System.err.println("Keystore has not been initialized, the system cannot continue.");
			return;
		}
		
		try {
			Integer initial = this.systemKeystore.size();
			this.systemKeystore.setCertificateEntry(host, Keystore.extractX509FromFile(certificate));
			this.saveKeystore();
			Integer result = this.systemKeystore.size();
			System.out.println(host + " was successfully added to the keystore from " + initial + " to " + result + " certificates.");
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private void changeKeystorePassword(String password){
		if(!this.initialized){
			System.err.println("Keystore has not been initialized, the system cannot continue.");
			return;
		}
		
		this.keystorePassword = password.toCharArray();
		try {
			this.saveKeystore();
			System.out.println("Keystore password successfully changed.");
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void saveKeystore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException{
		if(!this.initialized){
			System.err.println("Keystore has not been initialized, the system cannot continue.");
			return;
		}
		
		this.systemKeystore.store(this.getKeystoreOutputStream(), this.keystorePassword);
	}
	
	public static void main(String...args) throws NoSuchAlgorithmException, CertificateException, IOException{
		Keystore kstore = Keystore.DEFAULT_KEYSTORE;
		kstore.addCertificate("test");
	}
}
