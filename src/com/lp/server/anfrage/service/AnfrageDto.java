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
package com.lp.server.anfrage.service;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.service.BelegDto;

public class AnfrageDto extends BelegDto implements Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * private Integer iId; private String cNr; private String mandantCNr;
	 * private String anfrageartCNr; private String anfragestatusCNr; private
	 * String belegartCNr; private Timestamp tBelegdatum;
	 */
	private Integer lieferantIIdAnfrageadresse;
	private Integer ansprechpartnerIIdLieferant;
	private Integer liefergruppeIId;
	// private String cBez;
	private String cAngebotnummer;
	// private String waehrungCNrAnfragewaehrung;
	// private Double fWechselkursmandantwaehrungzuanfragewaehrung;
	private Timestamp tAnliefertermin;

	private Integer anfrageerledigungsgrundIId;

	public Integer getAnfrageerledigungsgrundIId() {
		return anfrageerledigungsgrundIId;
	}

	public void setAnfrageerledigungsgrundIId(Integer anfrageerledigungsgrundIId) {
		this.anfrageerledigungsgrundIId = anfrageerledigungsgrundIId;
	}

	private Timestamp tAngebotdatum;

	public Timestamp getTAngebotdatum() {
		return tAngebotdatum;
	}

	public void setTAngebotdatum(Timestamp angebotdatum) {
		tAngebotdatum = angebotdatum;
	}

	public Timestamp getTAngebotgueltigbis() {
		return tAngebotgueltigbis;
	}

	public void setTAngebotgueltigbis(Timestamp angebotgueltigbis) {
		tAngebotgueltigbis = angebotgueltigbis;
	}

	private Timestamp tAngebotgueltigbis;
	/*
	 * private Integer kostenstelleIId; private Double fAllgemeinerrabattsatz;
	 * private Integer lieferartIId; private Integer zahlungszielIId; private
	 * Integer spediteurIId;
	 */
	// private BigDecimal nGesamtanfragewertinanfragewaehrung;
	private BigDecimal nTransportkosteninanfragewaehrung;
	/*
	 * private Integer anfragetextIIdKopftext; private String
	 * xKopftextuebersteuert; private Integer anfragetextIIdFusstext; private
	 * String xFusstextuebersteuert; private Timestamp tGedruckt; private
	 * Integer personalIIdStorniert; private Timestamp tStorniert; private
	 * Integer personalIIdAnlegen; private Timestamp tAnlegen; private Integer
	 * personalIIdAendern; private Timestamp tAendern; private Integer
	 * personalIIdManuellerledigt; private Timestamp tManuellerledigt;
	 */
	private Integer anfrageIIdLiefergruppenanfrage;

	public Integer getLieferantIIdAnfrageadresse() {
		return lieferantIIdAnfrageadresse;
	}

	public void setLieferantIIdAnfrageadresse(Integer lieferantIIdAnfrageadresse) {
		this.lieferantIIdAnfrageadresse = lieferantIIdAnfrageadresse;
	}

	public Integer getAnsprechpartnerIIdLieferant() {
		return ansprechpartnerIIdLieferant;
	}

	public void setAnsprechpartnerIIdLieferant(
			Integer ansprechpartnerIIdLieferant) {
		this.ansprechpartnerIIdLieferant = ansprechpartnerIIdLieferant;
	}

	public Integer getLiefergruppeIId() {
		return this.liefergruppeIId;
	}

	public void setLiefergruppeIId(Integer iIdliefergruppeI) {
		this.liefergruppeIId = iIdliefergruppeI;
	}

	public String getCAngebotnummer() {
		return cAngebotnummer;
	}

	public void setCAngebotnummer(String cAngebotnummer) {
		this.cAngebotnummer = cAngebotnummer;
	}

	public Timestamp getTAnliefertermin() {
		return tAnliefertermin;
	}

	public void setTAnliefertermin(Timestamp tAnliefertermin) {
		this.tAnliefertermin = tAnliefertermin;
	}

	public BigDecimal getNTransportkosteninanfragewaehrung() {
		return nTransportkosteninanfragewaehrung;
	}

	public void setNTransportkosteninanfragewaehrung(
			BigDecimal nTransportkosteninanfragewaehrung) {
		this.nTransportkosteninanfragewaehrung = nTransportkosteninanfragewaehrung;
	}

	public Integer getAnfrageIIdLiefergruppenanfrage() {
		return this.anfrageIIdLiefergruppenanfrage;
	}

	public void setAnfrageIIdLiefergruppenanfrage(
			Integer anfrageIIdLiefergruppenanfrageI) {
		this.anfrageIIdLiefergruppenanfrage = anfrageIIdLiefergruppenanfrageI;
	}

	private String cLieferartort;

	public String getCLieferartort() {
		return cLieferartort;
	}

	public void setCLieferartort(String cLieferartort) {
		this.cLieferartort = cLieferartort;
	}

	public String toString() {
		String returnString = super.toString();
		returnString += ", " + lieferantIIdAnfrageadresse;
		returnString += ", " + ansprechpartnerIIdLieferant;
		returnString += ", " + liefergruppeIId;
		returnString += ", " + cAngebotnummer;
		returnString += ", " + tAnliefertermin;
		returnString += ", " + nTransportkosteninanfragewaehrung;
		returnString += ", " + anfrageIIdLiefergruppenanfrage;
		return returnString;
	}

	public Object clone() {
		AnfrageDto anfrageDto = (AnfrageDto) cloneAsBelegDto(new AnfrageDto());

		anfrageDto.setLieferantIIdAnfrageadresse(this
				.getLieferantIIdAnfrageadresse());
		anfrageDto.setAnsprechpartnerIIdLieferant(this
				.getAnsprechpartnerIIdLieferant());
		anfrageDto.setLiefergruppeIId(this.getLiefergruppeIId());
		anfrageDto.setCAngebotnummer(this.getCAngebotnummer());
		anfrageDto.setNTransportkosteninanfragewaehrung(this
				.getNTransportkosteninanfragewaehrung());
		// Ausnahme in der Anfrage: Die Konditionen werden NACH dem Drucken
		// gespeichert
		anfrageDto.setBelegtextIIdKopftext(this.getBelegtextIIdKopftext());
		anfrageDto.setBelegtextIIdFusstext(this.getBelegtextIIdFusstext());
		// IMS 1775 spezieller Kopf- und Fusstext sollen uebernommen werden
		anfrageDto.setXKopftextuebersteuert(getXKopftextuebersteuert());
		anfrageDto.setXFusstextuebersteuert(getXFusstextuebersteuert());
		// Referenz auf Liefergruppenanfrage null

		return anfrageDto;
	}
}
