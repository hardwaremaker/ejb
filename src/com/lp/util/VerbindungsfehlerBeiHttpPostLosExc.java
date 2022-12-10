package com.lp.util;

import com.lp.server.fertigung.service.LosDto;
import com.lp.server.system.service.HttpProxyConfig;

public class VerbindungsfehlerBeiHttpPostLosExc extends EJBExceptionData {
	private static final long serialVersionUID = -4496326302175572606L;

	private LosDto losDto;
	private HttpProxyConfig httpProxyConfig;
	
	public VerbindungsfehlerBeiHttpPostLosExc(LosDto losDto, HttpProxyConfig httpProxyConfig) {
		setLosDto(losDto);
		setHttpProxyConfig(httpProxyConfig);
	}
	
	public LosDto getLosDto() {
		return losDto;
	}
	
	public void setLosDto(LosDto losDto) {
		this.losDto = losDto;
	}
	
	public HttpProxyConfig getHttpProxyConfig() {
		return httpProxyConfig;
	}
	
	public void setHttpProxyConfig(HttpProxyConfig httpProxyConfig) {
		this.httpProxyConfig = httpProxyConfig;
	}

}
