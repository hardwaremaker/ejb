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
package com.lp.server.inserat.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRInserat implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private Integer bestellposition_i_id;

    /** nullable persistent field */
    private Integer lieferant_i_id;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_rubrik;

    /** nullable persistent field */
    private String c_rubrik2;

    /** nullable persistent field */
    private String c_stichwort;

    /** nullable persistent field */
    private String c_stichwort2;

    /** nullable persistent field */
    private String x_anhang;

    /** nullable persistent field */
    private Date t_termin;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private Date t_manuellerledigt;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal n_nettoeinzelpreis_ek;

    /** nullable persistent field */
    private BigDecimal n_nettoeinzelpreis_vk;

    /** nullable persistent field */
    private Date t_erschienen;

    /** nullable persistent field */
    private Date t_verrechnen;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** nullable persistent field */
    private FLRBestellposition flrbestellposition;

    /** nullable persistent field */
    private FLRPersonal flrvertreter;

    /** nullable persistent field */
    private FLRPersonal flrpersonalverrechnen;

    /** nullable persistent field */
    private FLRPersonal flrpersonalanleger;

    /** nullable persistent field */
    private FLRPersonal flrpersonalaenderer;

    /** nullable persistent field */
    private FLRArtikel flrartikel_inseratart;

    /** persistent field */
    private Set rechnungset;

    /** persistent field */
    private Set artikelset;

    /** persistent field */
    private Set erset;

    /** full constructor */
    public FLRInserat(String mandant_c_nr, String c_nr, Integer bestellposition_i_id, Integer lieferant_i_id, String status_c_nr, String c_bez, String c_rubrik, String c_rubrik2, String c_stichwort, String c_stichwort2, String x_anhang, Date t_termin, Date t_belegdatum, Date t_manuellerledigt, BigDecimal n_menge, BigDecimal n_nettoeinzelpreis_ek, BigDecimal n_nettoeinzelpreis_vk, Date t_erschienen, Date t_verrechnen, FLRLieferant flrlieferant, FLRBestellposition flrbestellposition, FLRPersonal flrvertreter, FLRPersonal flrpersonalverrechnen, FLRPersonal flrpersonalanleger, FLRPersonal flrpersonalaenderer, FLRArtikel flrartikel_inseratart, Set rechnungset, Set artikelset, Set erset) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.bestellposition_i_id = bestellposition_i_id;
        this.lieferant_i_id = lieferant_i_id;
        this.status_c_nr = status_c_nr;
        this.c_bez = c_bez;
        this.c_rubrik = c_rubrik;
        this.c_rubrik2 = c_rubrik2;
        this.c_stichwort = c_stichwort;
        this.c_stichwort2 = c_stichwort2;
        this.x_anhang = x_anhang;
        this.t_termin = t_termin;
        this.t_belegdatum = t_belegdatum;
        this.t_manuellerledigt = t_manuellerledigt;
        this.n_menge = n_menge;
        this.n_nettoeinzelpreis_ek = n_nettoeinzelpreis_ek;
        this.n_nettoeinzelpreis_vk = n_nettoeinzelpreis_vk;
        this.t_erschienen = t_erschienen;
        this.t_verrechnen = t_verrechnen;
        this.flrlieferant = flrlieferant;
        this.flrbestellposition = flrbestellposition;
        this.flrvertreter = flrvertreter;
        this.flrpersonalverrechnen = flrpersonalverrechnen;
        this.flrpersonalanleger = flrpersonalanleger;
        this.flrpersonalaenderer = flrpersonalaenderer;
        this.flrartikel_inseratart = flrartikel_inseratart;
        this.rechnungset = rechnungset;
        this.artikelset = artikelset;
        this.erset = erset;
    }

    /** default constructor */
    public FLRInserat() {
    }

    /** minimal constructor */
    public FLRInserat(Set rechnungset, Set artikelset, Set erset) {
        this.rechnungset = rechnungset;
        this.artikelset = artikelset;
        this.erset = erset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public Integer getBestellposition_i_id() {
        return this.bestellposition_i_id;
    }

    public void setBestellposition_i_id(Integer bestellposition_i_id) {
        this.bestellposition_i_id = bestellposition_i_id;
    }

    public Integer getLieferant_i_id() {
        return this.lieferant_i_id;
    }

    public void setLieferant_i_id(Integer lieferant_i_id) {
        this.lieferant_i_id = lieferant_i_id;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_rubrik() {
        return this.c_rubrik;
    }

    public void setC_rubrik(String c_rubrik) {
        this.c_rubrik = c_rubrik;
    }

    public String getC_rubrik2() {
        return this.c_rubrik2;
    }

    public void setC_rubrik2(String c_rubrik2) {
        this.c_rubrik2 = c_rubrik2;
    }

    public String getC_stichwort() {
        return this.c_stichwort;
    }

    public void setC_stichwort(String c_stichwort) {
        this.c_stichwort = c_stichwort;
    }

    public String getC_stichwort2() {
        return this.c_stichwort2;
    }

    public void setC_stichwort2(String c_stichwort2) {
        this.c_stichwort2 = c_stichwort2;
    }

    public String getX_anhang() {
        return this.x_anhang;
    }

    public void setX_anhang(String x_anhang) {
        this.x_anhang = x_anhang;
    }

    public Date getT_termin() {
        return this.t_termin;
    }

    public void setT_termin(Date t_termin) {
        this.t_termin = t_termin;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public Date getT_manuellerledigt() {
        return this.t_manuellerledigt;
    }

    public void setT_manuellerledigt(Date t_manuellerledigt) {
        this.t_manuellerledigt = t_manuellerledigt;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public BigDecimal getN_nettoeinzelpreis_ek() {
        return this.n_nettoeinzelpreis_ek;
    }

    public void setN_nettoeinzelpreis_ek(BigDecimal n_nettoeinzelpreis_ek) {
        this.n_nettoeinzelpreis_ek = n_nettoeinzelpreis_ek;
    }

    public BigDecimal getN_nettoeinzelpreis_vk() {
        return this.n_nettoeinzelpreis_vk;
    }

    public void setN_nettoeinzelpreis_vk(BigDecimal n_nettoeinzelpreis_vk) {
        this.n_nettoeinzelpreis_vk = n_nettoeinzelpreis_vk;
    }

    public Date getT_erschienen() {
        return this.t_erschienen;
    }

    public void setT_erschienen(Date t_erschienen) {
        this.t_erschienen = t_erschienen;
    }

    public Date getT_verrechnen() {
        return this.t_verrechnen;
    }

    public void setT_verrechnen(Date t_verrechnen) {
        this.t_verrechnen = t_verrechnen;
    }

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public FLRBestellposition getFlrbestellposition() {
        return this.flrbestellposition;
    }

    public void setFlrbestellposition(FLRBestellposition flrbestellposition) {
        this.flrbestellposition = flrbestellposition;
    }

    public FLRPersonal getFlrvertreter() {
        return this.flrvertreter;
    }

    public void setFlrvertreter(FLRPersonal flrvertreter) {
        this.flrvertreter = flrvertreter;
    }

    public FLRPersonal getFlrpersonalverrechnen() {
        return this.flrpersonalverrechnen;
    }

    public void setFlrpersonalverrechnen(FLRPersonal flrpersonalverrechnen) {
        this.flrpersonalverrechnen = flrpersonalverrechnen;
    }

    public FLRPersonal getFlrpersonalanleger() {
        return this.flrpersonalanleger;
    }

    public void setFlrpersonalanleger(FLRPersonal flrpersonalanleger) {
        this.flrpersonalanleger = flrpersonalanleger;
    }

    public FLRPersonal getFlrpersonalaenderer() {
        return this.flrpersonalaenderer;
    }

    public void setFlrpersonalaenderer(FLRPersonal flrpersonalaenderer) {
        this.flrpersonalaenderer = flrpersonalaenderer;
    }

    public FLRArtikel getFlrartikel_inseratart() {
        return this.flrartikel_inseratart;
    }

    public void setFlrartikel_inseratart(FLRArtikel flrartikel_inseratart) {
        this.flrartikel_inseratart = flrartikel_inseratart;
    }

    public Set getRechnungset() {
        return this.rechnungset;
    }

    public void setRechnungset(Set rechnungset) {
        this.rechnungset = rechnungset;
    }

    public Set getArtikelset() {
        return this.artikelset;
    }

    public void setArtikelset(Set artikelset) {
        this.artikelset = artikelset;
    }

    public Set getErset() {
        return this.erset;
    }

    public void setErset(Set erset) {
        this.erset = erset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
