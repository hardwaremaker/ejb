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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class MaschinenzeitdatenDto implements Serializable{
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer id) {
		iId = id;
	}
	public Timestamp getTVon() {
		return tVon;
	}
	public void setTVon(Timestamp von) {
		tVon = von;
	}
	public Timestamp getTBis() {
		return tBis;
	}
	public void setTBis(Timestamp bis) {
		tBis = bis;
	}
	public String getCBemerkung() {
		return cBemerkung;
	}
	public void setCBemerkung(String bemerkung) {
		cBemerkung = bemerkung;
	}
	public Timestamp getTAnlegen() {
		return tAnlegen;
	}
	public void setTAnlegen(Timestamp anlegen) {
		tAnlegen = anlegen;
	}
	public Timestamp getTAendern() {
		return tAendern;
	}
	public void setTAendern(Timestamp aendern) {
		tAendern = aendern;
	}
	public Integer getMaschineIId() {
		return maschineIId;
	}
	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}
	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}
	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}
	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}
	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	private Integer iId;
	private Timestamp tVon;
	private Timestamp tBis;
	private String cBemerkung;
	private Timestamp tAnlegen;
	private Timestamp tAendern;
	private Integer maschineIId;
	public Integer getLossollarbeitsplanIId() {
		return lossollarbeitsplanIId;
	}
	public void setLossollarbeitsplanIId(Integer lossollarbeitsplanIId) {
		this.lossollarbeitsplanIId = lossollarbeitsplanIId;
	}
	private Integer personalIIdAendern;
	private Integer personalIIdAnlegen;
	private Integer lossollarbeitsplanIId;
	private Integer personalIIdGestartet;
	public Integer getPersonalIIdGestartet() {
		return personalIIdGestartet;
	}
	public void setPersonalIIdGestartet(Integer personalIIdGestartet) {
		this.personalIIdGestartet = personalIIdGestartet;
	}

}
