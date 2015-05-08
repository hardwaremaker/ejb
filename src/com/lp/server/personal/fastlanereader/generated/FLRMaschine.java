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
package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRMaschine implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_inventarnummer;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private String c_identifikationsnr;

    /** nullable persistent field */
    private Short b_autoendebeigeht;

    /** nullable persistent field */
    private Date t_kaufdatum;

    /** nullable persistent field */
    private Integer maschinengruppe_i_id;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRMaschinengruppe flrmaschinengruppe;

    /** full constructor */
    public FLRMaschine(String mandant_c_nr, String c_inventarnummer, String c_bez, Short b_versteckt, String c_identifikationsnr, Short b_autoendebeigeht, Date t_kaufdatum, Integer maschinengruppe_i_id, com.lp.server.personal.fastlanereader.generated.FLRMaschinengruppe flrmaschinengruppe) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_inventarnummer = c_inventarnummer;
        this.c_bez = c_bez;
        this.b_versteckt = b_versteckt;
        this.c_identifikationsnr = c_identifikationsnr;
        this.b_autoendebeigeht = b_autoendebeigeht;
        this.t_kaufdatum = t_kaufdatum;
        this.maschinengruppe_i_id = maschinengruppe_i_id;
        this.flrmaschinengruppe = flrmaschinengruppe;
    }

    /** default constructor */
    public FLRMaschine() {
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

    public String getC_inventarnummer() {
        return this.c_inventarnummer;
    }

    public void setC_inventarnummer(String c_inventarnummer) {
        this.c_inventarnummer = c_inventarnummer;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public String getC_identifikationsnr() {
        return this.c_identifikationsnr;
    }

    public void setC_identifikationsnr(String c_identifikationsnr) {
        this.c_identifikationsnr = c_identifikationsnr;
    }

    public Short getB_autoendebeigeht() {
        return this.b_autoendebeigeht;
    }

    public void setB_autoendebeigeht(Short b_autoendebeigeht) {
        this.b_autoendebeigeht = b_autoendebeigeht;
    }

    public Date getT_kaufdatum() {
        return this.t_kaufdatum;
    }

    public void setT_kaufdatum(Date t_kaufdatum) {
        this.t_kaufdatum = t_kaufdatum;
    }

    public Integer getMaschinengruppe_i_id() {
        return this.maschinengruppe_i_id;
    }

    public void setMaschinengruppe_i_id(Integer maschinengruppe_i_id) {
        this.maschinengruppe_i_id = maschinengruppe_i_id;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRMaschinengruppe getFlrmaschinengruppe() {
        return this.flrmaschinengruppe;
    }

    public void setFlrmaschinengruppe(com.lp.server.personal.fastlanereader.generated.FLRMaschinengruppe flrmaschinengruppe) {
        this.flrmaschinengruppe = flrmaschinengruppe;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
