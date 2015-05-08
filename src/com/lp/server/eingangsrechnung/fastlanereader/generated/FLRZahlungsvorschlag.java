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
package com.lp.server.eingangsrechnung.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZahlungsvorschlag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer zahlungsvorschlaglauf_i_id;

    /** nullable persistent field */
    private Integer eingangsrechnung_i_id;

    /** nullable persistent field */
    private Short b_bezahlen;

    /** nullable persistent field */
    private Date t_faellig;

    /** nullable persistent field */
    private BigDecimal n_angewandterskontosatz;

    /** nullable persistent field */
    private BigDecimal n_er_brutto_betrag;

    /** nullable persistent field */
    private BigDecimal n_bereits_bezahlt;

    /** nullable persistent field */
    private BigDecimal n_zahlbetrag;

    /** nullable persistent field */
    private Short b_waere_vollstaendig_bezahlt;

    /** nullable persistent field */
    private com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungReport flreingangsrechnungreport;

    /** nullable persistent field */
    private com.lp.server.eingangsrechnung.fastlanereader.generated.FLRZahlungsvorschlaglauf flrzahlungsvorschlaglauf;

    /** full constructor */
    public FLRZahlungsvorschlag(Integer zahlungsvorschlaglauf_i_id, Integer eingangsrechnung_i_id, Short b_bezahlen, Date t_faellig, BigDecimal n_angewandterskontosatz, BigDecimal n_er_brutto_betrag, BigDecimal n_bereits_bezahlt, BigDecimal n_zahlbetrag, Short b_waere_vollstaendig_bezahlt, com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungReport flreingangsrechnungreport, com.lp.server.eingangsrechnung.fastlanereader.generated.FLRZahlungsvorschlaglauf flrzahlungsvorschlaglauf) {
        this.zahlungsvorschlaglauf_i_id = zahlungsvorschlaglauf_i_id;
        this.eingangsrechnung_i_id = eingangsrechnung_i_id;
        this.b_bezahlen = b_bezahlen;
        this.t_faellig = t_faellig;
        this.n_angewandterskontosatz = n_angewandterskontosatz;
        this.n_er_brutto_betrag = n_er_brutto_betrag;
        this.n_bereits_bezahlt = n_bereits_bezahlt;
        this.n_zahlbetrag = n_zahlbetrag;
        this.b_waere_vollstaendig_bezahlt = b_waere_vollstaendig_bezahlt;
        this.flreingangsrechnungreport = flreingangsrechnungreport;
        this.flrzahlungsvorschlaglauf = flrzahlungsvorschlaglauf;
    }

    /** default constructor */
    public FLRZahlungsvorschlag() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getZahlungsvorschlaglauf_i_id() {
        return this.zahlungsvorschlaglauf_i_id;
    }

    public void setZahlungsvorschlaglauf_i_id(Integer zahlungsvorschlaglauf_i_id) {
        this.zahlungsvorschlaglauf_i_id = zahlungsvorschlaglauf_i_id;
    }

    public Integer getEingangsrechnung_i_id() {
        return this.eingangsrechnung_i_id;
    }

    public void setEingangsrechnung_i_id(Integer eingangsrechnung_i_id) {
        this.eingangsrechnung_i_id = eingangsrechnung_i_id;
    }

    public Short getB_bezahlen() {
        return this.b_bezahlen;
    }

    public void setB_bezahlen(Short b_bezahlen) {
        this.b_bezahlen = b_bezahlen;
    }

    public Date getT_faellig() {
        return this.t_faellig;
    }

    public void setT_faellig(Date t_faellig) {
        this.t_faellig = t_faellig;
    }

    public BigDecimal getN_angewandterskontosatz() {
        return this.n_angewandterskontosatz;
    }

    public void setN_angewandterskontosatz(BigDecimal n_angewandterskontosatz) {
        this.n_angewandterskontosatz = n_angewandterskontosatz;
    }

    public BigDecimal getN_er_brutto_betrag() {
        return this.n_er_brutto_betrag;
    }

    public void setN_er_brutto_betrag(BigDecimal n_er_brutto_betrag) {
        this.n_er_brutto_betrag = n_er_brutto_betrag;
    }

    public BigDecimal getN_bereits_bezahlt() {
        return this.n_bereits_bezahlt;
    }

    public void setN_bereits_bezahlt(BigDecimal n_bereits_bezahlt) {
        this.n_bereits_bezahlt = n_bereits_bezahlt;
    }

    public BigDecimal getN_zahlbetrag() {
        return this.n_zahlbetrag;
    }

    public void setN_zahlbetrag(BigDecimal n_zahlbetrag) {
        this.n_zahlbetrag = n_zahlbetrag;
    }

    public Short getB_waere_vollstaendig_bezahlt() {
        return this.b_waere_vollstaendig_bezahlt;
    }

    public void setB_waere_vollstaendig_bezahlt(Short b_waere_vollstaendig_bezahlt) {
        this.b_waere_vollstaendig_bezahlt = b_waere_vollstaendig_bezahlt;
    }

    public com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungReport getFlreingangsrechnungreport() {
        return this.flreingangsrechnungreport;
    }

    public void setFlreingangsrechnungreport(com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungReport flreingangsrechnungreport) {
        this.flreingangsrechnungreport = flreingangsrechnungreport;
    }

    public com.lp.server.eingangsrechnung.fastlanereader.generated.FLRZahlungsvorschlaglauf getFlrzahlungsvorschlaglauf() {
        return this.flrzahlungsvorschlaglauf;
    }

    public void setFlrzahlungsvorschlaglauf(com.lp.server.eingangsrechnung.fastlanereader.generated.FLRZahlungsvorschlaglauf flrzahlungsvorschlaglauf) {
        this.flrzahlungsvorschlaglauf = flrzahlungsvorschlaglauf;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
