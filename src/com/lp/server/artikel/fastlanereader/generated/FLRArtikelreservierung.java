package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikelreservierung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_belegartpositionid;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private Timestamp t_liefertermin;

    /** nullable persistent field */
    private String c_belegartnr;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel;

    /** full constructor */
    public FLRArtikelreservierung(Integer i_belegartpositionid, Integer artikel_i_id, BigDecimal n_menge, Timestamp t_liefertermin, String c_belegartnr, com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel) {
        this.i_belegartpositionid = i_belegartpositionid;
        this.artikel_i_id = artikel_i_id;
        this.n_menge = n_menge;
        this.t_liefertermin = t_liefertermin;
        this.c_belegartnr = c_belegartnr;
        this.flrartikel = flrartikel;
    }

    /** default constructor */
    public FLRArtikelreservierung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_belegartpositionid() {
        return this.i_belegartpositionid;
    }

    public void setI_belegartpositionid(Integer i_belegartpositionid) {
        this.i_belegartpositionid = i_belegartpositionid;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public Timestamp getT_liefertermin() {
        return this.t_liefertermin;
    }

    public void setT_liefertermin(Timestamp t_liefertermin) {
        this.t_liefertermin = t_liefertermin;
    }

    public String getC_belegartnr() {
        return this.c_belegartnr;
    }

    public void setC_belegartnr(String c_belegartnr) {
        this.c_belegartnr = c_belegartnr;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
