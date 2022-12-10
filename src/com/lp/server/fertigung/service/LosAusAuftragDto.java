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
package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;

public class LosAusAuftragDto implements Serializable {

	private LosDto losDto = null;
	private AuftragpositionDto auftragpositionDto = null;
	private BigDecimal lagerstand = null;
	private BigDecimal reservierungen = null;
	private BigDecimal fehlmengen = null;
	private Integer auftragspositionsnummer = null;
	private Integer iSort = null;
	private boolean bDatumVerschoben = false;
	private boolean bGesperrt = false;
	private boolean bKommentareVorhanden = false;
	
	private boolean auftragspositionIstErledigt = false;
	
	
	public boolean isAuftragspositionIstErledigt() {
		return auftragspositionIstErledigt;
	}

	public void setAuftragspositionIstErledigt(boolean auftragspositionIstErledigt) {
		this.auftragspositionIstErledigt = auftragspositionIstErledigt;
	}

	ArrayList<StuecklistearbeitsplanDto> alStuecklistearbeitsplan =new ArrayList<StuecklistearbeitsplanDto>();
	
	public ArrayList<StuecklistearbeitsplanDto> getAlStuecklistearbeitsplan() {
		return alStuecklistearbeitsplan;
	}

	public void setAlStuecklistearbeitsplan(
			ArrayList<StuecklistearbeitsplanDto> alStuecklistearbeitsplan) {
		this.alStuecklistearbeitsplan = alStuecklistearbeitsplan;
	}

	public boolean isGesperrt() {
		return bGesperrt;
	}

	public void setBGesperrt(boolean bGesperrt) {
		this.bGesperrt = bGesperrt;
	}

	public boolean isKommentareVorhanden() {
		return bKommentareVorhanden;
	}

	public void setBKommentareVorhanden(boolean bKommentareVorhanden) {
		this.bKommentareVorhanden = bKommentareVorhanden;
	}

	private BigDecimal offeneFertigungsmenge = null;
	
	private LosDto[] bereitsVorhandeneLose =null;

	public LosDto[] getBereitsVorhandeneLose() {
		return bereitsVorhandeneLose;
	}

	public void setBereitsVorhandeneLose(LosDto[] bereitsVorhandeneLose) {
		this.bereitsVorhandeneLose = bereitsVorhandeneLose;
	}

	public BigDecimal getOffeneFertigungsmenge() {
		return offeneFertigungsmenge;
	}

	public void setOffeneFertigungsmenge(BigDecimal offeneFertigungsmenge) {
		this.offeneFertigungsmenge = offeneFertigungsmenge;
	}

	public boolean isBDatumVerschoben() {
		return bDatumVerschoben;
	}

	public void setBDatumVerschoben(boolean datumVerschoben) {
		bDatumVerschoben = datumVerschoben;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer sort) {
		iSort = sort;
	}

	public Integer getAuftragspositionsnummer() {
		return auftragspositionsnummer;
	}

	public void setAuftragspositionsnummer(Integer auftragspositionsnummer) {
		this.auftragspositionsnummer = auftragspositionsnummer;
	}

	public LosDto getLosDto() {
		return losDto;
	}

	public void setLosDto(LosDto losDto) {
		this.losDto = losDto;
	}

	public AuftragpositionDto getAuftragpositionDto() {
		return auftragpositionDto;
	}

	public void setAuftragpositionDto(AuftragpositionDto auftragpositionDto) {
		this.auftragpositionDto = auftragpositionDto;
	}

	public BigDecimal getLagerstand() {
		return lagerstand;
	}

	public void setLagerstand(BigDecimal lagerstand) {
		this.lagerstand = lagerstand;
	}

	public BigDecimal getReservierungen() {
		return reservierungen;
	}

	public void setReservierungen(BigDecimal reservierungen) {
		this.reservierungen = reservierungen;
	}

	public BigDecimal getFehlmengen() {
		return fehlmengen;
	}

	public void setFehlmengen(BigDecimal fehlmengen) {
		this.fehlmengen = fehlmengen;
	}

}
