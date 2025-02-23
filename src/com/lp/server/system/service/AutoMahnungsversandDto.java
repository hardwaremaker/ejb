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
package com.lp.server.system.service;

import java.io.Serializable;

public class AutoMahnungsversandDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private Integer mahnlaufIId;
	private String cDrucker;
	private String cVersandart;

	private String cEmailZusaetzlich;

	public String getCEmailZusaetzlich() {
		return cEmailZusaetzlich;
	}

	public void setCEmailZusaetzlich(String cEmailZusaetzlich) {
		this.cEmailZusaetzlich = cEmailZusaetzlich;
	}
	
	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getMahnlaufIId() {
		return mahnlaufIId;
	}

	public void setMahnlaufIId(Integer mahnlaufIId) {
		this.mahnlaufIId = mahnlaufIId;
	}

	public String getCDrucker() {
		return cDrucker;
	}

	public void setCDrucker(String cDrucker) {
		this.cDrucker = cDrucker;
	}

	public String getCVersandart() {
		return cVersandart;
	}

	public void setCVersandart(String cVersandart) {
		this.cVersandart = cVersandart;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AutoMahnungsversandDto))
			return false;
		AutoMahnungsversandDto that = (AutoMahnungsversandDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.mahnlaufIId == null ? this.mahnlaufIId == null
				: that.mahnlaufIId.equals(this.mahnlaufIId)))
			return false;
		if (!(that.cDrucker == null ? this.cDrucker == null : that.cDrucker
				.equals(this.cDrucker)))
			return false;
		if (!(that.cVersandart == null ? this.cVersandart == null
				: that.cVersandart.equals(this.cVersandart)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.mahnlaufIId.hashCode();
		result = 37 * result + this.cDrucker.hashCode();
		result = 37 * result + this.cVersandart.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(160);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("mandantCNr:").append(mandantCNr);
		returnStringBuffer.append("mahnlaufIId:").append(mahnlaufIId);
		returnStringBuffer.append("cDrucker:").append(cDrucker);
		returnStringBuffer.append("cVersandart:").append(cVersandart);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}
