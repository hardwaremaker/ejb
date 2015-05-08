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

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBankkonto;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchungDetail;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKassenbuch;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungZahlung;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLREingangsrechnungzahlung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer eingangsrechnung_i_id;

    /** nullable persistent field */
    private Date t_zahldatum;

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
    private Integer i_auszug;

    /** nullable persistent field */
    private com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungzahlung flreingangsrechnungzahlunggutschrift;

    /** nullable persistent field */
    private FLRFinanzBankkonto flrbankverbindung;

    /** nullable persistent field */
    private FLRFinanzKassenbuch flrkassenbuch;

    /** nullable persistent field */
    private com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnunggutschrift;

    /** nullable persistent field */
    private FLRRechnungZahlung flrrechnungzahlung;

    /** nullable persistent field */
    private com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung;

    /** nullable persistent field */
    private FLRFinanzBuchungDetail flrfinanzbuchungdetail;

    /** full constructor */
    public FLREingangsrechnungzahlung(Integer eingangsrechnung_i_id, Date t_zahldatum, String zahlungsart_c_nr, BigDecimal n_kurs, BigDecimal n_betrag, BigDecimal n_betragfw, BigDecimal n_betrag_ust, BigDecimal n_betrag_ustfw, Integer i_auszug, com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungzahlung flreingangsrechnungzahlunggutschrift, FLRFinanzBankkonto flrbankverbindung, FLRFinanzKassenbuch flrkassenbuch, com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnunggutschrift, FLRRechnungZahlung flrrechnungzahlung, com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung, FLRFinanzBuchungDetail flrfinanzbuchungdetail) {
        this.eingangsrechnung_i_id = eingangsrechnung_i_id;
        this.t_zahldatum = t_zahldatum;
        this.zahlungsart_c_nr = zahlungsart_c_nr;
        this.n_kurs = n_kurs;
        this.n_betrag = n_betrag;
        this.n_betragfw = n_betragfw;
        this.n_betrag_ust = n_betrag_ust;
        this.n_betrag_ustfw = n_betrag_ustfw;
        this.i_auszug = i_auszug;
        this.flreingangsrechnungzahlunggutschrift = flreingangsrechnungzahlunggutschrift;
        this.flrbankverbindung = flrbankverbindung;
        this.flrkassenbuch = flrkassenbuch;
        this.flreingangsrechnunggutschrift = flreingangsrechnunggutschrift;
        this.flrrechnungzahlung = flrrechnungzahlung;
        this.flreingangsrechnung = flreingangsrechnung;
        this.flrfinanzbuchungdetail = flrfinanzbuchungdetail;
    }

    /** default constructor */
    public FLREingangsrechnungzahlung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getEingangsrechnung_i_id() {
        return this.eingangsrechnung_i_id;
    }

    public void setEingangsrechnung_i_id(Integer eingangsrechnung_i_id) {
        this.eingangsrechnung_i_id = eingangsrechnung_i_id;
    }

    public Date getT_zahldatum() {
        return this.t_zahldatum;
    }

    public void setT_zahldatum(Date t_zahldatum) {
        this.t_zahldatum = t_zahldatum;
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

    public Integer getI_auszug() {
        return this.i_auszug;
    }

    public void setI_auszug(Integer i_auszug) {
        this.i_auszug = i_auszug;
    }

    public com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungzahlung getFlreingangsrechnungzahlunggutschrift() {
        return this.flreingangsrechnungzahlunggutschrift;
    }

    public void setFlreingangsrechnungzahlunggutschrift(com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungzahlung flreingangsrechnungzahlunggutschrift) {
        this.flreingangsrechnungzahlunggutschrift = flreingangsrechnungzahlunggutschrift;
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

    public com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung getFlreingangsrechnunggutschrift() {
        return this.flreingangsrechnunggutschrift;
    }

    public void setFlreingangsrechnunggutschrift(com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnunggutschrift) {
        this.flreingangsrechnunggutschrift = flreingangsrechnunggutschrift;
    }

    public FLRRechnungZahlung getFlrrechnungzahlung() {
        return this.flrrechnungzahlung;
    }

    public void setFlrrechnungzahlung(FLRRechnungZahlung flrrechnungzahlung) {
        this.flrrechnungzahlung = flrrechnungzahlung;
    }

    public com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung getFlreingangsrechnung() {
        return this.flreingangsrechnung;
    }

    public void setFlreingangsrechnung(com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung) {
        this.flreingangsrechnung = flreingangsrechnung;
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
