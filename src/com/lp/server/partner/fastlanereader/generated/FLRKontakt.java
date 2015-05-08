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

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKontakt implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_titel;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private Date t_kontakt;

    /** nullable persistent field */
    private Date t_kontaktbis;

    /** nullable persistent field */
    private Date t_wiedervorlage;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRKontaktart flrkontaktart;

    /** nullable persistent field */
    private FLRPersonal flrpersonal;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner flransprechpartner;

    /** full constructor */
    public FLRKontakt(String c_titel, Date t_erledigt, Date t_kontakt, Date t_kontaktbis, Date t_wiedervorlage, Integer partner_i_id, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner, com.lp.server.partner.fastlanereader.generated.FLRKontaktart flrkontaktart, FLRPersonal flrpersonal, com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner flransprechpartner) {
        this.c_titel = c_titel;
        this.t_erledigt = t_erledigt;
        this.t_kontakt = t_kontakt;
        this.t_kontaktbis = t_kontaktbis;
        this.t_wiedervorlage = t_wiedervorlage;
        this.partner_i_id = partner_i_id;
        this.flrpartner = flrpartner;
        this.flrkontaktart = flrkontaktart;
        this.flrpersonal = flrpersonal;
        this.flransprechpartner = flransprechpartner;
    }

    /** default constructor */
    public FLRKontakt() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_titel() {
        return this.c_titel;
    }

    public void setC_titel(String c_titel) {
        this.c_titel = c_titel;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public Date getT_kontakt() {
        return this.t_kontakt;
    }

    public void setT_kontakt(Date t_kontakt) {
        this.t_kontakt = t_kontakt;
    }

    public Date getT_kontaktbis() {
        return this.t_kontaktbis;
    }

    public void setT_kontaktbis(Date t_kontaktbis) {
        this.t_kontaktbis = t_kontaktbis;
    }

    public Date getT_wiedervorlage() {
        return this.t_wiedervorlage;
    }

    public void setT_wiedervorlage(Date t_wiedervorlage) {
        this.t_wiedervorlage = t_wiedervorlage;
    }

    public Integer getPartner_i_id() {
        return this.partner_i_id;
    }

    public void setPartner_i_id(Integer partner_i_id) {
        this.partner_i_id = partner_i_id;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRKontaktart getFlrkontaktart() {
        return this.flrkontaktart;
    }

    public void setFlrkontaktart(com.lp.server.partner.fastlanereader.generated.FLRKontaktart flrkontaktart) {
        this.flrkontaktart = flrkontaktart;
    }

    public FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner getFlransprechpartner() {
        return this.flransprechpartner;
    }

    public void setFlransprechpartner(com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner flransprechpartner) {
        this.flransprechpartner = flransprechpartner;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
