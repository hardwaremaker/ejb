package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikellieferant implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer gebinde_i_id;

    /** nullable persistent field */
    private Integer lieferant_i_id;

    /** nullable persistent field */
    private String c_artikelnrlieferant;

    /** nullable persistent field */
    private Date t_preisgueltigab;

    /** nullable persistent field */
    private Date t_preisgueltigbis;

    /** nullable persistent field */
    private String c_bezbeilieferant;

    /** nullable persistent field */
    private BigDecimal n_einzelpreis;

    /** nullable persistent field */
    private BigDecimal n_nettopreis;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer i_wiederbeschaffungszeit;

    /** nullable persistent field */
    private Short b_webshop;

    /** nullable persistent field */
    private Short b_nicht_lieferbar;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRGebinde flrgebinde;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel;

    /** persistent field */
    private Set staffelset;

    /** full constructor */
    public FLRArtikellieferant(Integer artikel_i_id, Integer gebinde_i_id, Integer lieferant_i_id, String c_artikelnrlieferant, Date t_preisgueltigab, Date t_preisgueltigbis, String c_bezbeilieferant, BigDecimal n_einzelpreis, BigDecimal n_nettopreis, Integer i_sort, Integer i_wiederbeschaffungszeit, Short b_webshop, Short b_nicht_lieferbar, FLRLieferant flrlieferant, com.lp.server.artikel.fastlanereader.generated.FLRGebinde flrgebinde, com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel, Set staffelset) {
        this.artikel_i_id = artikel_i_id;
        this.gebinde_i_id = gebinde_i_id;
        this.lieferant_i_id = lieferant_i_id;
        this.c_artikelnrlieferant = c_artikelnrlieferant;
        this.t_preisgueltigab = t_preisgueltigab;
        this.t_preisgueltigbis = t_preisgueltigbis;
        this.c_bezbeilieferant = c_bezbeilieferant;
        this.n_einzelpreis = n_einzelpreis;
        this.n_nettopreis = n_nettopreis;
        this.i_sort = i_sort;
        this.i_wiederbeschaffungszeit = i_wiederbeschaffungszeit;
        this.b_webshop = b_webshop;
        this.b_nicht_lieferbar = b_nicht_lieferbar;
        this.flrlieferant = flrlieferant;
        this.flrgebinde = flrgebinde;
        this.flrartikel = flrartikel;
        this.staffelset = staffelset;
    }

    /** default constructor */
    public FLRArtikellieferant() {
    }

    /** minimal constructor */
    public FLRArtikellieferant(Set staffelset) {
        this.staffelset = staffelset;
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

    public Integer getGebinde_i_id() {
        return this.gebinde_i_id;
    }

    public void setGebinde_i_id(Integer gebinde_i_id) {
        this.gebinde_i_id = gebinde_i_id;
    }

    public Integer getLieferant_i_id() {
        return this.lieferant_i_id;
    }

    public void setLieferant_i_id(Integer lieferant_i_id) {
        this.lieferant_i_id = lieferant_i_id;
    }

    public String getC_artikelnrlieferant() {
        return this.c_artikelnrlieferant;
    }

    public void setC_artikelnrlieferant(String c_artikelnrlieferant) {
        this.c_artikelnrlieferant = c_artikelnrlieferant;
    }

    public Date getT_preisgueltigab() {
        return this.t_preisgueltigab;
    }

    public void setT_preisgueltigab(Date t_preisgueltigab) {
        this.t_preisgueltigab = t_preisgueltigab;
    }

    public Date getT_preisgueltigbis() {
        return this.t_preisgueltigbis;
    }

    public void setT_preisgueltigbis(Date t_preisgueltigbis) {
        this.t_preisgueltigbis = t_preisgueltigbis;
    }

    public String getC_bezbeilieferant() {
        return this.c_bezbeilieferant;
    }

    public void setC_bezbeilieferant(String c_bezbeilieferant) {
        this.c_bezbeilieferant = c_bezbeilieferant;
    }

    public BigDecimal getN_einzelpreis() {
        return this.n_einzelpreis;
    }

    public void setN_einzelpreis(BigDecimal n_einzelpreis) {
        this.n_einzelpreis = n_einzelpreis;
    }

    public BigDecimal getN_nettopreis() {
        return this.n_nettopreis;
    }

    public void setN_nettopreis(BigDecimal n_nettopreis) {
        this.n_nettopreis = n_nettopreis;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getI_wiederbeschaffungszeit() {
        return this.i_wiederbeschaffungszeit;
    }

    public void setI_wiederbeschaffungszeit(Integer i_wiederbeschaffungszeit) {
        this.i_wiederbeschaffungszeit = i_wiederbeschaffungszeit;
    }

    public Short getB_webshop() {
        return this.b_webshop;
    }

    public void setB_webshop(Short b_webshop) {
        this.b_webshop = b_webshop;
    }

    public Short getB_nicht_lieferbar() {
        return this.b_nicht_lieferbar;
    }

    public void setB_nicht_lieferbar(Short b_nicht_lieferbar) {
        this.b_nicht_lieferbar = b_nicht_lieferbar;
    }

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRGebinde getFlrgebinde() {
        return this.flrgebinde;
    }

    public void setFlrgebinde(com.lp.server.artikel.fastlanereader.generated.FLRGebinde flrgebinde) {
        this.flrgebinde = flrgebinde;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public Set getStaffelset() {
        return this.staffelset;
    }

    public void setStaffelset(Set staffelset) {
        this.staffelset = staffelset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
