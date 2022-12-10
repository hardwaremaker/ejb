package com.lp.util;

import com.lp.server.fertigung.service.LosDto;
import com.lp.server.system.service.HttpRequestConfig;

public class UnerwarteterStatusCodeBeiHttpLosExc extends EJBExceptionData {
	private static final long serialVersionUID = -8455364369317128536L;

	private LosDto losDto;
	private Integer statusCode;
	private String reasonPhrase;
	private HttpRequestConfig httpRequestConfig;
	
	public UnerwarteterStatusCodeBeiHttpLosExc(LosDto losDto,
			Integer statusCode, String reasonPhrase,
			HttpRequestConfig httpRequestConfig) {
		setLosDto(losDto);
		setStatusCode(statusCode);
		setReasonPhrase(reasonPhrase);
		setHttpRequestConfig(httpRequestConfig);
	}

	public void setLosDto(LosDto losDto) {
		this.losDto = losDto;
	}
	
	public LosDto getLosDto() {
		return losDto;
	}
	
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	
	public Integer getStatusCode() {
		return statusCode;
	}
	
	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}
	
	public String getReasonPhrase() {
		return reasonPhrase;
	}
	
	public void setHttpRequestConfig(HttpRequestConfig httpRequestConfig) {
		this.httpRequestConfig = httpRequestConfig;
	}
	
	public HttpRequestConfig getHttpRequestConfig() {
		return httpRequestConfig;
	}
}
