package com.lp.server.personal.fastlanereader.generated.service;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRHvmaparameterPK implements Serializable {

    /** identifier field */
    private String c_nr;

    /** identifier field */
    private String c_kategorie;

    /** full constructor */
    public FLRHvmaparameterPK(String c_nr, String c_kategorie) {
        this.c_nr = c_nr;
        this.c_kategorie = c_kategorie;
    }

    /** default constructor */
    public FLRHvmaparameterPK() {
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("c_nr", getC_nr())
            .append("c_kategorie", getC_kategorie())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRHvmaparameterPK) ) return false;
        FLRHvmaparameterPK castOther = (FLRHvmaparameterPK) other;
        return new EqualsBuilder()
            .append(this.getC_nr(), castOther.getC_nr())
            .append(this.getC_kategorie(), castOther.getC_kategorie())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getC_nr())
            .append(getC_kategorie())
            .toHashCode();
    }

}
