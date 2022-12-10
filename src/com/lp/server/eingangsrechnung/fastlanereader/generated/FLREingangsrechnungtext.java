package com.lp.server.eingangsrechnung.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLREingangsrechnungtext implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String locale_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_textinhalt;

    /** full constructor */
    public FLREingangsrechnungtext(String mandant_c_nr, String locale_c_nr, String c_nr, String c_textinhalt) {
        this.mandant_c_nr = mandant_c_nr;
        this.locale_c_nr = locale_c_nr;
        this.c_nr = c_nr;
        this.c_textinhalt = c_textinhalt;
    }

    /** default constructor */
    public FLREingangsrechnungtext() {
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

    public String getLocale_c_nr() {
        return this.locale_c_nr;
    }

    public void setLocale_c_nr(String locale_c_nr) {
        this.locale_c_nr = locale_c_nr;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getC_textinhalt() {
        return this.c_textinhalt;
    }

    public void setC_textinhalt(String c_textinhalt) {
        this.c_textinhalt = c_textinhalt;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
