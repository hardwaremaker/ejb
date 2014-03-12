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
package com.lp.server.partner.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class LieferantbeurteilungDto implements Serializable {
	private Integer iId;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer id) {
		iId = id;
	}

	public Timestamp getTDatum() {
		return tDatum;
	}

	public void setTDatum(Timestamp datum) {
		tDatum = datum;
	}

	public Short getBGesperrt() {
		return bGesperrt;
	}

	public void setBGesperrt(Short gesperrt) {
		bGesperrt = gesperrt;
	}

	public Integer getIPunkte() {
		return iPunkte;
	}

	public void setIPunkte(Integer punkte) {
		iPunkte = punkte;
	}

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	private Timestamp tDatum;

	private Short bGesperrt;

	private Integer iPunkte;

	private Integer lieferantIId;
	
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp aendern) {
		tAendern = aendern;
	}

	private Short bManuellgeaendert;

	private String cKlasse;

	private String cKommentar;
	
	public Short getBManuellgeaendert() {
		return bManuellgeaendert;
	}

	public void setBManuellgeaendert(Short manuellgeaendert) {
		bManuellgeaendert = manuellgeaendert;
	}

	public String getCKlasse() {
		return cKlasse;
	}

	public void setCKlasse(String klasse) {
		cKlasse = klasse;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String kommentar) {
		cKommentar = kommentar;
	}

	private static final long serialVersionUID = 1L;

}
