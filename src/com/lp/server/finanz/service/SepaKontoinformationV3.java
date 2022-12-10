package com.lp.server.finanz.service;

import java.io.Serializable;

public class SepaKontoinformationV3 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = -4909844694228653040L;

	private String name;
	private String iban;
	private String bic;

	public SepaKontoinformationV3() {
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIban() {
		return iban;
	}
	
	public void setIban(String iban) {
		this.iban = iban;
	}
	
	public String getBic() {
		return bic;
	}
	
	public void setBic(String bic) {
		this.bic = bic;
	}
}
