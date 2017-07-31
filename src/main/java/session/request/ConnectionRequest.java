package session.request;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import session.Session;
import session.SessionConnection;

public class ConnectionRequest {
	
	private Session session = null;
	private JSONObject requestData = null;
	private ArrayList<Integer> successfulResponseCodes = null;
	private SessionConnection.REQUEST_METHOD requestMethod = null;
	private URL url = null;
	
	public ConnectionRequest(Session session, SessionConnection.REQUEST_METHOD requestMethod, URL url) {
		this.session = session;
		this.requestMethod = requestMethod;
		this.url = url;
	}
	
	public ConnectionRequest with(String key, Object value) {
		if(this.requestData == null) {
			this.requestData = new JSONObject();
		}
		this.requestData.put(key, value);
		return this;
	}
	
	public ConnectionRequest with(JSONObject requestData) {
		if(this.requestData == null) {
			this.requestData = requestData;
		} else {
			requestData.keySet().forEach(key -> {
				this.requestData.put(key, requestData.get(key));
			});
		}
		return this;
	}
	
	public ConnectionRequest forResponseCodes(Integer...responseCodes) {
		this.successfulResponseCodes = new ArrayList<Integer>(Arrays.asList(responseCodes));
		return this;
	}
	
	private SessionConnection getConnection() {
		SessionConnection connection = null;
		try {
			connection = this.session.getConnection(this.url, this.requestData, this.requestMethod);
			if(this.successfulResponseCodes != null && this.successfulResponseCodes.size() > 0) {
				if(this.successfulResponseCodes.contains(connection.getServerResponseCode())) {
					return connection;
				} else {
					return null;
				}
			} else {
				return connection;
			}
		} catch (IOException e) {
			System.err.println("Couldn't retrieve request due to an IOException");
			e.printStackTrace();
			return null;
		}
	}
	
	public JSONObject toGetJSONObjectResponse() {
		SessionConnection connection = this.getConnection();
		if(connection != null) {
			try {
				return connection.getJSONObjectResponse();
			} catch (JSONException e) {
				System.err.println("There was an issue with the JSON marshalling.");
				e.printStackTrace();
				return new JSONObject();
			}
		} else {
			return new JSONObject();
		}
	}
	
	public JSONArray toGetJSONArrayResponse() {
		SessionConnection connection = this.getConnection();
		if(connection != null) {
			try {
				return connection.getJSONArrayResponse();
			} catch (JSONException e) {
				System.err.println("There was an issue with the JSON marshalling.");
				e.printStackTrace();
				return new JSONArray();
			}
		} else {
			return new JSONArray();
		}
	}
	
	public Boolean toGetBooleanResponse() {
		return this.getConnection() != null;
	}
	
	public String toGetStringResponse() {
		SessionConnection connection = this.getConnection();
		if(connection != null) {
			return connection.getResponse();
		} else {
			return "";
		}
	}
}
