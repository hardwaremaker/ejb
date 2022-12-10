package com.lp.service.plscript;

public class ParameterNotFoundException extends Exception {
	private static final long serialVersionUID = -8203039094355043845L;

	private String paramName;
	
	public ParameterNotFoundException(String paramName) {
		super("Parameter '" + paramName + "' not found");
		this.paramName = "" + paramName;
	}
	
	public String getParameterName() {
		return paramName;
	}
}
