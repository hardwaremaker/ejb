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
package com.lp.server.angebotstkl.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.util.Helper;

public class EinkaufsangebotDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private String cNr;
	private Timestamp tBelegdatum;
	private String cProjekt;
	private Integer kundeIId;
	private Integer ansprechpartnerIId;
	private BigDecimal nMenge1;
	private BigDecimal nMenge2;
	private BigDecimal nMenge3;
	private BigDecimal nMenge4;
	private BigDecimal nMenge5;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;
	private Integer personalIIdAendern;
	private Timestamp tAendern;

	private String cKommentar;

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String kommentar) {
		cKommentar = kommentar;
	}
	
	private Timestamp tFertigungstermin;

	public Timestamp getTFertigungstermin() {
		return tFertigungstermin;
	}

	public void setTFertigungstermin(Timestamp tFertigungstermin) {
		this.tFertigungstermin = tFertigungstermin;
	}
	
	private Integer iSortierung;
	
	
	public Integer getISortierung() {
		return iSortierung;
	}

	public void setISortierung(Integer iSortierung) {
		this.iSortierung = iSortierung;
	}
	
	private Integer iOptimierenMenge;
	
	public Integer getIOptimierenMenge() {
		return iOptimierenMenge;
	}

	public void setIOptimierenMenge(Integer iOptimierenMenge) {
		this.iOptimierenMenge = iOptimierenMenge;
	}

	
	private Integer iOptimierenLieferzeit;

	public Integer getIOptimierenLieferzeit() {
		return iOptimierenLieferzeit;
	}

	public void setIOptimierenLieferzeit(Integer iOptimierenLieferzeit) {
		this.iOptimierenLieferzeit = iOptimierenLieferzeit;
	}

	private Short bOptimierenMinmenge;

	public Short getBOptimierenMinmenge() {
		return bOptimierenMinmenge;
	}

	public void setBOptimierenMinmenge(Short bOptimierenMinmenge) {
		this.bOptimierenMinmenge = bOptimierenMinmenge;
	}

	private Short bOptimierenVerpackungseinheit;

	public Short getBOptimierenVerpackungseinheit() {
		return bOptimierenVerpackungseinheit;
	}

	public void setBOptimierenVerpackungseinheit(Short bOptimierenVerpackungseinheit) {
		this.bOptimierenVerpackungseinheit = bOptimierenVerpackungseinheit;
	}

	private Short bKundeExportieren;

	public Short getBKundeExportieren() {
		return bKundeExportieren;
	}

	public void setBKundeExportieren(Short bKundeExportieren) {
		this.bKundeExportieren = bKundeExportieren;
	}

	private Timestamp tLiefertermin;

	public Timestamp getTLiefertermin() {
		return tLiefertermin;
	}

	public void setTLiefertermin(Timestamp tLiefertermin) {
		this.tLiefertermin = tLiefertermin;
	}

	private Integer iAnzahlwebabfragen;

	public Integer getIAnzahlwebabfragen() {
		return iAnzahlwebabfragen;
	}

	public void setIAnzahlwebabfragen(Integer iAnzahlwebabfragen) {
		this.iAnzahlwebabfragen = iAnzahlwebabfragen;
	}

	private Short bRoHs;

	public Short getBRoHs() {
		return this.bRoHs;
	}

	public void setBRoHs(Short bRoHs) {
		this.bRoHs = bRoHs;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Timestamp getTBelegdatum() {
		return tBelegdatum;
	}

	public void setTBelegdatum(Timestamp tBelegdatum) {
		this.tBelegdatum = tBelegdatum;
	}

	public String getCProjekt() {
		return cProjekt;
	}

	public void setCProjekt(String cProjekt) {
		this.cProjekt = cProjekt;
	}

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public BigDecimal getNMenge1() {
		return nMenge1;
	}

	public void setNMenge1(BigDecimal nMenge1) {
		this.nMenge1 = Helper.rundeKaufmaennisch(nMenge1, 4);
	}

	public BigDecimal getNMenge2() {
		return nMenge2;
	}

	public void setNMenge2(BigDecimal nMenge2) {
		this.nMenge2 = Helper.rundeKaufmaennisch(nMenge2, 4);
	}

	public BigDecimal getNMenge3() {
		return nMenge3;
	}

	public void setNMenge3(BigDecimal nMenge3) {
		this.nMenge3 = Helper.rundeKaufmaennisch(nMenge3, 4);
	}

	public BigDecimal getNMenge4() {
		return nMenge4;
	}

	public void setNMenge4(BigDecimal nMenge4) {
		this.nMenge4 = Helper.rundeKaufmaennisch(nMenge4, 4);
	}

	public BigDecimal getNMenge5() {
		return nMenge5;
	}

	public void setNMenge5(BigDecimal nMenge5) {
		this.nMenge5 = Helper.rundeKaufmaennisch(nMenge5, 4);
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

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof EinkaufsangebotDto))
			return false;
		EinkaufsangebotDto that = (EinkaufsangebotDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null : that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		if (!(that.tBelegdatum == null ? this.tBelegdatum == null : that.tBelegdatum.equals(this.tBelegdatum)))
			return false;
		if (!(that.cProjekt == null ? this.cProjekt == null : that.cProjekt.equals(this.cProjekt)))
			return false;
		if (!(that.kundeIId == null ? this.kundeIId == null : that.kundeIId.equals(this.kundeIId)))
			return false;
		if (!(that.ansprechpartnerIId == null ? this.ansprechpartnerIId == null
				: that.ansprechpartnerIId.equals(this.ansprechpartnerIId)))
			return false;
		if (!(that.nMenge1 == null ? this.nMenge1 == null : that.nMenge1.equals(this.nMenge1)))
			return false;
		if (!(that.nMenge2 == null ? this.nMenge2 == null : that.nMenge2.equals(this.nMenge2)))
			return false;
		if (!(that.nMenge3 == null ? this.nMenge3 == null : that.nMenge3.equals(this.nMenge3)))
			return false;
		if (!(that.nMenge4 == null ? this.nMenge4 == null : that.nMenge4.equals(this.nMenge4)))
			return false;
		if (!(that.nMenge5 == null ? this.nMenge5 == null : that.nMenge5.equals(this.nMenge5)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen.equals(this.tAnlegen)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern.equals(this.tAendern)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.tBelegdatum.hashCode();
		result = 37 * result + this.cProjekt.hashCode();
		result = 37 * result + this.kundeIId.hashCode();
		result = 37 * result + this.ansprechpartnerIId.hashCode();
		result = 37 * result + this.nMenge1.hashCode();
		result = 37 * result + this.nMenge2.hashCode();
		result = 37 * result + this.nMenge3.hashCode();
		result = 37 * result + this.nMenge4.hashCode();
		result = 37 * result + this.nMenge5.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(512);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("mandantCNr:").append(mandantCNr);
		returnStringBuffer.append("cNr:").append(cNr);
		returnStringBuffer.append("tBelegdatum:").append(tBelegdatum);
		returnStringBuffer.append("cProjekt:").append(cProjekt);
		returnStringBuffer.append("kundeIId:").append(kundeIId);
		returnStringBuffer.append("ansprechpartnerIId:").append(ansprechpartnerIId);
		returnStringBuffer.append("nMenge1:").append(nMenge1);
		returnStringBuffer.append("nMenge2:").append(nMenge2);
		returnStringBuffer.append("nMenge3:").append(nMenge3);
		returnStringBuffer.append("nMenge4:").append(nMenge4);
		returnStringBuffer.append("nMenge5:").append(nMenge5);
		returnStringBuffer.append("personalIIdAnlegen:").append(personalIIdAnlegen);
		returnStringBuffer.append("tAnlegen:").append(tAnlegen);
		returnStringBuffer.append("personalIIdAendern:").append(personalIIdAendern);
		returnStringBuffer.append("tAendern:").append(tAendern);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}
