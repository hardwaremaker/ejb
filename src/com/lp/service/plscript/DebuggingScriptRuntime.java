package com.lp.service.plscript;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

public class DebuggingScriptRuntime extends ScriptRuntime {
	private static final long serialVersionUID = -2764854002315969676L;
	private static final ILPLogger log = LPLogService.getInstance()
			.getLogger(DebuggingScriptRuntime.class);

	@Override
	public boolean existsVar(String variableName) {
		boolean b = super.existsVar(variableName);
		log.warn("existsVar '" + variableName + "' " + b + ".");
		return b;
	}
	
	@Override
	public void setVar(String variableName, Object value) {
		log.warn("setVar '" + variableName + "' to (" + (value != null ? value.toString() : "null")  + ").");
		super.setVar(variableName, value);
	}
	
	@Override
	public Object getVar(String variableName) {
		Object o = super.getVar(variableName);
		log.warn("getVar '" + variableName + "' is (" + (o != null ? o.toString() : "null") + ").");
		return o;
	}
}
