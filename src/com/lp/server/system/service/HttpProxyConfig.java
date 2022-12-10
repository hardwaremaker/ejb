package com.lp.server.system.service;

import java.io.Serializable;

public class HttpProxyConfig implements Serializable {
	private static final long serialVersionUID = -544693901848979104L;

	private String host ;
	private Integer port ;
	private boolean defined ;

	public HttpProxyConfig() {
	}
	
	public HttpProxyConfig(String host) {
		setHost(host) ;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		if(host != null && host.trim().length() > 5) {
			int defaultPort = 3128 ;

			int colonIndex = host.lastIndexOf(':') ;
			if(colonIndex != -1) {
				String s = host.substring(colonIndex) ;
				if(s.length() > 1) {
					try {
						defaultPort = Integer.parseInt(s.substring(1)) ;
					} catch(NumberFormatException e) {
					}
				}
				host = host.substring(0, colonIndex) ;
			}
			this.host = host;
			this.port = defaultPort ;
			this.defined = true ;			
		} else {
			this.defined = false ;
		}
	}

	public void setHost(String host, int port) {
		setHost(host) ;
		setPort(port) ;
	}
	
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * Gibt es diese Proxy-Konfiguration
	 * 
	 * @return true wenn es eine Proxy-Konfiguration gibt
	 */
	public boolean isDefined() {
		return defined;
	}
	public void setDefined(boolean defined) {
		this.defined = defined;
	}
	
	public String asString() {
		return getHost() + ":" + getPort();
	}
}
