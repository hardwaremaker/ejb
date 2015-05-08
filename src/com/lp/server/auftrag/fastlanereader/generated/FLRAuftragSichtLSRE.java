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
package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAuftragSichtLSRE implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_typ;

    /** nullable persistent field */
    private String c_nr_lieferschein;

    /** nullable persistent field */
    private Date t_belegdatum_lieferschein;

    /** nullable persistent field */
    private String status_c_nr_lieferschein;

    /** nullable persistent field */
    private BigDecimal n_wert_lieferschein;

    /** nullable persistent field */
    private String waehrung_c_nr_lieferschein;

    /** nullable persistent field */
    private String auftrag_i_id;

    /** nullable persistent field */
    private String i_id_rechnung;

    /** nullable persistent field */
    private String c_nr_rechnung;

    /** nullable persistent field */
    private Date t_belegdatum_rechnung;

    /** nullable persistent field */
    private String status_c_nr_rechnung;

    /** nullable persistent field */
    private BigDecimal n_wert_rechnung;

    /** nullable persistent field */
    private String waehrung_c_nr_rechnung;

    /** nullable persistent field */
    private BigDecimal n_offen;

    /** nullable persistent field */
    private FLRPersonal flrvertreter_lieferschein;

    /** nullable persistent field */
    private FLRPersonal flrvertreter_rechnung;

    /** full constructor */
    public FLRAuftragSichtLSRE(String c_typ, String c_nr_lieferschein, Date t_belegdatum_lieferschein, String status_c_nr_lieferschein, BigDecimal n_wert_lieferschein, String waehrung_c_nr_lieferschein, String auftrag_i_id, String i_id_rechnung, String c_nr_rechnung, Date t_belegdatum_rechnung, String status_c_nr_rechnung, BigDecimal n_wert_rechnung, String waehrung_c_nr_rechnung, BigDecimal n_offen, FLRPersonal flrvertreter_lieferschein, FLRPersonal flrvertreter_rechnung) {
        this.c_typ = c_typ;
        this.c_nr_lieferschein = c_nr_lieferschein;
        this.t_belegdatum_lieferschein = t_belegdatum_lieferschein;
        this.status_c_nr_lieferschein = status_c_nr_lieferschein;
        this.n_wert_lieferschein = n_wert_lieferschein;
        this.waehrung_c_nr_lieferschein = waehrung_c_nr_lieferschein;
        this.auftrag_i_id = auftrag_i_id;
        this.i_id_rechnung = i_id_rechnung;
        this.c_nr_rechnung = c_nr_rechnung;
        this.t_belegdatum_rechnung = t_belegdatum_rechnung;
        this.status_c_nr_rechnung = status_c_nr_rechnung;
        this.n_wert_rechnung = n_wert_rechnung;
        this.waehrung_c_nr_rechnung = waehrung_c_nr_rechnung;
        this.n_offen = n_offen;
        this.flrvertreter_lieferschein = flrvertreter_lieferschein;
        this.flrvertreter_rechnung = flrvertreter_rechnung;
    }

    /** default constructor */
    public FLRAuftragSichtLSRE() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_typ() {
        return this.c_typ;
    }

    public void setC_typ(String c_typ) {
        this.c_typ = c_typ;
    }

    public String getC_nr_lieferschein() {
        return this.c_nr_lieferschein;
    }

    public void setC_nr_lieferschein(String c_nr_lieferschein) {
        this.c_nr_lieferschein = c_nr_lieferschein;
    }

    public Date getT_belegdatum_lieferschein() {
        return this.t_belegdatum_lieferschein;
    }

    public void setT_belegdatum_lieferschein(Date t_belegdatum_lieferschein) {
        this.t_belegdatum_lieferschein = t_belegdatum_lieferschein;
    }

    public String getStatus_c_nr_lieferschein() {
        return this.status_c_nr_lieferschein;
    }

    public void setStatus_c_nr_lieferschein(String status_c_nr_lieferschein) {
        this.status_c_nr_lieferschein = status_c_nr_lieferschein;
    }

    public BigDecimal getN_wert_lieferschein() {
        return this.n_wert_lieferschein;
    }

    public void setN_wert_lieferschein(BigDecimal n_wert_lieferschein) {
        this.n_wert_lieferschein = n_wert_lieferschein;
    }

    public String getWaehrung_c_nr_lieferschein() {
        return this.waehrung_c_nr_lieferschein;
    }

    public void setWaehrung_c_nr_lieferschein(String waehrung_c_nr_lieferschein) {
        this.waehrung_c_nr_lieferschein = waehrung_c_nr_lieferschein;
    }

    public String getAuftrag_i_id() {
        return this.auftrag_i_id;
    }

    public void setAuftrag_i_id(String auftrag_i_id) {
        this.auftrag_i_id = auftrag_i_id;
    }

    public String getI_id_rechnung() {
        return this.i_id_rechnung;
    }

    public void setI_id_rechnung(String i_id_rechnung) {
        this.i_id_rechnung = i_id_rechnung;
    }

    public String getC_nr_rechnung() {
        return this.c_nr_rechnung;
    }

    public void setC_nr_rechnung(String c_nr_rechnung) {
        this.c_nr_rechnung = c_nr_rechnung;
    }

    public Date getT_belegdatum_rechnung() {
        return this.t_belegdatum_rechnung;
    }

    public void setT_belegdatum_rechnung(Date t_belegdatum_rechnung) {
        this.t_belegdatum_rechnung = t_belegdatum_rechnung;
    }

    public String getStatus_c_nr_rechnung() {
        return this.status_c_nr_rechnung;
    }

    public void setStatus_c_nr_rechnung(String status_c_nr_rechnung) {
        this.status_c_nr_rechnung = status_c_nr_rechnung;
    }

    public BigDecimal getN_wert_rechnung() {
        return this.n_wert_rechnung;
    }

    public void setN_wert_rechnung(BigDecimal n_wert_rechnung) {
        this.n_wert_rechnung = n_wert_rechnung;
    }

    public String getWaehrung_c_nr_rechnung() {
        return this.waehrung_c_nr_rechnung;
    }

    public void setWaehrung_c_nr_rechnung(String waehrung_c_nr_rechnung) {
        this.waehrung_c_nr_rechnung = waehrung_c_nr_rechnung;
    }

    public BigDecimal getN_offen() {
        return this.n_offen;
    }

    public void setN_offen(BigDecimal n_offen) {
        this.n_offen = n_offen;
    }

    public FLRPersonal getFlrvertreter_lieferschein() {
        return this.flrvertreter_lieferschein;
    }

    public void setFlrvertreter_lieferschein(FLRPersonal flrvertreter_lieferschein) {
        this.flrvertreter_lieferschein = flrvertreter_lieferschein;
    }

    public FLRPersonal getFlrvertreter_rechnung() {
        return this.flrvertreter_rechnung;
    }

    public void setFlrvertreter_rechnung(FLRPersonal flrvertreter_rechnung) {
        this.flrvertreter_rechnung = flrvertreter_rechnung;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
