package com.lp.server.rechnung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.system.fastlanereader.generated.FLRLand;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRMmz implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer land_i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private BigDecimal n_bis_wert;

    /** nullable persistent field */
    private BigDecimal n_zuschlag;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRLand flrland;

    /** full constructor */
    public FLRMmz(Integer artikel_i_id, Integer land_i_id, String mandant_c_nr, BigDecimal n_bis_wert, BigDecimal n_zuschlag, FLRArtikel flrartikel, FLRLand flrland) {
        this.artikel_i_id = artikel_i_id;
        this.land_i_id = land_i_id;
        this.mandant_c_nr = mandant_c_nr;
        this.n_bis_wert = n_bis_wert;
        this.n_zuschlag = n_zuschlag;
        this.flrartikel = flrartikel;
        this.flrland = flrland;
    }

    /** default constructor */
    public FLRMmz() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Integer getLand_i_id() {
        return this.land_i_id;
    }

    public void setLand_i_id(Integer land_i_id) {
        this.land_i_id = land_i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public BigDecimal getN_bis_wert() {
        return this.n_bis_wert;
    }

    public void setN_bis_wert(BigDecimal n_bis_wert) {
        this.n_bis_wert = n_bis_wert;
    }

    public BigDecimal getN_zuschlag() {
        return this.n_zuschlag;
    }

    public void setN_zuschlag(BigDecimal n_zuschlag) {
        this.n_zuschlag = n_zuschlag;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRLand getFlrland() {
        return this.flrland;
    }

    public void setFlrland(FLRLand flrland) {
        this.flrland = flrland;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
