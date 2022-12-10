package com.lp.server.stueckliste.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.artikel.fastlanereader.generated.FLRVerschleissteil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPruefkombination implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer artikel_i_id_kontakt;

    /** nullable persistent field */
    private Integer artikel_i_id_litze;

    /** nullable persistent field */
    private Integer artikel_i_id_litze2;

    /** nullable persistent field */
    private Integer verschleissteil_i_id;

    /** nullable persistent field */
    private BigDecimal n_crimphoehe_draht;

    /** nullable persistent field */
    private BigDecimal n_crimphoehe_isolation;

    /** nullable persistent field */
    private BigDecimal n_crimpbreite_draht;

    /** nullable persistent field */
    private BigDecimal n_crimpbreite_isolation;

    /** nullable persistent field */
    private BigDecimal n_toleranz_crimphoehe_draht;

    /** nullable persistent field */
    private BigDecimal n_toleranz_crimphoehe_isolation;

    /** nullable persistent field */
    private BigDecimal n_toleranz_crimpbreite_draht;

    /** nullable persistent field */
    private BigDecimal n_toleranz_crimpbreite_isolation;

    /** nullable persistent field */
    private BigDecimal n_toleranz_wert;

    /** nullable persistent field */
    private BigDecimal n_wert;

    /** nullable persistent field */
    private BigDecimal n_abzugskraft_litze;

    /** nullable persistent field */
    private BigDecimal n_abzugskraft_litze2;

    /** nullable persistent field */
    private Short b_standard;

    /** nullable persistent field */
    private Short b_doppelanschlag;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel_kontakt;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel_litze;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel_litze2;

    /** nullable persistent field */
    private FLRVerschleissteil flrverschleissteil;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRPruefart flrpruefart;

    /** persistent field */
    private Set pruefkombinationspr_set;

    /** full constructor */
    public FLRPruefkombination(Integer artikel_i_id_kontakt, Integer artikel_i_id_litze, Integer artikel_i_id_litze2, Integer verschleissteil_i_id, BigDecimal n_crimphoehe_draht, BigDecimal n_crimphoehe_isolation, BigDecimal n_crimpbreite_draht, BigDecimal n_crimpbreite_isolation, BigDecimal n_toleranz_crimphoehe_draht, BigDecimal n_toleranz_crimphoehe_isolation, BigDecimal n_toleranz_crimpbreite_draht, BigDecimal n_toleranz_crimpbreite_isolation, BigDecimal n_toleranz_wert, BigDecimal n_wert, BigDecimal n_abzugskraft_litze, BigDecimal n_abzugskraft_litze2, Short b_standard, Short b_doppelanschlag, FLRArtikelliste flrartikel_kontakt, FLRArtikelliste flrartikel_litze, FLRArtikelliste flrartikel_litze2, FLRVerschleissteil flrverschleissteil, com.lp.server.stueckliste.fastlanereader.generated.FLRPruefart flrpruefart, Set pruefkombinationspr_set) {
        this.artikel_i_id_kontakt = artikel_i_id_kontakt;
        this.artikel_i_id_litze = artikel_i_id_litze;
        this.artikel_i_id_litze2 = artikel_i_id_litze2;
        this.verschleissteil_i_id = verschleissteil_i_id;
        this.n_crimphoehe_draht = n_crimphoehe_draht;
        this.n_crimphoehe_isolation = n_crimphoehe_isolation;
        this.n_crimpbreite_draht = n_crimpbreite_draht;
        this.n_crimpbreite_isolation = n_crimpbreite_isolation;
        this.n_toleranz_crimphoehe_draht = n_toleranz_crimphoehe_draht;
        this.n_toleranz_crimphoehe_isolation = n_toleranz_crimphoehe_isolation;
        this.n_toleranz_crimpbreite_draht = n_toleranz_crimpbreite_draht;
        this.n_toleranz_crimpbreite_isolation = n_toleranz_crimpbreite_isolation;
        this.n_toleranz_wert = n_toleranz_wert;
        this.n_wert = n_wert;
        this.n_abzugskraft_litze = n_abzugskraft_litze;
        this.n_abzugskraft_litze2 = n_abzugskraft_litze2;
        this.b_standard = b_standard;
        this.b_doppelanschlag = b_doppelanschlag;
        this.flrartikel_kontakt = flrartikel_kontakt;
        this.flrartikel_litze = flrartikel_litze;
        this.flrartikel_litze2 = flrartikel_litze2;
        this.flrverschleissteil = flrverschleissteil;
        this.flrpruefart = flrpruefart;
        this.pruefkombinationspr_set = pruefkombinationspr_set;
    }

    /** default constructor */
    public FLRPruefkombination() {
    }

    /** minimal constructor */
    public FLRPruefkombination(Set pruefkombinationspr_set) {
        this.pruefkombinationspr_set = pruefkombinationspr_set;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getArtikel_i_id_kontakt() {
        return this.artikel_i_id_kontakt;
    }

    public void setArtikel_i_id_kontakt(Integer artikel_i_id_kontakt) {
        this.artikel_i_id_kontakt = artikel_i_id_kontakt;
    }

    public Integer getArtikel_i_id_litze() {
        return this.artikel_i_id_litze;
    }

    public void setArtikel_i_id_litze(Integer artikel_i_id_litze) {
        this.artikel_i_id_litze = artikel_i_id_litze;
    }

    public Integer getArtikel_i_id_litze2() {
        return this.artikel_i_id_litze2;
    }

    public void setArtikel_i_id_litze2(Integer artikel_i_id_litze2) {
        this.artikel_i_id_litze2 = artikel_i_id_litze2;
    }

    public Integer getVerschleissteil_i_id() {
        return this.verschleissteil_i_id;
    }

    public void setVerschleissteil_i_id(Integer verschleissteil_i_id) {
        this.verschleissteil_i_id = verschleissteil_i_id;
    }

    public BigDecimal getN_crimphoehe_draht() {
        return this.n_crimphoehe_draht;
    }

    public void setN_crimphoehe_draht(BigDecimal n_crimphoehe_draht) {
        this.n_crimphoehe_draht = n_crimphoehe_draht;
    }

    public BigDecimal getN_crimphoehe_isolation() {
        return this.n_crimphoehe_isolation;
    }

    public void setN_crimphoehe_isolation(BigDecimal n_crimphoehe_isolation) {
        this.n_crimphoehe_isolation = n_crimphoehe_isolation;
    }

    public BigDecimal getN_crimpbreite_draht() {
        return this.n_crimpbreite_draht;
    }

    public void setN_crimpbreite_draht(BigDecimal n_crimpbreite_draht) {
        this.n_crimpbreite_draht = n_crimpbreite_draht;
    }

    public BigDecimal getN_crimpbreite_isolation() {
        return this.n_crimpbreite_isolation;
    }

    public void setN_crimpbreite_isolation(BigDecimal n_crimpbreite_isolation) {
        this.n_crimpbreite_isolation = n_crimpbreite_isolation;
    }

    public BigDecimal getN_toleranz_crimphoehe_draht() {
        return this.n_toleranz_crimphoehe_draht;
    }

    public void setN_toleranz_crimphoehe_draht(BigDecimal n_toleranz_crimphoehe_draht) {
        this.n_toleranz_crimphoehe_draht = n_toleranz_crimphoehe_draht;
    }

    public BigDecimal getN_toleranz_crimphoehe_isolation() {
        return this.n_toleranz_crimphoehe_isolation;
    }

    public void setN_toleranz_crimphoehe_isolation(BigDecimal n_toleranz_crimphoehe_isolation) {
        this.n_toleranz_crimphoehe_isolation = n_toleranz_crimphoehe_isolation;
    }

    public BigDecimal getN_toleranz_crimpbreite_draht() {
        return this.n_toleranz_crimpbreite_draht;
    }

    public void setN_toleranz_crimpbreite_draht(BigDecimal n_toleranz_crimpbreite_draht) {
        this.n_toleranz_crimpbreite_draht = n_toleranz_crimpbreite_draht;
    }

    public BigDecimal getN_toleranz_crimpbreite_isolation() {
        return this.n_toleranz_crimpbreite_isolation;
    }

    public void setN_toleranz_crimpbreite_isolation(BigDecimal n_toleranz_crimpbreite_isolation) {
        this.n_toleranz_crimpbreite_isolation = n_toleranz_crimpbreite_isolation;
    }

    public BigDecimal getN_toleranz_wert() {
        return this.n_toleranz_wert;
    }

    public void setN_toleranz_wert(BigDecimal n_toleranz_wert) {
        this.n_toleranz_wert = n_toleranz_wert;
    }

    public BigDecimal getN_wert() {
        return this.n_wert;
    }

    public void setN_wert(BigDecimal n_wert) {
        this.n_wert = n_wert;
    }

    public BigDecimal getN_abzugskraft_litze() {
        return this.n_abzugskraft_litze;
    }

    public void setN_abzugskraft_litze(BigDecimal n_abzugskraft_litze) {
        this.n_abzugskraft_litze = n_abzugskraft_litze;
    }

    public BigDecimal getN_abzugskraft_litze2() {
        return this.n_abzugskraft_litze2;
    }

    public void setN_abzugskraft_litze2(BigDecimal n_abzugskraft_litze2) {
        this.n_abzugskraft_litze2 = n_abzugskraft_litze2;
    }

    public Short getB_standard() {
        return this.b_standard;
    }

    public void setB_standard(Short b_standard) {
        this.b_standard = b_standard;
    }

    public Short getB_doppelanschlag() {
        return this.b_doppelanschlag;
    }

    public void setB_doppelanschlag(Short b_doppelanschlag) {
        this.b_doppelanschlag = b_doppelanschlag;
    }

    public FLRArtikelliste getFlrartikel_kontakt() {
        return this.flrartikel_kontakt;
    }

    public void setFlrartikel_kontakt(FLRArtikelliste flrartikel_kontakt) {
        this.flrartikel_kontakt = flrartikel_kontakt;
    }

    public FLRArtikelliste getFlrartikel_litze() {
        return this.flrartikel_litze;
    }

    public void setFlrartikel_litze(FLRArtikelliste flrartikel_litze) {
        this.flrartikel_litze = flrartikel_litze;
    }

    public FLRArtikelliste getFlrartikel_litze2() {
        return this.flrartikel_litze2;
    }

    public void setFlrartikel_litze2(FLRArtikelliste flrartikel_litze2) {
        this.flrartikel_litze2 = flrartikel_litze2;
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

    public Set getPruefkombinationspr_set() {
        return this.pruefkombinationspr_set;
    }

    public void setPruefkombinationspr_set(Set pruefkombinationspr_set) {
        this.pruefkombinationspr_set = pruefkombinationspr_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
