package com.lp.server.forecast.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRLocale;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRImportdefspr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.forecast.fastlanereader.generated.FLRImportdef importdef;

    /** identifier field */
    private FLRLocale locale;

    /** full constructor */
    public FLRImportdefspr(String c_bez, com.lp.server.forecast.fastlanereader.generated.FLRImportdef importdef, FLRLocale locale) {
        this.c_bez = c_bez;
        this.importdef = importdef;
        this.locale = locale;
    }

    /** default constructor */
    public FLRImportdefspr() {
    }

    /** minimal constructor */
    public FLRImportdefspr(com.lp.server.forecast.fastlanereader.generated.FLRImportdef importdef, FLRLocale locale) {
        this.importdef = importdef;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.forecast.fastlanereader.generated.FLRImportdef getImportdef() {
        return this.importdef;
    }

    public void setImportdef(com.lp.server.forecast.fastlanereader.generated.FLRImportdef importdef) {
        this.importdef = importdef;
    }

    public FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("importdef", getImportdef())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRImportdefspr) ) return false;
        FLRImportdefspr castOther = (FLRImportdefspr) other;
        return new EqualsBuilder()
            .append(this.getImportdef(), castOther.getImportdef())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getImportdef())
            .append(getLocale())
            .toHashCode();
    }

}
