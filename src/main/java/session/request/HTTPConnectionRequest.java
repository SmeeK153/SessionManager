package session.request;

import java.net.URL;

import session.SessionConnection;
import session.SessionConnection.REQUEST_METHOD;

public interface HTTPConnectionRequest {
	
	public ConnectionRequest makeRequest(SessionConnection.REQUEST_METHOD method, URL url);
	
	public default ConnectionRequest GET(URL url) {
		return this.makeRequest(SessionConnection.REQUEST_METHOD.GET, url);
	}
	
	public default ConnectionRequest PUT(URL url) {
		return this.makeRequest(SessionConnection.REQUEST_METHOD.PUT, url);
	}
	
	public default ConnectionRequest POST(URL url) {
		return this.makeRequest(SessionConnection.REQUEST_METHOD.POST, url);
	}
	
	public default ConnectionRequest DELETE(URL url) {
		return this.makeRequest(SessionConnection.REQUEST_METHOD.DELETE, url);
	}
	
	public default ConnectionRequest HEAD(URL url) {
		return this.makeRequest(SessionConnection.REQUEST_METHOD.HEAD, url);
	}
	
	public default ConnectionRequest OPTIONS(URL url) {
		return this.makeRequest(SessionConnection.REQUEST_METHOD.OPTIONS, url);
	}
	
	public default ConnectionRequest CONNECT(URL url) {
		return this.makeRequest(SessionConnection.REQUEST_METHOD.CONNECT, url);
	}
	
	public default ConnectionRequest TRACE(URL url) {
		return this.makeRequest(SessionConnection.REQUEST_METHOD.TRACE, url);
	}
	
	public default ConnectionRequest PATCH(URL url) {
		return this.makeRequest(SessionConnection.REQUEST_METHOD.PATCH, url);
	}
}
