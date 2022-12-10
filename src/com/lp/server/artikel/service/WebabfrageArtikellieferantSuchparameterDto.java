package com.lp.server.artikel.service;

import java.io.Serializable;

public class WebabfrageArtikellieferantSuchparameterDto implements Serializable {
	private static final long serialVersionUID = 8159884570743853441L;

	private boolean useLieferantenartikelnummer;
	private boolean useHerstellernummer;
	private Integer parameter;
	
	public WebabfrageArtikellieferantSuchparameterDto(Integer parameter) {
		this.parameter = parameter;
		process();
	}

	public boolean useLieferantenartikelnummer() {
		return useLieferantenartikelnummer;
	}
	
	public boolean useHerstellernummer() {
		return useHerstellernummer;
	}
	
	private boolean process() {
		if (this.parameter == null) return false;
		
		if (1 == this.parameter) {
			useLieferantenartikelnummer = true;
			useHerstellernummer = false;
		
		} else if (2 == this.parameter) {
			useLieferantenartikelnummer = false;
			useHerstellernummer = true;

		} else {
			useLieferantenartikelnummer = true;
			useHerstellernummer = true;
		}
		
		return true;
	}

}
