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
package com.lp.server.finanz.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRFinanzFinanzamt implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer i_id;

    /** identifier field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_steuernummer;

    /** nullable persistent field */
    private String c_referat;

    /** nullable persistent field */
    private FLRPartner flrpartner;

    /** full constructor */
    public FLRFinanzFinanzamt(Integer i_id, String mandant_c_nr, String c_steuernummer, String c_referat, FLRPartner flrpartner) {
        this.i_id = i_id;
        this.mandant_c_nr = mandant_c_nr;
        this.c_steuernummer = c_steuernummer;
        this.c_referat = c_referat;
        this.flrpartner = flrpartner;
    }

    /** default constructor */
    public FLRFinanzFinanzamt() {
    }

    /** minimal constructor */
    public FLRFinanzFinanzamt(Integer i_id, String mandant_c_nr) {
        this.i_id = i_id;
        this.mandant_c_nr = mandant_c_nr;
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

    public String getC_steuernummer() {
        return this.c_steuernummer;
    }

    public void setC_steuernummer(String c_steuernummer) {
        this.c_steuernummer = c_steuernummer;
    }

    public String getC_referat() {
        return this.c_referat;
    }

    public void setC_referat(String c_referat) {
        this.c_referat = c_referat;
    }

    public FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .append("mandant_c_nr", getMandant_c_nr())
            .toString();
    }

}
