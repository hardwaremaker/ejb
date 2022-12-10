package com.lp.server.forecast.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRForecastpositionLiefersituation implements Serializable {

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
    private Integer personal_i_id_produktion;

    /** nullable persistent field */
    private com.lp.server.forecast.fastlanereader.generated.FLRForecastauftrag flrforecastauftrag;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** persistent field */
    private Set lieferscheinpositionen_set;

    /** persistent field */
    private Set lose_set;

    /** full constructor */
    public FLRForecastpositionLiefersituation(BigDecimal n_menge, Date t_termin, String c_bestellnummer, String x_kommentar, String forecastart_c_nr, String status_c_nr, Integer personal_i_id_produktion, com.lp.server.forecast.fastlanereader.generated.FLRForecastauftrag flrforecastauftrag, FLRArtikel flrartikel, Set lieferscheinpositionen_set, Set lose_set) {
        this.n_menge = n_menge;
        this.t_termin = t_termin;
        this.c_bestellnummer = c_bestellnummer;
        this.x_kommentar = x_kommentar;
        this.forecastart_c_nr = forecastart_c_nr;
        this.status_c_nr = status_c_nr;
        this.personal_i_id_produktion = personal_i_id_produktion;
        this.flrforecastauftrag = flrforecastauftrag;
        this.flrartikel = flrartikel;
        this.lieferscheinpositionen_set = lieferscheinpositionen_set;
        this.lose_set = lose_set;
    }

    /** default constructor */
    public FLRForecastpositionLiefersituation() {
    }

    /** minimal constructor */
    public FLRForecastpositionLiefersituation(Set lieferscheinpositionen_set, Set lose_set) {
        this.lieferscheinpositionen_set = lieferscheinpositionen_set;
        this.lose_set = lose_set;
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

    public Integer getPersonal_i_id_produktion() {
        return this.personal_i_id_produktion;
    }

    public void setPersonal_i_id_produktion(Integer personal_i_id_produktion) {
        this.personal_i_id_produktion = personal_i_id_produktion;
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

    public Set getLieferscheinpositionen_set() {
        return this.lieferscheinpositionen_set;
    }

    public void setLieferscheinpositionen_set(Set lieferscheinpositionen_set) {
        this.lieferscheinpositionen_set = lieferscheinpositionen_set;
    }

    public Set getLose_set() {
        return this.lose_set;
    }

    public void setLose_set(Set lose_set) {
        this.lose_set = lose_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
