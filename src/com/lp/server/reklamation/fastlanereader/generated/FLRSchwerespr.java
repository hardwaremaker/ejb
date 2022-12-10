package com.lp.server.reklamation.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRLocale;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRSchwerespr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.reklamation.fastlanereader.generated.FLRSchwere schwere;

    /** identifier field */
    private FLRLocale locale;

    /** full constructor */
    public FLRSchwerespr(String c_bez, com.lp.server.reklamation.fastlanereader.generated.FLRSchwere schwere, FLRLocale locale) {
        this.c_bez = c_bez;
        this.schwere = schwere;
        this.locale = locale;
    }

    /** default constructor */
    public FLRSchwerespr() {
    }

    /** minimal constructor */
    public FLRSchwerespr(com.lp.server.reklamation.fastlanereader.generated.FLRSchwere schwere, FLRLocale locale) {
        this.schwere = schwere;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.reklamation.fastlanereader.generated.FLRSchwere getSchwere() {
        return this.schwere;
    }

    public void setSchwere(com.lp.server.reklamation.fastlanereader.generated.FLRSchwere schwere) {
        this.schwere = schwere;
    }

    public FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("schwere", getSchwere())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRSchwerespr) ) return false;
        FLRSchwerespr castOther = (FLRSchwerespr) other;
        return new EqualsBuilder()
            .append(this.getSchwere(), castOther.getSchwere())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getSchwere())
            .append(getLocale())
            .toHashCode();
    }

}
