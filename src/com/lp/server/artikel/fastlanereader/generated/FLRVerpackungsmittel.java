package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVerpackungsmittel implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private BigDecimal n_gewicht_in_kg;

    /** persistent field */
    private Set verpackungsmittelspr_set;

    /** full constructor */
    public FLRVerpackungsmittel(String c_nr, String mandant_c_nr, BigDecimal n_gewicht_in_kg, Set verpackungsmittelspr_set) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.n_gewicht_in_kg = n_gewicht_in_kg;
        this.verpackungsmittelspr_set = verpackungsmittelspr_set;
    }

    /** default constructor */
    public FLRVerpackungsmittel() {
    }

    /** minimal constructor */
    public FLRVerpackungsmittel(Set verpackungsmittelspr_set) {
        this.verpackungsmittelspr_set = verpackungsmittelspr_set;
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

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public BigDecimal getN_gewicht_in_kg() {
        return this.n_gewicht_in_kg;
    }

    public void setN_gewicht_in_kg(BigDecimal n_gewicht_in_kg) {
        this.n_gewicht_in_kg = n_gewicht_in_kg;
    }

    public Set getVerpackungsmittelspr_set() {
        return this.verpackungsmittelspr_set;
    }

    public void setVerpackungsmittelspr_set(Set verpackungsmittelspr_set) {
        this.verpackungsmittelspr_set = verpackungsmittelspr_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
