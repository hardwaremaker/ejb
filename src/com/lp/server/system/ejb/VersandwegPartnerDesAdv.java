package com.lp.server.system.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;

@Entity
@Table(name=ITablenames.LP_VERSANDWEGPARTNERDESADV)
@PrimaryKeyJoinColumn(name="I_ID")
public class VersandwegPartnerDesAdv extends VersandwegPartner {
	private static final long serialVersionUID = 4173273991183448724L;

	@Column(name="C_ENDPUNKT")
	private String cEndpunkt;

	@Column(name="C_BENUTZER")
	private String cBenutzer;

	@Column(name="C_KENNWORT")
	private String cKennwort;
	
	@Column(name="C_UNBEMPFAENGER")
	private String cUnbEmpfaenger;

	
	public String getcEndpunkt() {
		return cEndpunkt;
	}

	public void setcEndpunkt(String cEndpunkt) {
		this.cEndpunkt = cEndpunkt;
	}

	public String getcBenutzer() {
		return cBenutzer;
	}

	public void setcBenutzer(String cBenutzer) {
		this.cBenutzer = cBenutzer;
	}

	public String getcKennwort() {
		return cKennwort;
	}

	public void setcKennwort(String cKennwort) {
		this.cKennwort = cKennwort;
	}

	public String getcUnbEmpfaenger() {
		return cUnbEmpfaenger;
	}

	public void setcUnbEmpfaenger(String cUnbEmpfaenger) {
		this.cUnbEmpfaenger = cUnbEmpfaenger;
	}
}
