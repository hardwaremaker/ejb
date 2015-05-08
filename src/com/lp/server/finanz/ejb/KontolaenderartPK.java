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
package com.lp.server.finanz.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KontolaenderartPK implements Serializable {
	@Column(name = "KONTO_I_ID", insertable = false, updatable = false)
	private Integer kontoIId;

	@Column(name = "LAENDERART_C_NR", insertable = false, updatable = false)
	private String laenderartCNr;

	@Column(name = "FINANZAMT_I_ID", insertable = false, updatable = false)
	private Integer finanzamtIId;
	
	@Column(name = "MANDANT_C_NR", insertable = false, updatable = false)
	private String mandantCNr;
	
	private static final long serialVersionUID = 1L;

	public KontolaenderartPK() {
	}
	
	public KontolaenderartPK(Integer kontoIId, String laenderartCNr, Integer finanzamtIId, String mandantCNr) {
		this.kontoIId = kontoIId;
		this.laenderartCNr = laenderartCNr;
		this.finanzamtIId = finanzamtIId;
		this.mandantCNr = mandantCNr;
	}
	
	public Integer getKontoIId() {
		return this.kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public String getLaenderartCNr() {
		return this.laenderartCNr;
	}

	public void setLaenderartCNr(String laenderartCNr) {
		this.laenderartCNr = laenderartCNr;
	}

	public void setFinanzamtIId(Integer finanzamtIId) {
		this.finanzamtIId = finanzamtIId;
	}

	public Integer getFinanzamtIId() {
		return finanzamtIId;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof KontolaenderartPK)) {
			return false;
		}
		KontolaenderartPK other = (KontolaenderartPK) o;
		return this.kontoIId.equals(other.kontoIId)
				&& this.laenderartCNr.equals(other.laenderartCNr)
				&& this.finanzamtIId.equals(other.finanzamtIId)
				&& this.mandantCNr.equals(other.mandantCNr);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.kontoIId.hashCode();
		hash = hash * prime + this.laenderartCNr.hashCode();
		hash = hash * prime + this.finanzamtIId.hashCode();
		hash = hash * prime + this.mandantCNr.hashCode();
		return hash;
	}

}
