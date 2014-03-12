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
public class FLRZeitmodelltag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date u_sollzeit;

    /** nullable persistent field */
    private Date u_autopauseab;

    /** nullable persistent field */
    private Date u_mindestpause;

    /** nullable persistent field */
    private Date u_autopauseab2;

    /** nullable persistent field */
    private Date u_mindestpause2;

    /** nullable persistent field */
    private Date u_erlaubteanwesenheitszeit;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRZeitmodell flrzeitmodell;

    /** full constructor */
    public FLRZeitmodelltag(Date u_sollzeit, Date u_autopauseab, Date u_mindestpause, Date u_autopauseab2, Date u_mindestpause2, Date u_erlaubteanwesenheitszeit, com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart, com.lp.server.personal.fastlanereader.generated.FLRZeitmodell flrzeitmodell) {
        this.u_sollzeit = u_sollzeit;
        this.u_autopauseab = u_autopauseab;
        this.u_mindestpause = u_mindestpause;
        this.u_autopauseab2 = u_autopauseab2;
        this.u_mindestpause2 = u_mindestpause2;
        this.u_erlaubteanwesenheitszeit = u_erlaubteanwesenheitszeit;
        this.flrtagesart = flrtagesart;
        this.flrzeitmodell = flrzeitmodell;
    }

    /** default constructor */
    public FLRZeitmodelltag() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getU_sollzeit() {
        return this.u_sollzeit;
    }

    public void setU_sollzeit(Date u_sollzeit) {
        this.u_sollzeit = u_sollzeit;
    }

    public Date getU_autopauseab() {
        return this.u_autopauseab;
    }

    public void setU_autopauseab(Date u_autopauseab) {
        this.u_autopauseab = u_autopauseab;
    }

    public Date getU_mindestpause() {
        return this.u_mindestpause;
    }

    public void setU_mindestpause(Date u_mindestpause) {
        this.u_mindestpause = u_mindestpause;
    }

    public Date getU_autopauseab2() {
        return this.u_autopauseab2;
    }

    public void setU_autopauseab2(Date u_autopauseab2) {
        this.u_autopauseab2 = u_autopauseab2;
    }

    public Date getU_mindestpause2() {
        return this.u_mindestpause2;
    }

    public void setU_mindestpause2(Date u_mindestpause2) {
        this.u_mindestpause2 = u_mindestpause2;
    }

    public Date getU_erlaubteanwesenheitszeit() {
        return this.u_erlaubteanwesenheitszeit;
    }

    public void setU_erlaubteanwesenheitszeit(Date u_erlaubteanwesenheitszeit) {
        this.u_erlaubteanwesenheitszeit = u_erlaubteanwesenheitszeit;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRTagesart getFlrtagesart() {
        return this.flrtagesart;
    }

    public void setFlrtagesart(com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart) {
        this.flrtagesart = flrtagesart;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRZeitmodell getFlrzeitmodell() {
        return this.flrzeitmodell;
    }

    public void setFlrzeitmodell(com.lp.server.personal.fastlanereader.generated.FLRZeitmodell flrzeitmodell) {
        this.flrzeitmodell = flrzeitmodell;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
