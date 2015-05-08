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
package com.lp.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;

public class StuecklisteInfoDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iiAnzahlPositionen;
	private ArrayList<StuecklisteMitStrukturDto> alStuecklisteAufgeloest;
	private Short bIstFremdfertigung;
	private BigDecimal gesamtDurchlaufzeitBisInklMeineEbene = null;


	public BigDecimal getGesamtDurchlaufzeitBisInklMeineEbene() {
		return gesamtDurchlaufzeitBisInklMeineEbene;
	}

	public void setGesamtDurchlaufzeitBisInklMeineEbene(
			BigDecimal gesamtDurchlaufzeitBisInklMeineEbene) {
		this.gesamtDurchlaufzeitBisInklMeineEbene = gesamtDurchlaufzeitBisInklMeineEbene;
	}

	public StuecklisteInfoDto() {
		iiAnzahlPositionen = new Integer(0);
		alStuecklisteAufgeloest = new ArrayList<StuecklisteMitStrukturDto>();
		bIstFremdfertigung = new Short((short) 0);
	}

	public Integer getIiAnzahlPositionen() {
		return iiAnzahlPositionen;
	}

	public void setIIAnzahlPositionen(Integer iiAnzahlPositionenI) {
		iiAnzahlPositionen = iiAnzahlPositionenI;
	}

	public ArrayList<StuecklisteMitStrukturDto> getAlStuecklisteAufgeloest() {
		return alStuecklisteAufgeloest;
	}

	public void setAlStuecklisteAufgeloest(
			ArrayList<StuecklisteMitStrukturDto> alStuecklisteAufgeloestI) {
		alStuecklisteAufgeloest = alStuecklisteAufgeloestI;
	}

	public Short getBIstFremdfertigung() {
		return bIstFremdfertigung;
	}

	public void setBIstFremdfertigung(Short bIstFremdfertigungI) {
		bIstFremdfertigung = bIstFremdfertigungI;
	}
}
