package session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SessionConnection {
	protected URL url;

	protected SessionConnection(URL url) {
		this.url = url;
	}

	/**
	 * Supported request methods types GET is the default assumed when ambiguous
	 */
	public static enum REQUEST_METHOD {
		GET, PUT, DELETE, HEAD, OPTIONS, CONNECT, TRACE, PATCH
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
	
	/**
	 * Creates an un-authenticated HTTPS connection
	 * 
	 * @return
	 * @throws IOException
	 */
	private HttpsURLConnection getNewConnection() throws IOException{
		return (HttpsURLConnection) this.url.openConnection();
	}
	
	public void printResponse() {
		try {
			System.out.println(this.getResponse());
		} catch (IOException e) {
			System.err.println("An error occurred, cannot print out response.");
			e.printStackTrace();
		}
	}

	public String getResponse() throws IOException {
		return this.getResponse(REQUEST_METHOD.GET);
	}

	public String getResponse(REQUEST_METHOD requestMethod) throws IOException {
		return this.getResponse(requestMethod, (JSONObject) null);
	}

	public String getResponse(JSONObject jsonParameters) throws IOException {
		return this.getResponse(REQUEST_METHOD.GET, jsonParameters);
	}

	public String getResponse(String jsonParameters) throws IOException, JSONException {
		return this.getResponse(REQUEST_METHOD.GET, new JSONObject(jsonParameters));
	}
	
	/*
	 * All calls converge on this method
	 */
	
	public String getResponse(REQUEST_METHOD requestMethod, JSONObject jsonParameters) throws IOException {
		HttpsURLConnection connection = this.getNewConnection();

		connection.setRequestMethod(SessionConnection.getRequestMethod(requestMethod));
		connection.setInstanceFollowRedirects(true);

		if (jsonParameters != null) {
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(jsonParameters.toString());
			writer.close();
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = null;
		String response = "";
		while ((line = reader.readLine()) != null) {
			response += line;
		}
		return response;
	}

	public String getResponse(REQUEST_METHOD requestMethod, String jsonParameters) throws IOException, JSONException {
		return this.getResponse(requestMethod, new JSONObject(jsonParameters));
	}

	
	
	
	public JSONObject getJSONObjectResponse() throws IOException, JSONException {
		return new JSONObject(this.getResponse());
	}

	public JSONObject getJSONObjectResponse(REQUEST_METHOD requestMethod) throws IOException, JSONException {
		return new JSONObject(this.getResponse(requestMethod));
	}

	public JSONObject getJSONObjectResponse(JSONObject jsonParameters) throws IOException, JSONException {
		return new JSONObject(this.getResponse(jsonParameters));
	}

	public JSONObject getJSONObjectResponse(String jsonParameters) throws IOException, JSONException {
		return new JSONObject(this.getResponse(jsonParameters));
	}

	public JSONObject getJSONObjectResponse(REQUEST_METHOD requestMethod, JSONObject jsonParameters)
			throws IOException, JSONException {
		return new JSONObject(this.getResponse(requestMethod, jsonParameters));
	}

	public JSONObject getJSONObjectResponse(REQUEST_METHOD requestMethod, String jsonParameters)
			throws IOException, JSONException {
		return new JSONObject(this.getResponse(requestMethod, jsonParameters));
	}

	
	
	public JSONArray getJSONArrayResponse() throws IOException, JSONException {
		return new JSONArray(this.getResponse());
	}
	
	public JSONArray getJSONArrayResponse(REQUEST_METHOD requestMethod) throws IOException, JSONException {
		return new JSONArray(this.getResponse(requestMethod));
	}

	public JSONArray getJSONArrayResponse(JSONObject jsonParameters) throws IOException, JSONException {
		return new JSONArray(this.getResponse(jsonParameters));
	}

	public JSONArray getJSONArrayResponse(String jsonParameters) throws IOException, JSONException {
		return new JSONArray(this.getResponse(jsonParameters));
	}

	public JSONArray getJSONArrayResponse(REQUEST_METHOD requestMethod, JSONObject jsonParameters) throws IOException, JSONException {
		return new JSONArray(this.getResponse(requestMethod,jsonParameters));
	}

	public JSONArray getJSONArrayResponse(REQUEST_METHOD requestMethod, String jsonParameters) throws IOException, JSONException {
		return new JSONArray(this.getResponse(requestMethod,jsonParameters));
	}
}
