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
package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRReise implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer fahrzeug_i_id;

    /** nullable persistent field */
    private Date t_zeit;

    /** nullable persistent field */
    private Short b_beginn;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private String c_fahrzeug;

    /** nullable persistent field */
    private String belegart_c_nr;

    /** nullable persistent field */
    private BigDecimal n_spesen;

    /** nullable persistent field */
    private Integer i_kmbeginn;

    /** nullable persistent field */
    private Integer i_kmende;

    /** nullable persistent field */
    private Integer i_belegartid;

    /** nullable persistent field */
    private Double f_faktor;

    /** nullable persistent field */
    private FLRPartner flrpartner;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRFahrzeug flrfahrzeug;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRDiaeten flrdiaeten;

    /** full constructor */
    public FLRReise(Integer personal_i_id, Integer fahrzeug_i_id, Date t_zeit, Short b_beginn, String c_kommentar, String c_fahrzeug, String belegart_c_nr, BigDecimal n_spesen, Integer i_kmbeginn, Integer i_kmende, Integer i_belegartid, Double f_faktor, FLRPartner flrpartner, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal, com.lp.server.personal.fastlanereader.generated.FLRFahrzeug flrfahrzeug, com.lp.server.personal.fastlanereader.generated.FLRDiaeten flrdiaeten) {
        this.personal_i_id = personal_i_id;
        this.fahrzeug_i_id = fahrzeug_i_id;
        this.t_zeit = t_zeit;
        this.b_beginn = b_beginn;
        this.c_kommentar = c_kommentar;
        this.c_fahrzeug = c_fahrzeug;
        this.belegart_c_nr = belegart_c_nr;
        this.n_spesen = n_spesen;
        this.i_kmbeginn = i_kmbeginn;
        this.i_kmende = i_kmende;
        this.i_belegartid = i_belegartid;
        this.f_faktor = f_faktor;
        this.flrpartner = flrpartner;
        this.flrpersonal = flrpersonal;
        this.flrfahrzeug = flrfahrzeug;
        this.flrdiaeten = flrdiaeten;
    }

    /** default constructor */
    public FLRReise() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public Integer getFahrzeug_i_id() {
        return this.fahrzeug_i_id;
    }

    public void setFahrzeug_i_id(Integer fahrzeug_i_id) {
        this.fahrzeug_i_id = fahrzeug_i_id;
    }

    public Date getT_zeit() {
        return this.t_zeit;
    }

    public void setT_zeit(Date t_zeit) {
        this.t_zeit = t_zeit;
    }

    public Short getB_beginn() {
        return this.b_beginn;
    }

    public void setB_beginn(Short b_beginn) {
        this.b_beginn = b_beginn;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public String getC_fahrzeug() {
        return this.c_fahrzeug;
    }

    public void setC_fahrzeug(String c_fahrzeug) {
        this.c_fahrzeug = c_fahrzeug;
    }

    public String getBelegart_c_nr() {
        return this.belegart_c_nr;
    }

    public void setBelegart_c_nr(String belegart_c_nr) {
        this.belegart_c_nr = belegart_c_nr;
    }

    public BigDecimal getN_spesen() {
        return this.n_spesen;
    }

    public void setN_spesen(BigDecimal n_spesen) {
        this.n_spesen = n_spesen;
    }

    public Integer getI_kmbeginn() {
        return this.i_kmbeginn;
    }

    public void setI_kmbeginn(Integer i_kmbeginn) {
        this.i_kmbeginn = i_kmbeginn;
    }

    public Integer getI_kmende() {
        return this.i_kmende;
    }

    public void setI_kmende(Integer i_kmende) {
        this.i_kmende = i_kmende;
    }

    public Integer getI_belegartid() {
        return this.i_belegartid;
    }

    public void setI_belegartid(Integer i_belegartid) {
        this.i_belegartid = i_belegartid;
    }

    public Double getF_faktor() {
        return this.f_faktor;
    }

    public void setF_faktor(Double f_faktor) {
        this.f_faktor = f_faktor;
    }

    public FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRFahrzeug getFlrfahrzeug() {
        return this.flrfahrzeug;
    }

    public void setFlrfahrzeug(com.lp.server.personal.fastlanereader.generated.FLRFahrzeug flrfahrzeug) {
        this.flrfahrzeug = flrfahrzeug;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRDiaeten getFlrdiaeten() {
        return this.flrdiaeten;
    }

    public void setFlrdiaeten(com.lp.server.personal.fastlanereader.generated.FLRDiaeten flrdiaeten) {
        this.flrdiaeten = flrdiaeten;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
