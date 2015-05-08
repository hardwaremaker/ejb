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
 *******************************************************************************/
package com.lp.server.fertigung.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLoszusatzstatus implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer los_i_id;

    /** nullable persistent field */
    private Integer zusatzstatus_i_id;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRZusatzstatus flrzusatzstatus;

    /** full constructor */
    public FLRLoszusatzstatus(Integer los_i_id, Integer zusatzstatus_i_id, Date t_aendern, com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos, com.lp.server.fertigung.fastlanereader.generated.FLRZusatzstatus flrzusatzstatus) {
        this.los_i_id = los_i_id;
        this.zusatzstatus_i_id = zusatzstatus_i_id;
        this.t_aendern = t_aendern;
        this.flrlos = flrlos;
        this.flrzusatzstatus = flrzusatzstatus;
    }

    /** default constructor */
    public FLRLoszusatzstatus() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public Integer getZusatzstatus_i_id() {
        return this.zusatzstatus_i_id;
    }

    public void setZusatzstatus_i_id(Integer zusatzstatus_i_id) {
        this.zusatzstatus_i_id = zusatzstatus_i_id;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRZusatzstatus getFlrzusatzstatus() {
        return this.flrzusatzstatus;
    }

    public void setFlrzusatzstatus(com.lp.server.fertigung.fastlanereader.generated.FLRZusatzstatus flrzusatzstatus) {
        this.flrzusatzstatus = flrzusatzstatus;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
