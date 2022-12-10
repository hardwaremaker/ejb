package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLandspr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.system.fastlanereader.generated.FLRLand land;

    /** identifier field */
    private com.lp.server.system.fastlanereader.generated.FLRLocale locale;

    /** full constructor */
    public FLRLandspr(String c_bez, com.lp.server.system.fastlanereader.generated.FLRLand land, com.lp.server.system.fastlanereader.generated.FLRLocale locale) {
        this.c_bez = c_bez;
        this.land = land;
        this.locale = locale;
    }

    /** default constructor */
    public FLRLandspr() {
    }

    /** minimal constructor */
    public FLRLandspr(com.lp.server.system.fastlanereader.generated.FLRLand land, com.lp.server.system.fastlanereader.generated.FLRLocale locale) {
        this.land = land;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.system.fastlanereader.generated.FLRLand getLand() {
        return this.land;
    }

    public void setLand(com.lp.server.system.fastlanereader.generated.FLRLand land) {
        this.land = land;
    }

    public com.lp.server.system.fastlanereader.generated.FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(com.lp.server.system.fastlanereader.generated.FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("land", getLand())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRLandspr) ) return false;
        FLRLandspr castOther = (FLRLandspr) other;
        return new EqualsBuilder()
            .append(this.getLand(), castOther.getLand())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getLand())
            .append(getLocale())
            .toHashCode();
    }

}
