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

import com.lp.server.util.IIId;

@HvDtoLogClass(name = HvDtoLogClass.PANELDATEN)
public class PaneldatenDto implements Serializable, IIId {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String panelCNr;
	private Integer panelbeschreibungIId;
	private String cKey;
	private String cDatentypkey;
	private byte[] oInhalt;
	private String xInhalt;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getPanelCNr() {
		return panelCNr;
	}

	public void setPanelCNr(String panelCNr) {
		this.panelCNr = panelCNr;
	}

	public Integer getPanelbeschreibungIId() {
		return panelbeschreibungIId;
	}

	public void setPanelbeschreibungIId(Integer panelbeschreibungIId) {
		this.panelbeschreibungIId = panelbeschreibungIId;
	}

	public String getCKey() {
		return cKey;
	}

	public void setCKey(String cKey) {
		this.cKey = cKey;
	}

	public String getCDatentypkey() {
		return cDatentypkey;
	}

	public void setCDatentypkey(String cDatentypkey) {
		this.cDatentypkey = cDatentypkey;
	}

	public byte[] getOInhalt() {
		return oInhalt;
	}

	public String getXInhalt() {
		return xInhalt;
	}

	public void setOInhalt(byte[] oInhalt) {
		this.oInhalt = oInhalt;
	}

	public void setXInhalt(String xInhalt) {
		this.xInhalt = xInhalt;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PaneldatenDto))
			return false;
		PaneldatenDto that = (PaneldatenDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.panelCNr == null ? this.panelCNr == null : that.panelCNr
				.equals(this.panelCNr)))
			return false;
		if (!(that.panelbeschreibungIId == null ? this.panelbeschreibungIId == null
				: that.panelbeschreibungIId.equals(this.panelbeschreibungIId)))
			return false;
		if (!(that.cKey == null ? this.cKey == null : that.cKey
				.equals(this.cKey)))
			return false;
		if (!(that.cDatentypkey == null ? this.cDatentypkey == null
				: that.cDatentypkey.equals(this.cDatentypkey)))
			return false;
		if (that.oInhalt != this.oInhalt)
			return false;
		if (!(that.xInhalt == null ? this.xInhalt == null : that.xInhalt
				.equals(this.xInhalt)))
			return false;
		
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.panelCNr.hashCode();
		result = 37 * result + this.panelbeschreibungIId.hashCode();
		result = 37 * result + this.cKey.hashCode();
		result = 37 * result + this.cDatentypkey.hashCode();

		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + panelCNr;
		returnString += ", " + panelbeschreibungIId;
		returnString += ", " + cKey;
		returnString += ", " + cDatentypkey;
		returnString += ", " + oInhalt;
		return returnString;
	}

	public Object clone() {
		PaneldatenDto paneldatenDto = new PaneldatenDto();

		paneldatenDto.setCDatentypkey(this.getCDatentypkey());
		paneldatenDto.setCKey(this.getCKey());
		paneldatenDto.setOInhalt(this.getOInhalt());
		paneldatenDto.setPanelbeschreibungIId(this.getPanelbeschreibungIId());
		paneldatenDto.setPanelCNr(this.getPanelCNr());
		paneldatenDto.setXInhalt(this.getXInhalt());
		return paneldatenDto;
	}
}
