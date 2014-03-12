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
package com.lp.server.kueche.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRLager;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBedienerlager implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_bedienernummer;

    /** nullable persistent field */
    private Integer lager_i_id;

    /** nullable persistent field */
    private FLRLager flrlager;

    /** full constructor */
    public FLRBedienerlager(String c_bedienernummer, Integer lager_i_id, FLRLager flrlager) {
        this.c_bedienernummer = c_bedienernummer;
        this.lager_i_id = lager_i_id;
        this.flrlager = flrlager;
    }

    /** default constructor */
    public FLRBedienerlager() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_bedienernummer() {
        return this.c_bedienernummer;
    }

    public void setC_bedienernummer(String c_bedienernummer) {
        this.c_bedienernummer = c_bedienernummer;
    }

    public Integer getLager_i_id() {
        return this.lager_i_id;
    }

    public void setLager_i_id(Integer lager_i_id) {
        this.lager_i_id = lager_i_id;
    }

    public FLRLager getFlrlager() {
        return this.flrlager;
    }

    public void setFlrlager(FLRLager flrlager) {
        this.flrlager = flrlager;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
