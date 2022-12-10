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
package com.lp.server.finanz.service;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKontolaenderartPK implements Serializable {
	private static final long serialVersionUID = 2924509899063820837L;

	/** identifier field */
    private Integer konto_i_id;

    /** identifier field */
    private String laenderart_c_nr;

    /** identifier field */
    private Integer finanzamt_i_id;

    /** identifier field */
    private String mandant_c_nr;

    private Integer reversechargeart_i_id ;
    
    /** full constructor */
    public FLRKontolaenderartPK(Integer konto_i_id, String laenderart_c_nr, Integer finanzamt_i_id, String mandant_c_nr, Integer reversechargeart_i_id) {
        this.konto_i_id = konto_i_id;
        this.laenderart_c_nr = laenderart_c_nr;
        this.finanzamt_i_id = finanzamt_i_id;
        this.mandant_c_nr = mandant_c_nr;
        this.setReversechargeart_i_id(reversechargeart_i_id) ;
    }

    /** default constructor */
    public FLRKontolaenderartPK() {
    }

    public Integer getKonto_i_id() {
        return this.konto_i_id;
    }

    public void setKonto_i_id(Integer konto_i_id) {
        this.konto_i_id = konto_i_id;
    }

    public String getLaenderart_c_nr() {
        return this.laenderart_c_nr;
    }

    public void setLaenderart_c_nr(String laenderart_c_nr) {
        this.laenderart_c_nr = laenderart_c_nr;
    }

    public Integer getFinanzamt_i_id() {
        return this.finanzamt_i_id;
    }

    public void setFinanzamt_i_id(Integer finanzamt_i_id) {
        this.finanzamt_i_id = finanzamt_i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

	public Integer getReversechargeart_i_id() {
		return reversechargeart_i_id;
	}

	public void setReversechargeart_i_id(Integer reversechargeart_i_id) {
		this.reversechargeart_i_id = reversechargeart_i_id;
	}


	public String toString() {
        return new ToStringBuilder(this)
            .append("konto_i_id", getKonto_i_id())
            .append("laenderart_c_nr", getLaenderart_c_nr())
            .append("finanzamt_i_id", getFinanzamt_i_id())
            .append("mandant_c_nr", getMandant_c_nr())
            .append("reversechargeart_i_id", getReversechargeart_i_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRKontolaenderartPK) ) return false;
        FLRKontolaenderartPK castOther = (FLRKontolaenderartPK) other;
        return new EqualsBuilder()
            .append(this.getKonto_i_id(), castOther.getKonto_i_id())
            .append(this.getLaenderart_c_nr(), castOther.getLaenderart_c_nr())
            .append(this.getFinanzamt_i_id(), castOther.getFinanzamt_i_id())
            .append(this.getMandant_c_nr(), castOther.getMandant_c_nr())
            .append(this.getReversechargeart_i_id(), castOther.getReversechargeart_i_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getKonto_i_id())
            .append(getLaenderart_c_nr())
            .append(getFinanzamt_i_id())
            .append(getMandant_c_nr())
            .append(getReversechargeart_i_id())
            .toHashCode();
    }
}
