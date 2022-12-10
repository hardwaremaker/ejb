package com.lp.server.stueckliste.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRLocale;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPruefartspr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRPruefart pruefart;

    /** identifier field */
    private FLRLocale locale;

    /** full constructor */
    public FLRPruefartspr(String c_bez, com.lp.server.stueckliste.fastlanereader.generated.FLRPruefart pruefart, FLRLocale locale) {
        this.c_bez = c_bez;
        this.pruefart = pruefart;
        this.locale = locale;
    }

    /** default constructor */
    public FLRPruefartspr() {
    }

    /** minimal constructor */
    public FLRPruefartspr(com.lp.server.stueckliste.fastlanereader.generated.FLRPruefart pruefart, FLRLocale locale) {
        this.pruefart = pruefart;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRPruefart getPruefart() {
        return this.pruefart;
    }

    public void setPruefart(com.lp.server.stueckliste.fastlanereader.generated.FLRPruefart pruefart) {
        this.pruefart = pruefart;
    }

    public FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("pruefart", getPruefart())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRPruefartspr) ) return false;
        FLRPruefartspr castOther = (FLRPruefartspr) other;
        return new EqualsBuilder()
            .append(this.getPruefart(), castOther.getPruefart())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getPruefart())
            .append(getLocale())
            .toHashCode();
    }

}
