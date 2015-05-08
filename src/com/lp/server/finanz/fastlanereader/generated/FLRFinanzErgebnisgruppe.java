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
package com.lp.server.finanz.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRFinanzErgebnisgruppe implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private Integer ergebnisgruppe_i_id_summe;

    /** nullable persistent field */
    private Short b_summe_negativ;

    /** nullable persistent field */
    private Short b_invertiert;

    /** nullable persistent field */
    private Short b_bilanzgruppe;

    /** nullable persistent field */
    private Short b_prozentbasis;

    /** nullable persistent field */
    private Integer i_typ;

    /** nullable persistent field */
    private Integer i_reihung;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzErgebnisgruppe flrergebnisgruppe_summe;

    /** full constructor */
    public FLRFinanzErgebnisgruppe(String mandant_c_nr, String c_bez, Integer ergebnisgruppe_i_id_summe, Short b_summe_negativ, Short b_invertiert, Short b_bilanzgruppe, Short b_prozentbasis, Integer i_typ, Integer i_reihung, com.lp.server.finanz.fastlanereader.generated.FLRFinanzErgebnisgruppe flrergebnisgruppe_summe) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_bez = c_bez;
        this.ergebnisgruppe_i_id_summe = ergebnisgruppe_i_id_summe;
        this.b_summe_negativ = b_summe_negativ;
        this.b_invertiert = b_invertiert;
        this.b_bilanzgruppe = b_bilanzgruppe;
        this.b_prozentbasis = b_prozentbasis;
        this.i_typ = i_typ;
        this.i_reihung = i_reihung;
        this.flrergebnisgruppe_summe = flrergebnisgruppe_summe;
    }

    /** default constructor */
    public FLRFinanzErgebnisgruppe() {
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

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public Integer getErgebnisgruppe_i_id_summe() {
        return this.ergebnisgruppe_i_id_summe;
    }

    public void setErgebnisgruppe_i_id_summe(Integer ergebnisgruppe_i_id_summe) {
        this.ergebnisgruppe_i_id_summe = ergebnisgruppe_i_id_summe;
    }

    public Short getB_summe_negativ() {
        return this.b_summe_negativ;
    }

    public void setB_summe_negativ(Short b_summe_negativ) {
        this.b_summe_negativ = b_summe_negativ;
    }

    public Short getB_invertiert() {
        return this.b_invertiert;
    }

    public void setB_invertiert(Short b_invertiert) {
        this.b_invertiert = b_invertiert;
    }

    public Short getB_bilanzgruppe() {
        return this.b_bilanzgruppe;
    }

    public void setB_bilanzgruppe(Short b_bilanzgruppe) {
        this.b_bilanzgruppe = b_bilanzgruppe;
    }

    public Short getB_prozentbasis() {
        return this.b_prozentbasis;
    }

    public void setB_prozentbasis(Short b_prozentbasis) {
        this.b_prozentbasis = b_prozentbasis;
    }

    public Integer getI_typ() {
        return this.i_typ;
    }

    public void setI_typ(Integer i_typ) {
        this.i_typ = i_typ;
    }

    public Integer getI_reihung() {
        return this.i_reihung;
    }

    public void setI_reihung(Integer i_reihung) {
        this.i_reihung = i_reihung;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzErgebnisgruppe getFlrergebnisgruppe_summe() {
        return this.flrergebnisgruppe_summe;
    }

    public void setFlrergebnisgruppe_summe(com.lp.server.finanz.fastlanereader.generated.FLRFinanzErgebnisgruppe flrergebnisgruppe_summe) {
        this.flrergebnisgruppe_summe = flrergebnisgruppe_summe;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
