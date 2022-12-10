package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRTaetigkeit implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String taetigkeitart_c_nr;

    /** nullable persistent field */
    private Short b_tagbuchbar;

    /** nullable persistent field */
    private Double f_bezahlt;

    /** nullable persistent field */
    private Short b_bdebuchbar;

    /** nullable persistent field */
    private Short b_darf_selber_buchen;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private String c_importkennzeichen;

    /** persistent field */
    private Set taetigkeitsprset;

    /** full constructor */
    public FLRTaetigkeit(String c_nr, String taetigkeitart_c_nr, Short b_tagbuchbar, Double f_bezahlt, Short b_bdebuchbar, Short b_darf_selber_buchen, Short b_versteckt, String c_importkennzeichen, Set taetigkeitsprset) {
        this.c_nr = c_nr;
        this.taetigkeitart_c_nr = taetigkeitart_c_nr;
        this.b_tagbuchbar = b_tagbuchbar;
        this.f_bezahlt = f_bezahlt;
        this.b_bdebuchbar = b_bdebuchbar;
        this.b_darf_selber_buchen = b_darf_selber_buchen;
        this.b_versteckt = b_versteckt;
        this.c_importkennzeichen = c_importkennzeichen;
        this.taetigkeitsprset = taetigkeitsprset;
    }

    /** default constructor */
    public FLRTaetigkeit() {
    }

    /** minimal constructor */
    public FLRTaetigkeit(Set taetigkeitsprset) {
        this.taetigkeitsprset = taetigkeitsprset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getTaetigkeitart_c_nr() {
        return this.taetigkeitart_c_nr;
    }

    public void setTaetigkeitart_c_nr(String taetigkeitart_c_nr) {
        this.taetigkeitart_c_nr = taetigkeitart_c_nr;
    }

    public Short getB_tagbuchbar() {
        return this.b_tagbuchbar;
    }

    public void setB_tagbuchbar(Short b_tagbuchbar) {
        this.b_tagbuchbar = b_tagbuchbar;
    }

    public Double getF_bezahlt() {
        return this.f_bezahlt;
    }

    public void setF_bezahlt(Double f_bezahlt) {
        this.f_bezahlt = f_bezahlt;
    }

    public Short getB_bdebuchbar() {
        return this.b_bdebuchbar;
    }

    public void setB_bdebuchbar(Short b_bdebuchbar) {
        this.b_bdebuchbar = b_bdebuchbar;
    }

    public Short getB_darf_selber_buchen() {
        return this.b_darf_selber_buchen;
    }

    public void setB_darf_selber_buchen(Short b_darf_selber_buchen) {
        this.b_darf_selber_buchen = b_darf_selber_buchen;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public String getC_importkennzeichen() {
        return this.c_importkennzeichen;
    }

    public void setC_importkennzeichen(String c_importkennzeichen) {
        this.c_importkennzeichen = c_importkennzeichen;
    }

    public Set getTaetigkeitsprset() {
        return this.taetigkeitsprset;
    }

    public void setTaetigkeitsprset(Set taetigkeitsprset) {
        this.taetigkeitsprset = taetigkeitsprset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
