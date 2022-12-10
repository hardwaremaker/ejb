package com.lp.server.stueckliste.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRVerschleissteil;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRStklpruefplan implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer stuecklisteposition_i_id_kontakt;

    /** nullable persistent field */
    private Integer stuecklisteposition_i_id_litze;

    /** nullable persistent field */
    private Integer stuecklisteposition_i_id_litze2;

    /** nullable persistent field */
    private Integer verschleissteil_i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer pruefkombination_i_id;

    /** nullable persistent field */
    private Integer stueckliste_i_id;

    /** nullable persistent field */
    private Short b_doppelanschlag;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition flrstuecklisteposition_kontakt;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition flrstuecklisteposition_litze;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition flrstuecklisteposition_litze2;

    /** nullable persistent field */
    private FLRVerschleissteil flrverschleissteil;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRPruefart flrpruefart;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste flrstueckliste;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombination flrpruefkombination;

    /** full constructor */
    public FLRStklpruefplan(Integer stuecklisteposition_i_id_kontakt, Integer stuecklisteposition_i_id_litze, Integer stuecklisteposition_i_id_litze2, Integer verschleissteil_i_id, Integer i_sort, Integer pruefkombination_i_id, Integer stueckliste_i_id, Short b_doppelanschlag, com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition flrstuecklisteposition_kontakt, com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition flrstuecklisteposition_litze, com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition flrstuecklisteposition_litze2, FLRVerschleissteil flrverschleissteil, com.lp.server.stueckliste.fastlanereader.generated.FLRPruefart flrpruefart, com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste flrstueckliste, com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombination flrpruefkombination) {
        this.stuecklisteposition_i_id_kontakt = stuecklisteposition_i_id_kontakt;
        this.stuecklisteposition_i_id_litze = stuecklisteposition_i_id_litze;
        this.stuecklisteposition_i_id_litze2 = stuecklisteposition_i_id_litze2;
        this.verschleissteil_i_id = verschleissteil_i_id;
        this.i_sort = i_sort;
        this.pruefkombination_i_id = pruefkombination_i_id;
        this.stueckliste_i_id = stueckliste_i_id;
        this.b_doppelanschlag = b_doppelanschlag;
        this.flrstuecklisteposition_kontakt = flrstuecklisteposition_kontakt;
        this.flrstuecklisteposition_litze = flrstuecklisteposition_litze;
        this.flrstuecklisteposition_litze2 = flrstuecklisteposition_litze2;
        this.flrverschleissteil = flrverschleissteil;
        this.flrpruefart = flrpruefart;
        this.flrstueckliste = flrstueckliste;
        this.flrpruefkombination = flrpruefkombination;
    }

    /** default constructor */
    public FLRStklpruefplan() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getStuecklisteposition_i_id_kontakt() {
        return this.stuecklisteposition_i_id_kontakt;
    }

    public void setStuecklisteposition_i_id_kontakt(Integer stuecklisteposition_i_id_kontakt) {
        this.stuecklisteposition_i_id_kontakt = stuecklisteposition_i_id_kontakt;
    }

    public Integer getStuecklisteposition_i_id_litze() {
        return this.stuecklisteposition_i_id_litze;
    }

    public void setStuecklisteposition_i_id_litze(Integer stuecklisteposition_i_id_litze) {
        this.stuecklisteposition_i_id_litze = stuecklisteposition_i_id_litze;
    }

    public Integer getStuecklisteposition_i_id_litze2() {
        return this.stuecklisteposition_i_id_litze2;
    }

    public void setStuecklisteposition_i_id_litze2(Integer stuecklisteposition_i_id_litze2) {
        this.stuecklisteposition_i_id_litze2 = stuecklisteposition_i_id_litze2;
    }

    public Integer getVerschleissteil_i_id() {
        return this.verschleissteil_i_id;
    }

    public void setVerschleissteil_i_id(Integer verschleissteil_i_id) {
        this.verschleissteil_i_id = verschleissteil_i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getPruefkombination_i_id() {
        return this.pruefkombination_i_id;
    }

    public void setPruefkombination_i_id(Integer pruefkombination_i_id) {
        this.pruefkombination_i_id = pruefkombination_i_id;
    }

    public Integer getStueckliste_i_id() {
        return this.stueckliste_i_id;
    }

    public void setStueckliste_i_id(Integer stueckliste_i_id) {
        this.stueckliste_i_id = stueckliste_i_id;
    }

    public Short getB_doppelanschlag() {
        return this.b_doppelanschlag;
    }

    public void setB_doppelanschlag(Short b_doppelanschlag) {
        this.b_doppelanschlag = b_doppelanschlag;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition getFlrstuecklisteposition_kontakt() {
        return this.flrstuecklisteposition_kontakt;
    }

    public void setFlrstuecklisteposition_kontakt(com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition flrstuecklisteposition_kontakt) {
        this.flrstuecklisteposition_kontakt = flrstuecklisteposition_kontakt;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition getFlrstuecklisteposition_litze() {
        return this.flrstuecklisteposition_litze;
    }

    public void setFlrstuecklisteposition_litze(com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition flrstuecklisteposition_litze) {
        this.flrstuecklisteposition_litze = flrstuecklisteposition_litze;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition getFlrstuecklisteposition_litze2() {
        return this.flrstuecklisteposition_litze2;
    }

    public void setFlrstuecklisteposition_litze2(com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition flrstuecklisteposition_litze2) {
        this.flrstuecklisteposition_litze2 = flrstuecklisteposition_litze2;
    }

    public FLRVerschleissteil getFlrverschleissteil() {
        return this.flrverschleissteil;
    }

    public void setFlrverschleissteil(FLRVerschleissteil flrverschleissteil) {
        this.flrverschleissteil = flrverschleissteil;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRPruefart getFlrpruefart() {
        return this.flrpruefart;
    }

    public void setFlrpruefart(com.lp.server.stueckliste.fastlanereader.generated.FLRPruefart flrpruefart) {
        this.flrpruefart = flrpruefart;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste getFlrstueckliste() {
        return this.flrstueckliste;
    }

    public void setFlrstueckliste(com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste flrstueckliste) {
        this.flrstueckliste = flrstueckliste;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombination getFlrpruefkombination() {
        return this.flrpruefkombination;
    }

    public void setFlrpruefkombination(com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombination flrpruefkombination) {
        this.flrpruefkombination = flrpruefkombination;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
