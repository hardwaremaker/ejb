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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.lp.server.partner.service.PartnerDto;
import com.lp.server.util.IModificationData;

public class KontoDto implements Serializable, IModificationData {
	private static final long serialVersionUID = -792343803475903366L;

	private Integer iId;
	private String mandantCNr;
	private String cNr;
	private String cBez;
	private Integer kontoIIdWeiterfuehrendUst;
	private String rechenregelCNrWeiterfuehrendUst;
	private Integer kontoIIdWeiterfuehrendBilanz;
	private String rechenregelCNrWeiterfuehrendBilanz;
	private Integer kontoIIdWeiterfuehrendSkonto;
	private String rechenregelCNrWeiterfuehrendSkonto;
	private Integer uvaartIId;
	private Date dGueltigvon;
	private Date dGueltigbis;
	private Integer finanzamtIId;
	private Integer kostenstelleIId;
	private Short bAutomeroeffnungsbuchung;
	private Short bAllgemeinsichtbar;
	private Short bManuellbebuchbar;
	private String kontoartCNr;
	private String kontotypCNr;
	private Integer ergebnisgruppeIId;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private String cLetztesortierung;
	private Integer iLetzteselektiertebuchung;
	private Short bVersteckt;
	private Integer steuerkategorieIId;
	private Integer steuerkategorieIIdReverse;
	private String csortierung;
	private String waehrungCNrDruck;

	private String xBemerkung;

	private Integer iGeschaeftsjahrEB;
	private Timestamp tEBAnlegen;

	private Short bOhneUst;
	
	public String getxBemerkung() {
		return xBemerkung;
	}

	public void setxBemerkung(String xBemerkung) {
		this.xBemerkung = xBemerkung;
	}

