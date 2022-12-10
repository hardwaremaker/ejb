package com.lp.server.stueckliste.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRLocale;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRStklparameterspr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRStklparameter stklparameter;

    /** identifier field */
    private FLRLocale locale;

    /** full constructor */
    public FLRStklparameterspr(String c_bez, com.lp.server.stueckliste.fastlanereader.generated.FLRStklparameter stklparameter, FLRLocale locale) {
        this.c_bez = c_bez;
        this.stklparameter = stklparameter;
        this.locale = locale;
    }

    /** default constructor */
    public FLRStklparameterspr() {
    }

    /** minimal constructor */
    public FLRStklparameterspr(com.lp.server.stueckliste.fastlanereader.generated.FLRStklparameter stklparameter, FLRLocale locale) {
        this.stklparameter = stklparameter;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRStklparameter getStklparameter() {
        return this.stklparameter;
    }

    public void setStklparameter(com.lp.server.stueckliste.fastlanereader.generated.FLRStklparameter stklparameter) {
        this.stklparameter = stklparameter;
    }

    public FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("stklparameter", getStklparameter())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRStklparameterspr) ) return false;
        FLRStklparameterspr castOther = (FLRStklparameterspr) other;
        return new EqualsBuilder()
            .append(this.getStklparameter(), castOther.getStklparameter())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getStklparameter())
            .append(getLocale())
            .toHashCode();
    }

}
