package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRLocale;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVerpackungsmittelspr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.artikel.fastlanereader.generated.FLRVerpackungsmittel verpackungsmittel;

    /** identifier field */
    private FLRLocale locale;

    /** full constructor */
    public FLRVerpackungsmittelspr(String c_bez, com.lp.server.artikel.fastlanereader.generated.FLRVerpackungsmittel verpackungsmittel, FLRLocale locale) {
        this.c_bez = c_bez;
        this.verpackungsmittel = verpackungsmittel;
        this.locale = locale;
    }

    /** default constructor */
    public FLRVerpackungsmittelspr() {
    }

    /** minimal constructor */
    public FLRVerpackungsmittelspr(com.lp.server.artikel.fastlanereader.generated.FLRVerpackungsmittel verpackungsmittel, FLRLocale locale) {
        this.verpackungsmittel = verpackungsmittel;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRVerpackungsmittel getVerpackungsmittel() {
        return this.verpackungsmittel;
    }

    public void setVerpackungsmittel(com.lp.server.artikel.fastlanereader.generated.FLRVerpackungsmittel verpackungsmittel) {
        this.verpackungsmittel = verpackungsmittel;
    }

    public FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("verpackungsmittel", getVerpackungsmittel())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRVerpackungsmittelspr) ) return false;
        FLRVerpackungsmittelspr castOther = (FLRVerpackungsmittelspr) other;
        return new EqualsBuilder()
            .append(this.getVerpackungsmittel(), castOther.getVerpackungsmittel())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getVerpackungsmittel())
            .append(getLocale())
            .toHashCode();
    }

}
