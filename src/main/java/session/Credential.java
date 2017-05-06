package session;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import core.StreamBuffer;

public class Credential {

	private String encodedCredentials = "";

	protected Credential(String username, String password) {
		this.encode(username, password);
	}

	private void encode(String username, String password) {
		this.encodedCredentials = new String(Base64.encodeBase64String((username + ":" + password).getBytes()));
	}

	private String[] decode() {
		return new String(Base64.decodeBase64(this.encodedCredentials.getBytes())).split(":");
	}
	
	protected JSONObject getBasicCredentialsJSON(){
		JSONObject json = new JSONObject();
		json.put("Authorization", "Basic " + this.encodedCredentials);
		return json;
	}
	
	protected JSONObject getOAuth1CredentialsJSON(){
		String[] user = this.decode();
		JSONObject json = new JSONObject();
		json.put("username", user[0]);
		json.put("password", user[1]);
		user = null;
		return json;
	}
}
