package com.lp.server.stueckliste.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRLocale;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRApkommentarspr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRApkommentar apkommentar;

    /** identifier field */
    private FLRLocale locale;

    /** full constructor */
    public FLRApkommentarspr(String c_bez, com.lp.server.stueckliste.fastlanereader.generated.FLRApkommentar apkommentar, FLRLocale locale) {
        this.c_bez = c_bez;
        this.apkommentar = apkommentar;
        this.locale = locale;
    }

    /** default constructor */
    public FLRApkommentarspr() {
    }

    /** minimal constructor */
    public FLRApkommentarspr(com.lp.server.stueckliste.fastlanereader.generated.FLRApkommentar apkommentar, FLRLocale locale) {
        this.apkommentar = apkommentar;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRApkommentar getApkommentar() {
        return this.apkommentar;
    }

    public void setApkommentar(com.lp.server.stueckliste.fastlanereader.generated.FLRApkommentar apkommentar) {
        this.apkommentar = apkommentar;
    }

    public FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("apkommentar", getApkommentar())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRApkommentarspr) ) return false;
        FLRApkommentarspr castOther = (FLRApkommentarspr) other;
        return new EqualsBuilder()
            .append(this.getApkommentar(), castOther.getApkommentar())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getApkommentar())
            .append(getLocale())
            .toHashCode();
    }

}
