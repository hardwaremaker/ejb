package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRFahrzeug implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_kennzeichen;

    /** nullable persistent field */
    private String fahrzeugverwendungsart_c_nr;

    /** nullable persistent field */
    private Short b_versteckt;

    /** full constructor */
    public FLRFahrzeug(String mandant_c_nr, String c_bez, String c_kennzeichen, String fahrzeugverwendungsart_c_nr, Short b_versteckt) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_bez = c_bez;
        this.c_kennzeichen = c_kennzeichen;
        this.fahrzeugverwendungsart_c_nr = fahrzeugverwendungsart_c_nr;
        this.b_versteckt = b_versteckt;
    }

    /** default constructor */
    public FLRFahrzeug() {
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

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_kennzeichen() {
        return this.c_kennzeichen;
    }

    public void setC_kennzeichen(String c_kennzeichen) {
        this.c_kennzeichen = c_kennzeichen;
    }

    public String getFahrzeugverwendungsart_c_nr() {
        return this.fahrzeugverwendungsart_c_nr;
    }

    public void setFahrzeugverwendungsart_c_nr(String fahrzeugverwendungsart_c_nr) {
        this.fahrzeugverwendungsart_c_nr = fahrzeugverwendungsart_c_nr;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
