package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRMaterial implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private BigDecimal n_gewicht_in_kg;

    /** persistent field */
    private Set materialsprset;

    /** full constructor */
    public FLRMaterial(String c_nr, BigDecimal n_gewicht_in_kg, Set materialsprset) {
        this.c_nr = c_nr;
        this.n_gewicht_in_kg = n_gewicht_in_kg;
        this.materialsprset = materialsprset;
    }

    /** default constructor */
    public FLRMaterial() {
    }

    /** minimal constructor */
    public FLRMaterial(Set materialsprset) {
        this.materialsprset = materialsprset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public BigDecimal getN_gewicht_in_kg() {
        return this.n_gewicht_in_kg;
    }

    public void setN_gewicht_in_kg(BigDecimal n_gewicht_in_kg) {
        this.n_gewicht_in_kg = n_gewicht_in_kg;
    }

    public Set getMaterialsprset() {
        return this.materialsprset;
    }

    public void setMaterialsprset(Set materialsprset) {
        this.materialsprset = materialsprset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
