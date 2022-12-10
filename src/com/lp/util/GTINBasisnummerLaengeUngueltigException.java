package com.lp.util;

public class GTINBasisnummerLaengeUngueltigException extends EJBExceptionData {

	private static final long serialVersionUID = -937553473286557306L;

	private String value;
	private String parametername;
	
	public GTINBasisnummerLaengeUngueltigException(String parametername, String value) {
		setValue(value);
		setParametername(parametername);
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getParametername() {
		return parametername;
	}
	
	public void setParametername(String parametername) {
		this.parametername = parametername;
	}
}
