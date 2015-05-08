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
package com.lp.server.instandhaltung.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRGeraetehistorie implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer geraet_i_id;

    /** nullable persistent field */
    private Integer personal_i_id_techniker;

    /** nullable persistent field */
    private Integer personal_i_id_aendern;

        /** nullable persistent field */
    private Date t_wartung;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private Short b_nichtmoeglich;

    /** nullable persistent field */
    private com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraet flrgeraet;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_techniker;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_aendern;

    /** full constructor */
    public FLRGeraetehistorie(Integer geraet_i_id, Integer personal_i_id_techniker, Integer personal_i_id_aendern, Date t_wartung, Date t_aendern, Short b_nichtmoeglich, com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraet flrgeraet, FLRPersonal flrpersonal_techniker, FLRPersonal flrpersonal_aendern) {
        this.geraet_i_id = geraet_i_id;
        this.personal_i_id_techniker = personal_i_id_techniker;
        this.personal_i_id_aendern = personal_i_id_aendern;
        this.t_wartung = t_wartung;
        this.t_aendern = t_aendern;
        this.b_nichtmoeglich = b_nichtmoeglich;
        this.flrgeraet = flrgeraet;
        this.flrpersonal_techniker = flrpersonal_techniker;
        this.flrpersonal_aendern = flrpersonal_aendern;
    }

    /** default constructor */
    public FLRGeraetehistorie() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getGeraet_i_id() {
        return this.geraet_i_id;
    }

    public void setGeraet_i_id(Integer geraet_i_id) {
        this.geraet_i_id = geraet_i_id;
    }

    public Integer getPersonal_i_id_techniker() {
        return this.personal_i_id_techniker;
    }

    public void setPersonal_i_id_techniker(Integer personal_i_id_techniker) {
        this.personal_i_id_techniker = personal_i_id_techniker;
    }

    public Integer getPersonal_i_id_aendern() {
        return this.personal_i_id_aendern;
    }

    public void setPersonal_i_id_aendern(Integer personal_i_id_aendern) {
        this.personal_i_id_aendern = personal_i_id_aendern;
    }

    public Date getT_wartung() {
        return this.t_wartung;
    }

    public void setT_wartung(Date t_wartung) {
        this.t_wartung = t_wartung;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public Short getB_nichtmoeglich() {
        return this.b_nichtmoeglich;
    }

    public void setB_nichtmoeglich(Short b_nichtmoeglich) {
        this.b_nichtmoeglich = b_nichtmoeglich;
    }

    public com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraet getFlrgeraet() {
        return this.flrgeraet;
    }

    public void setFlrgeraet(com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraet flrgeraet) {
        this.flrgeraet = flrgeraet;
    }

    public FLRPersonal getFlrpersonal_techniker() {
        return this.flrpersonal_techniker;
    }

    public void setFlrpersonal_techniker(FLRPersonal flrpersonal_techniker) {
        this.flrpersonal_techniker = flrpersonal_techniker;
    }

    public FLRPersonal getFlrpersonal_aendern() {
        return this.flrpersonal_aendern;
    }

    public void setFlrpersonal_aendern(FLRPersonal flrpersonal_aendern) {
        this.flrpersonal_aendern = flrpersonal_aendern;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
