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

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRSonderzeiten implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer taetigkeit_i_id;

    /** nullable persistent field */
    private Date t_datum;

    /** nullable persistent field */
    private Short b_tag;

    /** nullable persistent field */
    private Short b_halbtag;

    /** nullable persistent field */
    private Date u_stunden;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit flrtaetigkeit;

    /** full constructor */
    public FLRSonderzeiten(Integer personal_i_id, Integer taetigkeit_i_id, Date t_datum, Short b_tag, Short b_halbtag, Date u_stunden, com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit flrtaetigkeit) {
        this.personal_i_id = personal_i_id;
        this.taetigkeit_i_id = taetigkeit_i_id;
        this.t_datum = t_datum;
        this.b_tag = b_tag;
        this.b_halbtag = b_halbtag;
        this.u_stunden = u_stunden;
        this.flrtaetigkeit = flrtaetigkeit;
    }

    /** default constructor */
    public FLRSonderzeiten() {
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

    public Integer getTaetigkeit_i_id() {
        return this.taetigkeit_i_id;
    }

    public void setTaetigkeit_i_id(Integer taetigkeit_i_id) {
        this.taetigkeit_i_id = taetigkeit_i_id;
    }

    public Date getT_datum() {
        return this.t_datum;
    }

    public void setT_datum(Date t_datum) {
        this.t_datum = t_datum;
    }

    public Short getB_tag() {
        return this.b_tag;
    }

    public void setB_tag(Short b_tag) {
        this.b_tag = b_tag;
    }

    public Short getB_halbtag() {
        return this.b_halbtag;
    }

    public void setB_halbtag(Short b_halbtag) {
        this.b_halbtag = b_halbtag;
    }

    public Date getU_stunden() {
        return this.u_stunden;
    }

    public void setU_stunden(Date u_stunden) {
        this.u_stunden = u_stunden;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit getFlrtaetigkeit() {
        return this.flrtaetigkeit;
    }

    public void setFlrtaetigkeit(com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit flrtaetigkeit) {
        this.flrtaetigkeit = flrtaetigkeit;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
