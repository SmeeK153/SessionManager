package session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.StreamBuffer;
import keystore.Keystore;

public class SessionConnection {

	/**
	 * Supported request methods types GET is the default assumed when ambiguous
	 */
	public static enum REQUEST_METHOD {
		GET, PUT, POST, DELETE, HEAD, OPTIONS, CONNECT, TRACE, PATCH
	}

	/**
	 * Converts request method enumeration to usable string value.
	 * 
	 * @param method
	 * @return
	 */
	private static String getRequestMethod(REQUEST_METHOD method) {
		switch (method) {
		case GET:
			return "GET";
		case PUT:
			return "PUT";
		case POST:
			return "POST";
		case DELETE:
			return "DELETE";
		case HEAD:
			return "HEAD";
		case OPTIONS:
			return "OPTIONS";
		case CONNECT:
			return "CONNECT";
		case TRACE:
			return "TRACE";
		case PATCH:
			return "PATCH";
		default:
			return "GET";
		}

	}

	private Credential credential;
	private Keystore keystore;
	private HttpsURLConnection connection = null;
	private String responseContent;
	private String responseError;
	private Integer serverResponseCode;
	private String serverResponseMessage;
	private String cookie;

	protected SessionConnection(URL url, JSONObject requestBody, JSONObject requestHeaders) throws IOException {
		this(url, requestBody, requestHeaders, REQUEST_METHOD.GET, (Keystore) null);
	}

	protected SessionConnection(URL url, JSONObject requestData, JSONObject requestHeaders, Keystore keystore)
			throws IOException {
		this(url, requestData, requestHeaders, REQUEST_METHOD.GET, keystore);
	}

	protected SessionConnection(URL url, JSONObject requestData, JSONObject requestHeaders,
			REQUEST_METHOD requestMethod) throws IOException {
		this(url, requestData, requestHeaders, requestMethod, (Keystore) null);
	}

	protected SessionConnection(URL url, JSONObject requestData, JSONObject requestHeaders,
			REQUEST_METHOD requestMethod, Keystore keystore) throws IOException {
		System.out.println("\n");
		
		this.connection = (HttpsURLConnection) url.openConnection();
		System.out.println("Established connection with: " + url.toString());
		this.connection.setInstanceFollowRedirects(true);
		System.out.println("Setting up automatic redirects.");
		this.setRequestProperty(requestHeaders);
		
		if(requestData == null){
			this.setRequestMethod(requestMethod);
		} else {
			this.setRequestBody(requestData);
		}
		
		System.out.println("Request was successfully prepared.");
		this.responseContent = StreamBuffer.read(this.connection.getInputStream());
		System.out.println("Successfully connected to resource...");
		System.out.println("Retrieved request response.");
		try {
			this.responseError = StreamBuffer.read(this.connection.getErrorStream());
			System.out.println("Retrieved request error.");
		} catch (NullPointerException e) {
			System.out.println("The request did not provide an error stream to read.");
		}
		this.serverResponseCode = this.connection.getResponseCode();
		System.out.println("Retrieved server response code.");
		this.serverResponseMessage = this.connection.getResponseMessage();
		System.out.println("Retrieved server response message.");
		this.cookie = this.connection.getHeaderField("Set-Cookie");
		System.out.println("Retrieved request cookie.");
	}

	/**
	 * Sets the request's keystore resource for authentication
	 * 
	 * @param keystore
	 *            Keystore
	 */
	private void setKeystore(Keystore keystore) {
		if (keystore == null) {
			System.out.println("Set keystore resources to null.");
			return;
		} else {
			System.out.println("Using provided keystore resources.");
			this.connection.setSSLSocketFactory(keystore.getSSLSocketFactory());
		}
	}

	/**
	 * Sets the request's data payload
	 * 
	 * @param requestData
	 *            JSONObject
	 * @throws IOException
	 *             Thrown if the connection cannot be established
	 */
	private void setRequestBody(JSONObject requestData) throws IOException {
		if (requestData == null) {
			System.out.println("No request data provided.");
			return;
		}

		this.setRequestProperty("Content-Type", "application/json");
		this.setRequestProperty("Accept", "application/json");
		this.connection.setDoOutput(true);
		StreamBuffer.write(this.connection.getOutputStream(), requestData.toString());
		System.out.println("Request body data added.");
	}

	/**
	 * Sets the request's properties from a JSON
	 * 
	 * @param requestHeaders
	 *            JSONObject
	 */
	private void setRequestProperty(JSONObject requestHeaders) {
		if (requestHeaders == null) {
			return;
		}

		Iterator<String> keys = requestHeaders.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			this.setRequestProperty(key, requestHeaders.getString(key));
		}
	}

	/**
	 * Sets each individual request property
	 * 
	 * @param property
	 *            Request property
	 * @param value
	 *            Request property value
	 */
	private void setRequestProperty(String property, String value) {
		this.connection.setRequestProperty(property, value);
		System.out.println("Set request property: " + property);
	}

	/**
	 * Sets the request method type to be used
	 * 
	 * @param requestMethod
	 *            REQUEST_METHOD
	 */
	private void setRequestMethod(REQUEST_METHOD requestMethod) {
		String method = this.getRequestMethod(requestMethod);

		try {
			System.out.println("Setting request method to: " + method);
			this.connection.setRequestMethod(method);
		} catch (ProtocolException e) {
			System.err.println("Request method " + method + "failed, falling back to GET");
			this.setRequestMethod(REQUEST_METHOD.GET);
			e.printStackTrace();
		}
	}

	protected String getCookie() {
		return this.cookie;
	}

	protected Integer getServerResponseCode() {
		return this.serverResponseCode;
	}

	protected String getServerResponseMessage() {
		return this.serverResponseMessage;
	}

	protected String getServerResponseVerboseMessage() {
		return this.getServerResponseCode() + " : " + this.getServerResponseMessage();
	}

	protected String getResponse() {
		return this.responseContent;
	}

	public void printResponse() {
		System.out.println(this.getResponse());
	}

	public JSONObject getJSONObjectResponse() throws IOException, JSONException {
		return new JSONObject(this.getResponse());
	}

	public JSONArray getJSONArrayResponse() throws IOException, JSONException {
		return new JSONArray(this.getResponse());
	}
}
