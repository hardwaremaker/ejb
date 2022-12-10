package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKennungspr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.system.fastlanereader.generated.FLRKennung kennung_i_id;

    /** identifier field */
    private com.lp.server.system.fastlanereader.generated.FLRLocale locale_c_nr;

    /** full constructor */
    public FLRKennungspr(String c_bez, com.lp.server.system.fastlanereader.generated.FLRKennung kennung_i_id, com.lp.server.system.fastlanereader.generated.FLRLocale locale_c_nr) {
        this.c_bez = c_bez;
        this.kennung_i_id = kennung_i_id;
        this.locale_c_nr = locale_c_nr;
    }

    /** default constructor */
    public FLRKennungspr() {
    }

    /** minimal constructor */
    public FLRKennungspr(com.lp.server.system.fastlanereader.generated.FLRKennung kennung_i_id, com.lp.server.system.fastlanereader.generated.FLRLocale locale_c_nr) {
        this.kennung_i_id = kennung_i_id;
        this.locale_c_nr = locale_c_nr;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.system.fastlanereader.generated.FLRKennung getKennung_i_id() {
        return this.kennung_i_id;
    }

    public void setKennung_i_id(com.lp.server.system.fastlanereader.generated.FLRKennung kennung_i_id) {
        this.kennung_i_id = kennung_i_id;
    }

    public com.lp.server.system.fastlanereader.generated.FLRLocale getLocale_c_nr() {
        return this.locale_c_nr;
    }

    public void setLocale_c_nr(com.lp.server.system.fastlanereader.generated.FLRLocale locale_c_nr) {
        this.locale_c_nr = locale_c_nr;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("kennung_i_id", getKennung_i_id())
            .append("locale_c_nr", getLocale_c_nr())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRKennungspr) ) return false;
        FLRKennungspr castOther = (FLRKennungspr) other;
        return new EqualsBuilder()
            .append(this.getKennung_i_id(), castOther.getKennung_i_id())
            .append(this.getLocale_c_nr(), castOther.getLocale_c_nr())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getKennung_i_id())
            .append(getLocale_c_nr())
            .toHashCode();
    }

}
