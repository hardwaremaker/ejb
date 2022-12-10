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
package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.SystemFac;
import com.lp.service.BelegVerkaufDto;
import com.lp.util.Helper;

public class RechnungDto extends BelegVerkaufDto implements Serializable {
	private static final long serialVersionUID = 5934692223933611L;

	private Integer iId;
	private String mandantCNr;
	private Integer iGeschaeftsjahr;
	private String cNr;
	private Integer rechnungIIdZurechnung;
	private Integer kundeIId;
	private Integer ansprechpartnerIId;
	private Integer auftragIId;
	private Integer lieferscheinIId;
	private Integer lagerIId;
	private Timestamp tBelegdatum;
	private String statusCNr;
	private String rechnungartCNr;
	private Integer kostenstelleIId;
	private String waehrungCNr;
	private BigDecimal nKurs;
	private Integer mwstsatzIId;
	private Short bMwstallepositionen;
	private BigDecimal nWert;
	private BigDecimal nWertfw;
	private BigDecimal nWertust;
	private BigDecimal nWertustfw;
	private Short bMindermengenzuschlag;
	private BigDecimal nProvision;
	private String cProvisiontext;

	private Timestamp tGedruckt;
	private Timestamp tFibuuebernahme;
	private String cKopftextuebersteuert;
	private String cFusstextuebersteuert;
	private String cBestellnummer;
	private Timestamp tStorniert;
	private Integer personalIIdStorniert;
	private Date tBezahltdatum;
	private Date tMahnsperrebis;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Timestamp tManuellerledigt;
	private Integer personalIIdManuellerledigt;
	private Short bReversecharge;
	private Integer kundeIIdStatistikadresse;
	private Integer personalIIdVertreter;
	private Integer gutschriftsgrundIId;

	private Integer projektIId;
	private ReversechargeartDto rcArtDto;
	private Integer rcartId;
	private Timestamp tElektronisch;
	private Integer personalIIdElektronisch;

	private String zahlungsartCNr;

	public String getZahlungsartCNr() {
		return zahlungsartCNr;
	}

	public void setZahlungsartCNr(String zahlungsartCNr) {
		this.zahlungsartCNr = zahlungsartCNr;
	}

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	private Timestamp tZollpapier;
	private Integer personalIIdZollpapier;

	public Timestamp getTZollpapier() {
		return tZollpapier;
	}

	public void setTZollpapier(Timestamp tZollpapier) {
		this.tZollpapier = tZollpapier;
	}

	public Integer getPersonalIIdZollpapier() {
		return personalIIdZollpapier;
	}

	public void setPersonalIIdZollpapier(Integer personalIIdZollpapier) {
		this.personalIIdZollpapier = personalIIdZollpapier;
	}

	private String cMahnungsanmerkung;

	public String getCMahnungsanmerkung() {
		return cMahnungsanmerkung;
	}

	public void setCMahnungsanmerkung(String cMahnungsanmerkung) {
		this.cMahnungsanmerkung = cMahnungsanmerkung;
	}

	public String getCZollpapier() {
		return cZollpapier;
	}

	public void setCZollpapier(String cZollpapier) {
		this.cZollpapier = cZollpapier;
	}

	private String cZollpapier;

	boolean bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = false;

	public boolean isBMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben() {
		return bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben;
	}

