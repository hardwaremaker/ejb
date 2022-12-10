package com.lp.server.bestellung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRLager;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRWareneingang implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_lieferscheinnr;

    /** nullable persistent field */
    private Date t_lieferscheindatum;

    /** nullable persistent field */
    private BigDecimal n_transportkosten;

    /** nullable persistent field */
    private BigDecimal n_zollkosten;

    /** nullable persistent field */
    private BigDecimal n_bankspesen;

    /** nullable persistent field */
    private BigDecimal n_sonstigespesen;

    /** nullable persistent field */
    private BigDecimal n_wechselkurs;

    /** nullable persistent field */
    private Double f_gemeinkostenfaktor;

    /** nullable persistent field */
    private Double f_rabattsatz;

    /** nullable persistent field */
    private Date t_wareneingansdatum;

    /** nullable persistent field */
    private Integer bestellung_i_id;

    /** persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer lager_i_id;

    /** nullable persistent field */
    private Integer eingangsrechnung_i_id;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung;

    /** nullable persistent field */
    private FLRLager flrlager;

    /** nullable persistent field */
    private FLREingangsrechnung flreingangsrechnung;

    /** persistent field */
    private Set weps;

    /** full constructor */
    public FLRWareneingang(String c_lieferscheinnr, Date t_lieferscheindatum, BigDecimal n_transportkosten, BigDecimal n_zollkosten, BigDecimal n_bankspesen, BigDecimal n_sonstigespesen, BigDecimal n_wechselkurs, Double f_gemeinkostenfaktor, Double f_rabattsatz, Date t_wareneingansdatum, Integer bestellung_i_id, Integer i_sort, Integer lager_i_id, Integer eingangsrechnung_i_id, com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung, FLRLager flrlager, FLREingangsrechnung flreingangsrechnung, Set weps) {
        this.c_lieferscheinnr = c_lieferscheinnr;
        this.t_lieferscheindatum = t_lieferscheindatum;
        this.n_transportkosten = n_transportkosten;
        this.n_zollkosten = n_zollkosten;
        this.n_bankspesen = n_bankspesen;
        this.n_sonstigespesen = n_sonstigespesen;
        this.n_wechselkurs = n_wechselkurs;
        this.f_gemeinkostenfaktor = f_gemeinkostenfaktor;
        this.f_rabattsatz = f_rabattsatz;
        this.t_wareneingansdatum = t_wareneingansdatum;
        this.bestellung_i_id = bestellung_i_id;
        this.i_sort = i_sort;
        this.lager_i_id = lager_i_id;
        this.eingangsrechnung_i_id = eingangsrechnung_i_id;
        this.flrbestellung = flrbestellung;
        this.flrlager = flrlager;
        this.flreingangsrechnung = flreingangsrechnung;
        this.weps = weps;
    }

    /** default constructor */
    public FLRWareneingang() {
    }

    /** minimal constructor */
    public FLRWareneingang(Integer i_sort, Set weps) {
        this.i_sort = i_sort;
        this.weps = weps;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_lieferscheinnr() {
        return this.c_lieferscheinnr;
    }

    public void setC_lieferscheinnr(String c_lieferscheinnr) {
        this.c_lieferscheinnr = c_lieferscheinnr;
    }

    public Date getT_lieferscheindatum() {
        return this.t_lieferscheindatum;
    }

    public void setT_lieferscheindatum(Date t_lieferscheindatum) {
        this.t_lieferscheindatum = t_lieferscheindatum;
    }

    public BigDecimal getN_transportkosten() {
        return this.n_transportkosten;
    }

    public void setN_transportkosten(BigDecimal n_transportkosten) {
        this.n_transportkosten = n_transportkosten;
    }

    public BigDecimal getN_zollkosten() {
        return this.n_zollkosten;
    }

    public void setN_zollkosten(BigDecimal n_zollkosten) {
        this.n_zollkosten = n_zollkosten;
    }

    public BigDecimal getN_bankspesen() {
        return this.n_bankspesen;
    }

    public void setN_bankspesen(BigDecimal n_bankspesen) {
        this.n_bankspesen = n_bankspesen;
    }

    public BigDecimal getN_sonstigespesen() {
        return this.n_sonstigespesen;
    }

    public void setN_sonstigespesen(BigDecimal n_sonstigespesen) {
        this.n_sonstigespesen = n_sonstigespesen;
    }

    public BigDecimal getN_wechselkurs() {
        return this.n_wechselkurs;
    }

    public void setN_wechselkurs(BigDecimal n_wechselkurs) {
        this.n_wechselkurs = n_wechselkurs;
    }

    public Double getF_gemeinkostenfaktor() {
        return this.f_gemeinkostenfaktor;
    }

    public void setF_gemeinkostenfaktor(Double f_gemeinkostenfaktor) {
        this.f_gemeinkostenfaktor = f_gemeinkostenfaktor;
    }

    public Double getF_rabattsatz() {
        return this.f_rabattsatz;
    }

    public void setF_rabattsatz(Double f_rabattsatz) {
        this.f_rabattsatz = f_rabattsatz;
    }

    public Date getT_wareneingansdatum() {
        return this.t_wareneingansdatum;
    }

    public void setT_wareneingansdatum(Date t_wareneingansdatum) {
        this.t_wareneingansdatum = t_wareneingansdatum;
    }

    public Integer getBestellung_i_id() {
        return this.bestellung_i_id;
    }

    public void setBestellung_i_id(Integer bestellung_i_id) {
        this.bestellung_i_id = bestellung_i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getLager_i_id() {
        return this.lager_i_id;
    }

    public void setLager_i_id(Integer lager_i_id) {
        this.lager_i_id = lager_i_id;
    }

    public Integer getEingangsrechnung_i_id() {
        return this.eingangsrechnung_i_id;
    }

    public void setEingangsrechnung_i_id(Integer eingangsrechnung_i_id) {
        this.eingangsrechnung_i_id = eingangsrechnung_i_id;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRBestellung getFlrbestellung() {
        return this.flrbestellung;
    }

    public void setFlrbestellung(com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung) {
        this.flrbestellung = flrbestellung;
    }

    public FLRLager getFlrlager() {
        return this.flrlager;
    }

    public void setFlrlager(FLRLager flrlager) {
        this.flrlager = flrlager;
    }

    public FLREingangsrechnung getFlreingangsrechnung() {
        return this.flreingangsrechnung;
    }

    public void setFlreingangsrechnung(FLREingangsrechnung flreingangsrechnung) {
        this.flreingangsrechnung = flreingangsrechnung;
    }

    public Set getWeps() {
        return this.weps;
    }

    public void setWeps(Set weps) {
        this.weps = weps;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
