package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRLocale;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRNachrichtenartspr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.personal.fastlanereader.generated.FLRZeitmodell nachrichtenart;

    /** identifier field */
    private FLRLocale locale;

    /** full constructor */
    public FLRNachrichtenartspr(String c_bez, com.lp.server.personal.fastlanereader.generated.FLRZeitmodell nachrichtenart, FLRLocale locale) {
        this.c_bez = c_bez;
        this.nachrichtenart = nachrichtenart;
        this.locale = locale;
    }

    /** default constructor */
    public FLRNachrichtenartspr() {
    }

    /** minimal constructor */
    public FLRNachrichtenartspr(com.lp.server.personal.fastlanereader.generated.FLRZeitmodell nachrichtenart, FLRLocale locale) {
        this.nachrichtenart = nachrichtenart;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRZeitmodell getNachrichtenart() {
        return this.nachrichtenart;
    }

    public void setNachrichtenart(com.lp.server.personal.fastlanereader.generated.FLRZeitmodell nachrichtenart) {
        this.nachrichtenart = nachrichtenart;
    }

    public FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("nachrichtenart", getNachrichtenart())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRNachrichtenartspr) ) return false;
        FLRNachrichtenartspr castOther = (FLRNachrichtenartspr) other;
        return new EqualsBuilder()
            .append(this.getNachrichtenart(), castOther.getNachrichtenart())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getNachrichtenart())
            .append(getLocale())
            .toHashCode();
    }

}
