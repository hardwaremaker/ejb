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

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBankkonto;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZahlungsvorschlaglauf implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Date t_anlegen;

    /** nullable persistent field */
    private Date t_zahlungsstichtag;

    /** nullable persistent field */
    private Date t_naechsterzahlungslauf;

    /** nullable persistent field */
    private Short b_mitskonto;

    /** nullable persistent field */
    private Integer i_skontoueberziehungsfristintagen;

    /** nullable persistent field */
    private Integer bankverbindung_i_id;

    /** nullable persistent field */
    private Date t_gespeichert;

    /** nullable persistent field */
    private FLRFinanzBankkonto flrbankverbindung;

    /** nullable persistent field */
    private FLRPersonal flrpersonalgespeichert;

    /** full constructor */
    public FLRZahlungsvorschlaglauf(String mandant_c_nr, Date t_anlegen, Date t_zahlungsstichtag, Date t_naechsterzahlungslauf, Short b_mitskonto, Integer i_skontoueberziehungsfristintagen, Integer bankverbindung_i_id, Date t_gespeichert, FLRFinanzBankkonto flrbankverbindung, FLRPersonal flrpersonalgespeichert) {
        this.mandant_c_nr = mandant_c_nr;
        this.t_anlegen = t_anlegen;
        this.t_zahlungsstichtag = t_zahlungsstichtag;
        this.t_naechsterzahlungslauf = t_naechsterzahlungslauf;
        this.b_mitskonto = b_mitskonto;
        this.i_skontoueberziehungsfristintagen = i_skontoueberziehungsfristintagen;
        this.bankverbindung_i_id = bankverbindung_i_id;
        this.t_gespeichert = t_gespeichert;
        this.flrbankverbindung = flrbankverbindung;
        this.flrpersonalgespeichert = flrpersonalgespeichert;
    }

    /** default constructor */
    public FLRZahlungsvorschlaglauf() {
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

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public Date getT_zahlungsstichtag() {
        return this.t_zahlungsstichtag;
    }

    public void setT_zahlungsstichtag(Date t_zahlungsstichtag) {
        this.t_zahlungsstichtag = t_zahlungsstichtag;
    }

    public Date getT_naechsterzahlungslauf() {
        return this.t_naechsterzahlungslauf;
    }

    public void setT_naechsterzahlungslauf(Date t_naechsterzahlungslauf) {
        this.t_naechsterzahlungslauf = t_naechsterzahlungslauf;
    }

    public Short getB_mitskonto() {
        return this.b_mitskonto;
    }

    public void setB_mitskonto(Short b_mitskonto) {
        this.b_mitskonto = b_mitskonto;
    }

    public Integer getI_skontoueberziehungsfristintagen() {
        return this.i_skontoueberziehungsfristintagen;
    }

    public void setI_skontoueberziehungsfristintagen(Integer i_skontoueberziehungsfristintagen) {
        this.i_skontoueberziehungsfristintagen = i_skontoueberziehungsfristintagen;
    }

    public Integer getBankverbindung_i_id() {
        return this.bankverbindung_i_id;
    }

    public void setBankverbindung_i_id(Integer bankverbindung_i_id) {
        this.bankverbindung_i_id = bankverbindung_i_id;
    }

    public Date getT_gespeichert() {
        return this.t_gespeichert;
    }

    public void setT_gespeichert(Date t_gespeichert) {
        this.t_gespeichert = t_gespeichert;
    }

    public FLRFinanzBankkonto getFlrbankverbindung() {
        return this.flrbankverbindung;
    }

    public void setFlrbankverbindung(FLRFinanzBankkonto flrbankverbindung) {
        this.flrbankverbindung = flrbankverbindung;
    }

    public FLRPersonal getFlrpersonalgespeichert() {
        return this.flrpersonalgespeichert;
    }

    public void setFlrpersonalgespeichert(FLRPersonal flrpersonalgespeichert) {
        this.flrpersonalgespeichert = flrpersonalgespeichert;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
