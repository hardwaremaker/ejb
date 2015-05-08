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
 ******************************************************************************/
package com.lp.server.eingangsrechnung.fastlanereader.generated;

import com.lp.server.bestellung.fastlanereader.generated.FLRBestellung;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLREingangsrechnung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String eingangsrechnungart_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Integer i_geschaeftsjahr;

    /** nullable persistent field */
    private Integer eingangsrechnung_i_id_nachfolger;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private Date t_freigabedatum;

    /** nullable persistent field */
    private BigDecimal n_betragfw;

    /** nullable persistent field */
    private BigDecimal n_betrag;

    /** nullable persistent field */
    private BigDecimal n_ustbetragfw;

    /** nullable persistent field */
    private BigDecimal n_ustbetrag;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private String waehrung_c_nr;

    /** nullable persistent field */
    private String auftragwiederholungsintervall_c_nr;

    /** nullable persistent field */
    private String c_text;

    /** nullable persistent field */
    private String c_lieferantenrechnungsnummer;

    /** nullable persistent field */
    private Date t_fibuuebernahme;

    /** nullable persistent field */
    private Date t_gedruckt;

    /** nullable persistent field */
    private Date t_wiederholenderledigt;

    /** nullable persistent field */
    private Date t_zollimportpapier;

    /** nullable persistent field */
    private Integer personal_i_id_wiederholenderledigt;

    /** nullable persistent field */
    private Short b_igerwerb;

    /** nullable persistent field */
    private Short b_reversecharge;

    /** nullable persistent field */
    private FLRBestellung flrbestellung;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** nullable persistent field */
    private com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung_nachfolger;

    /** nullable persistent field */
    private com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungtextsuche flreingangsrechnungtextsuche;

    /** full constructor */
    public FLREingangsrechnung(String eingangsrechnungart_c_nr, String c_nr, String mandant_c_nr, Integer i_geschaeftsjahr, Integer eingangsrechnung_i_id_nachfolger, Date t_belegdatum, Date t_freigabedatum, BigDecimal n_betragfw, BigDecimal n_betrag, BigDecimal n_ustbetragfw, BigDecimal n_ustbetrag, String status_c_nr, String waehrung_c_nr, String auftragwiederholungsintervall_c_nr, String c_text, String c_lieferantenrechnungsnummer, Date t_fibuuebernahme, Date t_gedruckt, Date t_wiederholenderledigt, Date t_zollimportpapier, Integer personal_i_id_wiederholenderledigt, Short b_igerwerb, Short b_reversecharge, FLRBestellung flrbestellung, FLRLieferant flrlieferant, com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung_nachfolger, com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungtextsuche flreingangsrechnungtextsuche) {
        this.eingangsrechnungart_c_nr = eingangsrechnungart_c_nr;
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.i_geschaeftsjahr = i_geschaeftsjahr;
        this.eingangsrechnung_i_id_nachfolger = eingangsrechnung_i_id_nachfolger;
        this.t_belegdatum = t_belegdatum;
        this.t_freigabedatum = t_freigabedatum;
        this.n_betragfw = n_betragfw;
        this.n_betrag = n_betrag;
        this.n_ustbetragfw = n_ustbetragfw;
        this.n_ustbetrag = n_ustbetrag;
        this.status_c_nr = status_c_nr;
        this.waehrung_c_nr = waehrung_c_nr;
        this.auftragwiederholungsintervall_c_nr = auftragwiederholungsintervall_c_nr;
        this.c_text = c_text;
        this.c_lieferantenrechnungsnummer = c_lieferantenrechnungsnummer;
        this.t_fibuuebernahme = t_fibuuebernahme;
        this.t_gedruckt = t_gedruckt;
        this.t_wiederholenderledigt = t_wiederholenderledigt;
        this.t_zollimportpapier = t_zollimportpapier;
        this.personal_i_id_wiederholenderledigt = personal_i_id_wiederholenderledigt;
        this.b_igerwerb = b_igerwerb;
        this.b_reversecharge = b_reversecharge;
        this.flrbestellung = flrbestellung;
        this.flrlieferant = flrlieferant;
        this.flreingangsrechnung_nachfolger = flreingangsrechnung_nachfolger;
        this.flreingangsrechnungtextsuche = flreingangsrechnungtextsuche;
    }

    /** default constructor */
    public FLREingangsrechnung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getEingangsrechnungart_c_nr() {
        return this.eingangsrechnungart_c_nr;
    }

    public void setEingangsrechnungart_c_nr(String eingangsrechnungart_c_nr) {
        this.eingangsrechnungart_c_nr = eingangsrechnungart_c_nr;
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

    public Integer getI_geschaeftsjahr() {
        return this.i_geschaeftsjahr;
    }

    public void setI_geschaeftsjahr(Integer i_geschaeftsjahr) {
        this.i_geschaeftsjahr = i_geschaeftsjahr;
    }

    public Integer getEingangsrechnung_i_id_nachfolger() {
        return this.eingangsrechnung_i_id_nachfolger;
    }

    public void setEingangsrechnung_i_id_nachfolger(Integer eingangsrechnung_i_id_nachfolger) {
        this.eingangsrechnung_i_id_nachfolger = eingangsrechnung_i_id_nachfolger;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public Date getT_freigabedatum() {
        return this.t_freigabedatum;
    }

    public void setT_freigabedatum(Date t_freigabedatum) {
        this.t_freigabedatum = t_freigabedatum;
    }

    public BigDecimal getN_betragfw() {
        return this.n_betragfw;
    }

    public void setN_betragfw(BigDecimal n_betragfw) {
        this.n_betragfw = n_betragfw;
    }

    public BigDecimal getN_betrag() {
        return this.n_betrag;
    }

    public void setN_betrag(BigDecimal n_betrag) {
        this.n_betrag = n_betrag;
    }

    public BigDecimal getN_ustbetragfw() {
        return this.n_ustbetragfw;
    }

    public void setN_ustbetragfw(BigDecimal n_ustbetragfw) {
        this.n_ustbetragfw = n_ustbetragfw;
    }

    public BigDecimal getN_ustbetrag() {
        return this.n_ustbetrag;
    }

    public void setN_ustbetrag(BigDecimal n_ustbetrag) {
        this.n_ustbetrag = n_ustbetrag;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public String getWaehrung_c_nr() {
        return this.waehrung_c_nr;
    }

    public void setWaehrung_c_nr(String waehrung_c_nr) {
        this.waehrung_c_nr = waehrung_c_nr;
    }

    public String getAuftragwiederholungsintervall_c_nr() {
        return this.auftragwiederholungsintervall_c_nr;
    }

    public void setAuftragwiederholungsintervall_c_nr(String auftragwiederholungsintervall_c_nr) {
        this.auftragwiederholungsintervall_c_nr = auftragwiederholungsintervall_c_nr;
    }

    public String getC_text() {
        return this.c_text;
    }

    public void setC_text(String c_text) {
        this.c_text = c_text;
    }

    public String getC_lieferantenrechnungsnummer() {
        return this.c_lieferantenrechnungsnummer;
    }

    public void setC_lieferantenrechnungsnummer(String c_lieferantenrechnungsnummer) {
        this.c_lieferantenrechnungsnummer = c_lieferantenrechnungsnummer;
    }

    public Date getT_fibuuebernahme() {
        return this.t_fibuuebernahme;
    }

    public void setT_fibuuebernahme(Date t_fibuuebernahme) {
        this.t_fibuuebernahme = t_fibuuebernahme;
    }

    public Date getT_gedruckt() {
        return this.t_gedruckt;
    }

    public void setT_gedruckt(Date t_gedruckt) {
        this.t_gedruckt = t_gedruckt;
    }

    public Date getT_wiederholenderledigt() {
        return this.t_wiederholenderledigt;
    }

    public void setT_wiederholenderledigt(Date t_wiederholenderledigt) {
        this.t_wiederholenderledigt = t_wiederholenderledigt;
    }

    public Date getT_zollimportpapier() {
        return this.t_zollimportpapier;
    }

    public void setT_zollimportpapier(Date t_zollimportpapier) {
        this.t_zollimportpapier = t_zollimportpapier;
    }

    public Integer getPersonal_i_id_wiederholenderledigt() {
        return this.personal_i_id_wiederholenderledigt;
    }

    public void setPersonal_i_id_wiederholenderledigt(Integer personal_i_id_wiederholenderledigt) {
        this.personal_i_id_wiederholenderledigt = personal_i_id_wiederholenderledigt;
    }

    public Short getB_igerwerb() {
        return this.b_igerwerb;
    }

    public void setB_igerwerb(Short b_igerwerb) {
        this.b_igerwerb = b_igerwerb;
    }

    public Short getB_reversecharge() {
        return this.b_reversecharge;
    }

    public void setB_reversecharge(Short b_reversecharge) {
        this.b_reversecharge = b_reversecharge;
    }

    public FLRBestellung getFlrbestellung() {
        return this.flrbestellung;
    }

    public void setFlrbestellung(FLRBestellung flrbestellung) {
        this.flrbestellung = flrbestellung;
    }

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung getFlreingangsrechnung_nachfolger() {
        return this.flreingangsrechnung_nachfolger;
    }

    public void setFlreingangsrechnung_nachfolger(com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung_nachfolger) {
        this.flreingangsrechnung_nachfolger = flreingangsrechnung_nachfolger;
    }

    public com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungtextsuche getFlreingangsrechnungtextsuche() {
        return this.flreingangsrechnungtextsuche;
    }

    public void setFlreingangsrechnungtextsuche(com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungtextsuche flreingangsrechnungtextsuche) {
        this.flreingangsrechnungtextsuche = flreingangsrechnungtextsuche;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
