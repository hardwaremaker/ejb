package com.lp.server.angebotstkl.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRMaterial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAgstklmaterial implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer agstkl_i_id;

    /** nullable persistent field */
    private Integer material_i_id;

    /** nullable persistent field */
    private String c_materialtyp;

    /** nullable persistent field */
    private BigDecimal n_dimension1;

    /** nullable persistent field */
    private BigDecimal n_dimension2;

    /** nullable persistent field */
    private BigDecimal n_dimension3;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private BigDecimal n_gewichtpreis;

    /** nullable persistent field */
    private com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl;

    /** nullable persistent field */
    private FLRMaterial flrmaterial;

    /** full constructor */
    public FLRAgstklmaterial(Integer i_sort, Integer agstkl_i_id, Integer material_i_id, String c_materialtyp, BigDecimal n_dimension1, BigDecimal n_dimension2, BigDecimal n_dimension3, String c_bez, BigDecimal n_gewichtpreis, com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl, FLRMaterial flrmaterial) {
        this.i_sort = i_sort;
        this.agstkl_i_id = agstkl_i_id;
        this.material_i_id = material_i_id;
        this.c_materialtyp = c_materialtyp;
        this.n_dimension1 = n_dimension1;
        this.n_dimension2 = n_dimension2;
        this.n_dimension3 = n_dimension3;
        this.c_bez = c_bez;
        this.n_gewichtpreis = n_gewichtpreis;
        this.flragstkl = flragstkl;
        this.flrmaterial = flrmaterial;
    }

    /** default constructor */
    public FLRAgstklmaterial() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getAgstkl_i_id() {
        return this.agstkl_i_id;
    }

    public void setAgstkl_i_id(Integer agstkl_i_id) {
        this.agstkl_i_id = agstkl_i_id;
    }

    public Integer getMaterial_i_id() {
        return this.material_i_id;
    }

    public void setMaterial_i_id(Integer material_i_id) {
        this.material_i_id = material_i_id;
    }

    public String getC_materialtyp() {
        return this.c_materialtyp;
    }

    public void setC_materialtyp(String c_materialtyp) {
        this.c_materialtyp = c_materialtyp;
    }

    public BigDecimal getN_dimension1() {
        return this.n_dimension1;
    }

    public void setN_dimension1(BigDecimal n_dimension1) {
        this.n_dimension1 = n_dimension1;
    }

    public BigDecimal getN_dimension2() {
        return this.n_dimension2;
    }

    public void setN_dimension2(BigDecimal n_dimension2) {
        this.n_dimension2 = n_dimension2;
    }

    public BigDecimal getN_dimension3() {
        return this.n_dimension3;
    }

    public void setN_dimension3(BigDecimal n_dimension3) {
        this.n_dimension3 = n_dimension3;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public BigDecimal getN_gewichtpreis() {
        return this.n_gewichtpreis;
    }

    public void setN_gewichtpreis(BigDecimal n_gewichtpreis) {
        this.n_gewichtpreis = n_gewichtpreis;
    }

    public com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl getFlragstkl() {
        return this.flragstkl;
    }

    public void setFlragstkl(com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl) {
        this.flragstkl = flragstkl;
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
