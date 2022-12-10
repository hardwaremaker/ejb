package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRDateiverweis implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_laufwerk;

    /** nullable persistent field */
    private String c_unc;

    /** full constructor */
    public FLRDateiverweis(String mandant_c_nr, String c_laufwerk, String c_unc) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_laufwerk = c_laufwerk;
        this.c_unc = c_unc;
    }

    /** default constructor */
    public FLRDateiverweis() {
    }

    /** minimal constructor */
    public FLRDateiverweis(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getC_laufwerk() {
        return this.c_laufwerk;
    }

    public void setC_laufwerk(String c_laufwerk) {
        this.c_laufwerk = c_laufwerk;
    }

    public String getC_unc() {
        return this.c_unc;
    }

    public void setC_unc(String c_unc) {
        this.c_unc = c_unc;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
