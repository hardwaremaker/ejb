package com.lp.server.finanz.service;

import java.io.Serializable;

public class SepaKontoinformation implements Serializable {

	private static final long serialVersionUID = 8174552578354288908L;

	private String name;
	private String iban;
	private String bic;

	public SepaKontoinformation() {
		super();
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

	@Override
	public String toString() {
		return "SepaKontoinformation [name=" + String.valueOf(name) + ", iban=" + String.valueOf(iban)
				+ ", bic=" + String.valueOf(bic) + "]";
	}
}
