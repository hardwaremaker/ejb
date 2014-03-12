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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;

public class KollektivDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private String cBez;
	private Short bVerbraucheuestd;

	public Short getBUestdverteilen() {
		return bUestdverteilen;
	}

	public void setBUestdverteilen(Short uestdverteilen) {
		bUestdverteilen = uestdverteilen;
	}

	private Short bUestdverteilen;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return cBez;
	}

	private BigDecimal nNormalstunden;
	private Time uBlockzeitab;
	private Time uBlockzeitbis;
	private BigDecimal nFaktoruestd50;
	private BigDecimal nFaktoruestd100;
	private BigDecimal nFaktormehrstd;
	private Short bUestdabsollstderbracht;

	public BigDecimal getNFaktoruestd200() {
		return nFaktoruestd200;
	}

	public void setNFaktoruestd200(BigDecimal faktoruestd200) {
		nFaktoruestd200 = faktoruestd200;
	}

	public BigDecimal getN200prozentigeab() {
		return n200prozentigeab;
	}

	public void setN200prozentigeab(BigDecimal n200prozentigeab) {
		this.n200prozentigeab = n200prozentigeab;
	}

	private BigDecimal nFaktoruestd200;
	private BigDecimal n200prozentigeab;

	public Short getBUestdabsollstderbracht() {
		return bUestdabsollstderbracht;
	}

	public void setBUestdabsollstderbracht(Short uestdabsollstderbracht) {
		bUestdabsollstderbracht = uestdabsollstderbracht;
	}

	public BigDecimal getNFaktormehrstd() {
		return nFaktormehrstd;
	}

	public void setNFaktormehrstd(BigDecimal faktormehrstd) {
		nFaktormehrstd = faktormehrstd;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Short getBVerbraucheuestd() {
		return bVerbraucheuestd;
	}

	public BigDecimal getNNormalstunden() {
		return nNormalstunden;
	}

	public Time getUBlockzeitbis() {
		return uBlockzeitbis;
	}

	public Time getUBlockzeitab() {
		return uBlockzeitab;
	}

	public BigDecimal getNFaktoruestd50() {
		return nFaktoruestd50;
	}

	public BigDecimal getNFaktoruestd100() {
		return nFaktoruestd100;
	}

	public void setBVerbraucheuestd(Short bVerbraucheuestd) {
		this.bVerbraucheuestd = bVerbraucheuestd;
	}

	public void setNNormalstunden(BigDecimal nNormalstunden) {
		this.nNormalstunden = nNormalstunden;
	}

	public void setUBlockzeitbis(Time uBlockzeitbis) {
		this.uBlockzeitbis = uBlockzeitbis;
	}

	public void setUBlockzeitab(Time uBlockzeitab) {
		this.uBlockzeitab = uBlockzeitab;
	}

	public void setNFaktoruestd50(BigDecimal nFaktoruestd50) {
		this.nFaktoruestd50 = nFaktoruestd50;
	}

	public void setNFaktoruestd100(BigDecimal nFaktoruestd100) {
		this.nFaktoruestd100 = nFaktoruestd100;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof KollektivDto)) {
			return false;
		}
		KollektivDto that = (KollektivDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez))) {
			return false;
		}
		if (!(that.bVerbraucheuestd == null ? this.bVerbraucheuestd == null
				: that.bVerbraucheuestd.equals(this.bVerbraucheuestd))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.bVerbraucheuestd.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cBez;
		returnString += ", " + bVerbraucheuestd;
		return returnString;
	}
}
