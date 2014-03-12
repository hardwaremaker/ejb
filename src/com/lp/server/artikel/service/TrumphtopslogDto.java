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
import java.math.BigDecimal;
import java.sql.Timestamp;

public class TrumphtopslogDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cImportfilename;
	private String cError = "";
	private Integer artikelIId;
	private Integer artikelIIdMaterial;
	private BigDecimal nGewicht;
	private BigDecimal nGestpreisneu;
	private Integer iBearbeitungszeit;
	private Timestamp tAnlegen;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCImportfilename() {
		return cImportfilename;
	}

	public void setCImportfilename(String cImportfilename) {
		this.cImportfilename = cImportfilename;
	}

	public String getCError() {
		return cError;
	}

	public void setCError(String cError) {
		if (cError != null && cError.length() > 79) {
			this.cError = cError.substring(0, 79);
		} else {
			this.cError = cError;
		}
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getArtikelIIdMaterial() {
		return artikelIIdMaterial;
	}

	public void setArtikelIIdMaterial(Integer artikelIIdMaterial) {
		this.artikelIIdMaterial = artikelIIdMaterial;
	}

	public BigDecimal getNGewicht() {
		return nGewicht;
	}

	public void setNGewicht(BigDecimal nGewicht) {
		this.nGewicht = nGewicht;
	}

	public BigDecimal getNGestpreisneu() {
		return nGestpreisneu;
	}

	public void setNGestpreisneu(BigDecimal nGestpreisneu) {
		this.nGestpreisneu = nGestpreisneu;
	}

	public Integer getIBearbeitungszeit() {
		return iBearbeitungszeit;
	}

	public void setIBearbeitungszeit(Integer iBearbeitungszeit) {
		this.iBearbeitungszeit = iBearbeitungszeit;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof TrumphtopslogDto))
			return false;
		TrumphtopslogDto that = (TrumphtopslogDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.cImportfilename == null ? this.cImportfilename == null
				: that.cImportfilename.equals(this.cImportfilename)))
			return false;
		if (!(that.cError == null ? this.cError == null : that.cError
				.equals(this.cError)))
			return false;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId)))
			return false;
		if (!(that.artikelIIdMaterial == null ? this.artikelIIdMaterial == null
				: that.artikelIIdMaterial.equals(this.artikelIIdMaterial)))
			return false;
		if (!(that.nGewicht == null ? this.nGewicht == null : that.nGewicht
				.equals(this.nGewicht)))
			return false;
		if (!(that.nGestpreisneu == null ? this.nGestpreisneu == null
				: that.nGestpreisneu.equals(this.nGestpreisneu)))
			return false;
		if (!(that.iBearbeitungszeit == null ? this.iBearbeitungszeit == null
				: that.iBearbeitungszeit.equals(this.iBearbeitungszeit)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cImportfilename.hashCode();
		result = 37 * result + this.cError.hashCode();
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.artikelIIdMaterial.hashCode();
		result = 37 * result + this.nGewicht.hashCode();
		result = 37 * result + this.nGestpreisneu.hashCode();
		result = 37 * result + this.iBearbeitungszeit.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(288);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("cImportfilename:").append(cImportfilename);
		returnStringBuffer.append("cError:").append(cError);
		returnStringBuffer.append("artikelIId:").append(artikelIId);
		returnStringBuffer.append("artikelIIdMaterial:").append(
				artikelIIdMaterial);
		returnStringBuffer.append("nGewicht:").append(nGewicht);
		returnStringBuffer.append("nGestpreisneu:").append(nGestpreisneu);
		returnStringBuffer.append("iBearbeitungszeit:").append(
				iBearbeitungszeit);
		returnStringBuffer.append("tAnlegen:").append(tAnlegen);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}
