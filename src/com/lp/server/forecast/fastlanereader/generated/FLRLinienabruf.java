package com.lp.server.forecast.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLinienabruf implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer forecastposition_i_id;

    /** nullable persistent field */
    private String c_linie;

    /** nullable persistent field */
    private String c_bereich_nr;

    /** nullable persistent field */
    private String c_bereich_bez;

    /** nullable persistent field */
    private String c_bestellnummer;

    /** nullable persistent field */
    private Date t_produktionstermin;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private com.lp.server.forecast.fastlanereader.generated.FLRForecastposition flrforecastposition;

    /** full constructor */
    public FLRLinienabruf(Integer forecastposition_i_id, String c_linie, String c_bereich_nr, String c_bereich_bez, String c_bestellnummer, Date t_produktionstermin, BigDecimal n_menge, com.lp.server.forecast.fastlanereader.generated.FLRForecastposition flrforecastposition) {
        this.forecastposition_i_id = forecastposition_i_id;
        this.c_linie = c_linie;
        this.c_bereich_nr = c_bereich_nr;
        this.c_bereich_bez = c_bereich_bez;
        this.c_bestellnummer = c_bestellnummer;
        this.t_produktionstermin = t_produktionstermin;
        this.n_menge = n_menge;
        this.flrforecastposition = flrforecastposition;
    }

    /** default constructor */
    public FLRLinienabruf() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getForecastposition_i_id() {
        return this.forecastposition_i_id;
    }

    public void setForecastposition_i_id(Integer forecastposition_i_id) {
        this.forecastposition_i_id = forecastposition_i_id;
    }

    public String getC_linie() {
        return this.c_linie;
    }

    public void setC_linie(String c_linie) {
        this.c_linie = c_linie;
    }

    public String getC_bereich_nr() {
        return this.c_bereich_nr;
    }

    public void setC_bereich_nr(String c_bereich_nr) {
        this.c_bereich_nr = c_bereich_nr;
    }

    public String getC_bereich_bez() {
        return this.c_bereich_bez;
    }

    public void setC_bereich_bez(String c_bereich_bez) {
        this.c_bereich_bez = c_bereich_bez;
    }

    public String getC_bestellnummer() {
        return this.c_bestellnummer;
    }

    public void setC_bestellnummer(String c_bestellnummer) {
        this.c_bestellnummer = c_bestellnummer;
    }

    public Date getT_produktionstermin() {
        return this.t_produktionstermin;
    }

    public void setT_produktionstermin(Date t_produktionstermin) {
        this.t_produktionstermin = t_produktionstermin;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public com.lp.server.forecast.fastlanereader.generated.FLRForecastposition getFlrforecastposition() {
        return this.flrforecastposition;
    }

    public void setFlrforecastposition(com.lp.server.forecast.fastlanereader.generated.FLRForecastposition flrforecastposition) {
        this.flrforecastposition = flrforecastposition;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
