package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVerfuegbarkeit implements Serializable {

    /** identifier field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer los_i_id;

    /** nullable persistent field */
    private BigDecimal n_sollmenge;

    /** nullable persistent field */
    private BigDecimal n_lagerstand;

    /** nullable persistent field */
    private BigDecimal n_soll_minus_lager_plus_bestellt;

    /** nullable persistent field */
    private BigDecimal n_fehlmenge_minus_lager_plus_bestellt;

    /** nullable persistent field */
    private BigDecimal n_fehlmenge_minus_lager;

    /** nullable persistent field */
    private Integer i_anzahlsperren;

    /** nullable persistent field */
    private FLRArtikelliste flrartikelliste;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos;

    /** full constructor */
    public FLRVerfuegbarkeit(Integer los_i_id, BigDecimal n_sollmenge, BigDecimal n_lagerstand, BigDecimal n_soll_minus_lager_plus_bestellt, BigDecimal n_fehlmenge_minus_lager_plus_bestellt, BigDecimal n_fehlmenge_minus_lager, Integer i_anzahlsperren, FLRArtikelliste flrartikelliste, com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos) {
        this.los_i_id = los_i_id;
        this.n_sollmenge = n_sollmenge;
        this.n_lagerstand = n_lagerstand;
        this.n_soll_minus_lager_plus_bestellt = n_soll_minus_lager_plus_bestellt;
        this.n_fehlmenge_minus_lager_plus_bestellt = n_fehlmenge_minus_lager_plus_bestellt;
        this.n_fehlmenge_minus_lager = n_fehlmenge_minus_lager;
        this.i_anzahlsperren = i_anzahlsperren;
        this.flrartikelliste = flrartikelliste;
        this.flrlos = flrlos;
    }

    /** default constructor */
    public FLRVerfuegbarkeit() {
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public BigDecimal getN_sollmenge() {
        return this.n_sollmenge;
    }

    public void setN_sollmenge(BigDecimal n_sollmenge) {
        this.n_sollmenge = n_sollmenge;
    }

    public BigDecimal getN_lagerstand() {
        return this.n_lagerstand;
    }

    public void setN_lagerstand(BigDecimal n_lagerstand) {
        this.n_lagerstand = n_lagerstand;
    }

    public BigDecimal getN_soll_minus_lager_plus_bestellt() {
        return this.n_soll_minus_lager_plus_bestellt;
    }

    public void setN_soll_minus_lager_plus_bestellt(BigDecimal n_soll_minus_lager_plus_bestellt) {
        this.n_soll_minus_lager_plus_bestellt = n_soll_minus_lager_plus_bestellt;
    }

    public BigDecimal getN_fehlmenge_minus_lager_plus_bestellt() {
        return this.n_fehlmenge_minus_lager_plus_bestellt;
    }

    public void setN_fehlmenge_minus_lager_plus_bestellt(BigDecimal n_fehlmenge_minus_lager_plus_bestellt) {
        this.n_fehlmenge_minus_lager_plus_bestellt = n_fehlmenge_minus_lager_plus_bestellt;
    }

    public BigDecimal getN_fehlmenge_minus_lager() {
        return this.n_fehlmenge_minus_lager;
    }

    public void setN_fehlmenge_minus_lager(BigDecimal n_fehlmenge_minus_lager) {
        this.n_fehlmenge_minus_lager = n_fehlmenge_minus_lager;
    }

    public Integer getI_anzahlsperren() {
        return this.i_anzahlsperren;
    }

    public void setI_anzahlsperren(Integer i_anzahlsperren) {
        this.i_anzahlsperren = i_anzahlsperren;
    }

    public FLRArtikelliste getFlrartikelliste() {
        return this.flrartikelliste;
    }

    public void setFlrartikelliste(FLRArtikelliste flrartikelliste) {
        this.flrartikelliste = flrartikelliste;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("artikel_i_id", getArtikel_i_id())
            .toString();
    }

}
