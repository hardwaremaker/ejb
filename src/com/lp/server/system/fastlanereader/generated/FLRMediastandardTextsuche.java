package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRMediastandardTextsuche implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String datenformat_c_nr;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String locale_c_nr;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private String c_inhalt_o_media;

    /** full constructor */
    public FLRMediastandardTextsuche(String c_nr, String datenformat_c_nr, String mandant_c_nr, String locale_c_nr, Short b_versteckt, String c_inhalt_o_media) {
        this.c_nr = c_nr;
        this.datenformat_c_nr = datenformat_c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.locale_c_nr = locale_c_nr;
        this.b_versteckt = b_versteckt;
        this.c_inhalt_o_media = c_inhalt_o_media;
    }

    /** default constructor */
    public FLRMediastandardTextsuche() {
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

    public String getDatenformat_c_nr() {
        return this.datenformat_c_nr;
    }

    public void setDatenformat_c_nr(String datenformat_c_nr) {
        this.datenformat_c_nr = datenformat_c_nr;
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

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public String getC_inhalt_o_media() {
        return this.c_inhalt_o_media;
    }

    public void setC_inhalt_o_media(String c_inhalt_o_media) {
        this.c_inhalt_o_media = c_inhalt_o_media;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
