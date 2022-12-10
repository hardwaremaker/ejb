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
package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;

import com.lp.server.eingangsrechnung.ejb.Eingangsrechnung;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCnr;
import com.lp.server.system.service.HvDtoLogIgnore;
import com.lp.server.util.IIId;
import com.lp.server.util.IModificationData;
import com.lp.server.util.logger.LogEventPayload;


@HvDtoLogClass(name = HvDtoLogClass.EINGANGSRECHNUNG)
public class EingangsrechnungDto implements Serializable, IIId, LogEventPayload, IModificationData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cNr;
	private Integer iGeschaeftsjahr;
	private String mandantCNr;
	private String eingangsrechnungartCNr;
	private Date dBelegdatum;
	private Date dFreigabedatum;
	private Integer lieferantIId;
	private String cText;
	private Integer kostenstelleIId;
	private Integer zahlungszielIId;
	private Integer bestellungIId;
	private Integer kontoIId;
	private BigDecimal nBetrag;
	private BigDecimal nBetragfw;
	private BigDecimal nUstBetrag;
	private BigDecimal nUstBetragfw;
	private Integer mwstsatzIId;
	private BigDecimal nKurs;
	private String waehrungCNr;
	private String statusCNr;
	private Date dBezahltdatum;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Integer personalIIdManuellerledigt;
	private Timestamp tManuellerledigt;
	private String cLieferantenrechnungsnummer;
	private Timestamp tFibuuebernahme;
	private String cKundendaten;
	private Timestamp tGedruckt;
	private Timestamp tMahndatum;
	private Integer mahnstufeIId;

	private Short bMitpositionen;
	private String cWeartikel;

	private Timestamp tWiederholenderledigt;
	private Integer personalIIdWiederholenderledigt;

	private String cKopftextuebersteuert;
	private String cFusstextuebersteuert;
	
	private String cZollimportpapier;
	private Timestamp tZollimportpapier;
	private Integer personalIIdZollimportpapier;
	private Integer eingangsrechnungIdZollimport;
	
	private Short bReversecharge;
	private Short bIgErwerb;
	private Integer eingangsrechnungIIdNachfolger;
	private String wiederholungsintervallCNr;

	private java.sql.Timestamp tGeprueft;

	public java.sql.Timestamp getTGeprueft() {
		return tGeprueft;
	}

	public void setTGeprueft(java.sql.Timestamp tGeprueft) {
		this.tGeprueft = tGeprueft;
	}

	public Integer getPersonalIIdGeprueft() {
		return personalIIdGeprueft;
	}

	public void setPersonalIIdGeprueft(Integer personalIIdGeprueft) {
		this.personalIIdGeprueft = personalIIdGeprueft;
	}

	@Column(name = "PERSONAL_I_ID_GEPRUEFT")
	private Integer personalIIdGeprueft;
	
	private Integer reversechargeartId ;
	
	public Short getBMitpositionen() {
		return bMitpositionen;
	}

	public void setBMitpositionen(Short bMitpositionen) {
		this.bMitpositionen = bMitpositionen;
	}
	
	private Integer personalIIdAbwBankverbindung;

	public Integer getPersonalIIdAbwBankverbindung() {
		return personalIIdAbwBankverbindung;
	}

	public void setPersonalIIdAbwBankverbindung(Integer personalIIdAbwBankverbindung) {
		this.personalIIdAbwBankverbindung = personalIIdAbwBankverbindung;
	}

	public String getCWeartikel() {
		return cWeartikel;
	}

	public void setCWeartikel(String cWeartikel) {
		this.cWeartikel = cWeartikel;
	}
	
	public String getCKopftextuebersteuert() {
		return this.cKopftextuebersteuert;
	}

	public void setCKopftextuebersteuert(String cKopftextuebersteuert) {
		this.cKopftextuebersteuert = cKopftextuebersteuert;
	}

	public String getCFusstextuebersteuert() {
		return this.cFusstextuebersteuert;
	}

	public void setCFusstextuebersteuert(String cFusstextuebersteuert) {
		this.cFusstextuebersteuert = cFusstextuebersteuert;
	}

	public String getCZollimportpapier() {
		return cZollimportpapier;
	}

	public void setCZollimportpapier(String cZollimportpapier) {
		this.cZollimportpapier = cZollimportpapier;
	}


	public Timestamp getTZollimportpapier() {
		return tZollimportpapier;
	}

	public void setTZollimportpapier(Timestamp tZollimportpapier) {
		this.tZollimportpapier = tZollimportpapier;
	}

	public Integer getPersonalIIdZollimportpapier() {
		return personalIIdZollimportpapier;
	}

	public void setPersonalIIdZollimportpapier(
			Integer personalIIdZollimportpapier) {
		this.personalIIdZollimportpapier = personalIIdZollimportpapier;
	}

	@HvDtoLogIdCnr(entityClass = Eingangsrechnung.class)
	public Integer getEingangsrechnungIdZollimport() {
		return eingangsrechnungIdZollimport;
	}

	public void setEingangsrechnungIdZollimport(
			Integer eingangsrechnungIdZollimport) {
		this.eingangsrechnungIdZollimport = eingangsrechnungIdZollimport;
	}

	public Timestamp getTWiederholenderledigt() {
		return tWiederholenderledigt;
	}

	public void setTWiederholenderledigt(Timestamp tWiederholenderledigt) {
		this.tWiederholenderledigt = tWiederholenderledigt;
	}

	public Integer getPersonalIIdWiederholenderledigt() {
		return personalIIdWiederholenderledigt;
	}

	public void setPersonalIIdWiederholenderledigt(
			Integer personalIIdWiederholenderledigt) {
		this.personalIIdWiederholenderledigt = personalIIdWiederholenderledigt;
	}

	public Timestamp getTMahndatum() {
		return this.tMahndatum;
	}

	public void setTMahndatum(Timestamp tMahndatum) {
		this.tMahndatum = tMahndatum;
	}

	public Integer getMahnstufeIId() {
		return this.mahnstufeIId;
	}

	public void setMahnstufeIId(Integer mahnstufeIId) {
		this.mahnstufeIId = mahnstufeIId;
	}

