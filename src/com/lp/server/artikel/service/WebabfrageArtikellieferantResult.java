package com.lp.server.artikel.service;

import java.io.Serializable;

public class WebabfrageArtikellieferantResult implements Serializable {
	private static final long serialVersionUID = -4700320920811457373L;

	private WebPartSearchResult webPartSearchResult;
	private ArtikellieferantDto artikellieferantDto;
	private boolean artikellieferantUpdated;
	
	public WebabfrageArtikellieferantResult(ArtikellieferantDto artikellieferantDto) {
		setArtikellieferantDto(artikellieferantDto);
		artikellieferantUpdated = false;
	}

	public WebabfrageArtikellieferantResult(ArtikellieferantDto artikellieferantDto, WebPartSearchResult webPartSearchResult) {
		this(artikellieferantDto);
		setWebPartSearchResult(webPartSearchResult);
	}

	public WebPartSearchResult getWebPartSearchResult() {
		return webPartSearchResult;
	}
	
	public void setWebPartSearchResult(WebPartSearchResult webPartSearchResult) {
		this.webPartSearchResult = webPartSearchResult;
	}
	
	public ArtikellieferantDto getArtikellieferantDto() {
		return artikellieferantDto;
	}
	
	public void setArtikellieferantDto(ArtikellieferantDto artikellieferantDto) {
		this.artikellieferantDto = artikellieferantDto;
	}
	
	public boolean wasArtikellieferantUpdated() {
		return artikellieferantUpdated;
	}
	
	public void setArtikellieferantUpdated(boolean artikellieferantUpdated) {
		this.artikellieferantUpdated = artikellieferantUpdated;
	}
}
