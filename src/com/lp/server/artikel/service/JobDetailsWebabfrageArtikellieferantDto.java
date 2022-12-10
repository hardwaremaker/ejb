package com.lp.server.artikel.service;

import com.lp.server.system.service.JobDetailsDto;

public class JobDetailsWebabfrageArtikellieferantDto extends JobDetailsDto {
	private static final long serialVersionUID = -4299547556823192747L;

	private String cEmailErfolgreich;
	private String cEmailFehler;
	
	public JobDetailsWebabfrageArtikellieferantDto() {
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
