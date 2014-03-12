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
import java.sql.Timestamp;

public class DiaetentagessatzDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private Integer diaetenIId;
	private Timestamp tGueltigab;
	private Integer iAbstunden;
	private Short bStundenweise;
	private BigDecimal nStundensatz;
	private BigDecimal nTagessatz;
	private BigDecimal nMindestsatz;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getDiaetenIId() {
		return diaetenIId;
	}

	public void setDiaetenIId(Integer diaetenIId) {
		this.diaetenIId = diaetenIId;
	}

	public Timestamp getTGueltigab() {
		return tGueltigab;
	}

	public void setTGueltigab(Timestamp tGueltigab) {
		this.tGueltigab = tGueltigab;
	}

	public Integer getIAbstunden() {
		return iAbstunden;
	}

	public void setIAbstunden(Integer iAbstunden) {
		this.iAbstunden = iAbstunden;
	}

	public Short getBStundenweise() {
		return bStundenweise;
	}

	public void setBStundenweise(Short bStundenweise) {
		this.bStundenweise = bStundenweise;
	}

	public BigDecimal getNStundensatz() {
		return nStundensatz;
	}

	public void setNStundensatz(BigDecimal nStundensatz) {
		this.nStundensatz = nStundensatz;
	}

	public BigDecimal getNTagessatz() {
		return nTagessatz;
	}

	public void setNTagessatz(BigDecimal nTagessatz) {
		this.nTagessatz = nTagessatz;
	}

	public BigDecimal getNMindestsatz() {
		return nMindestsatz;
	}

	public void setNMindestsatz(BigDecimal nMindestsatz) {
		this.nMindestsatz = nMindestsatz;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof DiaetentagessatzDto))
			return false;
		DiaetentagessatzDto that = (DiaetentagessatzDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.diaetenIId == null ? this.diaetenIId == null
				: that.diaetenIId.equals(this.diaetenIId)))
			return false;
		if (!(that.tGueltigab == null ? this.tGueltigab == null
				: that.tGueltigab.equals(this.tGueltigab)))
			return false;
		if (!(that.iAbstunden == null ? this.iAbstunden == null
				: that.iAbstunden.equals(this.iAbstunden)))
			return false;
		if (!(that.bStundenweise == null ? this.bStundenweise == null
				: that.bStundenweise.equals(this.bStundenweise)))
			return false;
		if (!(that.nStundensatz == null ? this.nStundensatz == null
				: that.nStundensatz.equals(this.nStundensatz)))
			return false;
		if (!(that.nTagessatz == null ? this.nTagessatz == null
				: that.nTagessatz.equals(this.nTagessatz)))
			return false;
		if (!(that.nMindestsatz == null ? this.nMindestsatz == null
				: that.nMindestsatz.equals(this.nMindestsatz)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.diaetenIId.hashCode();
		result = 37 * result + this.tGueltigab.hashCode();
		result = 37 * result + this.iAbstunden.hashCode();
		result = 37 * result + this.bStundenweise.hashCode();
		result = 37 * result + this.nStundensatz.hashCode();
		result = 37 * result + this.nTagessatz.hashCode();
		result = 37 * result + this.nMindestsatz.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(256);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("diaetenIId:").append(diaetenIId);
		returnStringBuffer.append("tGueltigab:").append(tGueltigab);
		returnStringBuffer.append("iAbstunden:").append(iAbstunden);
		returnStringBuffer.append("bStundenweise:").append(bStundenweise);
		returnStringBuffer.append("nStundensatz:").append(nStundensatz);
		returnStringBuffer.append("nTagessatz:").append(nTagessatz);
		returnStringBuffer.append("nMindestsatz:").append(nMindestsatz);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}
