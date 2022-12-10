package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikelbestellt implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_belegartpositionid;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private Date t_liefertermin;

    /** nullable persistent field */
    private String c_belegartnr;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRBestellposition flrbestellposition;

    /** full constructor */
    public FLRArtikelbestellt(Integer i_belegartpositionid, BigDecimal n_menge, Date t_liefertermin, String c_belegartnr, com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel, FLRBestellposition flrbestellposition) {
        this.i_belegartpositionid = i_belegartpositionid;
        this.n_menge = n_menge;
        this.t_liefertermin = t_liefertermin;
        this.c_belegartnr = c_belegartnr;
        this.flrartikel = flrartikel;
        this.flrbestellposition = flrbestellposition;
    }

    /** default constructor */
    public FLRArtikelbestellt() {
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

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public Date getT_liefertermin() {
        return this.t_liefertermin;
    }

    public void setT_liefertermin(Date t_liefertermin) {
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

    public FLRBestellposition getFlrbestellposition() {
        return this.flrbestellposition;
    }

    public void setFlrbestellposition(FLRBestellposition flrbestellposition) {
        this.flrbestellposition = flrbestellposition;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
