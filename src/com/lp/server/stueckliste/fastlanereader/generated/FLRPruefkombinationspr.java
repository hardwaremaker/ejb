package com.lp.server.stueckliste.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRLocale;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPruefkombinationspr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombination pruefkombinationspr;

    /** identifier field */
    private FLRLocale locale;

    /** full constructor */
    public FLRPruefkombinationspr(String c_bez, com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombination pruefkombinationspr, FLRLocale locale) {
        this.c_bez = c_bez;
        this.pruefkombinationspr = pruefkombinationspr;
        this.locale = locale;
    }

    /** default constructor */
    public FLRPruefkombinationspr() {
    }

    /** minimal constructor */
    public FLRPruefkombinationspr(com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombination pruefkombinationspr, FLRLocale locale) {
        this.pruefkombinationspr = pruefkombinationspr;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombination getPruefkombinationspr() {
        return this.pruefkombinationspr;
    }

    public void setPruefkombinationspr(com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombination pruefkombinationspr) {
        this.pruefkombinationspr = pruefkombinationspr;
    }

    public FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("pruefkombinationspr", getPruefkombinationspr())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRPruefkombinationspr) ) return false;
        FLRPruefkombinationspr castOther = (FLRPruefkombinationspr) other;
        return new EqualsBuilder()
            .append(this.getPruefkombinationspr(), castOther.getPruefkombinationspr())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getPruefkombinationspr())
            .append(getLocale())
            .toHashCode();
    }

}
