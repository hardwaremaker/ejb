package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.service.FLRHvmaparameterPK;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRHvmaparameter implements Serializable {

    /** identifier field */
    private FLRHvmaparameterPK id_comp;

    /** nullable persistent field */
    private Integer hvmalizenz_i_id;

    /** nullable persistent field */
    private String c_datentyp;

    /** nullable persistent field */
    private String c_default;

    /** nullable persistent field */
    private String c_bemerkung;

    /** nullable persistent field */
    private Short b_uebertragen;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRHvmalizenz flrhvmalizenz;

    /** full constructor */
    public FLRHvmaparameter(FLRHvmaparameterPK id_comp, Integer hvmalizenz_i_id, String c_datentyp, String c_default, String c_bemerkung, Short b_uebertragen, com.lp.server.personal.fastlanereader.generated.FLRHvmalizenz flrhvmalizenz) {
        this.id_comp = id_comp;
        this.hvmalizenz_i_id = hvmalizenz_i_id;
        this.c_datentyp = c_datentyp;
        this.c_default = c_default;
        this.c_bemerkung = c_bemerkung;
        this.b_uebertragen = b_uebertragen;
        this.flrhvmalizenz = flrhvmalizenz;
    }

    /** default constructor */
    public FLRHvmaparameter() {
    }

    /** minimal constructor */
    public FLRHvmaparameter(FLRHvmaparameterPK id_comp) {
        this.id_comp = id_comp;
    }

    public FLRHvmaparameterPK getId_comp() {
        return this.id_comp;
    }

    public void setId_comp(FLRHvmaparameterPK id_comp) {
        this.id_comp = id_comp;
    }

    public Integer getHvmalizenz_i_id() {
        return this.hvmalizenz_i_id;
    }

    public void setHvmalizenz_i_id(Integer hvmalizenz_i_id) {
        this.hvmalizenz_i_id = hvmalizenz_i_id;
    }

    public String getC_datentyp() {
        return this.c_datentyp;
    }

    public void setC_datentyp(String c_datentyp) {
        this.c_datentyp = c_datentyp;
    }

    public String getC_default() {
        return this.c_default;
    }

    public void setC_default(String c_default) {
        this.c_default = c_default;
    }

    public String getC_bemerkung() {
        return this.c_bemerkung;
    }

    public void setC_bemerkung(String c_bemerkung) {
        this.c_bemerkung = c_bemerkung;
    }

    public Short getB_uebertragen() {
        return this.b_uebertragen;
    }

    public void setB_uebertragen(Short b_uebertragen) {
        this.b_uebertragen = b_uebertragen;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRHvmalizenz getFlrhvmalizenz() {
        return this.flrhvmalizenz;
    }

    public void setFlrhvmalizenz(com.lp.server.personal.fastlanereader.generated.FLRHvmalizenz flrhvmalizenz) {
        this.flrhvmalizenz = flrhvmalizenz;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id_comp", getId_comp())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRHvmaparameter) ) return false;
        FLRHvmaparameter castOther = (FLRHvmaparameter) other;
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
