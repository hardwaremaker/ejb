package com.lp.util;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.WebPartSearchResult;

public class WebabfrageArtikellieferantException extends EJBExceptionData {
	private static final long serialVersionUID = 7388299989958836538L;

	private ArtikelDto artikelDto;
	private ArtikellieferantDto artikellieferantDto;
	private WebPartSearchResult resultLiefNr;
	private WebPartSearchResult resultHstNr;

	public WebabfrageArtikellieferantException(ArtikelDto artikelDto, ArtikellieferantDto artikellieferantDto, WebPartSearchResult resultLiefNr, WebPartSearchResult resultHstNr) {
		setArtikelDto(artikelDto);
		setArtikellieferantDto(artikellieferantDto);
		setResultHstNr(resultHstNr);
		setResultLiefNr(resultLiefNr);
	}

	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}
	
	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}
	
	public ArtikellieferantDto getArtikellieferantDto() {
		return artikellieferantDto;
	}
	
	public void setArtikellieferantDto(ArtikellieferantDto artikellieferantDto) {
		this.artikellieferantDto = artikellieferantDto;
	}
	
	public WebPartSearchResult getResultLiefNr() {
		return resultLiefNr;
	}
	
	public void setResultLiefNr(WebPartSearchResult resultLiefNr) {
		this.resultLiefNr = resultLiefNr;
	}
	
	public WebPartSearchResult getResultHstNr() {
		return resultHstNr;
	}
	
	public void setResultHstNr(WebPartSearchResult resultHstNr) {
		this.resultHstNr = resultHstNr;
	}
}