	public void setBMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben(
			boolean mwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben) {
		bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = mwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben;
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

	public Integer getIGeschaeftsjahr() {
		return iGeschaeftsjahr;
	}

	public void setIGeschaeftsjahr(Integer iGeschaeftsjahr) {
		this.iGeschaeftsjahr = iGeschaeftsjahr;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Integer getRechnungIIdZurechnung() {
		return rechnungIIdZurechnung;
	}

	public void setRechnungIIdZurechnung(Integer rechnungIIdZurechnung) {
		this.rechnungIIdZurechnung = rechnungIIdZurechnung;
	}

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getAuftragIId() {
		return auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public Integer getLieferscheinIId() {
		return lieferscheinIId;
	}

	public void setLieferscheinIId(Integer lieferscheinIId) {
		this.lieferscheinIId = lieferscheinIId;
	}

	public Integer getLagerIId() {
		return lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

	/*
	 * public java.sql.Date getTBelegdatum() { return this.dBelegdatum; }
	 * 
	 * public void setTBelegdatum(Date tBelegdatum) { this.dBelegdatum =
	 * tBelegdatum; }
	 */
	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	public String getRechnungartCNr() {
		return rechnungartCNr;
	}

	public void setRechnungartCNr(String rechnungartCNr) {
		this.rechnungartCNr = rechnungartCNr;
	}

	public Integer getKostenstelleIId() {
		return kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public String getWaehrungCNr() {
		return waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	public BigDecimal getNKurs() {
		return nKurs;
	}

	public void setNKurs(BigDecimal nKurs) {
		this.nKurs = nKurs;
	}

	@Override
	public Double getFWechselkursmandantwaehrungzubelegwaehrung() {
		return new Double(nKurs.doubleValue());
	}

	@Override
	public void setFWechselkursmandantwaehrungzubelegwaehrung(Double fWechselkursmandantwaehrungzubelegwaehrung) {
		BigDecimal bd = new BigDecimal(fWechselkursmandantwaehrungzubelegwaehrung);
		bd = Helper.rundeKaufmaennisch(bd, 4);
		setNKurs(bd);
	}

	public Integer getMwstsatzIId() {
		return mwstsatzIId;
	}

	public void setMwstsatzIId(Integer mwstsatzIId) {
		this.mwstsatzIId = mwstsatzIId;
	}

	public Short getBMwstallepositionen() {
		return bMwstallepositionen;
	}

	public void setBMwstallepositionen(Short bMwstallepositionen) {
		this.bMwstallepositionen = bMwstallepositionen;
	}

	private BigDecimal nMtlZahlbetrag;

	private Integer iZahltagMtlZahlbetrag;

	public BigDecimal getNMtlZahlbetrag() {
		return nMtlZahlbetrag;
	}

	public void setNMtlZahlbetrag(BigDecimal nMtlZahlbetrag) {
		this.nMtlZahlbetrag = nMtlZahlbetrag;
	}

	public Integer getIZahltagMtlZahlbetrag() {
		return iZahltagMtlZahlbetrag;
	}

	public void setIZahltagMtlZahlbetrag(Integer iZahltagMtlZahlbetrag) {
		this.iZahltagMtlZahlbetrag = iZahltagMtlZahlbetrag;
	}

	public BigDecimal getNWert() {
		return nWert;
	}

	public void setNWert(BigDecimal nWert) {
		this.nWert = nWert;
	}

	public BigDecimal getNWertfw() {
		return nWertfw;
	}

	public void setNWertfw(BigDecimal nWertfw) {
		this.nWertfw = nWertfw;
	}

	public BigDecimal getNWertust() {
		return nWertust;
	}

	@Override
	public BigDecimal getNGesamtwertinbelegwaehrung() {
		return getNWertfw();
	}

	public void setNWertust(BigDecimal nWertust) {
		this.nWertust = nWertust;
	}

	public BigDecimal getNWertustfw() {
		return nWertustfw;
	}

	public void setNWertustfw(BigDecimal nWertustfw) {
		this.nWertustfw = nWertustfw;
	}

	public Short getBMindermengenzuschlag() {
		return bMindermengenzuschlag;
	}

	public void setBMindermengenzuschlag(Short bMindermengenzuschlag) {
		this.bMindermengenzuschlag = bMindermengenzuschlag;
	}

	public BigDecimal getNProvision() {
		return nProvision;
	}

	public void setNProvision(BigDecimal nProvision) {
		this.nProvision = nProvision;
	}

	public String getCProvisiontext() {
		return cProvisiontext;
	}

	public void setCProvisiontext(String cProvisiontext) {
		this.cProvisiontext = cProvisiontext;
	}

	public Timestamp getTGedruckt() {
		return tGedruckt;
	}

	public void setTGedruckt(Timestamp tGedruckt) {
		this.tGedruckt = tGedruckt;
	}

	public Timestamp getTFibuuebernahme() {
		return tFibuuebernahme;
	}

	public void setTFibuuebernahme(Timestamp tFibuuebernahme) {
		this.tFibuuebernahme = tFibuuebernahme;
	}

	public String getCKopftextuebersteuert() {
		return cKopftextuebersteuert;
	}

	public void setCKopftextuebersteuert(String cKopftextuebersteuert) {
		this.cKopftextuebersteuert = cKopftextuebersteuert;
	}

	public String getCFusstextuebersteuert() {
		return cFusstextuebersteuert;
	}

	public void setCFusstextuebersteuert(String cFusstextuebersteuert) {
		this.cFusstextuebersteuert = cFusstextuebersteuert;
	}

	public Timestamp getTStorniert() {
		return tStorniert;
	}

	public void setTStorniert(Timestamp tStorniert) {
		this.tStorniert = tStorniert;
	}

	public Integer getPersonalIIdStorniert() {
		return personalIIdStorniert;
	}

	public void setPersonalIIdStorniert(Integer personalIIdStorniert) {
		this.personalIIdStorniert = personalIIdStorniert;
	}

	public Date getTBezahltdatum() {
		return tBezahltdatum;
	}

	public void setTBezahltdatum(Date tBezahltdatum) {
		this.tBezahltdatum = tBezahltdatum;
	}

	public Date getTMahnsperrebis() {
		return tMahnsperrebis;
	}

	public void setTMahnsperrebis(Date tMahnsperrebis) {
		this.tMahnsperrebis = tMahnsperrebis;
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

	public Timestamp getTManuellerledigt() {
		return tManuellerledigt;
	}

	public void setTManuellerledigt(Timestamp tManuellerledigt) {
		this.tManuellerledigt = tManuellerledigt;
	}

	public Integer getPersonalIIdManuellerledigt() {
		return personalIIdManuellerledigt;
	}

	public void setPersonalIIdManuellerledigt(Integer personalIIdManuellerledigt) {
		this.personalIIdManuellerledigt = personalIIdManuellerledigt;
	}

	public String getCBestellnummer() {
		return cBestellnummer;
	}

	public void setCBestellnummer(String cBestellnummer) {
		this.cBestellnummer = cBestellnummer;
	}

	// public Short getBReversecharge() {
	// return bReversecharge;
	// }
	//
	public boolean isReverseCharge() {
		// return Helper.short2boolean(getBReversecharge());
		return bReversecharge == null ? false : bReversecharge > 0;
	}

	public Integer getKundeIIdStatistikadresse() {
		return kundeIIdStatistikadresse;
	}

	public void setBReversecharge(Short bReversecharge) {
		this.bReversecharge = bReversecharge;
	}

	public void setKundeIIdStatistikadresse(Integer kundeIIdStatistikadresse) {
		this.kundeIIdStatistikadresse = kundeIIdStatistikadresse;
	}

	public Integer getPersonalIIdVertreter() {
		return personalIIdVertreter;
	}

	public void setPersonalIIdVertreter(Integer personalIIdVertreter) {
		this.personalIIdVertreter = personalIIdVertreter;
	}

	public void setGutschriftsgrundIId(Integer gutschriftsgrundIId) {
		this.gutschriftsgrundIId = gutschriftsgrundIId;
	}

	public Integer getGutschriftsgrundIId() {
		return this.gutschriftsgrundIId;
	}

	private String cLieferartort;

	public String getCLieferartort() {
		return cLieferartort;
	}

	public void setCLieferartort(String cLieferartort) {
		this.cLieferartort = cLieferartort;
	}

	public AngebotDto cloneAsAngebotDto() {
		AngebotDto angebotDto = new AngebotDto();

		// iId, rechnungIId, cNr, angebotIId null
		angebotDto.setMandantCNr(getMandantCNr());
		angebotDto.setStatusCNr(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT);
		angebotDto.setArtCNr(AngebotServiceFac.ANGEBOTART_FREI);
		angebotDto.setAngeboteinheitCNr(SystemFac.EINHEIT_WOCHE);
		angebotDto.setILieferzeitinstunden(0);
		angebotDto.setBelegartCNr(LocaleFac.BELEGART_ANGEBOT);

		angebotDto.setTBelegdatum(new Timestamp(System.currentTimeMillis())); // jetzt
		// !
		angebotDto.setKundeIIdAngebotsadresse(kundeIId);

		// nun dann kann der Ansprechpartner uebernommen werden
		angebotDto.setAnsprechpartnerIIdKunde(ansprechpartnerIId);

		angebotDto.setPersonalIIdVertreter(personalIIdVertreter);

		angebotDto.setFAuftragswahrscheinlichkeit(0D);
		angebotDto.setIGarantie(0);

		angebotDto.setCBez(getCBez());
		angebotDto.setProjektIId(getProjektIId());
		// lagerIID am Client setzen
		angebotDto.setKostenstelleIId(getKostenstelleIId());

		// rueckgabetermin null
		angebotDto.setFVersteckterAufschlag(getFVersteckterAufschlag());
		// MB IMS 1532 da der LS keinen Projektrabatt hat, werden fuer den LS
		// allgemeiner Rabatt und Projektrabatt zusammengefasst
		Double dRabattsatz = new Double(100.0 - ((1.0 - (getFAllgemeinerRabattsatz().doubleValue() / 100.0))
				* (1.0 - (getFProjektierungsrabattsatz().doubleValue() / 100.0)) * 100.0));
		// lieferscheinDto.setFAllgemeinerRabatt(getFAllgemeinerRabattsatz());
		angebotDto.setFAllgemeinerRabattsatz(dRabattsatz);
		// nGesamtwertInLieferscheinwaehrung, nGestehungswertInMandantenwaehrung
		// null

		angebotDto.setLieferartIId(getLieferartIId());
		angebotDto.setZahlungszielIId(getZahlungszielIId());
		angebotDto.setSpediteurIId(getSpediteurIId());

		angebotDto.setWaehrungCNr(getWaehrungCNr());
		// wechselkurs, kopftext, fusstext am Client bestimmen
		// der Rest null

		return angebotDto;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof RechnungDto))
			return false;
		RechnungDto that = (RechnungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null : that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.iGeschaeftsjahr == null ? this.iGeschaeftsjahr == null
				: that.iGeschaeftsjahr.equals(this.iGeschaeftsjahr)))
			return false;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		if (!(that.rechnungIIdZurechnung == null ? this.rechnungIIdZurechnung == null
				: that.rechnungIIdZurechnung.equals(this.rechnungIIdZurechnung)))
			return false;
		if (!(that.kundeIId == null ? this.kundeIId == null : that.kundeIId.equals(this.kundeIId)))
			return false;
		if (!(that.ansprechpartnerIId == null ? this.ansprechpartnerIId == null
				: that.ansprechpartnerIId.equals(this.ansprechpartnerIId)))
			return false;
		if (!(that.auftragIId == null ? this.auftragIId == null : that.auftragIId.equals(this.auftragIId)))
			return false;
		if (!(that.lieferscheinIId == null ? this.lieferscheinIId == null
				: that.lieferscheinIId.equals(this.lieferscheinIId)))
			return false;
		if (!(that.lagerIId == null ? this.lagerIId == null : that.lagerIId.equals(this.lagerIId)))
			return false;
		if (!(that.tBelegdatum == null ? this.tBelegdatum == null : that.tBelegdatum.equals(this.tBelegdatum)))
			return false;
		if (!(that.statusCNr == null ? this.statusCNr == null : that.statusCNr.equals(this.statusCNr)))
			return false;
		if (!(that.rechnungartCNr == null ? this.rechnungartCNr == null
				: that.rechnungartCNr.equals(this.rechnungartCNr)))
			return false;
		if (!(that.kostenstelleIId == null ? this.kostenstelleIId == null
				: that.kostenstelleIId.equals(this.kostenstelleIId)))
			return false;
		if (!(that.waehrungCNr == null ? this.waehrungCNr == null : that.waehrungCNr.equals(this.waehrungCNr)))
			return false;
		if (!(that.nKurs == null ? this.nKurs == null : that.nKurs.equals(this.nKurs)))
			return false;
		if (!(that.mwstsatzIId == null ? this.mwstsatzIId == null : that.mwstsatzIId.equals(this.mwstsatzIId)))
			return false;
		if (!(that.bMwstallepositionen == null ? this.bMwstallepositionen == null
				: that.bMwstallepositionen.equals(this.bMwstallepositionen)))
			return false;
		if (!(that.nWert == null ? this.nWert == null : that.nWert.equals(this.nWert)))
			return false;
		if (!(that.nWertfw == null ? this.nWertfw == null : that.nWertfw.equals(this.nWertfw)))
			return false;
		if (!(that.nWertust == null ? this.nWertust == null : that.nWertust.equals(this.nWertust)))
			return false;
		if (!(that.nWertustfw == null ? this.nWertustfw == null : that.nWertustfw.equals(this.nWertustfw)))
			return false;
		if (!(that.fVersteckterAufschlag == null ? this.fVersteckterAufschlag == null
				: that.fVersteckterAufschlag.equals(this.fVersteckterAufschlag)))
			return false;
		if (!(that.fAllgemeinerRabattsatz == null ? this.fAllgemeinerRabattsatz == null
				: that.fAllgemeinerRabattsatz.equals(this.fAllgemeinerRabattsatz)))
			return false;
		if (!(that.bMindermengenzuschlag == null ? this.bMindermengenzuschlag == null
				: that.bMindermengenzuschlag.equals(this.bMindermengenzuschlag)))
			return false;
		if (!(that.nProvision == null ? this.nProvision == null : that.nProvision.equals(this.nProvision)))
			return false;
		if (!(that.cProvisiontext == null ? this.cProvisiontext == null
				: that.cProvisiontext.equals(this.cProvisiontext)))
			return false;
		if (!(that.getZahlungszielIId() == null ? this.getZahlungszielIId() == null
				: that.getZahlungszielIId().equals(this.getZahlungszielIId())))
			return false;
		if (!(that.getLieferartIId() == null ? this.getLieferartIId() == null
				: that.getLieferartIId().equals(this.getLieferartIId())))
			return false;
		if (!(that.getSpediteurIId() == null ? this.getSpediteurIId() == null
				: that.getSpediteurIId().equals(this.getSpediteurIId())))
			return false;
		if (!(that.tGedruckt == null ? this.tGedruckt == null : that.tGedruckt.equals(this.tGedruckt)))
			return false;
		if (!(that.tFibuuebernahme == null ? this.tFibuuebernahme == null
				: that.tFibuuebernahme.equals(this.tFibuuebernahme)))
			return false;
		if (!(that.cKopftextuebersteuert == null ? this.cKopftextuebersteuert == null
				: that.cKopftextuebersteuert.equals(this.cKopftextuebersteuert)))
			return false;
		if (!(that.cFusstextuebersteuert == null ? this.cFusstextuebersteuert == null
				: that.cFusstextuebersteuert.equals(this.cFusstextuebersteuert)))
			return false;
		if (!(that.tStorniert == null ? this.tStorniert == null : that.tStorniert.equals(this.tStorniert)))
			return false;
		if (!(that.personalIIdStorniert == null ? this.personalIIdStorniert == null
				: that.personalIIdStorniert.equals(this.personalIIdStorniert)))
			return false;
		if (!(that.tBezahltdatum == null ? this.tBezahltdatum == null : that.tBezahltdatum.equals(this.tBezahltdatum)))
			return false;
		if (!(that.tMahnsperrebis == null ? this.tMahnsperrebis == null
				: that.tMahnsperrebis.equals(this.tMahnsperrebis)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen.equals(this.tAnlegen)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern.equals(this.tAendern)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tManuellerledigt == null ? this.tManuellerledigt == null
				: that.tManuellerledigt.equals(this.tManuellerledigt)))
			return false;
		if (!(that.personalIIdManuellerledigt == null ? this.personalIIdManuellerledigt == null
				: that.personalIIdManuellerledigt.equals(this.personalIIdManuellerledigt)))
			return false;
		if (!(that.cBestellnummer == null ? this.cBestellnummer == null
				: that.cBestellnummer.equals(this.cBestellnummer)))
			return false;
		if (!(that.bReversecharge == null ? this.bReversecharge == null
				: that.bReversecharge.equals(this.bReversecharge)))
			return false;
		if (!(that.kundeIIdStatistikadresse == null ? this.kundeIIdStatistikadresse == null
				: that.kundeIIdStatistikadresse.equals(this.kundeIIdStatistikadresse)))
			return false;
		if (!(that.personalIIdVertreter == null ? this.personalIIdVertreter == null
				: that.personalIIdVertreter.equals(this.personalIIdVertreter)))
			return false;
		if (!(that.gutschriftsgrundIId == null ? this.gutschriftsgrundIId == null
				: that.gutschriftsgrundIId.equals(this.gutschriftsgrundIId)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.iGeschaeftsjahr.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.rechnungIIdZurechnung.hashCode();
		result = 37 * result + this.kundeIId.hashCode();
		result = 37 * result + this.ansprechpartnerIId.hashCode();
		result = 37 * result + this.auftragIId.hashCode();
		result = 37 * result + this.lieferscheinIId.hashCode();
		result = 37 * result + this.lagerIId.hashCode();
		result = 37 * result + this.tBelegdatum.hashCode();
		result = 37 * result + this.statusCNr.hashCode();
		result = 37 * result + this.rechnungartCNr.hashCode();
		result = 37 * result + this.kostenstelleIId.hashCode();
		result = 37 * result + this.waehrungCNr.hashCode();
		result = 37 * result + this.nKurs.hashCode();
		result = 37 * result + this.mwstsatzIId.hashCode();
		result = 37 * result + this.bMwstallepositionen.hashCode();
		result = 37 * result + this.nWert.hashCode();
		result = 37 * result + this.nWertfw.hashCode();
		result = 37 * result + this.nWertust.hashCode();
		result = 37 * result + this.nWertustfw.hashCode();
		result = 37 * result + this.fVersteckterAufschlag.hashCode();
		result = 37 * result + this.fAllgemeinerRabattsatz.hashCode();
		result = 37 * result + this.bMindermengenzuschlag.hashCode();
		result = 37 * result + this.nProvision.hashCode();
		result = 37 * result + this.cProvisiontext.hashCode();
		result = 37 * result + this.getZahlungszielIId().hashCode();
		result = 37 * result + this.getLieferartIId().hashCode();
		result = 37 * result + this.getSpediteurIId().hashCode();
		result = 37 * result + this.tGedruckt.hashCode();
		result = 37 * result + this.tFibuuebernahme.hashCode();
		result = 37 * result + this.cKopftextuebersteuert.hashCode();
		result = 37 * result + this.cFusstextuebersteuert.hashCode();
		result = 37 * result + this.tStorniert.hashCode();
		result = 37 * result + this.personalIIdStorniert.hashCode();
		result = 37 * result + this.tBezahltdatum.hashCode();
		result = 37 * result + this.tMahnsperrebis.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tManuellerledigt.hashCode();
		result = 37 * result + this.personalIIdManuellerledigt.hashCode();
		result = 37 * result + this.cBestellnummer.hashCode();
		result = 37 * result + this.bReversecharge.hashCode();
		result = 37 * result + this.kundeIIdStatistikadresse.hashCode();
		result = 37 * result + this.personalIIdVertreter.hashCode();
		result = 37 * result + this.gutschriftsgrundIId.hashCode();
		result = 37 * result + this.rcartId.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(1632);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("mandantCNr:").append(mandantCNr);
		returnStringBuffer.append("iGeschaeftsjahr:").append(iGeschaeftsjahr);
		returnStringBuffer.append("cNr:").append(cNr);
		returnStringBuffer.append("rechnungIIdZurechnung:").append(rechnungIIdZurechnung);
		returnStringBuffer.append("kundeIId:").append(kundeIId);
		returnStringBuffer.append("ansprechpartnerIId:").append(ansprechpartnerIId);
		returnStringBuffer.append("auftragIId:").append(auftragIId);
		returnStringBuffer.append("lieferscheinIId:").append(lieferscheinIId);
		returnStringBuffer.append("lagerIId:").append(lagerIId);
		returnStringBuffer.append("tBelegdatum:").append(tBelegdatum);
		returnStringBuffer.append("statusCNr:").append(statusCNr);
		returnStringBuffer.append("rechnungartCNr:").append(rechnungartCNr);
		returnStringBuffer.append("kostenstelleIId:").append(kostenstelleIId);
		returnStringBuffer.append("waehrungCNr:").append(waehrungCNr);
		returnStringBuffer.append("nKurs:").append(nKurs);
		returnStringBuffer.append("mwstsatzIId:").append(mwstsatzIId);
		returnStringBuffer.append("bMwstallepositionen:").append(bMwstallepositionen);
		returnStringBuffer.append("nWert:").append(nWert);
		returnStringBuffer.append("nWertfw:").append(nWertfw);
		returnStringBuffer.append("nWertust:").append(nWertust);
		returnStringBuffer.append("nWertustfw:").append(nWertustfw);
		returnStringBuffer.append("fVersteckteraufschlag:").append(fVersteckterAufschlag);
		returnStringBuffer.append("fAllgemeinerrabattsatz:").append(fAllgemeinerRabattsatz);
		returnStringBuffer.append("bMindermengenzuschlag:").append(bMindermengenzuschlag);
		returnStringBuffer.append("nProvision:").append(nProvision);
		returnStringBuffer.append("cProvisiontext:").append(cProvisiontext);
		returnStringBuffer.append("zahlungszielIId:").append(getZahlungszielIId());
		returnStringBuffer.append("lieferartIId:").append(getLieferartIId());
		returnStringBuffer.append("spediteurIId:").append(getSpediteurIId());
		returnStringBuffer.append("tGedruckt:").append(tGedruckt);
		returnStringBuffer.append("tFibuuebernahme:").append(tFibuuebernahme);
		returnStringBuffer.append("cKopftextuebersteuert:").append(cKopftextuebersteuert);
		returnStringBuffer.append("cFusstextuebersteuert:").append(cFusstextuebersteuert);
		returnStringBuffer.append("tStorniert:").append(tStorniert);
		returnStringBuffer.append("personalIIdStorniert:").append(personalIIdStorniert);
		returnStringBuffer.append("tBezahltdatum:").append(tBezahltdatum);
		returnStringBuffer.append("tMahnsperrebis:").append(tMahnsperrebis);
		returnStringBuffer.append("tAnlegen:").append(tAnlegen);
		returnStringBuffer.append("personalIIdAnlegen:").append(personalIIdAnlegen);
		returnStringBuffer.append("tAendern:").append(tAendern);
		returnStringBuffer.append("personalIIdAendern:").append(personalIIdAendern);
		returnStringBuffer.append("tManuellerledigt:").append(tManuellerledigt);
		returnStringBuffer.append("personalIIdManuellerledigt:").append(personalIIdManuellerledigt);
		returnStringBuffer.append("cBestellnummer:").append(cBestellnummer);
		returnStringBuffer.append("bReversecharge:").append(bReversecharge);
		returnStringBuffer.append("kundeIIdStatistikadresse:").append(kundeIIdStatistikadresse);
		returnStringBuffer.append("personalIIdVertreter:").append(personalIIdVertreter);
		returnStringBuffer.append("gutschriftsgrundIId:").append(gutschriftsgrundIId);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}

	public boolean isAnzahlungsRechnung() {
		return RechnungFac.RECHNUNGART_ANZAHLUNG.equals(getRechnungartCNr());
	}

	public boolean isSchlussRechnung() {
		return RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG.equals(getRechnungartCNr());
	}

	public boolean isStorniert() {
		return RechnungFac.STATUS_STORNIERT.equals(getStatusCNr());
	}

	public boolean isAngelegt() {
		return RechnungFac.STATUS_ANGELEGT.equals(getStatusCNr());
	}
	
	public boolean isBezahlt() {
		return RechnungFac.STATUS_BEZAHLT.equals(getStatusCNr());
	}

	public ReversechargeartDto getRcArtDto() {
		return rcArtDto;
	}

	public void setRcArt(ReversechargeartDto rcArtDto) {
		this.rcArtDto = rcArtDto;
	}

	public Integer getReversechargeartId() {
		return rcartId;
	}

	public void setReversechargeartId(Integer reversechargeartId) {
		this.rcartId = reversechargeartId;
	}

	public Timestamp getTElektronisch() {
		return tElektronisch;
	}

	public void setTElektronisch(Timestamp tElektronisch) {
		this.tElektronisch = tElektronisch;
	}

	public Integer getPersonalIIdElektronisch() {
		return personalIIdElektronisch;
	}

	public void setPersonalIIdElektronisch(Integer personalIIdElektronisch) {
		this.personalIIdElektronisch = personalIIdElektronisch;
	}
}
