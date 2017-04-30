package session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class SessionConnection {
	private URL url = null;
	private BasicAuthentication auth;
	private HttpsURLConnection connection;
	
	protected SessionConnection(URL url){
		
	}
	
	private enum REQUEST_METHOD{
		GET,PUT,DELETE,HEAD,OPTIONS,CONNECT,TRACE,PATCH
	}
	
	private static JSONObject createJSONFromString(String json){
		if(json != null){
			return new JSONObject(json);
		}
		return null;
	}
	
	public void printResponse(){
		System.out.println(this.getResponse().toString());
	}

	public String getResponse(){
		 return getResponse(REQUEST_METHOD.GET);
	}
	
	public JSONObject getJSONResponse(){
		return new JSONObject(this.getResponse());
	}
	
	public JSONArray getJSONArrayResponse(){
		return new JSONArray(this.getResponse());
	}

	public String getResponse(REQUEST_METHOD requestMethod){
		String json = null;
		return getResponse(requestMethod,json);
	}
	
	public JSONObject getJSONResponse(REQUEST_METHOD requestMethod){
		return new JSONObject(this.getResponse(requestMethod));
	}
	
	public JSONArray getJSONArrayResponse(REQUEST_METHOD requestMethod){
		return new JSONArray(this.getResponse(requestMethod));
	}

	public String getResponse(JSONObject json){
		return getResponse(REQUEST_METHOD.GET,json);
	}
	
	public JSONObject getJSONResponse(JSONObject json){
		return new JSONObject(this.getResponse(json));
	} 
	
	public JSONArray getJSONArrayResponse(JSONObject json){
		return new JSONArray(this.getResponse(json));
	}

	public String getResponse(REQUEST_METHOD requestMethod, String json){
		return getResponse(requestMethod,createJSONFromString(json));
	}
	
	public JSONObject getJSONResponse(REQUEST_METHOD requestMethod, String json){
		return new JSONObject(this.getResponse(requestMethod,json));
	}
	
	public JSONArray getJSONArrayResponse(REQUEST_METHOD requestMethod, String json){
		return new JSONArray(this.getResponse(requestMethod,json));
	}

	public String getResponse(REQUEST_METHOD requestMethod, JSONObject json){
		try {
			this.connection.setRequestMethod(requestMethod.toString());
			this.connection.setInstanceFollowRedirects(true);
			
			if(json != null){
				this.connection.setDoOutput(true);
				this.connection.setRequestProperty("Content-Type", "application/json");
				this.connection.setRequestProperty("Accept", "application/json");
				OutputStreamWriter writer = new OutputStreamWriter(this.connection.getOutputStream());
				writer.write(json.toString());
				writer.close();
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
			String line = null;
			String response = "";
			while((line = reader.readLine())!=null){
				response += line;
			}
			
			return response;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public JSONObject getJSONResponse(REQUEST_METHOD requestMethod, JSONObject json){
		return new JSONObject(this.getResponse(requestMethod,json));
	}
	
	public JSONArray getJSONArrayResponse(REQUEST_METHOD requestMethod, JSONObject json){
		return new JSONArray(this.getResponse(requestMethod,json));
	}

}
