package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRVerschleissteil;
import com.lp.server.artikel.fastlanereader.generated.FLRWerkzeug;
import com.lp.server.stueckliste.fastlanereader.generated.FLRPruefart;
import com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombination;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLospruefplan implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer lossollmaterial_i_id_kontakt;

    /** nullable persistent field */
    private Integer lossollmaterial_i_id_litze;

    /** nullable persistent field */
    private Integer lossollmaterial_i_id_litze2;

    /** nullable persistent field */
    private Integer werkzeug_i_id;

    /** nullable persistent field */
    private Integer verschleissteil_i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer pruefkombination_i_id;

    /** nullable persistent field */
    private Short b_doppelanschlag;

    /** nullable persistent field */
    private Integer los_i_id;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial_kontakt;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial_litze;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial_litze2;

    /** nullable persistent field */
    private FLRWerkzeug flrwerkzeug;

    /** nullable persistent field */
    private FLRVerschleissteil flrverschleissteil;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos;

    /** nullable persistent field */
    private FLRPruefart flrpruefart;

    /** nullable persistent field */
    private FLRPruefkombination flrpruefkombination;

    /** full constructor */
    public FLRLospruefplan(Integer lossollmaterial_i_id_kontakt, Integer lossollmaterial_i_id_litze, Integer lossollmaterial_i_id_litze2, Integer werkzeug_i_id, Integer verschleissteil_i_id, Integer i_sort, Integer pruefkombination_i_id, Short b_doppelanschlag, Integer los_i_id, com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial_kontakt, com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial_litze, com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial_litze2, FLRWerkzeug flrwerkzeug, FLRVerschleissteil flrverschleissteil, com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos, FLRPruefart flrpruefart, FLRPruefkombination flrpruefkombination) {
        this.lossollmaterial_i_id_kontakt = lossollmaterial_i_id_kontakt;
        this.lossollmaterial_i_id_litze = lossollmaterial_i_id_litze;
        this.lossollmaterial_i_id_litze2 = lossollmaterial_i_id_litze2;
        this.werkzeug_i_id = werkzeug_i_id;
        this.verschleissteil_i_id = verschleissteil_i_id;
        this.i_sort = i_sort;
        this.pruefkombination_i_id = pruefkombination_i_id;
        this.b_doppelanschlag = b_doppelanschlag;
        this.los_i_id = los_i_id;
        this.flrlossollmaterial_kontakt = flrlossollmaterial_kontakt;
        this.flrlossollmaterial_litze = flrlossollmaterial_litze;
        this.flrlossollmaterial_litze2 = flrlossollmaterial_litze2;
        this.flrwerkzeug = flrwerkzeug;
        this.flrverschleissteil = flrverschleissteil;
        this.flrlos = flrlos;
        this.flrpruefart = flrpruefart;
        this.flrpruefkombination = flrpruefkombination;
    }

    /** default constructor */
    public FLRLospruefplan() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getLossollmaterial_i_id_kontakt() {
        return this.lossollmaterial_i_id_kontakt;
    }

    public void setLossollmaterial_i_id_kontakt(Integer lossollmaterial_i_id_kontakt) {
        this.lossollmaterial_i_id_kontakt = lossollmaterial_i_id_kontakt;
    }

    public Integer getLossollmaterial_i_id_litze() {
        return this.lossollmaterial_i_id_litze;
    }

    public void setLossollmaterial_i_id_litze(Integer lossollmaterial_i_id_litze) {
        this.lossollmaterial_i_id_litze = lossollmaterial_i_id_litze;
    }

    public Integer getLossollmaterial_i_id_litze2() {
        return this.lossollmaterial_i_id_litze2;
    }

    public void setLossollmaterial_i_id_litze2(Integer lossollmaterial_i_id_litze2) {
        this.lossollmaterial_i_id_litze2 = lossollmaterial_i_id_litze2;
    }

    public Integer getWerkzeug_i_id() {
        return this.werkzeug_i_id;
    }

    public void setWerkzeug_i_id(Integer werkzeug_i_id) {
        this.werkzeug_i_id = werkzeug_i_id;
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

    public Short getB_doppelanschlag() {
        return this.b_doppelanschlag;
    }

    public void setB_doppelanschlag(Short b_doppelanschlag) {
        this.b_doppelanschlag = b_doppelanschlag;
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial getFlrlossollmaterial_kontakt() {
        return this.flrlossollmaterial_kontakt;
    }

    public void setFlrlossollmaterial_kontakt(com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial_kontakt) {
        this.flrlossollmaterial_kontakt = flrlossollmaterial_kontakt;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial getFlrlossollmaterial_litze() {
        return this.flrlossollmaterial_litze;
    }

    public void setFlrlossollmaterial_litze(com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial_litze) {
        this.flrlossollmaterial_litze = flrlossollmaterial_litze;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial getFlrlossollmaterial_litze2() {
        return this.flrlossollmaterial_litze2;
    }

    public void setFlrlossollmaterial_litze2(com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial_litze2) {
        this.flrlossollmaterial_litze2 = flrlossollmaterial_litze2;
    }

    public FLRWerkzeug getFlrwerkzeug() {
        return this.flrwerkzeug;
    }

    public void setFlrwerkzeug(FLRWerkzeug flrwerkzeug) {
        this.flrwerkzeug = flrwerkzeug;
    }

    public FLRVerschleissteil getFlrverschleissteil() {
        return this.flrverschleissteil;
    }

    public void setFlrverschleissteil(FLRVerschleissteil flrverschleissteil) {
        this.flrverschleissteil = flrverschleissteil;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public FLRPruefart getFlrpruefart() {
        return this.flrpruefart;
    }

    public void setFlrpruefart(FLRPruefart flrpruefart) {
        this.flrpruefart = flrpruefart;
    }

    public FLRPruefkombination getFlrpruefkombination() {
        return this.flrpruefkombination;
    }

    public void setFlrpruefkombination(FLRPruefkombination flrpruefkombination) {
        this.flrpruefkombination = flrpruefkombination;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
