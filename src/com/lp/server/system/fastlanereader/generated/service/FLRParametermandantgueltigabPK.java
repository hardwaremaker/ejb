package com.lp.server.system.fastlanereader.generated.service;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRParametermandantgueltigabPK implements Serializable {

    /** identifier field */
    private String c_nr;

    /** identifier field */
    private String mandant_c_nr;

    /** identifier field */
    private String c_kategorie;

    /** identifier field */
    private Date t_gueltigab;

    /** full constructor */
    public FLRParametermandantgueltigabPK(String c_nr, String mandant_c_nr, String c_kategorie, Date t_gueltigab) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.c_kategorie = c_kategorie;
        this.t_gueltigab = t_gueltigab;
    }

    /** default constructor */
    public FLRParametermandantgueltigabPK() {
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

    public String getC_kategorie() {
        return this.c_kategorie;
    }

    public void setC_kategorie(String c_kategorie) {
        this.c_kategorie = c_kategorie;
    }

    public Date getT_gueltigab() {
        return this.t_gueltigab;
    }

    public void setT_gueltigab(Date t_gueltigab) {
        this.t_gueltigab = t_gueltigab;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("c_nr", getC_nr())
            .append("mandant_c_nr", getMandant_c_nr())
            .append("c_kategorie", getC_kategorie())
            .append("t_gueltigab", getT_gueltigab())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRParametermandantgueltigabPK) ) return false;
        FLRParametermandantgueltigabPK castOther = (FLRParametermandantgueltigabPK) other;
        return new EqualsBuilder()
            .append(this.getC_nr(), castOther.getC_nr())
            .append(this.getMandant_c_nr(), castOther.getMandant_c_nr())
            .append(this.getC_kategorie(), castOther.getC_kategorie())
            .append(this.getT_gueltigab(), castOther.getT_gueltigab())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getC_nr())
            .append(getMandant_c_nr())
            .append(getC_kategorie())
            .append(getT_gueltigab())
            .toHashCode();
    }

}
