package com.lp.util;

public class LinienabrufProduktionKeinLosGefundenException extends EJBExceptionData {
	private static final long serialVersionUID = 1893705699363963424L;

	private String kundeKBezLieferadresse;
	private String artikelCnr;
	private Integer forecastpositionIId;
	
	public LinienabrufProduktionKeinLosGefundenException(String kundeKBezLieferadresse, 
			String artikelCnr, Integer forecastpositionIId) {
		setKundeKBezLieferadresse(kundeKBezLieferadresse);
		setArtikelCnr(artikelCnr);
		setForecastpositionIId(forecastpositionIId);
	}
	
	public String getArtikelCnr() {
		return artikelCnr;
	}
	
	public void setArtikelCnr(String artikelCnr) {
		this.artikelCnr = artikelCnr;
	}
	
	public Integer getForecastpositionIId() {
		return forecastpositionIId;
	}
	
	public void setForecastpositionIId(Integer forecastpositionIId) {
		this.forecastpositionIId = forecastpositionIId;
	}
	
	public void setKundeKBezLieferadresse(String kundeKBezLieferadresse) {
		this.kundeKBezLieferadresse = kundeKBezLieferadresse;
	}
	
	public String getKundeKBezLieferadresse() {
		return kundeKBezLieferadresse;
	}
}
