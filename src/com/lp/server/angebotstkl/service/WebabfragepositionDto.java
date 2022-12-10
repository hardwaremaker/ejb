package com.lp.server.angebotstkl.service;

import java.io.Serializable;
import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;

public class WebabfragepositionDto implements Serializable {
	
	private static final long serialVersionUID = -3722196585912123697L;

	private EinkaufsangebotpositionDto einkaufsangebotpositionDto;
	private ArtikelDto artikelDto;
	private List<ArtikellieferantDto> artikellieferantDtos;

	public WebabfragepositionDto() {
	}

	public EinkaufsangebotpositionDto getEinkaufsangebotpositionDto() {
		return einkaufsangebotpositionDto;
	}

	public void setEinkaufsangebotpositionDto(
			EinkaufsangebotpositionDto einkaufsangebotpositionDto) {
		this.einkaufsangebotpositionDto = einkaufsangebotpositionDto;
	}

	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	public List<ArtikellieferantDto> getArtikellieferantDtos() {
		return artikellieferantDtos;
	}

	public void setArtikellieferantDtos(
			List<ArtikellieferantDto> artikellieferantDtos) {
		this.artikellieferantDtos = artikellieferantDtos;
	}

	public Boolean hasArtikelDto() {
		return getArtikelDto() != null;
	}
	
	public Boolean hasArtikellieferantDto() {
		return getArtikellieferantDtos() != null || getArtikellieferantDtos().isEmpty();
	}
}
