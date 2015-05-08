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
import java.util.Arrays;

public class VersandanhangDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer versandauftragIId;
	private String cDateiname;
	private byte[] oInhalt;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getVersandauftragIId() {
		return versandauftragIId;
	}

	public void setVersandauftragIId(Integer versandauftragIId) {
		this.versandauftragIId = versandauftragIId;
	}

	public String getCDateiname() {
		return cDateiname;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
	}

	public byte[] getOInhalt() {
		return oInhalt;
	}

	public void setOInhalt(byte[] oInhalt) {
		this.oInhalt = oInhalt;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof VersandanhangDto))
			return false;
		VersandanhangDto that = (VersandanhangDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.versandauftragIId == null ? this.versandauftragIId == null
				: that.versandauftragIId.equals(this.versandauftragIId)))
			return false;
		if (!(that.cDateiname == null ? this.cDateiname == null
				: that.cDateiname.equals(this.cDateiname)))
			return false;
		if (!(that.oInhalt == null ? this.oInhalt == null : 
			Arrays.equals(that.oInhalt, this.oInhalt)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.versandauftragIId.hashCode();
		result = 37 * result + this.cDateiname.hashCode();
		result = 37 * result + Arrays.hashCode(oInhalt);
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(128);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("versandauftragIId:").append(
				versandauftragIId);
		returnStringBuffer.append("cDateiname:").append(cDateiname);
		returnStringBuffer.append("oInhalt:").append(oInhalt);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}
