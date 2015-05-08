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
package com.lp.server.angebot.service;

import java.sql.Timestamp;
import java.util.Calendar;

import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.service.BelegDto;
import com.lp.service.BelegVerkaufDto;

public class AngebotDto extends BelegVerkaufDto implements Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private Integer iId;
	// private String cNr;
	// private String mandantCNr;
	// private String angebotartCNr;
	// private String angebotstatusCNr;
	// private String belegartCNr;
	// private Timestamp tBelegdatum;
	private Timestamp tAnfragedatum;
	private Timestamp tAngebotsgueltigkeitbis;
	private Integer kundeIIdAngebotsadresse;
	private Integer ansprechpartnerIIdKunde;
	private Integer personalIIdVertreter;
	// private String cBez;
	// private String waehrungCNrAngebotswaehrung;
	// private Double fWechselkursmandantwaehrungzuangebotswaehrung;
	private Integer iLieferzeitinstunden;
	private String angeboteinheitCNr;
	// private Integer kostenstelleIId;
	private String angeboterledigungsgrundCNr;
	private Timestamp tNachfasstermin;
	private Timestamp tRealisierungstermin;
	private Double fAuftragswahrscheinlichkeit;
	private String xAblageort;

	public String getCKundenanfrage() {
		return cKundenanfrage;
	}

	public void setCKundenanfrage(String cKundenanfrage) {
		this.cKundenanfrage = cKundenanfrage;
	}

	private String cKundenanfrage;
	// private Double fAllgemeinerrabattsatz;

	// private Integer lieferartIId;
	// private Integer zahlungszielIId;
	// private Integer spediteurIId;
	private Integer iGarantie;
	// private BigDecimal nGesamtangebotswertinangebotswaehrung;
	// private Integer angebottextIIdKopftext;
	// private String xKopftextuebersteuert;
	// private Integer angebottextIIdFusstext;
	// private String xFusstextuebersteuert;
	private String xExternerkommentar;
	private String xInternerkommentar;

	/*
	 * private Timestamp tGedruckt; private Integer personalIIdStorniert;
	 * private Timestamp tStorniert; private Integer personalIIdManuellerledigt;
	 * private Timestamp tManuellerledigt; private Integer personalIIdAnlegen;
	 * private Timestamp tAnlegen; private Integer personalIIdAendern; private
	 * Timestamp tAendern;
	 */

	public Timestamp getTAnfragedatum() {
		return tAnfragedatum;
	}

	public void setTAnfragedatum(Timestamp tAnfragedatum) {
		this.tAnfragedatum = tAnfragedatum;
	}

	public Timestamp getTAngebotsgueltigkeitbis() {
		return tAngebotsgueltigkeitbis;
	}

	public void setTAngebotsgueltigkeitbis(Timestamp tAngebotsgueltigkeitbis) {
		this.tAngebotsgueltigkeitbis = tAngebotsgueltigkeitbis;
	}

	public Integer getKundeIIdAngebotsadresse() {
		return kundeIIdAngebotsadresse;
	}

	public void setKundeIIdAngebotsadresse(Integer kundeIIdAngebotsadresse) {
		this.kundeIIdAngebotsadresse = kundeIIdAngebotsadresse;
	}

	public Integer getAnsprechpartnerIIdKunde() {
		return ansprechpartnerIIdKunde;
	}

	public void setAnsprechpartnerIIdKunde(Integer ansprechpartnerIIdKunde) {
		this.ansprechpartnerIIdKunde = ansprechpartnerIIdKunde;
	}

	public Integer getPersonalIIdVertreter() {
		return personalIIdVertreter;
	}

	public void setPersonalIIdVertreter(Integer personalIIdVertreter) {
		this.personalIIdVertreter = personalIIdVertreter;
	}

	public Integer getILieferzeitinstunden() {
		return iLieferzeitinstunden;
	}

	public void setILieferzeitinstunden(Integer iLieferzeitinstunden) {
		this.iLieferzeitinstunden = iLieferzeitinstunden;
	}

	public String getAngeboteinheitCNr() {
		return angeboteinheitCNr;
	}

	public void setAngeboteinheitCNr(String angeboteinheitCNr) {
		this.angeboteinheitCNr = angeboteinheitCNr;
	}

	public String getAngeboterledigungsgrundCNr() {
		return angeboterledigungsgrundCNr;
	}

	public void setAngeboterledigungsgrundCNr(String angeboterledigungsgrundCNr) {
		this.angeboterledigungsgrundCNr = angeboterledigungsgrundCNr;
	}

	public Timestamp getTNachfasstermin() {
		return tNachfasstermin;
	}

	public void setTNachfasstermin(Timestamp tNachfasstermin) {
		this.tNachfasstermin = tNachfasstermin;
	}

	public Timestamp getTRealisierungstermin() {
		return tRealisierungstermin;
	}

	public void setTRealisierungstermin(Timestamp tRealisierungstermin) {
		this.tRealisierungstermin = tRealisierungstermin;
	}

	public Double getFAuftragswahrscheinlichkeit() {
		return fAuftragswahrscheinlichkeit;
	}

	public void setFAuftragswahrscheinlichkeit(
			Double fAuftragswahrscheinlichkeit) {
		this.fAuftragswahrscheinlichkeit = fAuftragswahrscheinlichkeit;
	}

	public String getXAblageort() {
		return xAblageort;
	}

	public void setXAblageort(String xAblageort) {
		this.xAblageort = xAblageort;
	}

	public Integer getIGarantie() {
		return iGarantie;
	}

	public void setIGarantie(Integer iGarantie) {
		this.iGarantie = iGarantie;
	}

	public String getXExternerkommentar() {
		return xExternerkommentar;
	}

	public void setXExternerkommentar(String xExternerkommentar) {
		this.xExternerkommentar = xExternerkommentar;
	}

	public String getXInternerkommentar() {
		return this.xInternerkommentar;
	}

	public void setXInternerkommentar(String xInternerkommentar) {
		this.xInternerkommentar = xInternerkommentar;
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
		returnString += ", " + tAnfragedatum;
		returnString += ", " + tAngebotsgueltigkeitbis;
		returnString += ", " + kundeIIdAngebotsadresse;
		returnString += ", " + ansprechpartnerIIdKunde;
		returnString += ", " + personalIIdVertreter;
		returnString += ", " + iLieferzeitinstunden;
		returnString += ", " + angeboteinheitCNr;
		returnString += ", " + angeboterledigungsgrundCNr;
		returnString += ", " + tNachfasstermin;
		returnString += ", " + tRealisierungstermin;
		returnString += ", " + fAuftragswahrscheinlichkeit;
		returnString += ", " + xAblageort;
		returnString += ", " + fVersteckterAufschlag;
		returnString += ", " + fProjektierungsRabattsatz;
		returnString += ", " + iGarantie;
		return returnString;
	}

	public Object clone() {
		AngebotDto angebotDto = (AngebotDto) cloneAsBelegDto(new AngebotDto());

		angebotDto.tAnfragedatum = this.tAnfragedatum;
		angebotDto.tAngebotsgueltigkeitbis = this.tAngebotsgueltigkeitbis;
		angebotDto.kundeIIdAngebotsadresse = this.kundeIIdAngebotsadresse;
		angebotDto.ansprechpartnerIIdKunde = this.ansprechpartnerIIdKunde;
		angebotDto.personalIIdVertreter = this.personalIIdVertreter;
		angebotDto.iLieferzeitinstunden = this.iLieferzeitinstunden;
		angebotDto.angeboteinheitCNr = this.angeboteinheitCNr;
		// AngeboterledigungsgrundCNr null
		angebotDto.tRealisierungstermin = this.tRealisierungstermin;
		angebotDto.xAblageort = this.xAblageort;
		angebotDto.tNachfasstermin = this.tNachfasstermin;
		angebotDto.fAuftragswahrscheinlichkeit = new Double(0);
		angebotDto.fVersteckterAufschlag = this.fVersteckterAufschlag;
		angebotDto.nKorrekturbetrag = this.nKorrekturbetrag;
		angebotDto.fProjektierungsRabattsatz = this.fProjektierungsRabattsatz;
		angebotDto.iGarantie = this.iGarantie;
		// IMS 1775 spezieller Kopf- und Fusstext sollen uebernommen werden
		angebotDto.setXKopftextuebersteuert(getXKopftextuebersteuert());
		angebotDto.setXFusstextuebersteuert(getXFusstextuebersteuert());

		return angebotDto;
	}

	public AuftragDto cloneAsAuftragDto(Integer stdLieferzeit) {
		AuftragDto auftragDto = new AuftragDto();

		// iId, cNr, auftragIIdRahmenauftrag, angebotIId null
		auftragDto.setAngebotIId(this.getIId());
		auftragDto.setMandantCNr(this.getMandantCNr());
		auftragDto.setAuftragartCNr(AuftragServiceFac.AUFTRAGART_FREI);
		auftragDto.setBelegartCNr(LocaleFac.BELEGART_AUFTRAG);
		auftragDto.setKundeIIdAuftragsadresse(this.kundeIIdAngebotsadresse);
		auftragDto.setAnsprechpartnerIId(this.ansprechpartnerIIdKunde);
		auftragDto.setPersonalIIdVertreter(this.personalIIdVertreter);
		auftragDto.setKundeIIdLieferadresse(auftragDto
				.getKundeIIdAuftragsadresse());
		auftragDto.setKundeIIdRechnungsadresse(auftragDto
				.getKundeIIdAuftragsadresse());
		auftragDto.setCBezProjektbezeichnung(this.getCBez());
		auftragDto.setProjektIId(this.getProjektIId());
		auftragDto.setDBestelldatum(new Timestamp(System.currentTimeMillis()));
		// bestellnummer, bestelldatum, sonderrabattsatz null
		auftragDto.setCAuftragswaehrung(this.getWaehrungCNr());
		auftragDto.setFWechselkursmandantwaehrungzubelegwaehrung(this
				.getFWechselkursmandantwaehrungzubelegwaehrung());

		int iLieferzeitinWochen = iLieferzeitinstunden / 168;
		// SP1570
		if (iLieferzeitinWochen == 98 || iLieferzeitinWochen == 99) {
			// STD aus Parameter verwenden
			iLieferzeitinstunden = stdLieferzeit;
		}

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.DATE, c.get(Calendar.DATE) + iLieferzeitinstunden / 24);

		auftragDto.setDLiefertermin(new Timestamp(c.getTimeInMillis()));
		auftragDto.setDFinaltermin(new Timestamp(c.getTimeInMillis()));
		auftragDto.setKostIId(this.getKostenstelleIId());
		auftragDto.setFVersteckterAufschlag(this.fVersteckterAufschlag);
		auftragDto.setNKorrekturbetrag(this.nKorrekturbetrag);
		auftragDto.setFAllgemeinerRabattsatz(this.getFAllgemeinerRabattsatz());
		auftragDto.setFProjektierungsrabattsatz(this.fProjektierungsRabattsatz);
		auftragDto.setLieferartIId(this.getLieferartIId());
		auftragDto.setZahlungszielIId(this.getZahlungszielIId());
		auftragDto.setSpediteurIId(this.getSpediteurIId());
		auftragDto.setIGarantie(this.iGarantie);
		auftragDto
				.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT); // initial
		// ist
		// der
		// Auftrag
		// immer
		// angelegt
		auftragDto.setTBelegdatum(new Timestamp(System.currentTimeMillis())); // jetzt
		auftragDto.setBMitzusammenfassung(this.getBMitzusammenfassung());
		// !
		// Auftragswerte null
		// tGedruckt, Anlegen, Aendern, Stornieren, Manuell erledigen null
		// Kopftext, Fusstext null

		return auftragDto;
	}
}
