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
import java.sql.Timestamp;
import java.util.Locale;

import com.lp.util.Helper;

public class TheClientDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idUser = null;
	private String mandant = null;
	private Integer idPersonal = null;
	private Locale locUi = null;
	private Locale locKonzern = null;
	private Locale locMandant = null;
	private String benutzername = null;
	private Timestamp tsLoggedin = null;
	private Timestamp tsLoggedout = null;
	private char aKennwort[] = null;
	private String sMandantenwaehrung = null;
	private Integer iStatus = null;
	private Integer reportvarianteIId = null;
	private Integer systemrolleIId = null;

	public Integer getSystemrolleIId() {
		return systemrolleIId;
	}

	public void setSystemrolleIId(Integer systemrolleIId) {
		this.systemrolleIId = systemrolleIId;
	}

	private Integer geschaeftsJahr = null ;
	
	public String getIDUser() {
		return idUser;
	}

	public void setIDUser(String idUserI) {
		this.idUser = idUserI;
	}

	public String getMandant() {
		return mandant;
	}

	public void setMandant(String mandantI) {
		this.mandant = mandantI;
	}

	public Integer getReportvarianteIId() {
		return reportvarianteIId;
	}

	public void setReportvarianteIId(Integer reportvarianteIId) {
		this.reportvarianteIId = reportvarianteIId;
	}

	public Integer getIDPersonal() {
		return idPersonal;
	}

	public void setIDPersonal(Integer idPersonalI) {
		this.idPersonal = idPersonalI;
	}

	public Locale getLocUi() {
		return locUi;
	}

	public void setUiLoc(Locale uILocaleI) {
		this.locUi = uILocaleI;
	}

	public String getLocUiAsString() {
		return Helper.locale2String(locUi);
	}

	public Locale getLocKonzern() {
		return locKonzern;
	}

	public String getLocKonzernAsString() {
		return Helper.locale2String(locKonzern);
	}

	public void setLocKonzern(Locale locKonzernI) {
		this.locKonzern = locKonzernI;
	}

	public String getBenutzername() {
		return benutzername;
	}

	public void setBenutzername(String benutzernameI) {
		this.benutzername = benutzernameI;
	}

	public Timestamp getDLoggedin() {
		return tsLoggedin;
	}

	public void setTsLoggedin(Timestamp tsLoggedinI) {
		this.tsLoggedin = tsLoggedinI;
	}

	public Timestamp getTsLoggedout() {
		return tsLoggedout;
	}

	public void setTsLoggedout(Timestamp tsLoggedoutI) {
		this.tsLoggedout = tsLoggedoutI;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TheClientDto)) {
			return false;
		}
		TheClientDto that = (TheClientDto) obj;
		if (!(that.idUser == null ? this.idUser == null : that.idUser
				.equals(this.idUser))) {
			return false;
		}
		if (!(that.mandant == null ? this.mandant == null : that.mandant
				.equals(this.mandant))) {
			return false;
		}
		if (!(that.idPersonal == null ? this.idPersonal == null
				: that.idPersonal.equals(this.idPersonal))) {
			return false;
		}
		if (!(that.locUi == null ? this.locUi == null : that.locUi
				.equals(this.locUi))) {
			return false;
		}
		if (!(that.locKonzern == null ? this.locKonzern == null
				: that.locKonzern.equals(this.locKonzern))) {
			return false;
		}
		if (!(that.locMandant == null ? this.locMandant == null
				: that.locMandant.equals(this.locMandant))) {
			return false;
		}
		if (!(that.benutzername == null ? this.benutzername == null
				: that.benutzername.equals(this.benutzername))) {
			return false;
		}
		if (!(that.tsLoggedin == null ? this.tsLoggedin == null
				: that.tsLoggedin.equals(this.tsLoggedin))) {
			return false;
		}
		if (!(that.tsLoggedout == null ? this.tsLoggedout == null
				: that.tsLoggedout.equals(this.tsLoggedout))) {
			return false;
		}
//		if (!(that.aKennwort == null ? this.aKennwort == null : that.aKennwort
//				.equals(this.aKennwort))) {
//			return false;
//		}
		if (!(that.aKennwort == null ? this.aKennwort == null : java.util.Arrays.equals(that.aKennwort, this.aKennwort))) {
			return false;
		}

		if (!(that.geschaeftsJahr == null ? this.geschaeftsJahr == null
				: that.geschaeftsJahr.equals(this.geschaeftsJahr))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.idUser.hashCode();
		result = 37 * result + this.mandant.hashCode();
		result = 37 * result + this.idPersonal.hashCode();
		result = 37 * result + this.locUi.hashCode();
		result = 37 * result + this.locKonzern.hashCode();
		result = 37 * result + this.locMandant.hashCode();
		result = 37 * result + this.benutzername.hashCode();
		result = 37 * result + this.tsLoggedin.hashCode();
		result = 37 * result + this.tsLoggedout.hashCode();
		result = 37 * result + java.util.Arrays.hashCode(this.aKennwort);
		result = 37 * result + this.geschaeftsJahr.hashCode() ;
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += idUser;
		returnString += ", " + mandant;
		returnString += ", " + idPersonal;
		returnString += ", " + locUi;
		returnString += ", " + locKonzern;
		returnString += ", " + locMandant;
		returnString += ", " + benutzername;
		returnString += ", " + tsLoggedin;
		returnString += ", " + tsLoggedout;
		returnString += ", " + aKennwort.toString() ;
		return returnString;
	}

	public char[] getKennwortAsCharArray() {
		return aKennwort;
	}

	public String getKennwortAsString() {
		return String.valueOf(aKennwort);
	}

	public void setKennwort(char[] aKennwortI) {
		this.aKennwort = aKennwortI;
	}

	public String getLocMandantAsString() {
		return Helper.locale2String(this.locMandant);
	}

	public void setLocMandant(Locale locMandantI) {
		this.locMandant = locMandantI;
	}

	public void setSMandantenwaehrung(String sMandantenwaehrung) {
		this.sMandantenwaehrung = sMandantenwaehrung;
	}

	public void setIStatus(Integer iStatus) {
		this.iStatus = iStatus;
	}

	public Locale getLocMandant() {
		return this.locMandant;
	}

	public String getSMandantenwaehrung() {
		return sMandantenwaehrung;
	}

	public Integer getIStatus() {
		return iStatus;
	}

	public Integer getGeschaeftsJahr() {
		return geschaeftsJahr;
	}

	public void setGeschaeftsJahr(Integer geschaeftsJahr) {
		this.geschaeftsJahr = geschaeftsJahr;
	}

}
