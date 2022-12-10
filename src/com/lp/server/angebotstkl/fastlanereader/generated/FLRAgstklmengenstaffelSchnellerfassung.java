package com.lp.server.angebotstkl.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAgstklmengenstaffelSchnellerfassung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal n_aufschlag_az;

    /** nullable persistent field */
    private BigDecimal n_aufschlag_material;

    /** nullable persistent field */
    private BigDecimal n_wert_material;

    /** nullable persistent field */
    private BigDecimal n_wert_az;

    /** nullable persistent field */
    private BigDecimal n_preis_einheit;

    /** nullable persistent field */
    private Integer agstkl_i_id;

    /** nullable persistent field */
    private com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl;

    /** full constructor */
    public FLRAgstklmengenstaffelSchnellerfassung(BigDecimal n_menge, BigDecimal n_aufschlag_az, BigDecimal n_aufschlag_material, BigDecimal n_wert_material, BigDecimal n_wert_az, BigDecimal n_preis_einheit, Integer agstkl_i_id, com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl) {
        this.n_menge = n_menge;
        this.n_aufschlag_az = n_aufschlag_az;
        this.n_aufschlag_material = n_aufschlag_material;
        this.n_wert_material = n_wert_material;
        this.n_wert_az = n_wert_az;
        this.n_preis_einheit = n_preis_einheit;
        this.agstkl_i_id = agstkl_i_id;
        this.flragstkl = flragstkl;
    }

    /** default constructor */
    public FLRAgstklmengenstaffelSchnellerfassung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public BigDecimal getN_aufschlag_az() {
        return this.n_aufschlag_az;
    }

    public void setN_aufschlag_az(BigDecimal n_aufschlag_az) {
        this.n_aufschlag_az = n_aufschlag_az;
    }

    public BigDecimal getN_aufschlag_material() {
        return this.n_aufschlag_material;
    }

    public void setN_aufschlag_material(BigDecimal n_aufschlag_material) {
        this.n_aufschlag_material = n_aufschlag_material;
    }

    public BigDecimal getN_wert_material() {
        return this.n_wert_material;
    }

    public void setN_wert_material(BigDecimal n_wert_material) {
        this.n_wert_material = n_wert_material;
    }

    public BigDecimal getN_wert_az() {
        return this.n_wert_az;
    }

    public void setN_wert_az(BigDecimal n_wert_az) {
        this.n_wert_az = n_wert_az;
    }

    public BigDecimal getN_preis_einheit() {
        return this.n_preis_einheit;
    }

    public void setN_preis_einheit(BigDecimal n_preis_einheit) {
        this.n_preis_einheit = n_preis_einheit;
    }

    public Integer getAgstkl_i_id() {
        return this.agstkl_i_id;
    }

    public void setAgstkl_i_id(Integer agstkl_i_id) {
        this.agstkl_i_id = agstkl_i_id;
    }

    public com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl getFlragstkl() {
        return this.flragstkl;
    }

    public void setFlragstkl(com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl) {
        this.flragstkl = flragstkl;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
