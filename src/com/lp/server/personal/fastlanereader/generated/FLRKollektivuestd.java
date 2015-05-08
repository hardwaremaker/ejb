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
public class FLRKollektivuestd implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date u_bis;

    /** nullable persistent field */
    private Date u_ab;

    /** nullable persistent field */
    private Short b_restdestages;

    /** nullable persistent field */
    private Short b_unterignorieren;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRKollektiv flrkollektiv;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart;

    /** full constructor */
    public FLRKollektivuestd(Date u_bis, Date u_ab, Short b_restdestages, Short b_unterignorieren, com.lp.server.personal.fastlanereader.generated.FLRKollektiv flrkollektiv, com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart) {
        this.u_bis = u_bis;
        this.u_ab = u_ab;
        this.b_restdestages = b_restdestages;
        this.b_unterignorieren = b_unterignorieren;
        this.flrkollektiv = flrkollektiv;
        this.flrtagesart = flrtagesart;
    }

    /** default constructor */
    public FLRKollektivuestd() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getU_bis() {
        return this.u_bis;
    }

    public void setU_bis(Date u_bis) {
        this.u_bis = u_bis;
    }

    public Date getU_ab() {
        return this.u_ab;
    }

    public void setU_ab(Date u_ab) {
        this.u_ab = u_ab;
    }

    public Short getB_restdestages() {
        return this.b_restdestages;
    }

    public void setB_restdestages(Short b_restdestages) {
        this.b_restdestages = b_restdestages;
    }

    public Short getB_unterignorieren() {
        return this.b_unterignorieren;
    }

    public void setB_unterignorieren(Short b_unterignorieren) {
        this.b_unterignorieren = b_unterignorieren;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRKollektiv getFlrkollektiv() {
        return this.flrkollektiv;
    }

    public void setFlrkollektiv(com.lp.server.personal.fastlanereader.generated.FLRKollektiv flrkollektiv) {
        this.flrkollektiv = flrkollektiv;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRTagesart getFlrtagesart() {
        return this.flrtagesart;
    }

    public void setFlrtagesart(com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart) {
        this.flrtagesart = flrtagesart;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
