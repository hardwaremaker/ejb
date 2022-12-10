package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRHvmabenutzerparameter implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_kategorie;

    /** nullable persistent field */
    private String c_wert;

    /** nullable persistent field */
    private Integer hvmabenutzer_i_id;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRHvmabenutzer flrhvmabenutzer;

    /** full constructor */
    public FLRHvmabenutzerparameter(String c_nr, String c_kategorie, String c_wert, Integer hvmabenutzer_i_id, com.lp.server.personal.fastlanereader.generated.FLRHvmabenutzer flrhvmabenutzer) {
        this.c_nr = c_nr;
        this.c_kategorie = c_kategorie;
        this.c_wert = c_wert;
        this.hvmabenutzer_i_id = hvmabenutzer_i_id;
        this.flrhvmabenutzer = flrhvmabenutzer;
    }

    /** default constructor */
    public FLRHvmabenutzerparameter() {
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

    public String getC_kategorie() {
        return this.c_kategorie;
    }

    public void setC_kategorie(String c_kategorie) {
        this.c_kategorie = c_kategorie;
    }

    public String getC_wert() {
        return this.c_wert;
    }

    public void setC_wert(String c_wert) {
        this.c_wert = c_wert;
    }

    public Integer getHvmabenutzer_i_id() {
        return this.hvmabenutzer_i_id;
    }

    public void setHvmabenutzer_i_id(Integer hvmabenutzer_i_id) {
        this.hvmabenutzer_i_id = hvmabenutzer_i_id;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRHvmabenutzer getFlrhvmabenutzer() {
        return this.flrhvmabenutzer;
    }

    public void setFlrhvmabenutzer(com.lp.server.personal.fastlanereader.generated.FLRHvmabenutzer flrhvmabenutzer) {
        this.flrhvmabenutzer = flrhvmabenutzer;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
