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

import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPersonal implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_personalnummer;

    /** nullable persistent field */
    private Integer personalgruppe_i_id;

    /** nullable persistent field */
    private String c_ausweis;

    /** nullable persistent field */
    private String c_kurzzeichen;

    /** nullable persistent field */
    private String personalart_c_nr;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private Date t_geburtsdatum;

    /** nullable persistent field */
    private FLRPartner flrpartner;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelleabteilung;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstellestamm;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonalgruppe flrpersonalgruppe;

    /** full constructor */
    public FLRPersonal(String mandant_c_nr, String c_personalnummer, Integer personalgruppe_i_id, String c_ausweis, String c_kurzzeichen, String personalart_c_nr, Short b_versteckt, Date t_geburtsdatum, FLRPartner flrpartner, FLRKostenstelle flrkostenstelleabteilung, FLRKostenstelle flrkostenstellestamm, com.lp.server.personal.fastlanereader.generated.FLRPersonalgruppe flrpersonalgruppe) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_personalnummer = c_personalnummer;
        this.personalgruppe_i_id = personalgruppe_i_id;
        this.c_ausweis = c_ausweis;
        this.c_kurzzeichen = c_kurzzeichen;
        this.personalart_c_nr = personalart_c_nr;
        this.b_versteckt = b_versteckt;
        this.t_geburtsdatum = t_geburtsdatum;
        this.flrpartner = flrpartner;
        this.flrkostenstelleabteilung = flrkostenstelleabteilung;
        this.flrkostenstellestamm = flrkostenstellestamm;
        this.flrpersonalgruppe = flrpersonalgruppe;
    }

    /** default constructor */
    public FLRPersonal() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getC_personalnummer() {
        return this.c_personalnummer;
    }

    public void setC_personalnummer(String c_personalnummer) {
        this.c_personalnummer = c_personalnummer;
    }

    public Integer getPersonalgruppe_i_id() {
        return this.personalgruppe_i_id;
    }

    public void setPersonalgruppe_i_id(Integer personalgruppe_i_id) {
        this.personalgruppe_i_id = personalgruppe_i_id;
    }

    public String getC_ausweis() {
        return this.c_ausweis;
    }

    public void setC_ausweis(String c_ausweis) {
        this.c_ausweis = c_ausweis;
    }

    public String getC_kurzzeichen() {
        return this.c_kurzzeichen;
    }

    public void setC_kurzzeichen(String c_kurzzeichen) {
        this.c_kurzzeichen = c_kurzzeichen;
    }

    public String getPersonalart_c_nr() {
        return this.personalart_c_nr;
    }

    public void setPersonalart_c_nr(String personalart_c_nr) {
        this.personalart_c_nr = personalart_c_nr;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public Date getT_geburtsdatum() {
        return this.t_geburtsdatum;
    }

    public void setT_geburtsdatum(Date t_geburtsdatum) {
        this.t_geburtsdatum = t_geburtsdatum;
    }

    public FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public FLRKostenstelle getFlrkostenstelleabteilung() {
        return this.flrkostenstelleabteilung;
    }

    public void setFlrkostenstelleabteilung(FLRKostenstelle flrkostenstelleabteilung) {
        this.flrkostenstelleabteilung = flrkostenstelleabteilung;
    }

    public FLRKostenstelle getFlrkostenstellestamm() {
        return this.flrkostenstellestamm;
    }

    public void setFlrkostenstellestamm(FLRKostenstelle flrkostenstellestamm) {
        this.flrkostenstellestamm = flrkostenstellestamm;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonalgruppe getFlrpersonalgruppe() {
        return this.flrpersonalgruppe;
    }

    public void setFlrpersonalgruppe(com.lp.server.personal.fastlanereader.generated.FLRPersonalgruppe flrpersonalgruppe) {
        this.flrpersonalgruppe = flrpersonalgruppe;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
