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
package com.lp.server.partner.service;

import java.io.Serializable;
import java.sql.Timestamp;


public class KontaktDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer id) {
		iId = id;
	}
	public String getCTitel() {
		return cTitel;
	}
	public void setCTitel(String titel) {
		cTitel = titel;
	}
	public Integer getPartnerIId() {
		return partnerIId;
	}
	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}
	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}
	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}
	public Integer getPersonalIIdZugewiesener() {
		return personalIIdZugewiesener;
	}
	public void setPersonalIIdZugewiesener(Integer personalIIdZugewiesener) {
		this.personalIIdZugewiesener = personalIIdZugewiesener;
	}
	public Integer getKontaktartIId() {
		return kontaktartIId;
	}
	public void setKontaktartIId(Integer kontaktartIId) {
		this.kontaktartIId = kontaktartIId;
	}
	public Timestamp getTKontakt() {
		return tKontakt;
	}
	public void setTKontakt(Timestamp kontakt) {
		tKontakt = kontakt;
	}
	public Timestamp getTKontaktbis() {
		return tKontaktbis;
	}
	public void setTKontaktbis(Timestamp kontaktbis) {
		tKontaktbis = kontaktbis;
	}
	public Timestamp getTWiedervorlage() {
		return tWiedervorlage;
	}
	public void setTWiedervorlage(Timestamp wiedervorlage) {
		tWiedervorlage = wiedervorlage;
	}
	public String getXKommentar() {
		return xKommentar;
	}
	public void setXKommentar(String kommentar) {
		xKommentar = kommentar;
	}
	public Timestamp getTErledigt() {
		return tErledigt;
	}
	public void setTErledigt(Timestamp erledigt) {
		tErledigt = erledigt;
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
	public void setTAnlegen(Timestamp anlegen) {
		tAnlegen = anlegen;
	}
	private Integer iId;
	private String cTitel;
	private Integer partnerIId;
	private Integer ansprechpartnerIId;
	private Integer personalIIdZugewiesener;
	private Integer kontaktartIId;
	private Timestamp tKontakt;
	private Timestamp tKontaktbis;
	private Timestamp tWiedervorlage;
	private String xKommentar;
	private Timestamp tErledigt;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;

}