	private PartnerDto partnerDto = null;

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public String getKontonrBezeichnung() {
		String s = getCNr();
		if (getCBez() != null) {
			s += " " + getCBez();
		}
		return s;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	private Integer ergebnisgruppeIId_negativ;

	public Integer getErgebnisgruppeIId_negativ() {
		return ergebnisgruppeIId_negativ;
	}

	public void setErgebnisgruppeIId_negativ(Integer ergebnisgruppeIId_negativ) {
		this.ergebnisgruppeIId_negativ = ergebnisgruppeIId_negativ;
	}

	public String getPartnerKurzbezeichnung() {
		String s = "";
		if (partnerDto != null && partnerDto.getCKbez() != null) {
			return partnerDto.getCKbez();
		}
		return s;
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

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Integer getKontoIIdWeiterfuehrendUst() {
		return kontoIIdWeiterfuehrendUst;
	}

	public void setKontoIIdWeiterfuehrendUst(Integer kontoIIdWeiterfuehrendUst) {
		this.kontoIIdWeiterfuehrendUst = kontoIIdWeiterfuehrendUst;
	}

	public String getRechenregelCNrWeiterfuehrendUst() {
		return rechenregelCNrWeiterfuehrendUst;
	}

	public void setRechenregelCNrWeiterfuehrendUst(
			String rechenregelCNrWeiterfuehrendUst) {
		this.rechenregelCNrWeiterfuehrendUst = rechenregelCNrWeiterfuehrendUst;
	}

	public Integer getKontoIIdWeiterfuehrendBilanz() {
		return kontoIIdWeiterfuehrendBilanz;
	}

	public void setKontoIIdWeiterfuehrendBilanz(
			Integer kontoIIdWeiterfuehrendBilanz) {
		this.kontoIIdWeiterfuehrendBilanz = kontoIIdWeiterfuehrendBilanz;
	}

	public String getRechenregelCNrWeiterfuehrendBilanz() {
		return rechenregelCNrWeiterfuehrendBilanz;
	}

	public void setRechenregelCNrWeiterfuehrendBilanz(
			String rechenregelCNrWeiterfuehrendBilanz) {
		this.rechenregelCNrWeiterfuehrendBilanz = rechenregelCNrWeiterfuehrendBilanz;
	}

	public Integer getKontoIIdWeiterfuehrendSkonto() {
		return kontoIIdWeiterfuehrendSkonto;
	}

	public void setKontoIIdWeiterfuehrendSkonto(
			Integer kontoIIdWeiterfuehrendSkonto) {
		this.kontoIIdWeiterfuehrendSkonto = kontoIIdWeiterfuehrendSkonto;
	}

	public String getRechenregelCNrWeiterfuehrendSkonto() {
		return rechenregelCNrWeiterfuehrendSkonto;
	}

	public void setRechenregelCNrWeiterfuehrendSkonto(
			String rechenregelCNrWeiterfuehrendSkonto) {
		this.rechenregelCNrWeiterfuehrendSkonto = rechenregelCNrWeiterfuehrendSkonto;
	}

	public Date getDGueltigvon() {
		return dGueltigvon;
	}

	public void setDGueltigvon(Date dGueltigvon) {
		this.dGueltigvon = dGueltigvon;
	}

	public Date getDGueltigbis() {
		return dGueltigbis;
	}

	public void setDGueltigbis(Date dGueltigbis) {
		this.dGueltigbis = dGueltigbis;
	}

	public Integer getFinanzamtIId() {
		return finanzamtIId;
	}

	public void setFinanzamtIId(Integer finanzamtIId) {
		this.finanzamtIId = finanzamtIId;
	}

	public Integer getKostenstelleIId() {
		return kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public Short getBAutomeroeffnungsbuchung() {
		return bAutomeroeffnungsbuchung;
	}

	public void setBAutomeroeffnungsbuchung(Short bAutomeroeffnungsbuchung) {
		this.bAutomeroeffnungsbuchung = bAutomeroeffnungsbuchung;
	}

	public Short getBAllgemeinsichtbar() {
		return bAllgemeinsichtbar;
	}

	public void setBAllgemeinsichtbar(Short bAllgemeinsichtbar) {
		this.bAllgemeinsichtbar = bAllgemeinsichtbar;
	}

	public Short getBManuellbebuchbar() {
		return bManuellbebuchbar;
	}

	public void setBManuellbebuchbar(Short bManuellbebuchbar) {
		this.bManuellbebuchbar = bManuellbebuchbar;
	}

	public String getKontoartCNr() {
		return kontoartCNr;
	}

	public void setKontoartCNr(String kontoartCNr) {
		this.kontoartCNr = kontoartCNr;
	}

	public String getKontotypCNr() {
		return kontotypCNr;
	}

	public void setKontotypCNr(String kontotypCNr) {
		this.kontotypCNr = kontotypCNr;
	}

	public Integer getErgebnisgruppeIId() {
		return ergebnisgruppeIId;
	}

	public void setErgebnisgruppeIId(Integer ergebnisgruppeIId) {
		this.ergebnisgruppeIId = ergebnisgruppeIId;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public String getCLetztesortierung() {
		return cLetztesortierung;
	}

	public void setCLetztesortierung(String cLetztesortierung) {
		this.cLetztesortierung = cLetztesortierung;
	}

	public Integer getILetzteselektiertebuchung() {
		return iLetzteselektiertebuchung;
	}

	public void setILetzteselektiertebuchung(Integer iLetzteselektiertebuchung) {
		this.iLetzteselektiertebuchung = iLetzteselektiertebuchung;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public String getCsortierung() {
		return csortierung;
	}

	public void setCsortierung(String csortierung) {
		this.csortierung = csortierung;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof KontoDto))
			return false;
		KontoDto that = (KontoDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez)))
			return false;
		if (!(that.kontoIIdWeiterfuehrendUst == null ? this.kontoIIdWeiterfuehrendUst == null
				: that.kontoIIdWeiterfuehrendUst
						.equals(this.kontoIIdWeiterfuehrendUst)))
			return false;
		if (!(that.rechenregelCNrWeiterfuehrendUst == null ? this.rechenregelCNrWeiterfuehrendUst == null
				: that.rechenregelCNrWeiterfuehrendUst
						.equals(this.rechenregelCNrWeiterfuehrendUst)))
			return false;
		if (!(that.kontoIIdWeiterfuehrendBilanz == null ? this.kontoIIdWeiterfuehrendBilanz == null
				: that.kontoIIdWeiterfuehrendBilanz
						.equals(this.kontoIIdWeiterfuehrendBilanz)))
			return false;
		if (!(that.rechenregelCNrWeiterfuehrendBilanz == null ? this.rechenregelCNrWeiterfuehrendBilanz == null
				: that.rechenregelCNrWeiterfuehrendBilanz
						.equals(this.rechenregelCNrWeiterfuehrendBilanz)))
			return false;
		if (!(that.kontoIIdWeiterfuehrendSkonto == null ? this.kontoIIdWeiterfuehrendSkonto == null
				: that.kontoIIdWeiterfuehrendSkonto
						.equals(this.kontoIIdWeiterfuehrendSkonto)))
			return false;
		if (!(that.rechenregelCNrWeiterfuehrendSkonto == null ? this.rechenregelCNrWeiterfuehrendSkonto == null
				: that.rechenregelCNrWeiterfuehrendSkonto
						.equals(this.rechenregelCNrWeiterfuehrendSkonto)))
			return false;
		if (!(that.uvaartIId == null ? this.uvaartIId == null : that.uvaartIId
				.equals(this.uvaartIId)))
			return false;
		if (!(that.dGueltigvon == null ? this.dGueltigvon == null
				: that.dGueltigvon.equals(this.dGueltigvon)))
			return false;
		if (!(that.dGueltigbis == null ? this.dGueltigbis == null
				: that.dGueltigbis.equals(this.dGueltigbis)))
			return false;
		if (!(that.finanzamtIId == null ? this.finanzamtIId == null
				: that.finanzamtIId.equals(this.finanzamtIId)))
			return false;
		if (!(that.kostenstelleIId == null ? this.kostenstelleIId == null
				: that.kostenstelleIId.equals(this.kostenstelleIId)))
			return false;
		if (!(that.bAutomeroeffnungsbuchung == null ? this.bAutomeroeffnungsbuchung == null
				: that.bAutomeroeffnungsbuchung
						.equals(this.bAutomeroeffnungsbuchung)))
			return false;
		if (!(that.bAllgemeinsichtbar == null ? this.bAllgemeinsichtbar == null
				: that.bAllgemeinsichtbar.equals(this.bAllgemeinsichtbar)))
			return false;
		if (!(that.bManuellbebuchbar == null ? this.bManuellbebuchbar == null
				: that.bManuellbebuchbar.equals(this.bManuellbebuchbar)))
			return false;
		if (!(that.kontoartCNr == null ? this.kontoartCNr == null
				: that.kontoartCNr.equals(this.kontoartCNr)))
			return false;
		if (!(that.kontotypCNr == null ? this.kontotypCNr == null
				: that.kontotypCNr.equals(this.kontotypCNr)))
			return false;
		if (!(that.ergebnisgruppeIId == null ? this.ergebnisgruppeIId == null
				: that.ergebnisgruppeIId.equals(this.ergebnisgruppeIId)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.cLetztesortierung == null ? this.cLetztesortierung == null
				: that.cLetztesortierung.equals(this.cLetztesortierung)))
			return false;
		if (!(that.iLetzteselektiertebuchung == null ? this.iLetzteselektiertebuchung == null
				: that.iLetzteselektiertebuchung
						.equals(this.iLetzteselektiertebuchung)))
			return false;
		if (!(that.bVersteckt == null ? this.bVersteckt == null
				: that.bVersteckt.equals(this.bVersteckt)))
			return false;
		if (!(that.steuerkategorieIId == null ? this.steuerkategorieIId == null
				: that.steuerkategorieIId.equals(this.steuerkategorieIId)))
			return false;
		if (!(that.steuerkategorieIIdReverse == null ? this.steuerkategorieIIdReverse == null
				: that.steuerkategorieIIdReverse
						.equals(this.steuerkategorieIIdReverse)))
			return false;
		if (!(that.csortierung == null ? this.csortierung == null
				: that.csortierung.equals(this.csortierung)))
			return false;
		if (!(that.waehrungCNrDruck == null ? this.waehrungCNrDruck == null
				: that.waehrungCNrDruck.equals(this.waehrungCNrDruck)))
			return false;
		if (!(that.bOhneUst == null ? this.bOhneUst == null
				: that.bOhneUst.equals(this.bOhneUst)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.kontoIIdWeiterfuehrendUst.hashCode();
		result = 37 * result + this.rechenregelCNrWeiterfuehrendUst.hashCode();
		result = 37 * result + this.kontoIIdWeiterfuehrendBilanz.hashCode();
		result = 37 * result
				+ this.rechenregelCNrWeiterfuehrendBilanz.hashCode();
		result = 37 * result + this.kontoIIdWeiterfuehrendSkonto.hashCode();
		result = 37 * result
				+ this.rechenregelCNrWeiterfuehrendSkonto.hashCode();
		result = 37 * result + this.uvaartIId.hashCode();
		result = 37 * result + this.dGueltigvon.hashCode();
		result = 37 * result + this.dGueltigbis.hashCode();
		result = 37 * result + this.finanzamtIId.hashCode();
		result = 37 * result + this.kostenstelleIId.hashCode();
		result = 37 * result + this.bAutomeroeffnungsbuchung.hashCode();
		result = 37 * result + this.bAllgemeinsichtbar.hashCode();
		result = 37 * result + this.bManuellbebuchbar.hashCode();
		result = 37 * result + this.kontoartCNr.hashCode();
		result = 37 * result + this.kontotypCNr.hashCode();
		result = 37 * result + this.ergebnisgruppeIId.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.cLetztesortierung.hashCode();
		result = 37 * result + this.iLetzteselektiertebuchung.hashCode();
		result = 37 * result + this.bVersteckt.hashCode();
		result = 37 * result + this.steuerkategorieIId.hashCode();
		result = 37 * result + this.steuerkategorieIIdReverse.hashCode();
		result = 37 * result + this.csortierung.hashCode();
		result = 37 * result + this.waehrungCNrDruck.hashCode();
		result = 37 * result + this.bOhneUst.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + mandantCNr;
		returnString += ", " + cNr;
		returnString += ", " + cBez;
		returnString += ", " + kontoIIdWeiterfuehrendUst;
		returnString += ", " + rechenregelCNrWeiterfuehrendUst;
		returnString += ", " + kontoIIdWeiterfuehrendBilanz;
		returnString += ", " + rechenregelCNrWeiterfuehrendBilanz;
		returnString += ", " + kontoIIdWeiterfuehrendSkonto;
		returnString += ", " + rechenregelCNrWeiterfuehrendSkonto;
		returnString += ", " + uvaartIId;
		returnString += ", " + dGueltigvon;
		returnString += ", " + dGueltigbis;
		returnString += ", " + finanzamtIId;
		returnString += ", " + kostenstelleIId;
		returnString += ", " + bAutomeroeffnungsbuchung;
		returnString += ", " + bAllgemeinsichtbar;
		returnString += ", " + bManuellbebuchbar;
		returnString += ", " + kontoartCNr;
		returnString += ", " + kontotypCNr;
		returnString += ", " + ergebnisgruppeIId;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + cLetztesortierung;
		returnString += ", " + iLetzteselektiertebuchung;
		returnString += ", " + bVersteckt;
		returnString += ", " + steuerkategorieIId;
		returnString += ", " + steuerkategorieIIdReverse;
		returnString += ", " + csortierung;
		returnString += ", " + waehrungCNrDruck;
		returnString += ", " + bOhneUst;
		return returnString;
	}

	public void setSteuerkategorieIId(Integer steuerkategorieIId) {
		this.steuerkategorieIId = steuerkategorieIId;
	}

	public Integer getSteuerkategorieIId() {
		return steuerkategorieIId;
	}

	public void setSteuerkategorieIIdReverse(Integer steuerkategorieIIdReverse) {
		this.steuerkategorieIIdReverse = steuerkategorieIIdReverse;
	}

	public Integer getSteuerkategorieIIdReverse() {
		return steuerkategorieIIdReverse;
	}

	public void setUvaartIId(Integer uvaartIId) {
		this.uvaartIId = uvaartIId;
	}

	public Integer getUvaartIId() {
		return uvaartIId;
	}

	public void setWaehrungCNrDruck(String waehrungCNrDruck) {
		this.waehrungCNrDruck = waehrungCNrDruck;
	}

	public String getWaehrungCNrDruck() {
		return waehrungCNrDruck;
	}

	public Integer getiGeschaeftsjahrEB() {
		return iGeschaeftsjahrEB;
	}

	public void setiGeschaeftsjahrEB(Integer iGeschaeftsjahrEB) {
		this.iGeschaeftsjahrEB = iGeschaeftsjahrEB;
	}

	public Timestamp gettEBAnlegen() {
		return tEBAnlegen;
	}

	public void settEBAnlegen(Timestamp tEBAnlegen) {
		this.tEBAnlegen = tEBAnlegen;
	}

	public Short getBOhneUst() {
		return bOhneUst;
	}

	public void setBOhneUst(Short bOhneUst) {
		this.bOhneUst = bOhneUst;
	}
}
