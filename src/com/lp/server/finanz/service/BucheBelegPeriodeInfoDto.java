package com.lp.server.finanz.service;

import java.io.Serializable;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.service.LocaleFac;

public class BucheBelegPeriodeInfoDto implements Serializable {
	private static final long serialVersionUID = -5380577719698872127L;

	public enum BelegInfo {
		Ok,
		Anzahlung,
		Schlussrechnung
	}
	
	private String belegartCnr;
	private Integer belegId;
	private String belegCnr;
	private String rechnungArtCnr;
	private BelegInfo info;
	

	public BucheBelegPeriodeInfoDto(RechnungDto arDto) {
		this.setBelegartCnr(arDto.getBelegartCNr());
		this.setBelegId(arDto.getIId());
		this.setBelegCnr(arDto.getCNr());
		this.setRechnungArtCnr(arDto.getRechnungartCNr());
		
		if (arDto.isAnzahlungsRechnung()) {
			this.info = BelegInfo.Anzahlung;
		} else if (arDto.isSchlussRechnung()) {
			this.info = BelegInfo.Schlussrechnung;
		} else {
			this.info = BelegInfo.Ok;
		}
	}
	
	public BucheBelegPeriodeInfoDto(EingangsrechnungDto erDto) {
		this.setBelegartCnr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
		this.setBelegId(erDto.getIId());
		this.setBelegCnr(erDto.getCNr());
		this.setRechnungArtCnr(erDto.getEingangsrechnungartCNr());
		
		if (erDto.isAnzahlung()) {
			this.info = BelegInfo.Anzahlung;
		} else if (erDto.isSchlussrechnung()) {
			this.info = BelegInfo.Schlussrechnung;
		} else {
			this.info = BelegInfo.Ok;
		}
	}
	
	public boolean isAnzahlung() {
		return BelegInfo.Anzahlung.equals(this.info);
	}
	
	public boolean isSchlussrechnung() {
		return BelegInfo.Schlussrechnung.equals(this.info);
	}

	public String getBelegartCnr() {
		return belegartCnr;
	}

	public void setBelegartCnr(String belegartCnr) {
		this.belegartCnr = belegartCnr;
	}

	public Integer getBelegId() {
		return belegId;
	}

	public void setBelegId(Integer belegId) {
		this.belegId = belegId;
	}

	public String getBelegCnr() {
		return belegCnr;
	}

	public void setBelegCnr(String belegCnr) {
		this.belegCnr = belegCnr;
	}

	public String getRechnungArtCnr() {
		return rechnungArtCnr;
	}

	public void setRechnungArtCnr(String rechnungArtCnr) {
		this.rechnungArtCnr = rechnungArtCnr;
	}
	
	public BelegInfo getBelegInfo() {
		return this.info;
	}
}
