package com.lp.server.artikel.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArtikelMitVerpackungsgroessenDto implements Serializable {
	private static final long serialVersionUID = -3261721369225885734L;

	private ArtikelDto artikelDto ;
	private List<EinkaufseanDto> eanDtos ;
	
	public ArtikelMitVerpackungsgroessenDto() {
		eanDtos = new ArrayList<EinkaufseanDto>() ;
	}
	
	public ArtikelMitVerpackungsgroessenDto(ArtikelDto artikelDto) {
		setArtikelDto(artikelDto);
		setEanDtos(new ArrayList<EinkaufseanDto>()); 	
	}
	
	public ArtikelMitVerpackungsgroessenDto(ArtikelDto artikelDto, List<EinkaufseanDto> eanDtos) {
		setArtikelDto(artikelDto);
		setEanDtos(eanDtos); 	
	}
	
	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	public List<EinkaufseanDto> getEanDtos() {
		return eanDtos;
	}

	public void setEanDtos(List<EinkaufseanDto> eanDtos) {
		this.eanDtos = eanDtos;
	}
	
	public boolean hatVerpackungen() {
		return !getEanDtos().isEmpty();
	}
}
