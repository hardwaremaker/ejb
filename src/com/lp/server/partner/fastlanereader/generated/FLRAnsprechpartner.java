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
package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAnsprechpartner implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private Integer partner_i_id_ansprechpartner;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer ansprechpartnerfunktion_i_id;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private String x_bemerkung;

    /** nullable persistent field */
    private String c_email;

    /** nullable persistent field */
    private String c_fax;

    /** nullable persistent field */
    private String c_telefon;

    /** nullable persistent field */
    private String c_handy;

    /** nullable persistent field */
    private String c_direktfax;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartneransprechpartner;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartnerfunktion flransprechpartnerfunktion;
    
    /** nullable persistent field */
    private Short b_newsletter_empfaenger;

    /** full constructor */
    public FLRAnsprechpartner(Integer partner_i_id, Integer partner_i_id_ansprechpartner, Integer i_sort, Integer ansprechpartnerfunktion_i_id, Short b_versteckt, String x_bemerkung, String c_email, String c_fax, String c_telefon, String c_handy, String c_direktfax, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartneransprechpartner, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner, com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartnerfunktion flransprechpartnerfunktion, Short b_newsletter_empfaenger) {
        this.partner_i_id = partner_i_id;
        this.partner_i_id_ansprechpartner = partner_i_id_ansprechpartner;
        this.i_sort = i_sort;
        this.ansprechpartnerfunktion_i_id = ansprechpartnerfunktion_i_id;
        this.b_versteckt = b_versteckt;
        this.x_bemerkung = x_bemerkung;
        this.c_email = c_email;
        this.c_fax = c_fax;
        this.c_telefon = c_telefon;
        this.c_handy = c_handy;
        this.c_direktfax = c_direktfax;
        this.flrpartneransprechpartner = flrpartneransprechpartner;
        this.flrpartner = flrpartner;
        this.flransprechpartnerfunktion = flransprechpartnerfunktion;
        this.b_newsletter_empfaenger = b_newsletter_empfaenger;
    }

    /** default constructor */
    public FLRAnsprechpartner() {
    }
    
    public Short getB_newsletter_empfaenger() {
		return b_newsletter_empfaenger;
	}
    
    public void setB_newsletter_empfaenger(Short b_newsletter_empfaenger) {
		this.b_newsletter_empfaenger = b_newsletter_empfaenger;
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

    public Integer getPartner_i_id_ansprechpartner() {
        return this.partner_i_id_ansprechpartner;
    }

    public void setPartner_i_id_ansprechpartner(Integer partner_i_id_ansprechpartner) {
        this.partner_i_id_ansprechpartner = partner_i_id_ansprechpartner;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getAnsprechpartnerfunktion_i_id() {
        return this.ansprechpartnerfunktion_i_id;
    }

    public void setAnsprechpartnerfunktion_i_id(Integer ansprechpartnerfunktion_i_id) {
        this.ansprechpartnerfunktion_i_id = ansprechpartnerfunktion_i_id;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public String getX_bemerkung() {
        return this.x_bemerkung;
    }

    public void setX_bemerkung(String x_bemerkung) {
        this.x_bemerkung = x_bemerkung;
    }

    public String getC_email() {
        return this.c_email;
    }

    public void setC_email(String c_email) {
        this.c_email = c_email;
    }

    public String getC_fax() {
        return this.c_fax;
    }

    public void setC_fax(String c_fax) {
        this.c_fax = c_fax;
    }

    public String getC_telefon() {
        return this.c_telefon;
    }

    public void setC_telefon(String c_telefon) {
        this.c_telefon = c_telefon;
    }

    public String getC_handy() {
        return this.c_handy;
    }

    public void setC_handy(String c_handy) {
        this.c_handy = c_handy;
    }

    public String getC_direktfax() {
        return this.c_direktfax;
    }

    public void setC_direktfax(String c_direktfax) {
        this.c_direktfax = c_direktfax;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartneransprechpartner() {
        return this.flrpartneransprechpartner;
    }

    public void setFlrpartneransprechpartner(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartneransprechpartner) {
        this.flrpartneransprechpartner = flrpartneransprechpartner;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartnerfunktion getFlransprechpartnerfunktion() {
        return this.flransprechpartnerfunktion;
    }

    public void setFlransprechpartnerfunktion(com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartnerfunktion flransprechpartnerfunktion) {
        this.flransprechpartnerfunktion = flransprechpartnerfunktion;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
