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

import com.lp.server.partner.fastlanereader.generated.FLRLiefergruppe;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikel implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private String artikelart_c_nr;

    /** nullable persistent field */
    private Short b_lagerbewertet;

    /** nullable persistent field */
    private Short b_lagerbewirtschaftet;

    /** nullable persistent field */
    private Short b_seriennrtragend;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private Short b_chargennrtragend;

    /** nullable persistent field */
    private Double f_verschnittfaktor;

    /** nullable persistent field */
    private Double f_verschnittbasis;

    /** nullable persistent field */
    private Double f_vertreterprovisionmax;

    /** nullable persistent field */
    private Double f_lagermindest;

    /** nullable persistent field */
    private Double f_lagersoll;

    /** nullable persistent field */
    private Double f_fertigungssatzgroesse;

    /** nullable persistent field */
    private String c_artikelbezhersteller;

    /** nullable persistent field */
    private String c_artikelnrhersteller;

    /** nullable persistent field */
    private Integer i_sofortverbrauch;

    /** nullable persistent field */
    private Integer i_wartungsintervall;

    /** nullable persistent field */
    private String einheit_c_nr_bestellung;

    /** persistent field */
    private BigDecimal n_umrechnungsfaktor;

    /** nullable persistent field */
    private Short b_bestellmengeneinheitinvers;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe flrartikelgruppe;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe flrshopgruppe;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRMaterial flrmaterial;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse flrartikelklasse;

    /** nullable persistent field */
    private FLRLiefergruppe flrliefergruppe;

    /** persistent field */
    private Set artikellagerplatzset;

    /** full constructor */
    public FLRArtikel(String c_nr, String mandant_c_nr, String einheit_c_nr, String artikelart_c_nr, Short b_lagerbewertet, Short b_lagerbewirtschaftet, Short b_seriennrtragend, Short b_versteckt, Short b_chargennrtragend, Double f_verschnittfaktor, Double f_verschnittbasis, Double f_vertreterprovisionmax, Double f_lagermindest, Double f_lagersoll, Double f_fertigungssatzgroesse, String c_artikelbezhersteller, String c_artikelnrhersteller, Integer i_sofortverbrauch, Integer i_wartungsintervall, String einheit_c_nr_bestellung, BigDecimal n_umrechnungsfaktor, Short b_bestellmengeneinheitinvers, com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe flrartikelgruppe, com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe flrshopgruppe, com.lp.server.artikel.fastlanereader.generated.FLRMaterial flrmaterial, com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse flrartikelklasse, FLRLiefergruppe flrliefergruppe, Set artikellagerplatzset) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.einheit_c_nr = einheit_c_nr;
        this.artikelart_c_nr = artikelart_c_nr;
        this.b_lagerbewertet = b_lagerbewertet;
        this.b_lagerbewirtschaftet = b_lagerbewirtschaftet;
        this.b_seriennrtragend = b_seriennrtragend;
        this.b_versteckt = b_versteckt;
        this.b_chargennrtragend = b_chargennrtragend;
        this.f_verschnittfaktor = f_verschnittfaktor;
        this.f_verschnittbasis = f_verschnittbasis;
        this.f_vertreterprovisionmax = f_vertreterprovisionmax;
        this.f_lagermindest = f_lagermindest;
        this.f_lagersoll = f_lagersoll;
        this.f_fertigungssatzgroesse = f_fertigungssatzgroesse;
        this.c_artikelbezhersteller = c_artikelbezhersteller;
        this.c_artikelnrhersteller = c_artikelnrhersteller;
        this.i_sofortverbrauch = i_sofortverbrauch;
        this.i_wartungsintervall = i_wartungsintervall;
        this.einheit_c_nr_bestellung = einheit_c_nr_bestellung;
        this.n_umrechnungsfaktor = n_umrechnungsfaktor;
        this.b_bestellmengeneinheitinvers = b_bestellmengeneinheitinvers;
        this.flrartikelgruppe = flrartikelgruppe;
        this.flrshopgruppe = flrshopgruppe;
        this.flrmaterial = flrmaterial;
        this.flrartikelklasse = flrartikelklasse;
        this.flrliefergruppe = flrliefergruppe;
        this.artikellagerplatzset = artikellagerplatzset;
    }

    /** default constructor */
    public FLRArtikel() {
    }

    /** minimal constructor */
    public FLRArtikel(String mandant_c_nr, BigDecimal n_umrechnungsfaktor, Set artikellagerplatzset) {
        this.mandant_c_nr = mandant_c_nr;
        this.n_umrechnungsfaktor = n_umrechnungsfaktor;
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

    public String getArtikelart_c_nr() {
        return this.artikelart_c_nr;
    }

    public void setArtikelart_c_nr(String artikelart_c_nr) {
        this.artikelart_c_nr = artikelart_c_nr;
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

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public Short getB_chargennrtragend() {
        return this.b_chargennrtragend;
    }

    public void setB_chargennrtragend(Short b_chargennrtragend) {
        this.b_chargennrtragend = b_chargennrtragend;
    }

    public Double getF_verschnittfaktor() {
        return this.f_verschnittfaktor;
    }

    public void setF_verschnittfaktor(Double f_verschnittfaktor) {
        this.f_verschnittfaktor = f_verschnittfaktor;
    }

    public Double getF_verschnittbasis() {
        return this.f_verschnittbasis;
    }

    public void setF_verschnittbasis(Double f_verschnittbasis) {
        this.f_verschnittbasis = f_verschnittbasis;
    }

    public Double getF_vertreterprovisionmax() {
        return this.f_vertreterprovisionmax;
    }

    public void setF_vertreterprovisionmax(Double f_vertreterprovisionmax) {
        this.f_vertreterprovisionmax = f_vertreterprovisionmax;
    }

    public Double getF_lagermindest() {
        return this.f_lagermindest;
    }

    public void setF_lagermindest(Double f_lagermindest) {
        this.f_lagermindest = f_lagermindest;
    }

    public Double getF_lagersoll() {
        return this.f_lagersoll;
    }

    public void setF_lagersoll(Double f_lagersoll) {
        this.f_lagersoll = f_lagersoll;
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

    public Integer getI_sofortverbrauch() {
        return this.i_sofortverbrauch;
    }

    public void setI_sofortverbrauch(Integer i_sofortverbrauch) {
        this.i_sofortverbrauch = i_sofortverbrauch;
    }

    public Integer getI_wartungsintervall() {
        return this.i_wartungsintervall;
    }

    public void setI_wartungsintervall(Integer i_wartungsintervall) {
        this.i_wartungsintervall = i_wartungsintervall;
    }

    public String getEinheit_c_nr_bestellung() {
        return this.einheit_c_nr_bestellung;
    }

    public void setEinheit_c_nr_bestellung(String einheit_c_nr_bestellung) {
        this.einheit_c_nr_bestellung = einheit_c_nr_bestellung;
    }

    public BigDecimal getN_umrechnungsfaktor() {
        return this.n_umrechnungsfaktor;
    }

    public void setN_umrechnungsfaktor(BigDecimal n_umrechnungsfaktor) {
        this.n_umrechnungsfaktor = n_umrechnungsfaktor;
    }

    public Short getB_bestellmengeneinheitinvers() {
        return this.b_bestellmengeneinheitinvers;
    }

    public void setB_bestellmengeneinheitinvers(Short b_bestellmengeneinheitinvers) {
        this.b_bestellmengeneinheitinvers = b_bestellmengeneinheitinvers;
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

    public com.lp.server.artikel.fastlanereader.generated.FLRMaterial getFlrmaterial() {
        return this.flrmaterial;
    }

    public void setFlrmaterial(com.lp.server.artikel.fastlanereader.generated.FLRMaterial flrmaterial) {
        this.flrmaterial = flrmaterial;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse getFlrartikelklasse() {
        return this.flrartikelklasse;
    }

    public void setFlrartikelklasse(com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse flrartikelklasse) {
        this.flrartikelklasse = flrartikelklasse;
    }

    public FLRLiefergruppe getFlrliefergruppe() {
        return this.flrliefergruppe;
    }

    public void setFlrliefergruppe(FLRLiefergruppe flrliefergruppe) {
        this.flrliefergruppe = flrliefergruppe;
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
