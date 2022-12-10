package com.lp.server.bestellung.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBSZahlungsplan implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date t_termin;

    /** nullable persistent field */
    private BigDecimal n_betrag;

    /** nullable persistent field */
    private BigDecimal n_betrag_ursprung;

    /** persistent field */
    private Integer bestellung_i_id;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private String x_text;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung;

    /** nullable persistent field */
    private FLRPersonal flrpersonalerledigt;

    /** full constructor */
    public FLRBSZahlungsplan(Date t_termin, BigDecimal n_betrag, BigDecimal n_betrag_ursprung, Integer bestellung_i_id, String c_kommentar, String x_text, Date t_erledigt, com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung, FLRPersonal flrpersonalerledigt) {
        this.t_termin = t_termin;
        this.n_betrag = n_betrag;
        this.n_betrag_ursprung = n_betrag_ursprung;
        this.bestellung_i_id = bestellung_i_id;
        this.c_kommentar = c_kommentar;
        this.x_text = x_text;
        this.t_erledigt = t_erledigt;
        this.flrbestellung = flrbestellung;
        this.flrpersonalerledigt = flrpersonalerledigt;
    }

    /** default constructor */
    public FLRBSZahlungsplan() {
    }

    /** minimal constructor */
    public FLRBSZahlungsplan(Integer bestellung_i_id) {
        this.bestellung_i_id = bestellung_i_id;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getT_termin() {
        return this.t_termin;
    }

    public void setT_termin(Date t_termin) {
        this.t_termin = t_termin;
    }

    public BigDecimal getN_betrag() {
        return this.n_betrag;
    }

    public void setN_betrag(BigDecimal n_betrag) {
        this.n_betrag = n_betrag;
    }

    public BigDecimal getN_betrag_ursprung() {
        return this.n_betrag_ursprung;
    }

    public void setN_betrag_ursprung(BigDecimal n_betrag_ursprung) {
        this.n_betrag_ursprung = n_betrag_ursprung;
    }

    public Integer getBestellung_i_id() {
        return this.bestellung_i_id;
    }

    public void setBestellung_i_id(Integer bestellung_i_id) {
        this.bestellung_i_id = bestellung_i_id;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public String getX_text() {
        return this.x_text;
    }

    public void setX_text(String x_text) {
        this.x_text = x_text;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRBestellung getFlrbestellung() {
        return this.flrbestellung;
    }

    public void setFlrbestellung(com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung) {
        this.flrbestellung = flrbestellung;
    }

    public FLRPersonal getFlrpersonalerledigt() {
        return this.flrpersonalerledigt;
    }

    public void setFlrpersonalerledigt(FLRPersonal flrpersonalerledigt) {
        this.flrpersonalerledigt = flrpersonalerledigt;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
