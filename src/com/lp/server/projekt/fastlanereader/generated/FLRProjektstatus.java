package com.lp.server.projekt.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRProjektstatus implements Serializable {

    /** identifier field */
    private String status_c_nr;

    /** identifier field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Short b_erledigt;

    /** nullable persistent field */
    private Short b_gesperrt;

    /** nullable persistent field */
    private Integer i_sort;

    /** full constructor */
    public FLRProjektstatus(String status_c_nr, String mandant_c_nr, Short b_erledigt, Short b_gesperrt, Integer i_sort) {
        this.status_c_nr = status_c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.b_erledigt = b_erledigt;
        this.b_gesperrt = b_gesperrt;
        this.i_sort = i_sort;
    }

    /** default constructor */
    public FLRProjektstatus() {
    }

    /** minimal constructor */
    public FLRProjektstatus(String status_c_nr, String mandant_c_nr) {
        this.status_c_nr = status_c_nr;
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Short getB_erledigt() {
        return this.b_erledigt;
    }

    public void setB_erledigt(Short b_erledigt) {
        this.b_erledigt = b_erledigt;
    }

    public Short getB_gesperrt() {
        return this.b_gesperrt;
    }

    public void setB_gesperrt(Short b_gesperrt) {
        this.b_gesperrt = b_gesperrt;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("status_c_nr", getStatus_c_nr())
            .append("mandant_c_nr", getMandant_c_nr())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRProjektstatus) ) return false;
        FLRProjektstatus castOther = (FLRProjektstatus) other;
        return new EqualsBuilder()
            .append(this.getStatus_c_nr(), castOther.getStatus_c_nr())
            .append(this.getMandant_c_nr(), castOther.getMandant_c_nr())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getStatus_c_nr())
            .append(getMandant_c_nr())
            .toHashCode();
    }

}
