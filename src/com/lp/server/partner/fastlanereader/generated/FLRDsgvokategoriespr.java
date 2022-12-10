package com.lp.server.partner.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRLocale;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRDsgvokategoriespr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.partner.fastlanereader.generated.FLRDsgvokategorie identifikation;

    /** identifier field */
    private FLRLocale locale;

    /** full constructor */
    public FLRDsgvokategoriespr(String c_bez, com.lp.server.partner.fastlanereader.generated.FLRDsgvokategorie identifikation, FLRLocale locale) {
        this.c_bez = c_bez;
        this.identifikation = identifikation;
        this.locale = locale;
    }

    /** default constructor */
    public FLRDsgvokategoriespr() {
    }

    /** minimal constructor */
    public FLRDsgvokategoriespr(com.lp.server.partner.fastlanereader.generated.FLRDsgvokategorie identifikation, FLRLocale locale) {
        this.identifikation = identifikation;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRDsgvokategorie getIdentifikation() {
        return this.identifikation;
    }

    public void setIdentifikation(com.lp.server.partner.fastlanereader.generated.FLRDsgvokategorie identifikation) {
        this.identifikation = identifikation;
    }

    public FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("identifikation", getIdentifikation())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRDsgvokategoriespr) ) return false;
        FLRDsgvokategoriespr castOther = (FLRDsgvokategoriespr) other;
        return new EqualsBuilder()
            .append(this.getIdentifikation(), castOther.getIdentifikation())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getIdentifikation())
            .append(getLocale())
            .toHashCode();
    }

}
