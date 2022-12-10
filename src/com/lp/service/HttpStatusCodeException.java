package com.lp.service;

import com.lp.server.system.service.HttpRequestConfig;
import com.lp.server.util.HvHttpHeader;

public class HttpStatusCodeException extends Exception {
	private static final long serialVersionUID = -1505666555044550198L;

	private HttpRequestConfig requestConfig;
	private Integer statusCode;
	private String reasonPhrase;
	private HvHttpHeader hvHeader;
	
	public HttpStatusCodeException(HttpRequestConfig requestConfig, Integer statusCode, String reasonPhrase, HvHttpHeader hvHeader) {
		super("Unexpected status code '" + statusCode + " " + reasonPhrase + "' for request '" + requestConfig.getHostConfig().asString() + requestConfig.getResource() + "'");
		setStatusCode(statusCode);
		setReasonPhrase(reasonPhrase);
		setRequestConfig(requestConfig);
		setHvHeader(hvHeader);
	}
	
	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}
	
	public String getReasonPhrase() {
		return reasonPhrase;
	}
	
	public void setRequestConfig(HttpRequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}
	
	public HttpRequestConfig getRequestConfig() {
		return requestConfig;
	}
	
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	
	public Integer getStatusCode() {
		return statusCode;
	}
	
	public void setHvHeader(HvHttpHeader hvHeader) {
		this.hvHeader = hvHeader;
	}
	public HvHttpHeader getHvHeader() {
		return hvHeader;
	}
}
