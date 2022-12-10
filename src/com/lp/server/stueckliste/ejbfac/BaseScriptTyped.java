package com.lp.server.stueckliste.ejbfac;

import org.apache.log4j.Logger;

import com.lp.service.plscript.BaseScript;
import com.lp.util.LpLoggerCS;

public class BaseScriptTyped<T> extends BaseScript {
	private final static Logger log = LpLoggerCS.getInstance(BaseScriptTyped.class);

	private T payload;
	
	public BaseScriptTyped() {
	}
	
	public BaseScriptTyped(T payload) {
		setPayload(payload);
	}
	
	protected T getPayload() {
		return this.payload;
	}
	
	protected void setPayload(T payload) {
		this.payload = payload;
	}
	
	public void setPayloadAny(Object payload) {
		this.payload = (T)payload;
	}
	
	public void debug(String message) {
		log.debug(message);
	}
	
	public void info(String message) {
		log.info(message);
	}
	
	public void warn(String message) {
		log.warn(message);
	}
	
	public void error(String message) {
		log.error(message);
	}
	
	public Object sqlExecute(String sqlString) {
		return rt().getSql().execute(sqlString);
	}
	
	public Object[] sqlExecutes(String sqlString) {
		return rt().getSql().executes(sqlString);
	}
}
