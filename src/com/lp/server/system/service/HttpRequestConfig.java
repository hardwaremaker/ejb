package com.lp.server.system.service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpRequestConfig implements Serializable {
	private static final long serialVersionUID = 7491449988894664502L;

	private HttpProxyConfig hostConfig;
	private String resource;
	private Map<String, String> parameter;
	
	public HttpRequestConfig() {
	}
	
	public HttpRequestConfig(HttpProxyConfig hostConfig, String resource) {
		setHostConfig(hostConfig);
		setResource(resource);
	}
	
	public HttpProxyConfig getHostConfig() {
		return hostConfig;
	}
	
	public void setHostConfig(HttpProxyConfig hostConfig) {
		this.hostConfig = hostConfig;
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	private Map<String, String> getParameter() {
		if (parameter == null) {
			parameter = new HashMap<String, String>();
		}
		return parameter;
	}
	
	public void addParameter(String param, String value) {
		getParameter().put(param, value);
	}
	
	private String getParameterAsString() throws UnsupportedEncodingException {
		if (getParameter().isEmpty()) return "";
		
		StringBuilder builder = new StringBuilder();
		builder.append("?");
		Iterator<Entry<String, String>> iter = getParameter().entrySet().iterator();
		
		while (iter.hasNext()) {
			Entry<String, String> param = iter.next();
			builder.append(param.getKey())
				.append("=").append(URLEncoder.encode(param.getValue(), "UTF-8"));
			
			if (iter.hasNext()) {
				builder.append("&");
			}
		}
		
		return builder.toString();
	}
	
	public String getUri() throws UnsupportedEncodingException {
		return getHostConfig().asString() + getResource() + getParameterAsString();
	}
}
