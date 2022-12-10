package com.lp.server.forecast.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRForecastposition implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private Date t_termin;

    /** nullable persistent field */
    private String c_bestellnummer;

    /** nullable persistent field */
    private String x_kommentar;

    /** nullable persistent field */
    private String forecastart_c_nr;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private com.lp.server.forecast.fastlanereader.generated.FLRForecastauftrag flrforecastauftrag;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRArtikelliste flrartikelliste;

    /** persistent field */
    private Set linienabrufset;

    /** persistent field */
    private Set losset;

    /** full constructor */
    public FLRForecastposition(BigDecimal n_menge, Date t_termin, String c_bestellnummer, String x_kommentar, String forecastart_c_nr, String status_c_nr, com.lp.server.forecast.fastlanereader.generated.FLRForecastauftrag flrforecastauftrag, FLRArtikel flrartikel, FLRArtikelliste flrartikelliste, Set linienabrufset, Set losset) {
        this.n_menge = n_menge;
        this.t_termin = t_termin;
        this.c_bestellnummer = c_bestellnummer;
        this.x_kommentar = x_kommentar;
        this.forecastart_c_nr = forecastart_c_nr;
        this.status_c_nr = status_c_nr;
        this.flrforecastauftrag = flrforecastauftrag;
        this.flrartikel = flrartikel;
        this.flrartikelliste = flrartikelliste;
        this.linienabrufset = linienabrufset;
        this.losset = losset;
    }

    /** default constructor */
    public FLRForecastposition() {
    }

    /** minimal constructor */
    public FLRForecastposition(Set linienabrufset, Set losset) {
        this.linienabrufset = linienabrufset;
        this.losset = losset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public Date getT_termin() {
        return this.t_termin;
    }

    public void setT_termin(Date t_termin) {
        this.t_termin = t_termin;
    }

    public String getC_bestellnummer() {
        return this.c_bestellnummer;
    }

    public void setC_bestellnummer(String c_bestellnummer) {
        this.c_bestellnummer = c_bestellnummer;
    }

    public String getX_kommentar() {
        return this.x_kommentar;
    }

    public void setX_kommentar(String x_kommentar) {
        this.x_kommentar = x_kommentar;
    }

    public String getForecastart_c_nr() {
        return this.forecastart_c_nr;
    }

    public void setForecastart_c_nr(String forecastart_c_nr) {
        this.forecastart_c_nr = forecastart_c_nr;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public com.lp.server.forecast.fastlanereader.generated.FLRForecastauftrag getFlrforecastauftrag() {
        return this.flrforecastauftrag;
    }

    public void setFlrforecastauftrag(com.lp.server.forecast.fastlanereader.generated.FLRForecastauftrag flrforecastauftrag) {
        this.flrforecastauftrag = flrforecastauftrag;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRArtikelliste getFlrartikelliste() {
        return this.flrartikelliste;
    }

    public void setFlrartikelliste(FLRArtikelliste flrartikelliste) {
        this.flrartikelliste = flrartikelliste;
    }

    public Set getLinienabrufset() {
        return this.linienabrufset;
    }

    public void setLinienabrufset(Set linienabrufset) {
        this.linienabrufset = linienabrufset;
    }

    public Set getLosset() {
        return this.losset;
    }

    public void setLosset(Set losset) {
        this.losset = losset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
