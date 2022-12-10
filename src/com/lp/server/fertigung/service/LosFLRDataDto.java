package com.lp.server.fertigung.service;

import java.io.Serializable;

public class LosFLRDataDto implements ILosFLRData, Serializable {
	private static final long serialVersionUID = 838615431773245664L;

	private String statusCnr;
	private String kundeName;
	private String stklArtikelbezeichnung;
	private String stklArtikelbezeichnung2;
	private Long losBeginnMs;
	private Long losEndeMs;
	private String fertigungsort;
	private String artikelCnr;
	private String auftragCnr;
	
	@Override
	public String getStatusCnr() {
		return statusCnr;
	}

	@Override
	public void setStatusCnr(String statusCnr) {
		this.statusCnr = statusCnr;
	}

	@Override
	public String getKundeName() {
		return kundeName;
	}

	@Override
	public void setKundeName(String kundeName) {
		this.kundeName = kundeName;
	}

	@Override
	public String getStklArtikelbezeichnung() {
		return stklArtikelbezeichnung;
	}

	@Override
	public void setStklArtikelbezeichnung(String bez) {
		this.stklArtikelbezeichnung = bez;
	}

	@Override
	public String getStklArtikelbezeichnung2() {
		return stklArtikelbezeichnung2;
	}

	@Override
	public void setStklArtikelbezeichnung2(String bez2) {
		this.stklArtikelbezeichnung2 = bez2;
	}
	
	@Override
	public Long getLosBeginnMs() {
		return losBeginnMs;
	}
	
	public void setLosBeginnMs(Long losBeginnMs) {
		this.losBeginnMs = losBeginnMs;
	}
	
	public Long getLosEndeMs() {
		return losEndeMs;
	}
	
	public void setLosEndeMs(Long losEndeMs) {
		this.losEndeMs = losEndeMs;
	}
	
	public String getFertigungsort() {
		return fertigungsort;
	}
	
	public void setFertigungsort(String fertigungsort) {
		this.fertigungsort = fertigungsort;
	}
	
	public String getArtikelCnr() {
		return artikelCnr;
	}
	
	public void setArtikelCnr(String artikelCnr) {
		this.artikelCnr = artikelCnr;
	}
	
	public String getAuftragCnr() {
		return auftragCnr;
	}
	
	public void setAuftragCnr(String auftragCnr) {
		this.auftragCnr = auftragCnr;
	}
}
