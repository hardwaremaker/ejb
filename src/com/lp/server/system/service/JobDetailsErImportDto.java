package com.lp.server.system.service;

public class JobDetailsErImportDto extends JobDetailsDto {
	private static final long serialVersionUID = 6130941610726797265L;

	private String cImportPfad;
	private String cEmailErfolgreich;
	private String cEmailFehler;
	
	public JobDetailsErImportDto() {
	}

	public String getCImportPfad() {
		return cImportPfad;
	}
	
	public void setCImportPfad(String cImportPfad) {
		this.cImportPfad = cImportPfad;
	}
	
	public String getCEmailErfolgreich() {
		return cEmailErfolgreich;
	}
	
	public void setCEmailErfolgreich(String cEmailErfolgreich) {
		this.cEmailErfolgreich = cEmailErfolgreich;
	}
	
	public String getCEmailFehler() {
		return cEmailFehler;
	}
	
	public void setCEmailFehler(String cEmailFehler) {
		this.cEmailFehler = cEmailFehler;
	}
}
