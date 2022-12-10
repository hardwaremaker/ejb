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
package com.lp.server.artikel.service;

import java.io.Serializable;

public class SperrenDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cBez;
	private String mandantCNr;
	private Short bGesperrt;
	private Short bGesperrteinkauf;
	private Short bGesperrtverkauf;
	private Short bGesperrtlos;
	private Short bGesperrtstueckliste;
	private Short bDurchfertigung;

	private Short bDefaultBeiArtikelneuanlage;

	public Short getBDefaultBeiArtikelneuanlage() {
		return bDefaultBeiArtikelneuanlage;
	}

	public void setBDefaultBeiArtikelneuanlage(Short bDefaultBeiArtikelneuanlage) {
		this.bDefaultBeiArtikelneuanlage = bDefaultBeiArtikelneuanlage;
	}

	public Short getBDurchfertigung() {
		return bDurchfertigung;
	}

	public void setBDurchfertigung(Short bDurchfertigung) {
		this.bDurchfertigung = bDurchfertigung;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Short getBGesperrt() {
		return bGesperrt;
	}

	public void setBGesperrt(Short bGesperrt) {
		this.bGesperrt = bGesperrt;
	}

	public Short getBGesperrteinkauf() {
		return bGesperrteinkauf;
	}

	public void setBGesperrteinkauf(Short bGesperrteinkauf) {
		this.bGesperrteinkauf = bGesperrteinkauf;
	}

	public Short getBGesperrtverkauf() {
		return bGesperrtverkauf;
	}

	public void setBGesperrtverkauf(Short bGesperrtverkauf) {
		this.bGesperrtverkauf = bGesperrtverkauf;
	}

	public Short getBGesperrtlos() {
		return bGesperrtlos;
	}

	public void setBGesperrtlos(Short bGesperrtlos) {
		this.bGesperrtlos = bGesperrtlos;
	}

	public Short getBGesperrtstueckliste() {
		return bGesperrtstueckliste;
	}

	public void setBGesperrtstueckliste(Short bGesperrtstueckliste) {
		this.bGesperrtstueckliste = bGesperrtstueckliste;
	}

	private byte[] oBild;

	public byte[] getOBild() {
		return this.oBild;
	}

	public void setOBild(byte[] oBild) {
		this.oBild = oBild;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SperrenDto))
			return false;
		SperrenDto that = (SperrenDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.cBez == null ? this.cBez == null : that.cBez.equals(this.cBez)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null : that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.bGesperrt == null ? this.bGesperrt == null : that.bGesperrt.equals(this.bGesperrt)))
			return false;
		if (!(that.bGesperrteinkauf == null ? this.bGesperrteinkauf == null
				: that.bGesperrteinkauf.equals(this.bGesperrteinkauf)))
			return false;
		if (!(that.bGesperrtverkauf == null ? this.bGesperrtverkauf == null
				: that.bGesperrtverkauf.equals(this.bGesperrtverkauf)))
			return false;
		if (!(that.bGesperrtlos == null ? this.bGesperrtlos == null : that.bGesperrtlos.equals(this.bGesperrtlos)))
			return false;
		if (!(that.bGesperrtstueckliste == null ? this.bGesperrtstueckliste == null
				: that.bGesperrtstueckliste.equals(this.bGesperrtstueckliste)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.bGesperrt.hashCode();
		result = 37 * result + this.bGesperrteinkauf.hashCode();
		result = 37 * result + this.bGesperrtverkauf.hashCode();
		result = 37 * result + this.bGesperrtlos.hashCode();
		result = 37 * result + this.bGesperrtstueckliste.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(256);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("cBez:").append(cBez);
		returnStringBuffer.append("mandantCNr:").append(mandantCNr);
		returnStringBuffer.append("bGesperrt:").append(bGesperrt);
		returnStringBuffer.append("bGesperrteinkauf:").append(bGesperrteinkauf);
		returnStringBuffer.append("bGesperrtverkauf:").append(bGesperrtverkauf);
		returnStringBuffer.append("bGesperrtlos:").append(bGesperrtlos);
		returnStringBuffer.append("bGesperrtstueckliste:").append(bGesperrtstueckliste);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}
