package com.lp.server.system.service;

public class VersandwegPartnerOrdRspDto extends VersandwegPartnerDto {
	private static final long serialVersionUID = 7359120987264947489L;

	private String cEndpunkt;
	private String cBenutzer;
	private String cKennwort;	
	private String cUnbEmpfaenger;
	
	
	public String getCEndpunkt() {
		return cEndpunkt;
	}
	public void setCEndpunkt(String cEndpunkt) {
		this.cEndpunkt = cEndpunkt;
	}
	public String getCBenutzer() {
		return cBenutzer;
	}
	public void setCBenutzer(String cBenutzer) {
		this.cBenutzer = cBenutzer;
	}
	public String getCKennwort() {
		return cKennwort;
	}
	public void setCKennwort(String cKennwort) {
		this.cKennwort = cKennwort;
	}
	public String getCUnbEmpfaenger() {
		return cUnbEmpfaenger;
	}
	public void setCUnbEmpfaenger(String cUnbEmpfaenger) {
		this.cUnbEmpfaenger = cUnbEmpfaenger;
	}

}
