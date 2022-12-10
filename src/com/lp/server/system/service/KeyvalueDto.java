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

public class KeyvalueDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String cGruppe;
	private String cKey;
	private String cValue;
	private String cDatentyp;

	public KeyvalueDto() {

	}

	public KeyvalueDto(String cKey, String cValue) {
		this.cKey = cKey;
		
		if(cValue!=null && cValue.length()>=3000){
			cValue=cValue.substring(0, 2999);
		}
		this.cValue = cValue;
		this.cDatentyp="java.lang.String";
	}

	public String getCGruppe() {
		return cGruppe;
	}

	public void setCGruppe(String cGruppe) {
		this.cGruppe = cGruppe;
	}

	public String getCKey() {
		return cKey;
	}

	public void setCKey(String cKey) {
		this.cKey = cKey;
	}

	public String getCValue() {
		return cValue;
	}

	public void setCValue(String cValue) {
		this.cValue = cValue;
	}

	public String getCDatentyp() {
		return cDatentyp;
	}

	public void setCDatentyp(String cDatentyp) {
		this.cDatentyp = cDatentyp;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof KeyvalueDto))
			return false;
		KeyvalueDto that = (KeyvalueDto) obj;
		if (!(that.cGruppe == null ? this.cGruppe == null : that.cGruppe
				.equals(this.cGruppe)))
			return false;
		if (!(that.cKey == null ? this.cKey == null : that.cKey
				.equals(this.cKey)))
			return false;
		if (!(that.cValue == null ? this.cValue == null : that.cValue
				.equals(this.cValue)))
			return false;
		if (!(that.cDatentyp == null ? this.cDatentyp == null : that.cDatentyp
				.equals(this.cDatentyp)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.cGruppe.hashCode();
		result = 37 * result + this.cKey.hashCode();
		result = 37 * result + this.cValue.hashCode();
		result = 37 * result + this.cDatentyp.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += cGruppe;
		returnString += ", " + cKey;
		returnString += ", " + cValue;
		returnString += ", " + cDatentyp;
		return returnString;
	}
}
