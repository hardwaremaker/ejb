package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRLocale;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAbwesenheitsartspr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.personal.fastlanereader.generated.FLRAbwesenheitsart abwesenheitsart;

    /** identifier field */
    private FLRLocale locale;

    /** full constructor */
    public FLRAbwesenheitsartspr(String c_bez, com.lp.server.personal.fastlanereader.generated.FLRAbwesenheitsart abwesenheitsart, FLRLocale locale) {
        this.c_bez = c_bez;
        this.abwesenheitsart = abwesenheitsart;
        this.locale = locale;
    }

    /** default constructor */
    public FLRAbwesenheitsartspr() {
    }

    /** minimal constructor */
    public FLRAbwesenheitsartspr(com.lp.server.personal.fastlanereader.generated.FLRAbwesenheitsart abwesenheitsart, FLRLocale locale) {
        this.abwesenheitsart = abwesenheitsart;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRAbwesenheitsart getAbwesenheitsart() {
        return this.abwesenheitsart;
    }

    public void setAbwesenheitsart(com.lp.server.personal.fastlanereader.generated.FLRAbwesenheitsart abwesenheitsart) {
        this.abwesenheitsart = abwesenheitsart;
    }

    public FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("abwesenheitsart", getAbwesenheitsart())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRAbwesenheitsartspr) ) return false;
        FLRAbwesenheitsartspr castOther = (FLRAbwesenheitsartspr) other;
        return new EqualsBuilder()
            .append(this.getAbwesenheitsart(), castOther.getAbwesenheitsart())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getAbwesenheitsart())
            .append(getLocale())
            .toHashCode();
    }

}
