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
package com.lp.server.inserat.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRInseratrechnung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer inserat_i_id;

    /** nullable persistent field */
    private Integer rechnungposition_i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private FLRRechnungPosition flrrechnungposition;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private com.lp.server.inserat.fastlanereader.generated.FLRInserat flrinserat;

    /** full constructor */
    public FLRInseratrechnung(Integer inserat_i_id, Integer rechnungposition_i_id, Integer i_sort, Integer kunde_i_id, FLRRechnungPosition flrrechnungposition, FLRKunde flrkunde, com.lp.server.inserat.fastlanereader.generated.FLRInserat flrinserat) {
        this.inserat_i_id = inserat_i_id;
        this.rechnungposition_i_id = rechnungposition_i_id;
        this.i_sort = i_sort;
        this.kunde_i_id = kunde_i_id;
        this.flrrechnungposition = flrrechnungposition;
        this.flrkunde = flrkunde;
        this.flrinserat = flrinserat;
    }

    /** default constructor */
    public FLRInseratrechnung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getInserat_i_id() {
        return this.inserat_i_id;
    }

    public void setInserat_i_id(Integer inserat_i_id) {
        this.inserat_i_id = inserat_i_id;
    }

    public Integer getRechnungposition_i_id() {
        return this.rechnungposition_i_id;
    }

    public void setRechnungposition_i_id(Integer rechnungposition_i_id) {
        this.rechnungposition_i_id = rechnungposition_i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public FLRRechnungPosition getFlrrechnungposition() {
        return this.flrrechnungposition;
    }

    public void setFlrrechnungposition(FLRRechnungPosition flrrechnungposition) {
        this.flrrechnungposition = flrrechnungposition;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public com.lp.server.inserat.fastlanereader.generated.FLRInserat getFlrinserat() {
        return this.flrinserat;
    }

    public void setFlrinserat(com.lp.server.inserat.fastlanereader.generated.FLRInserat flrinserat) {
        this.flrinserat = flrinserat;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
