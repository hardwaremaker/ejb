package com.lp.server.system.service;

import java.io.Serializable;

import javax.persistence.Transient;

public class VersandwegPartnerCCDto extends VersandwegPartnerDto implements Serializable  {
	private static final long serialVersionUID = -8906157001382345535L;

	public enum SokoAdresstyp {
		AUFTRAGS_ADRESSE,
		LIEFER_ADRESSE,
		RECHNUNGS_ADRESSE
	} ;
	
	private String cKundennummer ;
	private String cKennwort ;
	private String cKeystore ;
	private String cKeystoreKennwort ;
	private Integer sokoAdresstyp ;
	
	public String getCKundennummer() {
		return cKundennummer;
	}

	public void setCKundennummer(String cKundennummer) {
		this.cKundennummer = cKundennummer;
	}

	public String getCKennwort() {
		return cKennwort;
	}

	public void setCKennwort(String cKennwort) {
		this.cKennwort = cKennwort;
	}	
	
	public String getCKeystore() {
		return cKeystore;
	}
	public void setCKeystore(String cKeystore) {
		this.cKeystore = cKeystore;
	}

	public String getCKeystoreKennwort() {
		return cKeystoreKennwort;
	}

	public void setCKeystoreKennwort(String cKeystoreKennwort) {
		this.cKeystoreKennwort = cKeystoreKennwort;
	}

	public Integer getSokoAdresstyp() {
		return sokoAdresstyp;
	}

	public void setSokoAdresstyp(Integer sokoAdresstyp) {
		this.sokoAdresstyp = sokoAdresstyp;
	}
	
	@Transient
	public SokoAdresstyp getSokoAdresstypEnum() {
		return SokoAdresstyp.values()[sokoAdresstyp] ;
	}
	
}
