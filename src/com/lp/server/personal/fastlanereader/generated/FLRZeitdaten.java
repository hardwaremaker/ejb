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
package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZeitdaten implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date t_zeit;

    /** nullable persistent field */
    private String c_belegartnr;

    /** nullable persistent field */
    private Short b_taetigkeitgeaendert;

    /** nullable persistent field */
    private Short b_automatikbuchung;

    /** nullable persistent field */
    private Integer i_belegartid;

    /** nullable persistent field */
    private Integer i_belegartpositionid;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer taetigkeit_i_id;

    /** nullable persistent field */
    private String c_bemerkungzubelegart;

    /** nullable persistent field */
    private String c_wowurdegebucht;

    /** nullable persistent field */
    private String x_kommentar;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit flrtaetigkeit;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel;

    /** full constructor */
    public FLRZeitdaten(Date t_zeit, String c_belegartnr, Short b_taetigkeitgeaendert, Short b_automatikbuchung, Integer i_belegartid, Integer i_belegartpositionid, Date t_aendern, Integer personal_i_id, Integer artikel_i_id, Integer taetigkeit_i_id, String c_bemerkungzubelegart, String c_wowurdegebucht, String x_kommentar, com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit flrtaetigkeit, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal, FLRArtikelliste flrartikel) {
        this.t_zeit = t_zeit;
        this.c_belegartnr = c_belegartnr;
        this.b_taetigkeitgeaendert = b_taetigkeitgeaendert;
        this.b_automatikbuchung = b_automatikbuchung;
        this.i_belegartid = i_belegartid;
        this.i_belegartpositionid = i_belegartpositionid;
        this.t_aendern = t_aendern;
        this.personal_i_id = personal_i_id;
        this.artikel_i_id = artikel_i_id;
        this.taetigkeit_i_id = taetigkeit_i_id;
        this.c_bemerkungzubelegart = c_bemerkungzubelegart;
        this.c_wowurdegebucht = c_wowurdegebucht;
        this.x_kommentar = x_kommentar;
        this.flrtaetigkeit = flrtaetigkeit;
        this.flrpersonal = flrpersonal;
        this.flrartikel = flrartikel;
    }

    /** default constructor */
    public FLRZeitdaten() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getT_zeit() {
        return this.t_zeit;
    }

    public void setT_zeit(Date t_zeit) {
        this.t_zeit = t_zeit;
    }

    public String getC_belegartnr() {
        return this.c_belegartnr;
    }

    public void setC_belegartnr(String c_belegartnr) {
        this.c_belegartnr = c_belegartnr;
    }

    public Short getB_taetigkeitgeaendert() {
        return this.b_taetigkeitgeaendert;
    }

    public void setB_taetigkeitgeaendert(Short b_taetigkeitgeaendert) {
        this.b_taetigkeitgeaendert = b_taetigkeitgeaendert;
    }

    public Short getB_automatikbuchung() {
        return this.b_automatikbuchung;
    }

    public void setB_automatikbuchung(Short b_automatikbuchung) {
        this.b_automatikbuchung = b_automatikbuchung;
    }

    public Integer getI_belegartid() {
        return this.i_belegartid;
    }

    public void setI_belegartid(Integer i_belegartid) {
        this.i_belegartid = i_belegartid;
    }

    public Integer getI_belegartpositionid() {
        return this.i_belegartpositionid;
    }

    public void setI_belegartpositionid(Integer i_belegartpositionid) {
        this.i_belegartpositionid = i_belegartpositionid;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Integer getTaetigkeit_i_id() {
        return this.taetigkeit_i_id;
    }

    public void setTaetigkeit_i_id(Integer taetigkeit_i_id) {
        this.taetigkeit_i_id = taetigkeit_i_id;
    }

    public String getC_bemerkungzubelegart() {
        return this.c_bemerkungzubelegart;
    }

    public void setC_bemerkungzubelegart(String c_bemerkungzubelegart) {
        this.c_bemerkungzubelegart = c_bemerkungzubelegart;
    }

    public String getC_wowurdegebucht() {
        return this.c_wowurdegebucht;
    }

    public void setC_wowurdegebucht(String c_wowurdegebucht) {
        this.c_wowurdegebucht = c_wowurdegebucht;
    }

    public String getX_kommentar() {
        return this.x_kommentar;
    }

    public void setX_kommentar(String x_kommentar) {
        this.x_kommentar = x_kommentar;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit getFlrtaetigkeit() {
        return this.flrtaetigkeit;
    }

    public void setFlrtaetigkeit(com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit flrtaetigkeit) {
        this.flrtaetigkeit = flrtaetigkeit;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public FLRArtikelliste getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikelliste flrartikel) {
        this.flrartikel = flrartikel;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
