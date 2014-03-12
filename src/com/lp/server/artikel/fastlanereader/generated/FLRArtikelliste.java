/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikelliste implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** persistent field */
    private String c_nr;

    /** persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** persistent field */
    private Short b_lagerbewertet;

    /** persistent field */
    private Short b_lagerbewirtschaftet;

    /** persistent field */
    private Short b_seriennrtragend;

    /** persistent field */
    private Short b_chargennrtragend;

    /** persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private Integer i_garantiezeit;

    /** persistent field */
    private String artikelart_c_nr;

    /** persistent field */
    private String c_referenznr;

    /** persistent field */
    private String einheit_c_nr_bestellung;

    /** persistent field */
    private BigDecimal n_umrechnugsfaktor;

    /** nullable persistent field */
    private Double f_fertigungssatzgroesse;

    /** nullable persistent field */
    private String c_artikelbezhersteller;

    /** nullable persistent field */
    private String c_artikelnrhersteller;

    /** nullable persistent field */
    private Integer mwstsatz_i_id;

    /** nullable persistent field */
    private Double f_lagermindest;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe flrartikelgruppe;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe flrshopgruppe;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse flrartikelklasse;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRGeometrie flrgeometrie;

    /** persistent field */
    private Set artikelsperreset;

    /** persistent field */
    private Set artikellieferantset;

    /** persistent field */
    private Set artikelsprset;

    /** persistent field */
    private Set artikellagerset;

    /** persistent field */
    private Set stuecklisten;

    /** persistent field */
    private Set artikellagerplatzset;

    /** full constructor */
    public FLRArtikelliste(String c_nr, String mandant_c_nr, String einheit_c_nr, Short b_lagerbewertet, Short b_lagerbewirtschaftet, Short b_seriennrtragend, Short b_chargennrtragend, Short b_versteckt, Integer i_garantiezeit, String artikelart_c_nr, String c_referenznr, String einheit_c_nr_bestellung, BigDecimal n_umrechnugsfaktor, Double f_fertigungssatzgroesse, String c_artikelbezhersteller, String c_artikelnrhersteller, Integer mwstsatz_i_id, Double f_lagermindest, com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe flrartikelgruppe, com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe flrshopgruppe, com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse flrartikelklasse, com.lp.server.artikel.fastlanereader.generated.FLRGeometrie flrgeometrie, Set artikelsperreset, Set artikellieferantset, Set artikelsprset, Set artikellagerset, Set stuecklisten, Set artikellagerplatzset) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.einheit_c_nr = einheit_c_nr;
        this.b_lagerbewertet = b_lagerbewertet;
        this.b_lagerbewirtschaftet = b_lagerbewirtschaftet;
        this.b_seriennrtragend = b_seriennrtragend;
        this.b_chargennrtragend = b_chargennrtragend;
        this.b_versteckt = b_versteckt;
        this.i_garantiezeit = i_garantiezeit;
        this.artikelart_c_nr = artikelart_c_nr;
        this.c_referenznr = c_referenznr;
        this.einheit_c_nr_bestellung = einheit_c_nr_bestellung;
        this.n_umrechnugsfaktor = n_umrechnugsfaktor;
        this.f_fertigungssatzgroesse = f_fertigungssatzgroesse;
        this.c_artikelbezhersteller = c_artikelbezhersteller;
        this.c_artikelnrhersteller = c_artikelnrhersteller;
        this.mwstsatz_i_id = mwstsatz_i_id;
        this.f_lagermindest = f_lagermindest;
        this.flrartikelgruppe = flrartikelgruppe;
        this.flrshopgruppe = flrshopgruppe;
        this.flrartikelklasse = flrartikelklasse;
        this.flrgeometrie = flrgeometrie;
        this.artikelsperreset = artikelsperreset;
        this.artikellieferantset = artikellieferantset;
        this.artikelsprset = artikelsprset;
        this.artikellagerset = artikellagerset;
        this.stuecklisten = stuecklisten;
        this.artikellagerplatzset = artikellagerplatzset;
    }

    /** default constructor */
    public FLRArtikelliste() {
    }

    /** minimal constructor */
    public FLRArtikelliste(String c_nr, String mandant_c_nr, Short b_lagerbewertet, Short b_lagerbewirtschaftet, Short b_seriennrtragend, Short b_chargennrtragend, Short b_versteckt, String artikelart_c_nr, String c_referenznr, String einheit_c_nr_bestellung, BigDecimal n_umrechnugsfaktor, Set artikelsperreset, Set artikellieferantset, Set artikelsprset, Set artikellagerset, Set stuecklisten, Set artikellagerplatzset) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.b_lagerbewertet = b_lagerbewertet;
        this.b_lagerbewirtschaftet = b_lagerbewirtschaftet;
        this.b_seriennrtragend = b_seriennrtragend;
        this.b_chargennrtragend = b_chargennrtragend;
        this.b_versteckt = b_versteckt;
        this.artikelart_c_nr = artikelart_c_nr;
        this.c_referenznr = c_referenznr;
        this.einheit_c_nr_bestellung = einheit_c_nr_bestellung;
        this.n_umrechnugsfaktor = n_umrechnugsfaktor;
        this.artikelsperreset = artikelsperreset;
        this.artikellieferantset = artikellieferantset;
        this.artikelsprset = artikelsprset;
        this.artikellagerset = artikellagerset;
        this.stuecklisten = stuecklisten;
        this.artikellagerplatzset = artikellagerplatzset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getEinheit_c_nr() {
        return this.einheit_c_nr;
    }

    public void setEinheit_c_nr(String einheit_c_nr) {
        this.einheit_c_nr = einheit_c_nr;
    }

    public Short getB_lagerbewertet() {
        return this.b_lagerbewertet;
    }

    public void setB_lagerbewertet(Short b_lagerbewertet) {
        this.b_lagerbewertet = b_lagerbewertet;
    }

    public Short getB_lagerbewirtschaftet() {
        return this.b_lagerbewirtschaftet;
    }

    public void setB_lagerbewirtschaftet(Short b_lagerbewirtschaftet) {
        this.b_lagerbewirtschaftet = b_lagerbewirtschaftet;
    }

    public Short getB_seriennrtragend() {
        return this.b_seriennrtragend;
    }

    public void setB_seriennrtragend(Short b_seriennrtragend) {
        this.b_seriennrtragend = b_seriennrtragend;
    }

    public Short getB_chargennrtragend() {
        return this.b_chargennrtragend;
    }

    public void setB_chargennrtragend(Short b_chargennrtragend) {
        this.b_chargennrtragend = b_chargennrtragend;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public Integer getI_garantiezeit() {
        return this.i_garantiezeit;
    }

    public void setI_garantiezeit(Integer i_garantiezeit) {
        this.i_garantiezeit = i_garantiezeit;
    }

    public String getArtikelart_c_nr() {
        return this.artikelart_c_nr;
    }

    public void setArtikelart_c_nr(String artikelart_c_nr) {
        this.artikelart_c_nr = artikelart_c_nr;
    }

    public String getC_referenznr() {
        return this.c_referenznr;
    }

    public void setC_referenznr(String c_referenznr) {
        this.c_referenznr = c_referenznr;
    }

    public String getEinheit_c_nr_bestellung() {
        return this.einheit_c_nr_bestellung;
    }

    public void setEinheit_c_nr_bestellung(String einheit_c_nr_bestellung) {
        this.einheit_c_nr_bestellung = einheit_c_nr_bestellung;
    }

    public BigDecimal getN_umrechnugsfaktor() {
        return this.n_umrechnugsfaktor;
    }

    public void setN_umrechnugsfaktor(BigDecimal n_umrechnugsfaktor) {
        this.n_umrechnugsfaktor = n_umrechnugsfaktor;
    }

    public Double getF_fertigungssatzgroesse() {
        return this.f_fertigungssatzgroesse;
    }

    public void setF_fertigungssatzgroesse(Double f_fertigungssatzgroesse) {
        this.f_fertigungssatzgroesse = f_fertigungssatzgroesse;
    }

    public String getC_artikelbezhersteller() {
        return this.c_artikelbezhersteller;
    }

    public void setC_artikelbezhersteller(String c_artikelbezhersteller) {
        this.c_artikelbezhersteller = c_artikelbezhersteller;
    }

    public String getC_artikelnrhersteller() {
        return this.c_artikelnrhersteller;
    }

    public void setC_artikelnrhersteller(String c_artikelnrhersteller) {
        this.c_artikelnrhersteller = c_artikelnrhersteller;
    }

    public Integer getMwstsatz_i_id() {
        return this.mwstsatz_i_id;
    }

    public void setMwstsatz_i_id(Integer mwstsatz_i_id) {
        this.mwstsatz_i_id = mwstsatz_i_id;
    }

    public Double getF_lagermindest() {
        return this.f_lagermindest;
    }

    public void setF_lagermindest(Double f_lagermindest) {
        this.f_lagermindest = f_lagermindest;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe getFlrartikelgruppe() {
        return this.flrartikelgruppe;
    }

    public void setFlrartikelgruppe(com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe flrartikelgruppe) {
        this.flrartikelgruppe = flrartikelgruppe;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe getFlrshopgruppe() {
        return this.flrshopgruppe;
    }

    public void setFlrshopgruppe(com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe flrshopgruppe) {
        this.flrshopgruppe = flrshopgruppe;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse getFlrartikelklasse() {
        return this.flrartikelklasse;
    }

    public void setFlrartikelklasse(com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse flrartikelklasse) {
        this.flrartikelklasse = flrartikelklasse;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRGeometrie getFlrgeometrie() {
        return this.flrgeometrie;
    }

    public void setFlrgeometrie(com.lp.server.artikel.fastlanereader.generated.FLRGeometrie flrgeometrie) {
        this.flrgeometrie = flrgeometrie;
    }

    public Set getArtikelsperreset() {
        return this.artikelsperreset;
    }

    public void setArtikelsperreset(Set artikelsperreset) {
        this.artikelsperreset = artikelsperreset;
    }

    public Set getArtikellieferantset() {
        return this.artikellieferantset;
    }

    public void setArtikellieferantset(Set artikellieferantset) {
        this.artikellieferantset = artikellieferantset;
    }

    public Set getArtikelsprset() {
        return this.artikelsprset;
    }

    public void setArtikelsprset(Set artikelsprset) {
        this.artikelsprset = artikelsprset;
    }

    public Set getArtikellagerset() {
        return this.artikellagerset;
    }

    public void setArtikellagerset(Set artikellagerset) {
        this.artikellagerset = artikellagerset;
    }

    public Set getStuecklisten() {
        return this.stuecklisten;
    }

    public void setStuecklisten(Set stuecklisten) {
        this.stuecklisten = stuecklisten;
    }

    public Set getArtikellagerplatzset() {
        return this.artikellagerplatzset;
    }

    public void setArtikellagerplatzset(Set artikellagerplatzset) {
        this.artikellagerplatzset = artikellagerplatzset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
