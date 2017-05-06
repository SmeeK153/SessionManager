//package session;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.ProtocolException;
//import java.net.URL;
//
//import javax.net.ssl.HttpsURLConnection;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import keystore.Keystore;
//import session.SessionConnection.REQUEST_METHOD;
//
//public class SecureSessionConnection extends SessionConnection {
//
//	private BasicAuthentication credentials;
//	private Keystore keystore;
//
//	protected SecureSessionConnection(URL url) {
//		this(url, (BasicAuthentication) null, (Keystore) null);
//	}
//
//	protected SecureSessionConnection(URL url, Keystore keystore) {
//		this(url, (BasicAuthentication) null, keystore);
//	}
//
//	protected SecureSessionConnection(URL url, BasicAuthentication credentials) {
//		this(url, credentials, (Keystore) null);
//	}
//
////	protected SecureSessionConnection(URL url, BasicAuthentication credentials, Keystore keystore) {
//////		super(url);
////		this.credentials = credentials;
////		this.keystore = keystore;
////	}
//
//	/**
//	 * Creates an authenticated HTTPS connection with the provided keystore `
//	 * 
//	 * @return
//	 * @throws IOException
//	 */
////	protected HttpsURLConnection getNewConnection() throws IOException {
////		HttpsURLConnection connection = (HttpsURLConnection) this.url.openConnection();
////
////		if (this.credentials != null) {
////			connection.setRequestProperty("Authorization", credentials.getAuthenticationParameter());
////		}
////
////		if (this.keystore != null) {
////			connection.setSSLSocketFactory(keystore.getSSLSocketFactory());
////		}
////		
////		return connection;
////	}
//
//	public void printResponse() {
//		try {
//			System.out.println(this.getResponse());
//		} catch (IOException e) {
//			System.err.println("An error occurred, cannot print out response.");
//			e.printStackTrace();
//		}
//	}
//
//	public String getResponse() throws IOException {
//		return this.getResponse(REQUEST_METHOD.GET);
//	}
//
//	public String getResponse(REQUEST_METHOD requestMethod) throws IOException {
//		return this.getResponse(requestMethod, (JSONObject) null);
//	}
//
//	public String getResponse(JSONObject jsonParameters) throws IOException {
//		return this.getResponse(REQUEST_METHOD.GET, jsonParameters);
//	}
//
//	public String getResponse(String jsonParameters) throws IOException, JSONException {
//		return this.getResponse(REQUEST_METHOD.GET, new JSONObject(jsonParameters));
//	}
//
//	/*
//	 * All calls converge on this method
//	 */
//
//	public String getResponse(REQUEST_METHOD requestMethod, JSONObject jsonParameters) throws IOException {
//		HttpsURLConnection connection = this.getNewConnection();
//
//		try {
//			connection.setRequestMethod(SessionConnection.getRequestMethod(requestMethod));
//		} catch (ProtocolException e1) {
//			System.err.println("Failed to set the request method to '"
//					+ SessionConnection.getRequestMethod(requestMethod) + "', defaulting to 'GET'");
//			e1.printStackTrace();
//		}
//		connection.setInstanceFollowRedirects(true);
//
//		if (jsonParameters != null) {
//			connection.setDoOutput(true);
//			connection.setRequestProperty("Content-Type", "application/json");
//			connection.setRequestProperty("Accept", "application/json");
//			OutputStreamWriter writer;
//			try {
//				writer = new OutputStreamWriter(connection.getOutputStream());
//				writer.write(jsonParameters.toString());
//				writer.close();
//			} catch (IOException e) {
//				System.err
//						.println("Failed to establish link to connection output stream for adding request parameters.");
//				e.printStackTrace();
//			}
//
//		}
//		
//		/*
//		 * Read the response
//		 */
//		BufferedReader reader = null;
//		try {
//			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//		} catch (IOException e) {
//			
//			/*
//			 * The response could not be access, read the error stream
//			 */
//			try {
//				BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
//				String line = null;
//				String content = "";
//				
//					while ((line = errorReader.readLine()) != null) {
//						content += line;
//					}
//					System.err.println("The remote server rejected the connection with the message:  " + content);
//			} catch (NullPointerException e2) {
//				/*
//				 * The error stream couldn't be accessed
//				 */
//				e2.printStackTrace();
//			} catch (IOException e2) {
//				e2.printStackTrace();
//			}
//
//			e.printStackTrace();
//		}
//		String line = null;
//		String response = "";
//		try {
//			while ((line = reader.readLine()) != null) {
//				response += line;
//			}
//		} catch (IOException e) {
//			System.err.println("Failed to establish link to connection input stream for the response.");
//			e.printStackTrace();
//		}
//		System.out.println("Server returned status of: " + connection.getResponseCode() + " " + connection.getResponseMessage());
//		return response;
//	}
//
//	public String getResponse(REQUEST_METHOD requestMethod, String jsonParameters) throws IOException, JSONException {
//		return this.getResponse(requestMethod, new JSONObject(jsonParameters));
//	}
//
//	public JSONObject getJSONObjectResponse() throws IOException, JSONException {
//		return new JSONObject(this.getResponse());
//	}
//
//	public JSONObject getJSONObjectResponse(REQUEST_METHOD requestMethod) throws IOException, JSONException {
//		return new JSONObject(this.getResponse(requestMethod));
//	}
//
//	public JSONObject getJSONObjectResponse(JSONObject jsonParameters) throws IOException, JSONException {
//		return new JSONObject(this.getResponse(jsonParameters));
//	}
//
//	public JSONObject getJSONObjectResponse(String jsonParameters) throws IOException, JSONException {
//		return new JSONObject(this.getResponse(jsonParameters));
//	}
//
//	public JSONObject getJSONObjectResponse(REQUEST_METHOD requestMethod, JSONObject jsonParameters)
//			throws IOException, JSONException {
//		return new JSONObject(this.getResponse(requestMethod, jsonParameters));
//	}
//
//	public JSONObject getJSONObjectResponse(REQUEST_METHOD requestMethod, String jsonParameters)
//			throws IOException, JSONException {
//		return new JSONObject(this.getResponse(requestMethod, jsonParameters));
//	}
//
//	public JSONArray getJSONArrayResponse() throws IOException, JSONException {
//		return new JSONArray(this.getResponse());
//	}
//
//	public JSONArray getJSONArrayResponse(REQUEST_METHOD requestMethod) throws IOException, JSONException {
//		return new JSONArray(this.getResponse(requestMethod));
//	}
//
//	public JSONArray getJSONArrayResponse(JSONObject jsonParameters) throws IOException, JSONException {
//		return new JSONArray(this.getResponse(jsonParameters));
//	}
//
//	public JSONArray getJSONArrayResponse(String jsonParameters) throws IOException, JSONException {
//		return new JSONArray(this.getResponse(jsonParameters));
//	}
//
//	public JSONArray getJSONArrayResponse(REQUEST_METHOD requestMethod, JSONObject jsonParameters)
//			throws IOException, JSONException {
//		return new JSONArray(this.getResponse(requestMethod, jsonParameters));
//	}
//
//	public JSONArray getJSONArrayResponse(REQUEST_METHOD requestMethod, String jsonParameters)
//			throws IOException, JSONException {
//		return new JSONArray(this.getResponse(requestMethod, jsonParameters));
//	}
//
//}
