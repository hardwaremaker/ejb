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

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBereitschaft implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date t_beginn;

    /** nullable persistent field */
    private Date t_ende;

    /** nullable persistent field */
    private String c_bemerkung;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRBereitschaftart flrbereitschaftart;

    /** full constructor */
    public FLRBereitschaft(Date t_beginn, Date t_ende, String c_bemerkung, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal, com.lp.server.personal.fastlanereader.generated.FLRBereitschaftart flrbereitschaftart) {
        this.t_beginn = t_beginn;
        this.t_ende = t_ende;
        this.c_bemerkung = c_bemerkung;
        this.flrpersonal = flrpersonal;
        this.flrbereitschaftart = flrbereitschaftart;
    }

    /** default constructor */
    public FLRBereitschaft() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getT_beginn() {
        return this.t_beginn;
    }

    public void setT_beginn(Date t_beginn) {
        this.t_beginn = t_beginn;
    }

    public Date getT_ende() {
        return this.t_ende;
    }

    public void setT_ende(Date t_ende) {
        this.t_ende = t_ende;
    }

    public String getC_bemerkung() {
        return this.c_bemerkung;
    }

    public void setC_bemerkung(String c_bemerkung) {
        this.c_bemerkung = c_bemerkung;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRBereitschaftart getFlrbereitschaftart() {
        return this.flrbereitschaftart;
    }

    public void setFlrbereitschaftart(com.lp.server.personal.fastlanereader.generated.FLRBereitschaftart flrbereitschaftart) {
        this.flrbereitschaftart = flrbereitschaftart;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
