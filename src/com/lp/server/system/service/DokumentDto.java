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
package com.lp.server.system.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;

public class DokumentDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private byte[] oInhalt;
	private String datenformatCNr;
	private String cDateiname;
	private String cBez;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;

	public Integer getIId() {
		return iId;
	}

	private DokumentschlagwortDto[] dokumentschlagwortDto;

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public byte[] getOInhalt() {
		return oInhalt;
	}

	public void setOInhalt(byte[] oInhalt) {
		this.oInhalt = oInhalt;
	}

	public String getDatenformatCNr() {
		return datenformatCNr;
	}

	public void setDatenformatCNr(String datenformatCNr) {
		this.datenformatCNr = datenformatCNr;
	}

	public String getCDateiname() {
		return cDateiname;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public DokumentschlagwortDto[] getDokumentschlagwortDto() {
		return dokumentschlagwortDto;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public void setDokumentschlagwortDto(
			DokumentschlagwortDto[] dokumentschlagwortDto) {
		this.dokumentschlagwortDto = dokumentschlagwortDto;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof DokumentDto))
			return false;
		DokumentDto that = (DokumentDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.oInhalt == null ? this.oInhalt == null : 
			Arrays.equals(that.oInhalt, this.oInhalt)))
			return false;
		if (!(that.datenformatCNr == null ? this.datenformatCNr == null
				: that.datenformatCNr.equals(this.datenformatCNr)))
			return false;
		if (!(that.cDateiname == null ? this.cDateiname == null
				: that.cDateiname.equals(this.cDateiname)))
			return false;
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.oInhalt.hashCode();
		result = 37 * result + this.datenformatCNr.hashCode();
		result = 37 * result + this.cDateiname.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(224);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("oInhalt:").append(oInhalt);
		returnStringBuffer.append("datenformatCNr:").append(datenformatCNr);
		returnStringBuffer.append("cDateiname:").append(cDateiname);
		returnStringBuffer.append("cBez:").append(cBez);
		returnStringBuffer.append("personalIIdAnlegen:").append(
				personalIIdAnlegen);
		returnStringBuffer.append("tAnlegen:").append(tAnlegen);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}
