package com.lp.server.forecast.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;

public class FLRForecastpositionProduktion implements Serializable {
	private static final long serialVersionUID = -6402048399362289917L;

    private Integer i_id;
    private BigDecimal n_menge;
    private Date t_termin;
    private String c_bestellnummer;
    private String x_kommentar;
    private String forecastart_c_nr;
    private String status_c_nr;
    private com.lp.server.forecast.fastlanereader.generated.FLRForecastauftrag flrforecastauftrag;
    private FLRArtikel flrartikel;
    private FLRLos flrlos;
    private Set<FLRLinienabruf> linienabrufset;
    private Integer personal_i_id_produktion;
    
    public FLRForecastpositionProduktion() {
	}

	public Integer getI_id() {
		return i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public BigDecimal getN_menge() {
		return n_menge;
	}

	public void setN_menge(BigDecimal n_menge) {
		this.n_menge = n_menge;
	}

	public Date getT_termin() {
		return t_termin;
	}

	public void setT_termin(Date t_termin) {
		this.t_termin = t_termin;
	}

	public String getC_bestellnummer() {
		return c_bestellnummer;
	}

	public void setC_bestellnummer(String c_bestellnummer) {
		this.c_bestellnummer = c_bestellnummer;
	}

	public String getX_kommentar() {
		return x_kommentar;
	}

	public void setX_kommentar(String x_kommentar) {
		this.x_kommentar = x_kommentar;
	}

	public String getForecastart_c_nr() {
		return forecastart_c_nr;
	}

	public void setForecastart_c_nr(String forecastart_c_nr) {
		this.forecastart_c_nr = forecastart_c_nr;
	}

	public String getStatus_c_nr() {
		return status_c_nr;
	}

	public void setStatus_c_nr(String status_c_nr) {
		this.status_c_nr = status_c_nr;
	}

	public com.lp.server.forecast.fastlanereader.generated.FLRForecastauftrag getFlrforecastauftrag() {
		return flrforecastauftrag;
	}

	public void setFlrforecastauftrag(
			com.lp.server.forecast.fastlanereader.generated.FLRForecastauftrag flrforecastauftrag) {
		this.flrforecastauftrag = flrforecastauftrag;
	}

	public FLRArtikel getFlrartikel() {
		return flrartikel;
	}

	public void setFlrartikel(FLRArtikel flrartikel) {
		this.flrartikel = flrartikel;
	}

	public FLRLos getFlrlos() {
		return flrlos;
	}

	public void setFlrlos(FLRLos flrlos) {
		this.flrlos = flrlos;
	}

	public Set<FLRLinienabruf> getLinienabrufset() {
		return linienabrufset;
	}

	public void setLinienabrufset(Set<FLRLinienabruf> linienabrufset) {
		this.linienabrufset = linienabrufset;
	}

	public Integer getPersonal_i_id_produktion() {
		return personal_i_id_produktion;
	}
	
	public void setPersonal_i_id_produktion(Integer personal_i_id_produktion) {
		this.personal_i_id_produktion = personal_i_id_produktion;
	}
}
