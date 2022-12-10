package com.lp.server.finanz.service;

import java.io.Serializable;

import com.lp.server.system.service.MwstsatzbezDto;

public class SteuerkategorieEntry implements Serializable {
	private static final long serialVersionUID = -6333473761355799306L;

	private Integer kontoId ;
	private String kontoCnr ;
	private String kontoBez ;
	private String kontoVerwendung ;
	private SteuerkategoriekontoDto steuerKategorieKonto ;
	private SteuerkategorieDto steuerKategorie ;
	private MwstsatzbezDto mwstsatzbezDto ;
	
	public SteuerkategorieEntry() { 
	}

	public SteuerkategorieEntry(SteuerkategoriekontoDto steuerKategorieKonto, 
			SteuerkategorieDto steuerkategorie, 
			Integer kontoId, String kontoCnr, String kontoBez, String kontoVerwendung,
			MwstsatzbezDto mwstsatzbezDto) {
		setKategorieKonto(steuerKategorieKonto);
		setKategorie(steuerkategorie);
		setKontoId(kontoId);
		setKontoCnr(kontoCnr) ;
		setKontoBez(kontoBez) ;
		setKontoVerwendung(kontoVerwendung);
		setMwstsatzbezDto(mwstsatzbezDto);
	}
	
	public Integer getKontoId() {
		return kontoId ;
	}
	
	public void setKontoId(Integer kontoId) {
		this.kontoId = kontoId ;
	}

	public SteuerkategoriekontoDto getKategorieKonto() {
		return steuerKategorieKonto;
	}

	public void setKategorieKonto(SteuerkategoriekontoDto steuerKategorieKonto) {
		this.steuerKategorieKonto = steuerKategorieKonto;
	}

	public SteuerkategorieDto getKategorie() {
		return steuerKategorie;
	}

	public void setKategorie(SteuerkategorieDto steuerKategorie) {
		this.steuerKategorie= steuerKategorie;
	}
		
	public String getKontoVerwendung() {
		return kontoVerwendung;
	}

	public void setKontoVerwendung(String kontoBez) {
		this.kontoVerwendung = kontoBez;
	}

	public String getKontoCnr() {
		return kontoCnr;
	}

	public void setKontoCnr(String kontoCnr) {
		this.kontoCnr = kontoCnr;
	}

	public String getKontoBez() {
		return kontoBez;
	}

	public void setKontoBez(String kontoBez) {
		this.kontoBez = kontoBez;
	}

	public String getMwstsatzBezeichnung() {
		return getMwstsatzbezDto().getCBezeichnung() ;
	}

	public MwstsatzbezDto getMwstsatzbezDto() {
		return mwstsatzbezDto;
	}

	public void setMwstsatzbezDto(MwstsatzbezDto mwstsatzbezDto) {
		this.mwstsatzbezDto = mwstsatzbezDto;
	}
}
