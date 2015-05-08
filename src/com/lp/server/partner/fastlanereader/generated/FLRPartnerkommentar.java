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
package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPartnerkommentar implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private String datenformat_c_nr;

    /** nullable persistent field */
    private Short b_kunde;

    /** nullable persistent field */
    private Integer i_art;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String x_kommentar;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartnerkommentarart flrpartnerkommentarart;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner;

    /** full constructor */
    public FLRPartnerkommentar(Integer partner_i_id, String datenformat_c_nr, Short b_kunde, Integer i_art, Integer i_sort, String x_kommentar, com.lp.server.partner.fastlanereader.generated.FLRPartnerkommentarart flrpartnerkommentarart, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner) {
        this.partner_i_id = partner_i_id;
        this.datenformat_c_nr = datenformat_c_nr;
        this.b_kunde = b_kunde;
        this.i_art = i_art;
        this.i_sort = i_sort;
        this.x_kommentar = x_kommentar;
        this.flrpartnerkommentarart = flrpartnerkommentarart;
        this.flrpartner = flrpartner;
    }

    /** default constructor */
    public FLRPartnerkommentar() {
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

    public String getDatenformat_c_nr() {
        return this.datenformat_c_nr;
    }

    public void setDatenformat_c_nr(String datenformat_c_nr) {
        this.datenformat_c_nr = datenformat_c_nr;
    }

    public Short getB_kunde() {
        return this.b_kunde;
    }

    public void setB_kunde(Short b_kunde) {
        this.b_kunde = b_kunde;
    }

    public Integer getI_art() {
        return this.i_art;
    }

    public void setI_art(Integer i_art) {
        this.i_art = i_art;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public String getX_kommentar() {
        return this.x_kommentar;
    }

    public void setX_kommentar(String x_kommentar) {
        this.x_kommentar = x_kommentar;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartnerkommentarart getFlrpartnerkommentarart() {
        return this.flrpartnerkommentarart;
    }

    public void setFlrpartnerkommentarart(com.lp.server.partner.fastlanereader.generated.FLRPartnerkommentarart flrpartnerkommentarart) {
        this.flrpartnerkommentarart = flrpartnerkommentarart;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
