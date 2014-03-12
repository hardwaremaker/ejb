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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class ArtikelartsprDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String localeCNr;
	private String artikelartCNr;
	private String cBez;
	private Timestamp tAnlegen;
	private Integer iAnlegen;
	private Timestamp tAendern;
	private Integer iAendern;

	public String getLocaleCNr() {
		return localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	public String getArtikelartCNr() {
		return artikelartCNr;
	}

	public void setArtikelartCNr(String artikelartCNr) {
		this.artikelartCNr = artikelartCNr;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getIAnlegen() {
		return iAnlegen;
	}

	public void setIAnlegen(Integer iAnlegen) {
		this.iAnlegen = iAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getIAendern() {
		return iAendern;
	}

	public void setIAendern(Integer iAendern) {
		this.iAendern = iAendern;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ArtikelartsprDto)) {
			return false;
		}
		ArtikelartsprDto that = (ArtikelartsprDto) obj;
		if (!(that.localeCNr == null ? this.localeCNr == null : that.localeCNr
				.equals(this.localeCNr))) {
			return false;
		}
		if (!(that.artikelartCNr == null ? this.artikelartCNr == null
				: that.artikelartCNr.equals(this.artikelartCNr))) {
			return false;
		}
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.localeCNr.hashCode();
		result = 37 * result + this.artikelartCNr.hashCode();
		result = 37 * result + this.cBez.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += localeCNr;
		returnString += ", " + artikelartCNr;
		returnString += ", " + cBez;
		return returnString;
	}
}
