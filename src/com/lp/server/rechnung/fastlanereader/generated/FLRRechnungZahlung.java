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

import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBankkonto;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchungDetail;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKassenbuch;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRRechnungZahlung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer rechnung_i_id;

    /** nullable persistent field */
    private Integer bankverbindung_i_id;

    /** nullable persistent field */
    private Date d_zahldatum;

    /** nullable persistent field */
    private String zahlungsart_c_nr;

    /** nullable persistent field */
    private BigDecimal n_kurs;

    /** nullable persistent field */
    private BigDecimal n_betrag;

    /** nullable persistent field */
    private BigDecimal n_betragfw;

    /** nullable persistent field */
    private BigDecimal n_betrag_ust;

    /** nullable persistent field */
    private BigDecimal n_betrag_ustfw;

    /** nullable persistent field */
    private Date d_wechsel_faellig_am;

    /** nullable persistent field */
    private Integer i_auszug;

    /** nullable persistent field */
    private FLRFinanzBankkonto flrbankverbindung;

    /** nullable persistent field */
    private FLRFinanzKassenbuch flrkassenbuch;

    /** nullable persistent field */
    private com.lp.server.rechnung.fastlanereader.generated.FLRRechnung flrrechnung;

    /** nullable persistent field */
    private FLREingangsrechnung flreingangsrechnung;

    /** nullable persistent field */
    private com.lp.server.rechnung.fastlanereader.generated.FLRRechnung flrrechnunggutschrift;

    /** nullable persistent field */
    private FLRFinanzBuchungDetail flrfinanzbuchungdetail;

    /** full constructor */
    public FLRRechnungZahlung(Integer rechnung_i_id, Integer bankverbindung_i_id, Date d_zahldatum, String zahlungsart_c_nr, BigDecimal n_kurs, BigDecimal n_betrag, BigDecimal n_betragfw, BigDecimal n_betrag_ust, BigDecimal n_betrag_ustfw, Date d_wechsel_faellig_am, Integer i_auszug, FLRFinanzBankkonto flrbankverbindung, FLRFinanzKassenbuch flrkassenbuch, com.lp.server.rechnung.fastlanereader.generated.FLRRechnung flrrechnung, FLREingangsrechnung flreingangsrechnung, com.lp.server.rechnung.fastlanereader.generated.FLRRechnung flrrechnunggutschrift, FLRFinanzBuchungDetail flrfinanzbuchungdetail) {
        this.rechnung_i_id = rechnung_i_id;
        this.bankverbindung_i_id = bankverbindung_i_id;
        this.d_zahldatum = d_zahldatum;
        this.zahlungsart_c_nr = zahlungsart_c_nr;
        this.n_kurs = n_kurs;
        this.n_betrag = n_betrag;
        this.n_betragfw = n_betragfw;
        this.n_betrag_ust = n_betrag_ust;
        this.n_betrag_ustfw = n_betrag_ustfw;
        this.d_wechsel_faellig_am = d_wechsel_faellig_am;
        this.i_auszug = i_auszug;
        this.flrbankverbindung = flrbankverbindung;
        this.flrkassenbuch = flrkassenbuch;
        this.flrrechnung = flrrechnung;
        this.flreingangsrechnung = flreingangsrechnung;
        this.flrrechnunggutschrift = flrrechnunggutschrift;
        this.flrfinanzbuchungdetail = flrfinanzbuchungdetail;
    }

    /** default constructor */
    public FLRRechnungZahlung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getRechnung_i_id() {
        return this.rechnung_i_id;
    }

    public void setRechnung_i_id(Integer rechnung_i_id) {
        this.rechnung_i_id = rechnung_i_id;
    }

    public Integer getBankverbindung_i_id() {
        return this.bankverbindung_i_id;
    }

    public void setBankverbindung_i_id(Integer bankverbindung_i_id) {
        this.bankverbindung_i_id = bankverbindung_i_id;
    }

    public Date getD_zahldatum() {
        return this.d_zahldatum;
    }

    public void setD_zahldatum(Date d_zahldatum) {
        this.d_zahldatum = d_zahldatum;
    }

    public String getZahlungsart_c_nr() {
        return this.zahlungsart_c_nr;
    }

    public void setZahlungsart_c_nr(String zahlungsart_c_nr) {
        this.zahlungsart_c_nr = zahlungsart_c_nr;
    }

    public BigDecimal getN_kurs() {
        return this.n_kurs;
    }

    public void setN_kurs(BigDecimal n_kurs) {
        this.n_kurs = n_kurs;
    }

    public BigDecimal getN_betrag() {
        return this.n_betrag;
    }

    public void setN_betrag(BigDecimal n_betrag) {
        this.n_betrag = n_betrag;
    }

    public BigDecimal getN_betragfw() {
        return this.n_betragfw;
    }

    public void setN_betragfw(BigDecimal n_betragfw) {
        this.n_betragfw = n_betragfw;
    }

    public BigDecimal getN_betrag_ust() {
        return this.n_betrag_ust;
    }

    public void setN_betrag_ust(BigDecimal n_betrag_ust) {
        this.n_betrag_ust = n_betrag_ust;
    }

    public BigDecimal getN_betrag_ustfw() {
        return this.n_betrag_ustfw;
    }

    public void setN_betrag_ustfw(BigDecimal n_betrag_ustfw) {
        this.n_betrag_ustfw = n_betrag_ustfw;
    }

    public Date getD_wechsel_faellig_am() {
        return this.d_wechsel_faellig_am;
    }

    public void setD_wechsel_faellig_am(Date d_wechsel_faellig_am) {
        this.d_wechsel_faellig_am = d_wechsel_faellig_am;
    }

    public Integer getI_auszug() {
        return this.i_auszug;
    }

    public void setI_auszug(Integer i_auszug) {
        this.i_auszug = i_auszug;
    }

    public FLRFinanzBankkonto getFlrbankverbindung() {
        return this.flrbankverbindung;
    }

    public void setFlrbankverbindung(FLRFinanzBankkonto flrbankverbindung) {
        this.flrbankverbindung = flrbankverbindung;
    }

    public FLRFinanzKassenbuch getFlrkassenbuch() {
        return this.flrkassenbuch;
    }

    public void setFlrkassenbuch(FLRFinanzKassenbuch flrkassenbuch) {
        this.flrkassenbuch = flrkassenbuch;
    }

    public com.lp.server.rechnung.fastlanereader.generated.FLRRechnung getFlrrechnung() {
        return this.flrrechnung;
    }

    public void setFlrrechnung(com.lp.server.rechnung.fastlanereader.generated.FLRRechnung flrrechnung) {
        this.flrrechnung = flrrechnung;
    }

    public FLREingangsrechnung getFlreingangsrechnung() {
        return this.flreingangsrechnung;
    }

    public void setFlreingangsrechnung(FLREingangsrechnung flreingangsrechnung) {
        this.flreingangsrechnung = flreingangsrechnung;
    }

    public com.lp.server.rechnung.fastlanereader.generated.FLRRechnung getFlrrechnunggutschrift() {
        return this.flrrechnunggutschrift;
    }

    public void setFlrrechnunggutschrift(com.lp.server.rechnung.fastlanereader.generated.FLRRechnung flrrechnunggutschrift) {
        this.flrrechnunggutschrift = flrrechnunggutschrift;
    }

    public FLRFinanzBuchungDetail getFlrfinanzbuchungdetail() {
        return this.flrfinanzbuchungdetail;
    }

    public void setFlrfinanzbuchungdetail(FLRFinanzBuchungDetail flrfinanzbuchungdetail) {
        this.flrfinanzbuchungdetail = flrfinanzbuchungdetail;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
