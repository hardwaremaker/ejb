package com.lp.server.finanz.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRFinanzUVAArt implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandantCNr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_kennzeichen;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Short b_keine_auswahl_bei_er;

    /** full constructor */
    public FLRFinanzUVAArt(String mandantCNr, String c_nr, String c_kennzeichen, Integer i_sort, Short b_keine_auswahl_bei_er) {
        this.mandantCNr = mandantCNr;
        this.c_nr = c_nr;
        this.c_kennzeichen = c_kennzeichen;
        this.i_sort = i_sort;
        this.b_keine_auswahl_bei_er = b_keine_auswahl_bei_er;
    }

    /** default constructor */
    public FLRFinanzUVAArt() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getMandantCNr() {
        return this.mandantCNr;
    }

    public void setMandantCNr(String mandantCNr) {
        this.mandantCNr = mandantCNr;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getC_kennzeichen() {
        return this.c_kennzeichen;
    }

    public void setC_kennzeichen(String c_kennzeichen) {
        this.c_kennzeichen = c_kennzeichen;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Short getB_keine_auswahl_bei_er() {
        return this.b_keine_auswahl_bei_er;
    }

    public void setB_keine_auswahl_bei_er(Short b_keine_auswahl_bei_er) {
        this.b_keine_auswahl_bei_er = b_keine_auswahl_bei_er;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
