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
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRFeiertag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_tag;

    /** nullable persistent field */
    private Integer i_monat;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private Integer i_offset_ostersonntag;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRReligion flrreligion;

    /** full constructor */
    public FLRFeiertag(Integer i_tag, Integer i_monat, String mandant_c_nr, String c_bez, Integer i_offset_ostersonntag, com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart, com.lp.server.personal.fastlanereader.generated.FLRReligion flrreligion) {
        this.i_tag = i_tag;
        this.i_monat = i_monat;
        this.mandant_c_nr = mandant_c_nr;
        this.c_bez = c_bez;
        this.i_offset_ostersonntag = i_offset_ostersonntag;
        this.flrtagesart = flrtagesart;
        this.flrreligion = flrreligion;
    }

    /** default constructor */
    public FLRFeiertag() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_tag() {
        return this.i_tag;
    }

    public void setI_tag(Integer i_tag) {
        this.i_tag = i_tag;
    }

    public Integer getI_monat() {
        return this.i_monat;
    }

    public void setI_monat(Integer i_monat) {
        this.i_monat = i_monat;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public Integer getI_offset_ostersonntag() {
        return this.i_offset_ostersonntag;
    }

    public void setI_offset_ostersonntag(Integer i_offset_ostersonntag) {
        this.i_offset_ostersonntag = i_offset_ostersonntag;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRTagesart getFlrtagesart() {
        return this.flrtagesart;
    }

    public void setFlrtagesart(com.lp.server.personal.fastlanereader.generated.FLRTagesart flrtagesart) {
        this.flrtagesart = flrtagesart;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRReligion getFlrreligion() {
        return this.flrreligion;
    }

    public void setFlrreligion(com.lp.server.personal.fastlanereader.generated.FLRReligion flrreligion) {
        this.flrreligion = flrreligion;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
