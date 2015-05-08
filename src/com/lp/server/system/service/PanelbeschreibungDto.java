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

public class PanelbeschreibungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String panelCNr;
	private String cName;
	private String cTyp;
	private String cTokeninresourcebundle;
	private Integer iGridx;
	private Integer iGridy;
	private Integer iGridwidth;
	private String mandantCNr;
	private Integer iGridheigth;
	private String cFill;
	private String cAnchor;
	private Integer iInsetsleft;
	private Integer iInsetsright;
	private Integer iInsetstop;
	private Integer iInsetsbottom;
	private Integer iIpadx;
	private Integer iIpady;
	private Short bMandatory;
	private Double fWeightx;
	private Double fWeighty;
	private Integer artgruIId;
	private String cDruckname;

	private String cDefault;

	public String getCDefault() {
		return cDefault;
	}

	public void setCDefault(String cDefault) {
		this.cDefault = cDefault;
	}

	private Integer partnerklasseIId;

	public Integer getPartnerklasseIId() {
		return partnerklasseIId;
	}

	public void setPartnerklasseIId(Integer partnerklasseIId) {
		this.partnerklasseIId = partnerklasseIId;
	}

	public Integer getArtgruIId() {
		return artgruIId;
	}

	public void setArtgruIId(Integer artgruIId) {
		this.artgruIId = artgruIId;
	}

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

	public String getCName() {
		return cName;
	}

	public void setCName(String cName) {
		this.cName = cName;
	}

	public String getCTyp() {
		return cTyp;
	}

	public void setCTyp(String cTyp) {
		this.cTyp = cTyp;
	}

	public String getCTokeninresourcebundle() {
		return cTokeninresourcebundle;
	}

	public void setCTokeninresourcebundle(String cTokeninresourcebundle) {
		this.cTokeninresourcebundle = cTokeninresourcebundle;
	}

	public Integer getIGridx() {
		return iGridx;
	}

	public void setIGridx(Integer iGridx) {
		this.iGridx = iGridx;
	}

	public Integer getIGridy() {
		return iGridy;
	}

	public void setIGridy(Integer iGridy) {
		this.iGridy = iGridy;
	}

	public Integer getIGridwidth() {
		return iGridwidth;
	}

	public void setIGridwidth(Integer iGridwidth) {
		this.iGridwidth = iGridwidth;
	}

	public Integer getIGridheigth() {
		return iGridheigth;
	}

	public void setIGridheigth(Integer iGridheigth) {
		this.iGridheigth = iGridheigth;
	}

	public String getCFill() {
		return cFill;
	}

	public void setCFill(String cFill) {
		this.cFill = cFill;
	}

	public String getCAnchor() {
		return cAnchor;
	}

	public void setCAnchor(String cAnchor) {
		this.cAnchor = cAnchor;
	}

	public Integer getIInsetsleft() {
		return iInsetsleft;
	}

	public void setIInsetsleft(Integer iInsetsleft) {
		this.iInsetsleft = iInsetsleft;
	}

	public Integer getIInsetsright() {
		return iInsetsright;
	}

	public void setIInsetsright(Integer iInsetsright) {
		this.iInsetsright = iInsetsright;
	}

	public Integer getIInsetstop() {
		return iInsetstop;
	}

	public void setIInsetstop(Integer iInsetstop) {
		this.iInsetstop = iInsetstop;
	}

	public Integer getIInsetsbottom() {
		return iInsetsbottom;
	}

	public void setIInsetsbottom(Integer iInsetsbottom) {
		this.iInsetsbottom = iInsetsbottom;
	}

	public Integer getIIpadx() {
		return iIpadx;
	}

	public void setIIpadx(Integer iIpadx) {
		this.iIpadx = iIpadx;
	}

	public Integer getIIpady() {
		return iIpady;
	}

	public void setIIpady(Integer iIpady) {
		this.iIpady = iIpady;
	}

	public Short getBMandatory() {
		return bMandatory;
	}

	public void setBMandatory(Short bMandatory) {
		this.bMandatory = bMandatory;
	}

	public Double getFWeightx() {
		return fWeightx;
	}

	public void setFWeightx(Double fWeightx) {
		this.fWeightx = fWeightx;
	}

	public Double getFWeighty() {
		return fWeighty;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setFWeighty(Double fWeighty) {
		this.fWeighty = fWeighty;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getCDruckname() {
		return cDruckname;
	}

	public void setCDruckname(String cDruckname) {
		this.cDruckname = cDruckname;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PanelbeschreibungDto))
			return false;
		PanelbeschreibungDto that = (PanelbeschreibungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.panelCNr == null ? this.panelCNr == null : that.panelCNr
				.equals(this.panelCNr)))
			return false;
		if (!(that.cName == null ? this.cName == null : that.cName
				.equals(this.cName)))
			return false;
		if (!(that.cTyp == null ? this.cTyp == null : that.cTyp
				.equals(this.cTyp)))
			return false;
		if (!(that.cTokeninresourcebundle == null ? this.cTokeninresourcebundle == null
				: that.cTokeninresourcebundle
						.equals(this.cTokeninresourcebundle)))
			return false;
		if (!(that.iGridx == null ? this.iGridx == null : that.iGridx
				.equals(this.iGridx)))
			return false;
		if (!(that.iGridy == null ? this.iGridy == null : that.iGridy
				.equals(this.iGridy)))
			return false;
		if (!(that.iGridwidth == null ? this.iGridwidth == null
				: that.iGridwidth.equals(this.iGridwidth)))
			return false;
		if (!(that.iGridheigth == null ? this.iGridheigth == null
				: that.iGridheigth.equals(this.iGridheigth)))
			return false;
		if (!(that.cFill == null ? this.cFill == null : that.cFill
				.equals(this.cFill)))
			return false;
		if (!(that.cAnchor == null ? this.cAnchor == null : that.cAnchor
				.equals(this.cAnchor)))
			return false;
		if (!(that.iInsetsleft == null ? this.iInsetsleft == null
				: that.iInsetsleft.equals(this.iInsetsleft)))
			return false;
		if (!(that.iInsetsright == null ? this.iInsetsright == null
				: that.iInsetsright.equals(this.iInsetsright)))
			return false;
		if (!(that.iInsetstop == null ? this.iInsetstop == null
				: that.iInsetstop.equals(this.iInsetstop)))
			return false;
		if (!(that.iInsetsbottom == null ? this.iInsetsbottom == null
				: that.iInsetsbottom.equals(this.iInsetsbottom)))
			return false;
		if (!(that.iIpadx == null ? this.iIpadx == null : that.iIpadx
				.equals(this.iIpadx)))
			return false;
		if (!(that.iIpady == null ? this.iIpady == null : that.iIpady
				.equals(this.iIpady)))
			return false;
		if (!(that.bMandatory == null ? this.bMandatory == null
				: that.bMandatory.equals(this.bMandatory)))
			return false;
		if (!(that.fWeightx == null ? this.fWeightx == null : that.fWeightx
				.equals(this.fWeightx)))
			return false;
		if (!(that.fWeighty == null ? this.fWeighty == null : that.fWeighty
				.equals(this.fWeighty)))
			return false;
		if (!(that.cDruckname == null ? this.cDruckname == null
				: that.cDruckname.equals(this.cDruckname)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.panelCNr.hashCode();
		result = 37 * result + this.cName.hashCode();
		result = 37 * result + this.cTyp.hashCode();
		result = 37 * result + this.cTokeninresourcebundle.hashCode();
		result = 37 * result + this.iGridx.hashCode();
		result = 37 * result + this.iGridy.hashCode();
		result = 37 * result + this.iGridwidth.hashCode();
		result = 37 * result + this.iGridheigth.hashCode();
		result = 37 * result + this.cFill.hashCode();
		result = 37 * result + this.cAnchor.hashCode();
		result = 37 * result + this.iInsetsleft.hashCode();
		result = 37 * result + this.iInsetsright.hashCode();
		result = 37 * result + this.iInsetstop.hashCode();
		result = 37 * result + this.iInsetsbottom.hashCode();
		result = 37 * result + this.iIpadx.hashCode();
		result = 37 * result + this.iIpady.hashCode();
		result = 37 * result + this.bMandatory.hashCode();
		result = 37 * result + this.fWeightx.hashCode();
		result = 37 * result + this.fWeighty.hashCode();
		result = 37 * result + this.cDruckname.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + panelCNr;
		returnString += ", " + cName;
		returnString += ", " + cTyp;
		returnString += ", " + cTokeninresourcebundle;
		returnString += ", " + iGridx;
		returnString += ", " + iGridy;
		returnString += ", " + iGridwidth;
		returnString += ", " + iGridheigth;
		returnString += ", " + cFill;
		returnString += ", " + cAnchor;
		returnString += ", " + iInsetsleft;
		returnString += ", " + iInsetsright;
		returnString += ", " + iInsetstop;
		returnString += ", " + iInsetsbottom;
		returnString += ", " + iIpadx;
		returnString += ", " + iIpady;
		returnString += ", " + bMandatory;
		returnString += ", " + fWeightx;
		returnString += ", " + fWeighty;
		returnString += ", " + cDruckname;
		return returnString;
	}
}
