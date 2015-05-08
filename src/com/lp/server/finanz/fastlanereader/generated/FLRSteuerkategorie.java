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
public class FLRSteuerkategorie implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private Short b_reversecharge;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer finanzamt_i_id;

    /** nullable persistent field */
    private Integer konto_i_id_verbindlichkeiten;

    /** nullable persistent field */
    private Integer konto_i_id_forderungen;

    /** full constructor */
    public FLRSteuerkategorie(String mandant_c_nr, String c_bez, String c_nr, Short b_reversecharge, Integer i_sort, Integer finanzamt_i_id, Integer konto_i_id_verbindlichkeiten, Integer konto_i_id_forderungen) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_bez = c_bez;
        this.c_nr = c_nr;
        this.b_reversecharge = b_reversecharge;
        this.i_sort = i_sort;
        this.finanzamt_i_id = finanzamt_i_id;
        this.konto_i_id_verbindlichkeiten = konto_i_id_verbindlichkeiten;
        this.konto_i_id_forderungen = konto_i_id_forderungen;
    }

    /** default constructor */
    public FLRSteuerkategorie() {
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

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public Short getB_reversecharge() {
        return this.b_reversecharge;
    }

    public void setB_reversecharge(Short b_reversecharge) {
        this.b_reversecharge = b_reversecharge;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getFinanzamt_i_id() {
        return this.finanzamt_i_id;
    }

    public void setFinanzamt_i_id(Integer finanzamt_i_id) {
        this.finanzamt_i_id = finanzamt_i_id;
    }

    public Integer getKonto_i_id_verbindlichkeiten() {
        return this.konto_i_id_verbindlichkeiten;
    }

    public void setKonto_i_id_verbindlichkeiten(Integer konto_i_id_verbindlichkeiten) {
        this.konto_i_id_verbindlichkeiten = konto_i_id_verbindlichkeiten;
    }

    public Integer getKonto_i_id_forderungen() {
        return this.konto_i_id_forderungen;
    }

    public void setKonto_i_id_forderungen(Integer konto_i_id_forderungen) {
        this.konto_i_id_forderungen = konto_i_id_forderungen;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
