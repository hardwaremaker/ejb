package com.lp.server.anfrage.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAnfragepositionReport implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String anfragepositionart_c_nr;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private BigDecimal n_richtpreis;

    /** nullable persistent field */
    private String c_zbez;

    /** nullable persistent field */
    private Integer anfrageposition_i_id_zugehoerig;

    /** nullable persistent field */
    private com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage flranfrage;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private com.lp.server.anfrage.fastlanereader.generated.FLRAnfrageposition flranfrageposition_zugehoerig;

    /** persistent field */
    private Set ersatztypen_set;

    /** persistent field */
    private Set lieferdaten_set;

    /** full constructor */
    public FLRAnfragepositionReport(Integer i_sort, String anfragepositionart_c_nr, BigDecimal n_menge, String einheit_c_nr, String c_bez, BigDecimal n_richtpreis, String c_zbez, Integer anfrageposition_i_id_zugehoerig, com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage flranfrage, FLRArtikel flrartikel, com.lp.server.anfrage.fastlanereader.generated.FLRAnfrageposition flranfrageposition_zugehoerig, Set ersatztypen_set, Set lieferdaten_set) {
        this.i_sort = i_sort;
        this.anfragepositionart_c_nr = anfragepositionart_c_nr;
        this.n_menge = n_menge;
        this.einheit_c_nr = einheit_c_nr;
        this.c_bez = c_bez;
        this.n_richtpreis = n_richtpreis;
        this.c_zbez = c_zbez;
        this.anfrageposition_i_id_zugehoerig = anfrageposition_i_id_zugehoerig;
        this.flranfrage = flranfrage;
        this.flrartikel = flrartikel;
        this.flranfrageposition_zugehoerig = flranfrageposition_zugehoerig;
        this.ersatztypen_set = ersatztypen_set;
        this.lieferdaten_set = lieferdaten_set;
    }

    /** default constructor */
    public FLRAnfragepositionReport() {
    }

    /** minimal constructor */
    public FLRAnfragepositionReport(Set ersatztypen_set, Set lieferdaten_set) {
        this.ersatztypen_set = ersatztypen_set;
        this.lieferdaten_set = lieferdaten_set;
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

    public String getAnfragepositionart_c_nr() {
        return this.anfragepositionart_c_nr;
    }

    public void setAnfragepositionart_c_nr(String anfragepositionart_c_nr) {
        this.anfragepositionart_c_nr = anfragepositionart_c_nr;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public String getEinheit_c_nr() {
        return this.einheit_c_nr;
    }

    public void setEinheit_c_nr(String einheit_c_nr) {
        this.einheit_c_nr = einheit_c_nr;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public BigDecimal getN_richtpreis() {
        return this.n_richtpreis;
    }

    public void setN_richtpreis(BigDecimal n_richtpreis) {
        this.n_richtpreis = n_richtpreis;
    }

    public String getC_zbez() {
        return this.c_zbez;
    }

    public void setC_zbez(String c_zbez) {
        this.c_zbez = c_zbez;
    }

    public Integer getAnfrageposition_i_id_zugehoerig() {
        return this.anfrageposition_i_id_zugehoerig;
    }

    public void setAnfrageposition_i_id_zugehoerig(Integer anfrageposition_i_id_zugehoerig) {
        this.anfrageposition_i_id_zugehoerig = anfrageposition_i_id_zugehoerig;
    }

    public com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage getFlranfrage() {
        return this.flranfrage;
    }

    public void setFlranfrage(com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage flranfrage) {
        this.flranfrage = flranfrage;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.anfrage.fastlanereader.generated.FLRAnfrageposition getFlranfrageposition_zugehoerig() {
        return this.flranfrageposition_zugehoerig;
    }

    public void setFlranfrageposition_zugehoerig(com.lp.server.anfrage.fastlanereader.generated.FLRAnfrageposition flranfrageposition_zugehoerig) {
        this.flranfrageposition_zugehoerig = flranfrageposition_zugehoerig;
    }

    public Set getErsatztypen_set() {
        return this.ersatztypen_set;
    }

    public void setErsatztypen_set(Set ersatztypen_set) {
        this.ersatztypen_set = ersatztypen_set;
    }

    public Set getLieferdaten_set() {
        return this.lieferdaten_set;
    }

    public void setLieferdaten_set(Set lieferdaten_set) {
        this.lieferdaten_set = lieferdaten_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