//	public Short getBReversecharge() {
//		return this.bReversecharge;
//	}
	
	/**
	 * @deprecated 
	 */
	public boolean isReversecharge() {
//		return Helper.short2boolean(this.bReversecharge);
		return bReversecharge == null ? false : bReversecharge > 0 ;
	}

	/**
	 * @deprecated 
	 */
	public void setBReversecharge(Short bReversecharge) {
		this.bReversecharge = bReversecharge;
	}


	public Integer getEingangsrechnungIIdNachfolger() {
		return eingangsrechnungIIdNachfolger;
	}

	public void setEingangsrechnungIIdNachfolger(
			Integer eingangsrechnungIIdNachfolger) {
		this.eingangsrechnungIIdNachfolger = eingangsrechnungIIdNachfolger;
	}


	public void setWiederholungsintervallCNr(String wiederholungsintervallCNr) {
		this.wiederholungsintervallCNr = wiederholungsintervallCNr;
	}

	public String getWiederholungsintervallCNr() {
		return wiederholungsintervallCNr;
	}

	public Timestamp getTGedruckt() {
		return tGedruckt;
	}

	public void setTGedruckt(Timestamp tGedruckt) {
		this.tGedruckt = tGedruckt;
	}

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

	public Integer getIGeschaeftsjahr() {
		return iGeschaeftsjahr;
	}

	public void setIGeschaeftsjahr(Integer iGeschaeftsjahr) {
		this.iGeschaeftsjahr = iGeschaeftsjahr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getEingangsrechnungartCNr() {
		return eingangsrechnungartCNr;
	}

	public void setEingangsrechnungartCNr(String eingangsrechnungartCNr) {
		this.eingangsrechnungartCNr = eingangsrechnungartCNr;
	}

	public Date getDBelegdatum() {
		return dBelegdatum;
	}

	public void setDBelegdatum(Date dBelegdatum) {
		this.dBelegdatum = dBelegdatum;
	}

	public Date getDFreigabedatum() {
		return dFreigabedatum;
	}

	public void setDFreigabedatum(Date dFreigabedatum) {
		this.dFreigabedatum = dFreigabedatum;
	}

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	public String getCText() {
		return cText;
	}

	public void setCText(String cText) {
		this.cText = cText;
	}

	public Integer getKostenstelleIId() {
		return kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public Integer getZahlungszielIId() {
		return zahlungszielIId;
	}

	public void setZahlungszielIId(Integer zahlungszielIId) {
		this.zahlungszielIId = zahlungszielIId;
	}

	public Integer getBestellungIId() {
		return bestellungIId;
	}

	public void setBestellungIId(Integer bestellungIId) {
		this.bestellungIId = bestellungIId;
	}

	public Integer getKontoIId() {
		return kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public BigDecimal getNBetrag() {
		return nBetrag;
	}

	public void setNBetrag(BigDecimal nBetrag) {
		this.nBetrag = nBetrag;
	}

	public BigDecimal getNBetragfw() {
		return nBetragfw;
	}

	public void setNBetragfw(BigDecimal nBetragfw) {
		this.nBetragfw = nBetragfw;
	}

	public BigDecimal getNUstBetrag() {
		return nUstBetrag;
	}

	public void setNUstBetrag(BigDecimal nUstBetrag) {
		this.nUstBetrag = nUstBetrag;
	}

	public BigDecimal getNUstBetragfw() {
		return nUstBetragfw;
	}

	public void setNUstBetragfw(BigDecimal nUstBetragfw) {
		this.nUstBetragfw = nUstBetragfw;
	}

	public Integer getMwstsatzIId() {
		return mwstsatzIId;
	}

	public void setMwstsatzIId(Integer mwstsatzIId) {
		this.mwstsatzIId = mwstsatzIId;
	}

	public BigDecimal getNKurs() {
		return nKurs;
	}

	public void setNKurs(BigDecimal nKurs) {
		this.nKurs = nKurs;
	}

	public String getWaehrungCNr() {
		return waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	public Date getDBezahltdatum() {
		return dBezahltdatum;
	}

	public void setDBezahltdatum(Date dBezahltdatum) {
		this.dBezahltdatum = dBezahltdatum;
	}
	@HvDtoLogIgnore
	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}
	@HvDtoLogIgnore
	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}
	@HvDtoLogIgnore
	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}
	@HvDtoLogIgnore
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

	public String getCLieferantenrechnungsnummer() {
		return cLieferantenrechnungsnummer;
	}

	public void setCLieferantenrechnungsnummer(
			String cLieferantenrechnungsnummer) {
		this.cLieferantenrechnungsnummer = cLieferantenrechnungsnummer;
	}

	public Timestamp getTFibuuebernahme() {
		return tFibuuebernahme;
	}

	public void setTFibuuebernahme(Timestamp tFibuuebernahme) {
		this.tFibuuebernahme = tFibuuebernahme;
	}

	public String getCKundendaten() {
		return cKundendaten;
	}

	public void setCKundendaten(String cKundendaten) {
		this.cKundendaten = cKundendaten;
	}

	public void setBIgErwerb(Short bIGErwerb) {
		this.bIgErwerb = bIGErwerb;
	}

	public Short getBIgErwerb() {
		return bIgErwerb;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof EingangsrechnungDto))
			return false;
		EingangsrechnungDto that = (EingangsrechnungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		if (!(that.iGeschaeftsjahr == null ? this.iGeschaeftsjahr == null
				: that.iGeschaeftsjahr.equals(this.iGeschaeftsjahr)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.eingangsrechnungartCNr == null ? this.eingangsrechnungartCNr == null
				: that.eingangsrechnungartCNr
						.equals(this.eingangsrechnungartCNr)))
			return false;
		if (!(that.dBelegdatum == null ? this.dBelegdatum == null
				: that.dBelegdatum.equals(this.dBelegdatum)))
			return false;
		if (!(that.dFreigabedatum == null ? this.dFreigabedatum == null
				: that.dFreigabedatum.equals(this.dFreigabedatum)))
			return false;
		if (!(that.lieferantIId == null ? this.lieferantIId == null
				: that.lieferantIId.equals(this.lieferantIId)))
			return false;
		if (!(that.cText == null ? this.cText == null : that.cText
				.equals(this.cText)))
			return false;
		if (!(that.kostenstelleIId == null ? this.kostenstelleIId == null
				: that.kostenstelleIId.equals(this.kostenstelleIId)))
			return false;
		if (!(that.zahlungszielIId == null ? this.zahlungszielIId == null
				: that.zahlungszielIId.equals(this.zahlungszielIId)))
			return false;
		if (!(that.bestellungIId == null ? this.bestellungIId == null
				: that.bestellungIId.equals(this.bestellungIId)))
			return false;
		if (!(that.kontoIId == null ? this.kontoIId == null : that.kontoIId
				.equals(this.kontoIId)))
			return false;
		if (!(that.nBetrag == null ? this.nBetrag == null : that.nBetrag
				.equals(this.nBetrag)))
			return false;
		if (!(that.nBetragfw == null ? this.nBetragfw == null : that.nBetragfw
				.equals(this.nBetragfw)))
			return false;
		if (!(that.nUstBetrag == null ? this.nUstBetrag == null
				: that.nUstBetrag.equals(this.nUstBetrag)))
			return false;
		if (!(that.nUstBetragfw == null ? this.nUstBetragfw == null
				: that.nUstBetragfw.equals(this.nUstBetragfw)))
			return false;
		if (!(that.mwstsatzIId == null ? this.mwstsatzIId == null
				: that.mwstsatzIId.equals(this.mwstsatzIId)))
			return false;
		if (!(that.nKurs == null ? this.nKurs == null : that.nKurs
				.equals(this.nKurs)))
			return false;
		if (!(that.waehrungCNr == null ? this.waehrungCNr == null
				: that.waehrungCNr.equals(this.waehrungCNr)))
			return false;
		if (!(that.statusCNr == null ? this.statusCNr == null : that.statusCNr
				.equals(this.statusCNr)))
			return false;
		if (!(that.dBezahltdatum == null ? this.dBezahltdatum == null
				: that.dBezahltdatum.equals(this.dBezahltdatum)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.personalIIdManuellerledigt == null ? this.personalIIdManuellerledigt == null
				: that.personalIIdManuellerledigt
						.equals(this.personalIIdManuellerledigt)))
			return false;
		if (!(that.tManuellerledigt == null ? this.tManuellerledigt == null
				: that.tManuellerledigt.equals(this.tManuellerledigt)))
			return false;
		if (!(that.cLieferantenrechnungsnummer == null ? this.cLieferantenrechnungsnummer == null
				: that.cLieferantenrechnungsnummer
						.equals(this.cLieferantenrechnungsnummer)))
			return false;
		if (!(that.tFibuuebernahme == null ? this.tFibuuebernahme == null
				: that.tFibuuebernahme.equals(this.tFibuuebernahme)))
			return false;
		if (!(that.cKundendaten == null ? this.cKundendaten == null
				: that.cKundendaten.equals(this.cKundendaten)))
			return false;
		if (!(that.tGedruckt == null ? this.tGedruckt == null : that.tGedruckt
				.equals(this.tGedruckt)))
			return false;
		if (!(that.bReversecharge == null ? this.bReversecharge == null
				: that.bReversecharge.equals(this.bReversecharge)))
			return false;
		if (!(that.bIgErwerb == null ? this.bIgErwerb == null : that.bIgErwerb
				.equals(this.bIgErwerb)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.iGeschaeftsjahr.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.eingangsrechnungartCNr.hashCode();
		result = 37 * result + this.dBelegdatum.hashCode();
		result = 37 * result + this.dFreigabedatum.hashCode();
		result = 37 * result + this.lieferantIId.hashCode();
		result = 37 * result + this.cText.hashCode();
		result = 37 * result + this.kostenstelleIId.hashCode();
		result = 37 * result + this.zahlungszielIId.hashCode();
		result = 37 * result + this.bestellungIId.hashCode();
		result = 37 * result + this.kontoIId.hashCode();
		result = 37 * result + this.nBetrag.hashCode();
		result = 37 * result + this.nBetragfw.hashCode();
		result = 37 * result + this.nUstBetrag.hashCode();
		result = 37 * result + this.nUstBetragfw.hashCode();
		result = 37 * result + this.mwstsatzIId.hashCode();
		result = 37 * result + this.nKurs.hashCode();
		result = 37 * result + this.waehrungCNr.hashCode();
		result = 37 * result + this.statusCNr.hashCode();
		result = 37 * result + this.dBezahltdatum.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdManuellerledigt.hashCode();
		result = 37 * result + this.tManuellerledigt.hashCode();
		result = 37 * result + this.cLieferantenrechnungsnummer.hashCode();
		result = 37 * result + this.tFibuuebernahme.hashCode();
		result = 37 * result + this.cKundendaten.hashCode();
		result = 37 * result + this.tGedruckt.hashCode();
		result = 37 * result + this.bReversecharge.hashCode();
		result = 37 * result + this.bIgErwerb.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cNr;
		returnString += ", " + iGeschaeftsjahr;
		returnString += ", " + mandantCNr;
		returnString += ", " + eingangsrechnungartCNr;
		returnString += ", " + dBelegdatum;
		returnString += ", " + dFreigabedatum;
		returnString += ", " + lieferantIId;
		returnString += ", " + cText;
		returnString += ", " + kostenstelleIId;
		returnString += ", " + zahlungszielIId;
		returnString += ", " + bestellungIId;
		returnString += ", " + kontoIId;
		returnString += ", " + nBetrag;
		returnString += ", " + nBetragfw;
		returnString += ", " + nUstBetrag;
		returnString += ", " + nUstBetragfw;
		returnString += ", " + mwstsatzIId;
		returnString += ", " + nKurs;
		returnString += ", " + waehrungCNr;
		returnString += ", " + statusCNr;
		returnString += ", " + dBezahltdatum;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdManuellerledigt;
		returnString += ", " + tManuellerledigt;
		returnString += ", " + cLieferantenrechnungsnummer;
		returnString += ", " + tFibuuebernahme;
		returnString += ", " + cKundendaten;
		returnString += ", " + tGedruckt;
		returnString += ", " + bReversecharge;
		returnString += ", " + bIgErwerb;
		return returnString;
	}

	public boolean isEingangsrechnung() {
		return EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG.equals(getEingangsrechnungartCNr()) ;
	}
	
	public boolean isZusatzkosten() {
		return EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN.equals(getEingangsrechnungartCNr()) ;
	}
	
	public boolean isGutschrift() {
		return EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT.equals(getEingangsrechnungartCNr()) ;
	}
	
	public boolean isAnzahlung() {
		return EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG.equals(getEingangsrechnungartCNr()) ;
	}
	
	public boolean isSchlussrechnung() {
		return EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG.equals(getEingangsrechnungartCNr()) ;
	}
	
	public boolean isStorniert() {
		return EingangsrechnungFac.STATUS_STORNIERT.equals(getStatusCNr());
	}
	
	@Override
	public String asString() {
		return "ER: [" + getCNr() + " (id:" + getIId() + ")" 
			+ ", Belegdatum:" + dBelegdatum 
			+ ", Betrag:" + nBetrag
			+ ", Art:" + getEingangsrechnungartCNr() + "]";
	}

	public Integer getReversechargeartId() {
		return reversechargeartId;
	}

	public void setReversechargeartId(Integer reversechargeartId) {
		this.reversechargeartId = reversechargeartId;
	}
}
