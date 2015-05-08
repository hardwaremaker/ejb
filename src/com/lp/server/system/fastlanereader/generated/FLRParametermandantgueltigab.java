package com.lp.server.system.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.service.FLRParametermandantgueltigabPK;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRParametermandantgueltigab implements Serializable {

    /** identifier field */
    private FLRParametermandantgueltigabPK id_comp;

    /** nullable persistent field */
    private String c_wert;

    /** full constructor */
    public FLRParametermandantgueltigab(FLRParametermandantgueltigabPK id_comp, String c_wert) {
        this.id_comp = id_comp;
        this.c_wert = c_wert;
    }

    /** default constructor */
    public FLRParametermandantgueltigab() {
    }

    /** minimal constructor */
    public FLRParametermandantgueltigab(FLRParametermandantgueltigabPK id_comp) {
        this.id_comp = id_comp;
    }

    public FLRParametermandantgueltigabPK getId_comp() {
        return this.id_comp;
    }

    public void setId_comp(FLRParametermandantgueltigabPK id_comp) {
        this.id_comp = id_comp;
    }

    public String getC_wert() {
        return this.c_wert;
    }

    public void setC_wert(String c_wert) {
        this.c_wert = c_wert;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id_comp", getId_comp())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRParametermandantgueltigab) ) return false;
        FLRParametermandantgueltigab castOther = (FLRParametermandantgueltigab) other;
        return new EqualsBuilder()
            .append(this.getId_comp(), castOther.getId_comp())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId_comp())
            .toHashCode();
    }

}
