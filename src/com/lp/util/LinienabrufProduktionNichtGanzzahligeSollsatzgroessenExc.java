package com.lp.util;

import com.lp.server.fertigung.service.LosDto;

public class LinienabrufProduktionNichtGanzzahligeSollsatzgroessenExc extends
		EJBExceptionData {
	private static final long serialVersionUID = -3958748504532782166L;

	private LosDto losDto;
	private String artikelCnr;
	
	public LinienabrufProduktionNichtGanzzahligeSollsatzgroessenExc(LosDto losDto, String artikelCnr) {
		setLosDto(losDto);
		setArtikelCnr(artikelCnr);
	}
	
	public void setLosDto(LosDto losDto) {
		this.losDto = losDto;
	}
	
	public LosDto getLosDto() {
		return losDto;
	}
	
	public void setArtikelCnr(String artikelCnr) {
		this.artikelCnr = artikelCnr;
	}
	
	public String getArtikelCnr() {
		return artikelCnr;
	}
}
