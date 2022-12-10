package com.lp.server.bestellung.service;

import java.io.Serializable;

import com.lp.server.artikel.service.ArtikelDto;

public class BestellpositionServiceDto implements Serializable {
	private static final long serialVersionUID = 5063103040110553191L;

	private BestellpositionDto positionDto;
	private ArtikelDto artikelDto;
	
	public BestellpositionServiceDto(BestellpositionDto positionDto) {
		setPositionDto(positionDto);
	}

	public BestellpositionServiceDto(BestellpositionDto positionDto, ArtikelDto artikelDto) {
		setPositionDto(positionDto);
		setArtikelDto(artikelDto);
	}
	
	public BestellpositionDto getPositionDto() {
		return positionDto;
	}

	public void setPositionDto(BestellpositionDto positionDto) {
		this.positionDto = positionDto;
	}

	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}
	
	public Integer getArtikelIId() {
		return getPositionDto().getArtikelIId();
	}
}
