package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZahltag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_monat;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Integer i_stichtag_netto;

    /** nullable persistent field */
    private Integer i_stichtag_lohnsteuer_relativ_zum_letzten;

    /** nullable persistent field */
    private Integer i_stichtag_sozialabgaben_relativ_zum_letzten;

    /** nullable persistent field */
    private Double f_faktor;

    /** nullable persistent field */
    private Short b_monatsletzter_netto;

    /** full constructor */
    public FLRZahltag(Integer i_monat, String mandant_c_nr, Integer i_stichtag_netto, Integer i_stichtag_lohnsteuer_relativ_zum_letzten, Integer i_stichtag_sozialabgaben_relativ_zum_letzten, Double f_faktor, Short b_monatsletzter_netto) {
        this.i_monat = i_monat;
        this.mandant_c_nr = mandant_c_nr;
        this.i_stichtag_netto = i_stichtag_netto;
        this.i_stichtag_lohnsteuer_relativ_zum_letzten = i_stichtag_lohnsteuer_relativ_zum_letzten;
        this.i_stichtag_sozialabgaben_relativ_zum_letzten = i_stichtag_sozialabgaben_relativ_zum_letzten;
        this.f_faktor = f_faktor;
        this.b_monatsletzter_netto = b_monatsletzter_netto;
    }

    /** default constructor */
    public FLRZahltag() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_monat() {
        return this.i_monat;
    }

    public void setI_monat(Integer i_monat) {
        this.i_monat = i_monat;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Integer getI_stichtag_netto() {
        return this.i_stichtag_netto;
    }

    public void setI_stichtag_netto(Integer i_stichtag_netto) {
        this.i_stichtag_netto = i_stichtag_netto;
    }

    public Integer getI_stichtag_lohnsteuer_relativ_zum_letzten() {
        return this.i_stichtag_lohnsteuer_relativ_zum_letzten;
    }

    public void setI_stichtag_lohnsteuer_relativ_zum_letzten(Integer i_stichtag_lohnsteuer_relativ_zum_letzten) {
        this.i_stichtag_lohnsteuer_relativ_zum_letzten = i_stichtag_lohnsteuer_relativ_zum_letzten;
    }

    public Integer getI_stichtag_sozialabgaben_relativ_zum_letzten() {
        return this.i_stichtag_sozialabgaben_relativ_zum_letzten;
    }

    public void setI_stichtag_sozialabgaben_relativ_zum_letzten(Integer i_stichtag_sozialabgaben_relativ_zum_letzten) {
        this.i_stichtag_sozialabgaben_relativ_zum_letzten = i_stichtag_sozialabgaben_relativ_zum_letzten;
    }

    public Double getF_faktor() {
        return this.f_faktor;
    }

    public void setF_faktor(Double f_faktor) {
        this.f_faktor = f_faktor;
    }

    public Short getB_monatsletzter_netto() {
        return this.b_monatsletzter_netto;
    }

    public void setB_monatsletzter_netto(Short b_monatsletzter_netto) {
        this.b_monatsletzter_netto = b_monatsletzter_netto;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
