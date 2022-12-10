package com.lp.server.system.service;

public class JobDetails4VendingExportDto extends JobDetailsDto {

	private static final long serialVersionUID = -2225125334077151323L;

	private Boolean bArtikel;
	private Boolean bKunden;
	private Boolean bLieferanten;
	private String cPfadPatternArtikel;
	private String cPfadPatternKunden;
	private String cPfadPatternLieferanten;
	private String cEmailFehler;
	private String cEmailErfolgreich;
	
	public JobDetails4VendingExportDto() {
	}

	public Boolean getBArtikel() {
		return bArtikel;
	}

	public void setBArtikel(Boolean bArtikel) {
		this.bArtikel = bArtikel;
	}

	public Boolean getBKunden() {
		return bKunden;
	}

	public void setBKunden(Boolean bKunden) {
		this.bKunden = bKunden;
	}

	public Boolean getBLieferanten() {
		return bLieferanten;
	}

	public void setBLieferanten(Boolean bLieferanten) {
		this.bLieferanten = bLieferanten;
	}

	public String getCPfadPatternKunden() {
		return cPfadPatternKunden;
	}

	public void setCPfadPatternKunden(String cPfadPatternKunden) {
		this.cPfadPatternKunden = cPfadPatternKunden;
	}

	public String getCPfadPatternLieferanten() {
		return cPfadPatternLieferanten;
	}

	public void setCPfadPatternLieferanten(String cPfadPatternLieferanten) {
		this.cPfadPatternLieferanten = cPfadPatternLieferanten;
	}

	public String getCEmailFehler() {
		return cEmailFehler;
	}

	public void setCEmailFehler(String cEmailFehler) {
		this.cEmailFehler = cEmailFehler;
	}

	public String getCEmailErfolgreich() {
		return cEmailErfolgreich;
	}

	public void setCEmailErfolgreich(String cEmailErfolgreich) {
		this.cEmailErfolgreich = cEmailErfolgreich;
	}

	public String getCPfadPatternArtikel() {
		return cPfadPatternArtikel;
	}

	public void setCPfadPatternArtikel(String cPfadPatternArtikel) {
		this.cPfadPatternArtikel = cPfadPatternArtikel;
	}

}
