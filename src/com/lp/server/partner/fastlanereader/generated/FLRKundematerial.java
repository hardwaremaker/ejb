package com.lp.server.partner.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRMaterial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKundematerial implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private String material_i_id;

    /** nullable persistent field */
    private String material_i_id_notierung;

    /** nullable persistent field */
    private BigDecimal n_materialbasis;

    /** nullable persistent field */
    private Short b_material_inklusive;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRMaterial flrmaterial;

    /** nullable persistent field */
    private FLRMaterial flrmaterial_notierung;

    /** full constructor */
    public FLRKundematerial(Integer kunde_i_id, String material_i_id, String material_i_id_notierung, BigDecimal n_materialbasis, Short b_material_inklusive, com.lp.server.partner.fastlanereader.generated.FLRKunde flrkunde, FLRMaterial flrmaterial, FLRMaterial flrmaterial_notierung) {
        this.kunde_i_id = kunde_i_id;
        this.material_i_id = material_i_id;
        this.material_i_id_notierung = material_i_id_notierung;
        this.n_materialbasis = n_materialbasis;
        this.b_material_inklusive = b_material_inklusive;
        this.flrkunde = flrkunde;
        this.flrmaterial = flrmaterial;
        this.flrmaterial_notierung = flrmaterial_notierung;
    }

    /** default constructor */
    public FLRKundematerial() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public String getMaterial_i_id() {
        return this.material_i_id;
    }

    public void setMaterial_i_id(String material_i_id) {
        this.material_i_id = material_i_id;
    }

    public String getMaterial_i_id_notierung() {
        return this.material_i_id_notierung;
    }

    public void setMaterial_i_id_notierung(String material_i_id_notierung) {
        this.material_i_id_notierung = material_i_id_notierung;
    }

    public BigDecimal getN_materialbasis() {
        return this.n_materialbasis;
    }

    public void setN_materialbasis(BigDecimal n_materialbasis) {
        this.n_materialbasis = n_materialbasis;
    }

    public Short getB_material_inklusive() {
        return this.b_material_inklusive;
    }

    public void setB_material_inklusive(Short b_material_inklusive) {
        this.b_material_inklusive = b_material_inklusive;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(com.lp.server.partner.fastlanereader.generated.FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public FLRMaterial getFlrmaterial() {
        return this.flrmaterial;
    }

    public void setFlrmaterial(FLRMaterial flrmaterial) {
        this.flrmaterial = flrmaterial;
    }

    public FLRMaterial getFlrmaterial_notierung() {
        return this.flrmaterial_notierung;
    }

    public void setFlrmaterial_notierung(FLRMaterial flrmaterial_notierung) {
        this.flrmaterial_notierung = flrmaterial_notierung;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
