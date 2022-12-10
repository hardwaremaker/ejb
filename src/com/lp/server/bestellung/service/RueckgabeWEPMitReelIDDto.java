package com.lp.server.bestellung.service;

import java.io.Serializable;

public class RueckgabeWEPMitReelIDDto implements Serializable {

	
	public RueckgabeWEPMitReelIDDto(String chargennummer, Integer weposIId){
		this.chargennummer=chargennummer;
		this.weposIId=weposIId;
	}
	
	private String chargennummer=null;
	private Integer weposIId=null;
	public String getChargennummer() {
		return chargennummer;
	}
	public void setChargennummer(String chargennummer) {
		this.chargennummer = chargennummer;
	}
	public Integer getWeposIId() {
		return weposIId;
	}
	public void setWeposIId(Integer weposIId) {
		this.weposIId = weposIId;
	}
}
