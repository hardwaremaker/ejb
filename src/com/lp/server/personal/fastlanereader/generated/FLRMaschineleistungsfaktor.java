package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRMaterial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRMaschineleistungsfaktor implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer maschine_i_id;

    /** nullable persistent field */
    private Integer material_i_id;

    /** nullable persistent field */
    private Date t_gueltigab;

    /** nullable persistent field */
    private BigDecimal n_faktor_in_prozent;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRMaschine flrmaschine;

    /** nullable persistent field */
    private FLRMaterial flrmaterial;

    /** full constructor */
    public FLRMaschineleistungsfaktor(Integer maschine_i_id, Integer material_i_id, Date t_gueltigab, BigDecimal n_faktor_in_prozent, com.lp.server.personal.fastlanereader.generated.FLRMaschine flrmaschine, FLRMaterial flrmaterial) {
        this.maschine_i_id = maschine_i_id;
        this.material_i_id = material_i_id;
        this.t_gueltigab = t_gueltigab;
        this.n_faktor_in_prozent = n_faktor_in_prozent;
        this.flrmaschine = flrmaschine;
        this.flrmaterial = flrmaterial;
    }

    /** default constructor */
    public FLRMaschineleistungsfaktor() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getMaschine_i_id() {
        return this.maschine_i_id;
    }

    public void setMaschine_i_id(Integer maschine_i_id) {
        this.maschine_i_id = maschine_i_id;
    }

    public Integer getMaterial_i_id() {
        return this.material_i_id;
    }

    public void setMaterial_i_id(Integer material_i_id) {
        this.material_i_id = material_i_id;
    }

    public Date getT_gueltigab() {
        return this.t_gueltigab;
    }

    public void setT_gueltigab(Date t_gueltigab) {
        this.t_gueltigab = t_gueltigab;
    }

    public BigDecimal getN_faktor_in_prozent() {
        return this.n_faktor_in_prozent;
    }

    public void setN_faktor_in_prozent(BigDecimal n_faktor_in_prozent) {
        this.n_faktor_in_prozent = n_faktor_in_prozent;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRMaschine getFlrmaschine() {
        return this.flrmaschine;
    }

    public void setFlrmaschine(com.lp.server.personal.fastlanereader.generated.FLRMaschine flrmaschine) {
        this.flrmaschine = flrmaschine;
    }

    public FLRMaterial getFlrmaterial() {
        return this.flrmaterial;
    }

    public void setFlrmaterial(FLRMaterial flrmaterial) {
        this.flrmaterial = flrmaterial;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
