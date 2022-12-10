package com.lp.server.artikel.ejbfac;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

public class HttpResponseHelper {

	private HttpResponse response;

	public HttpResponseHelper(HttpResponse response) {
		this.response = response;
	}
	
	public String statusLine() {
		return response.getStatusLine().toString();
	}
	
	public Integer statusCode() {
		return response.getStatusLine().getStatusCode();
	}
	
	public boolean hasHeader(String key) {
		return response.containsHeader(key);
	}
	
	public String getValueOfHeader(String key) {
		if (key == null) return null;
		
		for (Header header : response.getAllHeaders()) {
			if (key.equals(header.getName())) {
				return header.getValue();
			}
		}
		return null;
	}
	
	public String headersAsString() {
		StringBuilder builder = new StringBuilder("[");
		boolean first = true;
		for (Header header : response.getAllHeaders()) {
			builder.append(!first ? ", " : "");
			first = false;
			
			builder.append(header.getName())
				.append(": ")
				.append(header.getValue());
		}
		return builder.toString();
	}
}
