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
package com.lp.server.partner.service;

import java.io.Serializable;

public class SelektionDto implements Serializable {
	private static final long serialVersionUID = -9027411413774311132L;

	private Integer iId;
	private String cNr;
	private String mandantCNr;
	private Short bWebshop ;
	
	private SelektionsprDto selektionSprDto = null;

	public Integer getIId() {
		return iId;
	}

	public String getBezeichnung() {
		if (getSelektionsprDto() != null) {
			if (getSelektionsprDto().getCBez() != null) {
				return getSelektionsprDto().getCBez();
			} else {
				return getCNr().trim();
			}
		} else {
			return getCNr().trim();
		}
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public SelektionsprDto getSelektionsprDto() {
		return selektionSprDto;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public void setSelektionsprDto(SelektionsprDto selektionSprDto) {
		this.selektionSprDto = selektionSprDto;
	}

	public Short getbWebshop() {
		return bWebshop;
	}

	public void setbWebshop(Short bWebshop) {
		this.bWebshop = bWebshop;
	}

	public boolean isWebshop() {
		if(null == bWebshop) return false ;
		return 1 == bWebshop ;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SelektionDto))
			return false;
		SelektionDto that = (SelektionDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.bWebshop == null ? this.bWebshop == null : that.bWebshop.equals(this.bWebshop)))
			return false;
		
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.bWebshop.hashCode() ;
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cNr;
		returnString += ", " + mandantCNr;
		returnString += ", " + bWebshop ;
		return returnString;
	}

}
