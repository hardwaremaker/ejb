/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
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
 *******************************************************************************/
package com.lp.server.anfrage.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.fastlanereader.generated.FLRLiefergruppe;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAnfrage implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String anfrageart_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private Integer lfliefergruppe_i_id;

    /** nullable persistent field */
    private String anfragestatus_c_nr;

    /** nullable persistent field */
    private String waehrung_c_nr_anfragewaehrung;

    /** nullable persistent field */
    private Double f_wechselkursmandantwaehrungzuanfragewaehrung;

    /** nullable persistent field */
    private BigDecimal n_gesamtanfragewertinanfragewaehrung;

    /** nullable persistent field */
    private Date t_versandzeitpunkt;

    /** nullable persistent field */
    private String c_versandtype;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** nullable persistent field */
    private FLRLiefergruppe flrliefergruppe;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private FLRPersonal flrpersonalanleger;

    /** nullable persistent field */
    private FLRPersonal flrpersonalaenderer;

    /** full constructor */
    public FLRAnfrage(String mandant_c_nr, String c_nr, String anfrageart_c_nr, String c_bez, Date t_belegdatum, Integer lfliefergruppe_i_id, String anfragestatus_c_nr, String waehrung_c_nr_anfragewaehrung, Double f_wechselkursmandantwaehrungzuanfragewaehrung, BigDecimal n_gesamtanfragewertinanfragewaehrung, Date t_versandzeitpunkt, String c_versandtype, Integer projekt_i_id, FLRLieferant flrlieferant, FLRLiefergruppe flrliefergruppe, FLRKostenstelle flrkostenstelle, FLRProjekt flrprojekt, FLRPersonal flrpersonalanleger, FLRPersonal flrpersonalaenderer) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.anfrageart_c_nr = anfrageart_c_nr;
        this.c_bez = c_bez;
        this.t_belegdatum = t_belegdatum;
        this.lfliefergruppe_i_id = lfliefergruppe_i_id;
        this.anfragestatus_c_nr = anfragestatus_c_nr;
        this.waehrung_c_nr_anfragewaehrung = waehrung_c_nr_anfragewaehrung;
        this.f_wechselkursmandantwaehrungzuanfragewaehrung = f_wechselkursmandantwaehrungzuanfragewaehrung;
        this.n_gesamtanfragewertinanfragewaehrung = n_gesamtanfragewertinanfragewaehrung;
        this.t_versandzeitpunkt = t_versandzeitpunkt;
        this.c_versandtype = c_versandtype;
        this.projekt_i_id = projekt_i_id;
        this.flrlieferant = flrlieferant;
        this.flrliefergruppe = flrliefergruppe;
        this.flrkostenstelle = flrkostenstelle;
        this.flrprojekt = flrprojekt;
        this.flrpersonalanleger = flrpersonalanleger;
        this.flrpersonalaenderer = flrpersonalaenderer;
    }

    /** default constructor */
    public FLRAnfrage() {
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

    public String getAnfrageart_c_nr() {
        return this.anfrageart_c_nr;
    }

    public void setAnfrageart_c_nr(String anfrageart_c_nr) {
        this.anfrageart_c_nr = anfrageart_c_nr;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public Integer getLfliefergruppe_i_id() {
        return this.lfliefergruppe_i_id;
    }

    public void setLfliefergruppe_i_id(Integer lfliefergruppe_i_id) {
        this.lfliefergruppe_i_id = lfliefergruppe_i_id;
    }

    public String getAnfragestatus_c_nr() {
        return this.anfragestatus_c_nr;
    }

    public void setAnfragestatus_c_nr(String anfragestatus_c_nr) {
        this.anfragestatus_c_nr = anfragestatus_c_nr;
    }

    public String getWaehrung_c_nr_anfragewaehrung() {
        return this.waehrung_c_nr_anfragewaehrung;
    }

    public void setWaehrung_c_nr_anfragewaehrung(String waehrung_c_nr_anfragewaehrung) {
        this.waehrung_c_nr_anfragewaehrung = waehrung_c_nr_anfragewaehrung;
    }

    public Double getF_wechselkursmandantwaehrungzuanfragewaehrung() {
        return this.f_wechselkursmandantwaehrungzuanfragewaehrung;
    }

    public void setF_wechselkursmandantwaehrungzuanfragewaehrung(Double f_wechselkursmandantwaehrungzuanfragewaehrung) {
        this.f_wechselkursmandantwaehrungzuanfragewaehrung = f_wechselkursmandantwaehrungzuanfragewaehrung;
    }

    public BigDecimal getN_gesamtanfragewertinanfragewaehrung() {
        return this.n_gesamtanfragewertinanfragewaehrung;
    }

    public void setN_gesamtanfragewertinanfragewaehrung(BigDecimal n_gesamtanfragewertinanfragewaehrung) {
        this.n_gesamtanfragewertinanfragewaehrung = n_gesamtanfragewertinanfragewaehrung;
    }

    public Date getT_versandzeitpunkt() {
        return this.t_versandzeitpunkt;
    }

    public void setT_versandzeitpunkt(Date t_versandzeitpunkt) {
        this.t_versandzeitpunkt = t_versandzeitpunkt;
    }

    public String getC_versandtype() {
        return this.c_versandtype;
    }

    public void setC_versandtype(String c_versandtype) {
        this.c_versandtype = c_versandtype;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public FLRLiefergruppe getFlrliefergruppe() {
        return this.flrliefergruppe;
    }

    public void setFlrliefergruppe(FLRLiefergruppe flrliefergruppe) {
        this.flrliefergruppe = flrliefergruppe;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
