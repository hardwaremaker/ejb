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

import com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRMaschinenzeitdaten implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date t_von;

    /** nullable persistent field */
    private Date t_bis;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private Date t_anlegen;

    /** nullable persistent field */
    private Integer maschine_i_id;

    /** nullable persistent field */
    private Integer lossollarbeitsplan_i_id;

    /** nullable persistent field */
    private Integer personal_i_id_gestartet;

    /** nullable persistent field */
    private String c_bemerkung;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_gestartet;

    /** nullable persistent field */
    private FLRLossollarbeitsplan flrlossollarbeitsplan;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRMaschine flrmaschine;

    /** full constructor */
    public FLRMaschinenzeitdaten(Date t_von, Date t_bis, Date t_aendern, Date t_anlegen, Integer maschine_i_id, Integer lossollarbeitsplan_i_id, Integer personal_i_id_gestartet, String c_bemerkung, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_gestartet, FLRLossollarbeitsplan flrlossollarbeitsplan, com.lp.server.personal.fastlanereader.generated.FLRMaschine flrmaschine) {
        this.t_von = t_von;
        this.t_bis = t_bis;
        this.t_aendern = t_aendern;
        this.t_anlegen = t_anlegen;
        this.maschine_i_id = maschine_i_id;
        this.lossollarbeitsplan_i_id = lossollarbeitsplan_i_id;
        this.personal_i_id_gestartet = personal_i_id_gestartet;
        this.c_bemerkung = c_bemerkung;
        this.flrpersonal_gestartet = flrpersonal_gestartet;
        this.flrlossollarbeitsplan = flrlossollarbeitsplan;
        this.flrmaschine = flrmaschine;
    }

    /** default constructor */
    public FLRMaschinenzeitdaten() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getT_von() {
        return this.t_von;
    }

    public void setT_von(Date t_von) {
        this.t_von = t_von;
    }

    public Date getT_bis() {
        return this.t_bis;
    }

    public void setT_bis(Date t_bis) {
        this.t_bis = t_bis;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public Integer getMaschine_i_id() {
        return this.maschine_i_id;
    }

    public void setMaschine_i_id(Integer maschine_i_id) {
        this.maschine_i_id = maschine_i_id;
    }

    public Integer getLossollarbeitsplan_i_id() {
        return this.lossollarbeitsplan_i_id;
    }

    public void setLossollarbeitsplan_i_id(Integer lossollarbeitsplan_i_id) {
        this.lossollarbeitsplan_i_id = lossollarbeitsplan_i_id;
    }

    public Integer getPersonal_i_id_gestartet() {
        return this.personal_i_id_gestartet;
    }

    public void setPersonal_i_id_gestartet(Integer personal_i_id_gestartet) {
        this.personal_i_id_gestartet = personal_i_id_gestartet;
    }

    public String getC_bemerkung() {
        return this.c_bemerkung;
    }

    public void setC_bemerkung(String c_bemerkung) {
        this.c_bemerkung = c_bemerkung;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal_gestartet() {
        return this.flrpersonal_gestartet;
    }

    public void setFlrpersonal_gestartet(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_gestartet) {
        this.flrpersonal_gestartet = flrpersonal_gestartet;
    }

    public FLRLossollarbeitsplan getFlrlossollarbeitsplan() {
        return this.flrlossollarbeitsplan;
    }

    public void setFlrlossollarbeitsplan(FLRLossollarbeitsplan flrlossollarbeitsplan) {
        this.flrlossollarbeitsplan = flrlossollarbeitsplan;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRMaschine getFlrmaschine() {
        return this.flrmaschine;
    }

    public void setFlrmaschine(com.lp.server.personal.fastlanereader.generated.FLRMaschine flrmaschine) {
        this.flrmaschine = flrmaschine;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
