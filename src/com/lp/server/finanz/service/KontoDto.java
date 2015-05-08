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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.lp.server.artikel.ejb.Artkla;
import com.lp.server.artikel.ejb.Material;
import com.lp.server.finanz.ejb.Ergebnisgruppe;
import com.lp.server.finanz.ejb.Uvaart;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCBez;
import com.lp.server.system.service.HvDtoLogIdCnr;
import com.lp.server.util.IIId;
import com.lp.server.util.IModificationData;

@HvDtoLogClass(name = HvDtoLogClass.KONTO)
public class KontoDto implements Serializable, IIId, IModificationData {
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
	private String cSteuerart;

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

	@HvDtoLogIdCBez(entityClass = Ergebnisgruppe.class)
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

	@HvDtoLogIdCBez(entityClass = Ergebnisgruppe.class)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((bAllgemeinsichtbar == null) ? 0 : bAllgemeinsichtbar
						.hashCode());
		result = prime
				* result
				+ ((bAutomeroeffnungsbuchung == null) ? 0
						: bAutomeroeffnungsbuchung.hashCode());
		result = prime
				* result
				+ ((bManuellbebuchbar == null) ? 0 : bManuellbebuchbar
						.hashCode());
		result = prime * result
				+ ((bOhneUst == null) ? 0 : bOhneUst.hashCode());
		result = prime * result
				+ ((bVersteckt == null) ? 0 : bVersteckt.hashCode());
		result = prime * result + ((cBez == null) ? 0 : cBez.hashCode());
		result = prime
				* result
				+ ((cLetztesortierung == null) ? 0 : cLetztesortierung
						.hashCode());
		result = prime * result + ((cNr == null) ? 0 : cNr.hashCode());
		result = prime * result
				+ ((cSteuerart == null) ? 0 : cSteuerart.hashCode());
		result = prime * result
				+ ((csortierung == null) ? 0 : csortierung.hashCode());
		result = prime * result
				+ ((dGueltigbis == null) ? 0 : dGueltigbis.hashCode());
		result = prime * result
				+ ((dGueltigvon == null) ? 0 : dGueltigvon.hashCode());
		result = prime
				* result
				+ ((ergebnisgruppeIId == null) ? 0 : ergebnisgruppeIId
						.hashCode());
		result = prime
				* result
				+ ((ergebnisgruppeIId_negativ == null) ? 0
						: ergebnisgruppeIId_negativ.hashCode());
		result = prime * result
				+ ((finanzamtIId == null) ? 0 : finanzamtIId.hashCode());
		result = prime
				* result
				+ ((iGeschaeftsjahrEB == null) ? 0 : iGeschaeftsjahrEB
						.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime
				* result
				+ ((iLetzteselektiertebuchung == null) ? 0
						: iLetzteselektiertebuchung.hashCode());
		result = prime
				* result
				+ ((kontoIIdWeiterfuehrendBilanz == null) ? 0
						: kontoIIdWeiterfuehrendBilanz.hashCode());
		result = prime
				* result
				+ ((kontoIIdWeiterfuehrendSkonto == null) ? 0
						: kontoIIdWeiterfuehrendSkonto.hashCode());
		result = prime
				* result
				+ ((kontoIIdWeiterfuehrendUst == null) ? 0
						: kontoIIdWeiterfuehrendUst.hashCode());
		result = prime * result
				+ ((kontoartCNr == null) ? 0 : kontoartCNr.hashCode());
		result = prime * result
				+ ((kontotypCNr == null) ? 0 : kontotypCNr.hashCode());
		result = prime * result
				+ ((kostenstelleIId == null) ? 0 : kostenstelleIId.hashCode());
		result = prime * result
				+ ((mandantCNr == null) ? 0 : mandantCNr.hashCode());
		result = prime * result
				+ ((partnerDto == null) ? 0 : partnerDto.hashCode());
		result = prime
				* result
				+ ((personalIIdAendern == null) ? 0 : personalIIdAendern
						.hashCode());
		result = prime
				* result
				+ ((personalIIdAnlegen == null) ? 0 : personalIIdAnlegen
						.hashCode());
		result = prime
				* result
				+ ((rechenregelCNrWeiterfuehrendBilanz == null) ? 0
						: rechenregelCNrWeiterfuehrendBilanz.hashCode());
		result = prime
				* result
				+ ((rechenregelCNrWeiterfuehrendSkonto == null) ? 0
						: rechenregelCNrWeiterfuehrendSkonto.hashCode());
		result = prime
				* result
				+ ((rechenregelCNrWeiterfuehrendUst == null) ? 0
						: rechenregelCNrWeiterfuehrendUst.hashCode());
		result = prime
				* result
				+ ((steuerkategorieIId == null) ? 0 : steuerkategorieIId
						.hashCode());
		result = prime
				* result
				+ ((steuerkategorieIIdReverse == null) ? 0
						: steuerkategorieIIdReverse.hashCode());
		result = prime * result
				+ ((tAendern == null) ? 0 : tAendern.hashCode());
		result = prime * result
				+ ((tAnlegen == null) ? 0 : tAnlegen.hashCode());
		result = prime * result
				+ ((tEBAnlegen == null) ? 0 : tEBAnlegen.hashCode());
		result = prime * result
				+ ((uvaartIId == null) ? 0 : uvaartIId.hashCode());
		result = prime
				* result
				+ ((waehrungCNrDruck == null) ? 0 : waehrungCNrDruck.hashCode());
		result = prime * result
				+ ((xBemerkung == null) ? 0 : xBemerkung.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KontoDto other = (KontoDto) obj;
		if (bAllgemeinsichtbar == null) {
			if (other.bAllgemeinsichtbar != null)
				return false;
		} else if (!bAllgemeinsichtbar.equals(other.bAllgemeinsichtbar))
			return false;
		if (bAutomeroeffnungsbuchung == null) {
			if (other.bAutomeroeffnungsbuchung != null)
				return false;
		} else if (!bAutomeroeffnungsbuchung
				.equals(other.bAutomeroeffnungsbuchung))
			return false;
		if (bManuellbebuchbar == null) {
			if (other.bManuellbebuchbar != null)
				return false;
		} else if (!bManuellbebuchbar.equals(other.bManuellbebuchbar))
			return false;
		if (bOhneUst == null) {
			if (other.bOhneUst != null)
				return false;
		} else if (!bOhneUst.equals(other.bOhneUst))
			return false;
		if (bVersteckt == null) {
			if (other.bVersteckt != null)
				return false;
		} else if (!bVersteckt.equals(other.bVersteckt))
			return false;
		if (cBez == null) {
			if (other.cBez != null)
				return false;
		} else if (!cBez.equals(other.cBez))
			return false;
		if (cLetztesortierung == null) {
			if (other.cLetztesortierung != null)
				return false;
		} else if (!cLetztesortierung.equals(other.cLetztesortierung))
			return false;
		if (cNr == null) {
			if (other.cNr != null)
				return false;
		} else if (!cNr.equals(other.cNr))
			return false;
		if (cSteuerart == null) {
			if (other.cSteuerart != null)
				return false;
		} else if (!cSteuerart.equals(other.cSteuerart))
			return false;
		if (csortierung == null) {
			if (other.csortierung != null)
				return false;
		} else if (!csortierung.equals(other.csortierung))
			return false;
		if (dGueltigbis == null) {
			if (other.dGueltigbis != null)
				return false;
		} else if (!dGueltigbis.equals(other.dGueltigbis))
			return false;
		if (dGueltigvon == null) {
			if (other.dGueltigvon != null)
				return false;
		} else if (!dGueltigvon.equals(other.dGueltigvon))
			return false;
		if (ergebnisgruppeIId == null) {
			if (other.ergebnisgruppeIId != null)
				return false;
		} else if (!ergebnisgruppeIId.equals(other.ergebnisgruppeIId))
			return false;
		if (ergebnisgruppeIId_negativ == null) {
			if (other.ergebnisgruppeIId_negativ != null)
				return false;
		} else if (!ergebnisgruppeIId_negativ
				.equals(other.ergebnisgruppeIId_negativ))
			return false;
		if (finanzamtIId == null) {
			if (other.finanzamtIId != null)
				return false;
		} else if (!finanzamtIId.equals(other.finanzamtIId))
			return false;
		if (iGeschaeftsjahrEB == null) {
			if (other.iGeschaeftsjahrEB != null)
				return false;
		} else if (!iGeschaeftsjahrEB.equals(other.iGeschaeftsjahrEB))
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (iLetzteselektiertebuchung == null) {
			if (other.iLetzteselektiertebuchung != null)
				return false;
		} else if (!iLetzteselektiertebuchung
				.equals(other.iLetzteselektiertebuchung))
			return false;
		if (kontoIIdWeiterfuehrendBilanz == null) {
			if (other.kontoIIdWeiterfuehrendBilanz != null)
				return false;
		} else if (!kontoIIdWeiterfuehrendBilanz
				.equals(other.kontoIIdWeiterfuehrendBilanz))
			return false;
		if (kontoIIdWeiterfuehrendSkonto == null) {
			if (other.kontoIIdWeiterfuehrendSkonto != null)
				return false;
		} else if (!kontoIIdWeiterfuehrendSkonto
				.equals(other.kontoIIdWeiterfuehrendSkonto))
			return false;
		if (kontoIIdWeiterfuehrendUst == null) {
			if (other.kontoIIdWeiterfuehrendUst != null)
				return false;
		} else if (!kontoIIdWeiterfuehrendUst
				.equals(other.kontoIIdWeiterfuehrendUst))
			return false;
		if (kontoartCNr == null) {
			if (other.kontoartCNr != null)
				return false;
		} else if (!kontoartCNr.equals(other.kontoartCNr))
			return false;
		if (kontotypCNr == null) {
			if (other.kontotypCNr != null)
				return false;
		} else if (!kontotypCNr.equals(other.kontotypCNr))
			return false;
		if (kostenstelleIId == null) {
			if (other.kostenstelleIId != null)
				return false;
		} else if (!kostenstelleIId.equals(other.kostenstelleIId))
			return false;
		if (mandantCNr == null) {
			if (other.mandantCNr != null)
				return false;
		} else if (!mandantCNr.equals(other.mandantCNr))
			return false;
		if (partnerDto == null) {
			if (other.partnerDto != null)
				return false;
		} else if (!partnerDto.equals(other.partnerDto))
			return false;
		if (personalIIdAendern == null) {
			if (other.personalIIdAendern != null)
				return false;
		} else if (!personalIIdAendern.equals(other.personalIIdAendern))
			return false;
		if (personalIIdAnlegen == null) {
			if (other.personalIIdAnlegen != null)
				return false;
		} else if (!personalIIdAnlegen.equals(other.personalIIdAnlegen))
			return false;
		if (rechenregelCNrWeiterfuehrendBilanz == null) {
			if (other.rechenregelCNrWeiterfuehrendBilanz != null)
				return false;
		} else if (!rechenregelCNrWeiterfuehrendBilanz
				.equals(other.rechenregelCNrWeiterfuehrendBilanz))
			return false;
		if (rechenregelCNrWeiterfuehrendSkonto == null) {
			if (other.rechenregelCNrWeiterfuehrendSkonto != null)
				return false;
		} else if (!rechenregelCNrWeiterfuehrendSkonto
				.equals(other.rechenregelCNrWeiterfuehrendSkonto))
			return false;
		if (rechenregelCNrWeiterfuehrendUst == null) {
			if (other.rechenregelCNrWeiterfuehrendUst != null)
				return false;
		} else if (!rechenregelCNrWeiterfuehrendUst
				.equals(other.rechenregelCNrWeiterfuehrendUst))
			return false;
		if (steuerkategorieIId == null) {
			if (other.steuerkategorieIId != null)
				return false;
		} else if (!steuerkategorieIId.equals(other.steuerkategorieIId))
			return false;
		if (steuerkategorieIIdReverse == null) {
			if (other.steuerkategorieIIdReverse != null)
				return false;
		} else if (!steuerkategorieIIdReverse
				.equals(other.steuerkategorieIIdReverse))
			return false;
		if (tAendern == null) {
			if (other.tAendern != null)
				return false;
		} else if (!tAendern.equals(other.tAendern))
			return false;
		if (tAnlegen == null) {
			if (other.tAnlegen != null)
				return false;
		} else if (!tAnlegen.equals(other.tAnlegen))
			return false;
		if (tEBAnlegen == null) {
			if (other.tEBAnlegen != null)
				return false;
		} else if (!tEBAnlegen.equals(other.tEBAnlegen))
			return false;
		if (uvaartIId == null) {
			if (other.uvaartIId != null)
				return false;
		} else if (!uvaartIId.equals(other.uvaartIId))
			return false;
		if (waehrungCNrDruck == null) {
			if (other.waehrungCNrDruck != null)
				return false;
		} else if (!waehrungCNrDruck.equals(other.waehrungCNrDruck))
			return false;
		if (xBemerkung == null) {
			if (other.xBemerkung != null)
				return false;
		} else if (!xBemerkung.equals(other.xBemerkung))
			return false;
		return true;
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

	@HvDtoLogIdCnr(entityClass = Uvaart.class)
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

	public String getcSteuerart() {
		return cSteuerart;
	}

	public void setcSteuerart(String cSteuerart) {
		this.cSteuerart = cSteuerart;
	}
}
