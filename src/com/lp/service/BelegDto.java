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
import java.sql.Timestamp;

import com.lp.server.system.service.LocaleFac;

public abstract class BelegDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Integer iId;
	protected String cNr;
	protected String mandantCNr;
	private String artCNr;
	protected String statusCNr;
	private String belegartCNr;
	protected Timestamp tBelegdatum;
	private String cBez;
	protected String waehrungCNr;
	private Integer kostenstelleIId;
	protected Integer lieferartIId;
	protected Integer zahlungszielIId;
	protected Integer spediteurIId;
	private BigDecimal nGesamtwertinbelegwaehrung;
	private Integer belegtextIIdKopftext;
	private String xKopftextuebersteuert;
	private Integer belegtextIIdFusstext;
	private String xFusstextuebersteuert;
	private Timestamp tGedruckt;
	private Integer personalIIdStorniert;
	private Timestamp tStorniert;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Integer personalIIdManuellerledigt;
	private Timestamp tManuellerledigt;
	
	protected Double fAllgemeinerRabattsatz;
	protected Double fWechselkursmandantwaehrungzubelegwaehrung;
	
	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getArtCNr() {
		return this.artCNr;
	}

	public void setArtCNr(String artCNr) {
		this.artCNr = artCNr;
	}

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public Timestamp getTBelegdatum() {
		return tBelegdatum;
	}

	public void setTBelegdatum(Timestamp tBelegdatum) {
		this.tBelegdatum = tBelegdatum;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getWaehrungCNr() {
		return waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	public Double getFWechselkursmandantwaehrungzubelegwaehrung() {
		return this.fWechselkursmandantwaehrungzubelegwaehrung;
	}

	public void setFWechselkursmandantwaehrungzubelegwaehrung(
			Double fWechselkursmandantwaehrungzubelegwaehrung) {
		this.fWechselkursmandantwaehrungzubelegwaehrung = fWechselkursmandantwaehrungzubelegwaehrung;
	}

	public Integer getKostenstelleIId() {
		return kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public Double getFAllgemeinerRabattsatz() {
		return fAllgemeinerRabattsatz;
	}

	public void setFAllgemeinerRabattsatz(Double fAllgemeinerRabattsatz) {
		this.fAllgemeinerRabattsatz = fAllgemeinerRabattsatz;
	}
	private Short bMitzusammenfassung;

	public Short getBMitzusammenfassung() {
		return bMitzusammenfassung;
	}

	public void setBMitzusammenfassung(Short bMitzusammenfassung) {
		this.bMitzusammenfassung = bMitzusammenfassung;
	}
	public Integer getLieferartIId() {
		return lieferartIId;
	}

	public void setLieferartIId(Integer lieferartIId) {
		this.lieferartIId = lieferartIId;
	}

	public Integer getZahlungszielIId() {
		return zahlungszielIId;
	}

	public void setZahlungszielIId(Integer zahlungszielIId) {
		this.zahlungszielIId = zahlungszielIId;
	}

	public Integer getSpediteurIId() {
		return spediteurIId;
	}

	public void setSpediteurIId(Integer spediteurIId) {
		this.spediteurIId = spediteurIId;
	}

	public BigDecimal getNGesamtwertinbelegwaehrung() {
		return this.nGesamtwertinbelegwaehrung;
	}

	public void setNGesamtwertinbelegwaehrung(
			BigDecimal nGesamtwertinbelegwaehrung) {
		this.nGesamtwertinbelegwaehrung = nGesamtwertinbelegwaehrung;
	}

	public Integer getBelegtextIIdKopftext() {
		return belegtextIIdKopftext;
	}

	public void setBelegtextIIdKopftext(Integer belegtextIIdKopftext) {
		this.belegtextIIdKopftext = belegtextIIdKopftext;
	}

	public String getXKopftextuebersteuert() {
		return xKopftextuebersteuert;
	}

	public void setXKopftextuebersteuert(String xKopftextuebersteuert) {
		this.xKopftextuebersteuert = xKopftextuebersteuert;
	}

	public Integer getBelegtextIIdFusstext() {
		return belegtextIIdFusstext;
	}

	public void setBelegtextIIdFusstext(Integer belegtextIIdFusstext) {
		this.belegtextIIdFusstext = belegtextIIdFusstext;
	}

	public String getXFusstextuebersteuert() {
		return xFusstextuebersteuert;
	}

	public void setXFusstextuebersteuert(String xFusstextuebersteuert) {
		this.xFusstextuebersteuert = xFusstextuebersteuert;
	}

	public Timestamp getTGedruckt() {
		return tGedruckt;
	}

	public void setTGedruckt(Timestamp tGedruckt) {
		this.tGedruckt = tGedruckt;
	}

	public Integer getPersonalIIdStorniert() {
		return personalIIdStorniert;
	}

	public void setPersonalIIdStorniert(Integer personalIIdStorniert) {
		this.personalIIdStorniert = personalIIdStorniert;
	}

	public Timestamp getTStorniert() {
		return tStorniert;
	}

	public void setTStorniert(Timestamp tStorniert) {
		this.tStorniert = tStorniert;
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

	public Integer getPersonalIIdManuellerledigt() {
		return personalIIdManuellerledigt;
	}

	public void setPersonalIIdManuellerledigt(Integer personalIIdManuellerledigt) {
		this.personalIIdManuellerledigt = personalIIdManuellerledigt;
	}

	public Timestamp getTManuellerledigt() {
		return tManuellerledigt;
	}

	public void setTManuellerledigt(Timestamp tManuellerledigt) {
		this.tManuellerledigt = tManuellerledigt;
	}

	protected Integer projektIId;

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}
	
	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cNr;
		returnString += ", " + mandantCNr;
		returnString += ", " + artCNr;
		returnString += ", " + statusCNr;
		returnString += ", " + belegartCNr;
		returnString += ", " + tBelegdatum;
		returnString += ", " + cBez;
		returnString += ", " + waehrungCNr;
		returnString += ", " + fWechselkursmandantwaehrungzubelegwaehrung;
		returnString += ", " + kostenstelleIId;
		returnString += ", " + fAllgemeinerRabattsatz;
		returnString += ", " + lieferartIId;
		returnString += ", " + zahlungszielIId;
		returnString += ", " + spediteurIId;
		returnString += ", " + nGesamtwertinbelegwaehrung;
		returnString += ", " + belegtextIIdKopftext;
		returnString += ", " + xKopftextuebersteuert;
		returnString += ", " + belegtextIIdFusstext;
		returnString += ", " + xFusstextuebersteuert;
		returnString += ", " + tGedruckt;
		returnString += ", " + personalIIdStorniert;
		returnString += ", " + tStorniert;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		return returnString;
	}

	public Object cloneAsBelegDto(BelegDto belegDtoI) {
		// iId null
		// cNr null
		belegDtoI.mandantCNr = this.mandantCNr;
		belegDtoI.artCNr = this.artCNr;
		belegDtoI.statusCNr = LocaleFac.STATUS_ANGELEGT;
		belegDtoI.belegartCNr = this.belegartCNr;
		belegDtoI.tBelegdatum = new Timestamp(System.currentTimeMillis());
		belegDtoI.cBez = this.cBez;
		belegDtoI.waehrungCNr = this.waehrungCNr;
		// der Wechselkurs muss aus der aktuellen Wechelkurstabelle gesetzt
		// werden
		belegDtoI.kostenstelleIId = this.kostenstelleIId;
		belegDtoI.fAllgemeinerRabattsatz = this.fAllgemeinerRabattsatz;
		belegDtoI.lieferartIId = this.lieferartIId;
		belegDtoI.zahlungszielIId = this.zahlungszielIId;
		belegDtoI.spediteurIId = this.spediteurIId;
		belegDtoI.projektIId = this.projektIId;
		belegDtoI.bMitzusammenfassung = this.bMitzusammenfassung;
		// der Gesamtwert muss berechnet werden
		// Belegtexte werden erst beim Erfassen der Konditionen gesetzt
		// Druckzeitpunkt wird erst beim Drucken gesetzt
		// Anlegen, Aendern, Stornieren, Manuell erledigt null

		return belegDtoI;
	}
}
