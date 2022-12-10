package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRMaterialpreis implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String material_i_id;

    /** nullable persistent field */
    private Date t_gueltigab;

    /** nullable persistent field */
    private BigDecimal n_preis_pro_kg;

    /** full constructor */
    public FLRMaterialpreis(String material_i_id, Date t_gueltigab, BigDecimal n_preis_pro_kg) {
        this.material_i_id = material_i_id;
        this.t_gueltigab = t_gueltigab;
        this.n_preis_pro_kg = n_preis_pro_kg;
    }

    /** default constructor */
    public FLRMaterialpreis() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getMaterial_i_id() {
        return this.material_i_id;
    }

    public void setMaterial_i_id(String material_i_id) {
        this.material_i_id = material_i_id;
    }

    public Date getT_gueltigab() {
        return this.t_gueltigab;
    }

    public void setT_gueltigab(Date t_gueltigab) {
        this.t_gueltigab = t_gueltigab;
    }

    public BigDecimal getN_preis_pro_kg() {
        return this.n_preis_pro_kg;
    }

    public void setN_preis_pro_kg(BigDecimal n_preis_pro_kg) {
        this.n_preis_pro_kg = n_preis_pro_kg;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
