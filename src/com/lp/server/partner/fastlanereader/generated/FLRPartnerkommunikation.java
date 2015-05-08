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
package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPartnerkommunikation implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_inhalt;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRKommunikationsart flrkommunikationsart;

    /** full constructor */
    public FLRPartnerkommunikation(Integer partner_i_id, String c_bez, String c_inhalt, String mandant_c_nr, com.lp.server.partner.fastlanereader.generated.FLRKommunikationsart flrkommunikationsart) {
        this.partner_i_id = partner_i_id;
        this.c_bez = c_bez;
        this.c_inhalt = c_inhalt;
        this.mandant_c_nr = mandant_c_nr;
        this.flrkommunikationsart = flrkommunikationsart;
    }

    /** default constructor */
    public FLRPartnerkommunikation() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getPartner_i_id() {
        return this.partner_i_id;
    }

    public void setPartner_i_id(Integer partner_i_id) {
        this.partner_i_id = partner_i_id;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_inhalt() {
        return this.c_inhalt;
    }

    public void setC_inhalt(String c_inhalt) {
        this.c_inhalt = c_inhalt;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRKommunikationsart getFlrkommunikationsart() {
        return this.flrkommunikationsart;
    }

    public void setFlrkommunikationsart(com.lp.server.partner.fastlanereader.generated.FLRKommunikationsart flrkommunikationsart) {
        this.flrkommunikationsart = flrkommunikationsart;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
