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
package com.lp.server.rechnung.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRRechnung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private Integer i_geschaeftsjahr;

    /** nullable persistent field */
    private Integer ansprechpartner_i_id;

    /** nullable persistent field */
    private Date d_belegdatum;

    /** nullable persistent field */
    private String waehrung_c_nr;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private BigDecimal n_wertfw;

    /** nullable persistent field */
    private BigDecimal n_wert;

    /** nullable persistent field */
    private BigDecimal n_wertust;

    /** nullable persistent field */
    private BigDecimal n_wertustfw;

    /** nullable persistent field */
    private BigDecimal n_kurs;

    /** nullable persistent field */
    private Integer auftrag_i_id;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Integer lieferschein_i_id;

    /** nullable persistent field */
    private Integer rechnung_i_id_zurechnung;

    /** nullable persistent field */
    private Integer kunde_i_id_statistikadresse;

    /** nullable persistent field */
    private Date t_mahnsperrebis;

    /** nullable persistent field */
    private Date t_fibuuebernahme;

    /** nullable persistent field */
    private Date t_bezahltdatum;

    /** nullable persistent field */
    private Date t_versandzeitpunkt;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private String c_versandtype;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private com.lp.server.rechnung.fastlanereader.generated.FLRRechnungart flrrechnungart;

    /** nullable persistent field */
    private FLRPersonal flrvertreter;

    /** nullable persistent field */
    private FLRPersonal flrpersonalanleger;

    /** nullable persistent field */
    private FLRPersonal flrpersonalaenderer;

    /** nullable persistent field */
    private FLRKunde flrstatistikadresse;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** full constructor */
    public FLRRechnung(String mandant_c_nr, String c_nr, Integer i_geschaeftsjahr, Integer ansprechpartner_i_id, Date d_belegdatum, String waehrung_c_nr, String status_c_nr, BigDecimal n_wertfw, BigDecimal n_wert, BigDecimal n_wertust, BigDecimal n_wertustfw, BigDecimal n_kurs, Integer auftrag_i_id, Integer projekt_i_id, Integer lieferschein_i_id, Integer rechnung_i_id_zurechnung, Integer kunde_i_id_statistikadresse, Date t_mahnsperrebis, Date t_fibuuebernahme, Date t_bezahltdatum, Date t_versandzeitpunkt, Date t_aendern, String c_versandtype, FLRKunde flrkunde, com.lp.server.rechnung.fastlanereader.generated.FLRRechnungart flrrechnungart, FLRPersonal flrvertreter, FLRPersonal flrpersonalanleger, FLRPersonal flrpersonalaenderer, FLRKunde flrstatistikadresse, FLRKostenstelle flrkostenstelle) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.i_geschaeftsjahr = i_geschaeftsjahr;
        this.ansprechpartner_i_id = ansprechpartner_i_id;
        this.d_belegdatum = d_belegdatum;
        this.waehrung_c_nr = waehrung_c_nr;
        this.status_c_nr = status_c_nr;
        this.n_wertfw = n_wertfw;
        this.n_wert = n_wert;
        this.n_wertust = n_wertust;
        this.n_wertustfw = n_wertustfw;
        this.n_kurs = n_kurs;
        this.auftrag_i_id = auftrag_i_id;
        this.projekt_i_id = projekt_i_id;
        this.lieferschein_i_id = lieferschein_i_id;
        this.rechnung_i_id_zurechnung = rechnung_i_id_zurechnung;
        this.kunde_i_id_statistikadresse = kunde_i_id_statistikadresse;
        this.t_mahnsperrebis = t_mahnsperrebis;
        this.t_fibuuebernahme = t_fibuuebernahme;
        this.t_bezahltdatum = t_bezahltdatum;
        this.t_versandzeitpunkt = t_versandzeitpunkt;
        this.t_aendern = t_aendern;
        this.c_versandtype = c_versandtype;
        this.flrkunde = flrkunde;
        this.flrrechnungart = flrrechnungart;
        this.flrvertreter = flrvertreter;
        this.flrpersonalanleger = flrpersonalanleger;
        this.flrpersonalaenderer = flrpersonalaenderer;
        this.flrstatistikadresse = flrstatistikadresse;
        this.flrkostenstelle = flrkostenstelle;
    }

    /** default constructor */
    public FLRRechnung() {
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

    public Integer getI_geschaeftsjahr() {
        return this.i_geschaeftsjahr;
    }

    public void setI_geschaeftsjahr(Integer i_geschaeftsjahr) {
        this.i_geschaeftsjahr = i_geschaeftsjahr;
    }

    public Integer getAnsprechpartner_i_id() {
        return this.ansprechpartner_i_id;
    }

    public void setAnsprechpartner_i_id(Integer ansprechpartner_i_id) {
        this.ansprechpartner_i_id = ansprechpartner_i_id;
    }

    public Date getD_belegdatum() {
        return this.d_belegdatum;
    }

    public void setD_belegdatum(Date d_belegdatum) {
        this.d_belegdatum = d_belegdatum;
    }

    public String getWaehrung_c_nr() {
        return this.waehrung_c_nr;
    }

    public void setWaehrung_c_nr(String waehrung_c_nr) {
        this.waehrung_c_nr = waehrung_c_nr;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public BigDecimal getN_wertfw() {
        return this.n_wertfw;
    }

    public void setN_wertfw(BigDecimal n_wertfw) {
        this.n_wertfw = n_wertfw;
    }

    public BigDecimal getN_wert() {
        return this.n_wert;
    }

    public void setN_wert(BigDecimal n_wert) {
        this.n_wert = n_wert;
    }

    public BigDecimal getN_wertust() {
        return this.n_wertust;
    }

    public void setN_wertust(BigDecimal n_wertust) {
        this.n_wertust = n_wertust;
    }

    public BigDecimal getN_wertustfw() {
        return this.n_wertustfw;
    }

    public void setN_wertustfw(BigDecimal n_wertustfw) {
        this.n_wertustfw = n_wertustfw;
    }

    public BigDecimal getN_kurs() {
        return this.n_kurs;
    }

    public void setN_kurs(BigDecimal n_kurs) {
        this.n_kurs = n_kurs;
    }

    public Integer getAuftrag_i_id() {
        return this.auftrag_i_id;
    }

    public void setAuftrag_i_id(Integer auftrag_i_id) {
        this.auftrag_i_id = auftrag_i_id;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public Integer getLieferschein_i_id() {
        return this.lieferschein_i_id;
    }

    public void setLieferschein_i_id(Integer lieferschein_i_id) {
        this.lieferschein_i_id = lieferschein_i_id;
    }

    public Integer getRechnung_i_id_zurechnung() {
        return this.rechnung_i_id_zurechnung;
    }

    public void setRechnung_i_id_zurechnung(Integer rechnung_i_id_zurechnung) {
        this.rechnung_i_id_zurechnung = rechnung_i_id_zurechnung;
    }

    public Integer getKunde_i_id_statistikadresse() {
        return this.kunde_i_id_statistikadresse;
    }

    public void setKunde_i_id_statistikadresse(Integer kunde_i_id_statistikadresse) {
        this.kunde_i_id_statistikadresse = kunde_i_id_statistikadresse;
    }

    public Date getT_mahnsperrebis() {
        return this.t_mahnsperrebis;
    }

    public void setT_mahnsperrebis(Date t_mahnsperrebis) {
        this.t_mahnsperrebis = t_mahnsperrebis;
    }

    public Date getT_fibuuebernahme() {
        return this.t_fibuuebernahme;
    }

    public void setT_fibuuebernahme(Date t_fibuuebernahme) {
        this.t_fibuuebernahme = t_fibuuebernahme;
    }

    public Date getT_bezahltdatum() {
        return this.t_bezahltdatum;
    }

    public void setT_bezahltdatum(Date t_bezahltdatum) {
        this.t_bezahltdatum = t_bezahltdatum;
    }

    public Date getT_versandzeitpunkt() {
        return this.t_versandzeitpunkt;
    }

    public void setT_versandzeitpunkt(Date t_versandzeitpunkt) {
        this.t_versandzeitpunkt = t_versandzeitpunkt;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public String getC_versandtype() {
        return this.c_versandtype;
    }

    public void setC_versandtype(String c_versandtype) {
        this.c_versandtype = c_versandtype;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public com.lp.server.rechnung.fastlanereader.generated.FLRRechnungart getFlrrechnungart() {
        return this.flrrechnungart;
    }

    public void setFlrrechnungart(com.lp.server.rechnung.fastlanereader.generated.FLRRechnungart flrrechnungart) {
        this.flrrechnungart = flrrechnungart;
    }

    public FLRPersonal getFlrvertreter() {
        return this.flrvertreter;
    }

    public void setFlrvertreter(FLRPersonal flrvertreter) {
        this.flrvertreter = flrvertreter;
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

    public FLRKunde getFlrstatistikadresse() {
        return this.flrstatistikadresse;
    }

    public void setFlrstatistikadresse(FLRKunde flrstatistikadresse) {
        this.flrstatistikadresse = flrstatistikadresse;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
