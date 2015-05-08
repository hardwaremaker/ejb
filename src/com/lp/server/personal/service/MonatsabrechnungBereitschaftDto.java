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
package com.lp.server.personal.service;

public class MonatsabrechnungBereitschaftDto {
	public String getTagesartCNr() {
		return tagesartCNr;
	}

	public void setTagesartCNr(String tagesartCNr) {
		this.tagesartCNr = tagesartCNr;
	}

	public String getBereitschaftsart() {
		return bereitschaftsart;
	}

	public void setBereitschaftsart(String bereitschaftsart) {
		this.bereitschaftsart = bereitschaftsart;
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	public java.sql.Timestamp gettVon() {
		return tVon;
	}

	public void settVon(java.sql.Timestamp tVon) {
		this.tVon = tVon;
	}

	public java.sql.Timestamp gettBis() {
		return tBis;
	}

	public void settBis(java.sql.Timestamp tBis) {
		this.tBis = tBis;
	}

	String feiertag = null;

	public String getFeiertag() {
		return feiertag;
	}

	public void setFeiertag(String feiertag) {
		this.feiertag = feiertag;
	}

	String tagesartCNr = null;
	String bereitschaftsart = null;
	String bemerkung = null;
	java.sql.Timestamp tVon = null;
	java.sql.Timestamp tBis = null;
	Integer kw = null;
	Double dDauer = null;

	public Double getdDauer() {
		if (tBis != null && tVon != null) {
			return (double) (tBis.getTime() - tVon.getTime()) / 3600000;
		} else {
			return null;
		}

	}

	public Integer getKw() {
		return kw;
	}

	public void setKw(Integer kw) {
		this.kw = kw;
	}

	public MonatsabrechnungBereitschaftDto clone() {

		MonatsabrechnungBereitschaftDto berDto = new MonatsabrechnungBereitschaftDto();
		berDto.setBemerkung(this.getBemerkung());
		berDto.setKw(this.getKw());
		berDto.setTagesartCNr(this.getTagesartCNr());
		berDto.setBereitschaftsart(this.getBereitschaftsart());
		berDto.settVon(this.gettVon());
		berDto.settBis(this.gettBis());

		return berDto;
	}

}
