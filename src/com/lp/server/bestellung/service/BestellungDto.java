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
package com.lp.server.bestellung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.util.IIId;
import com.lp.server.util.IVersionable;
import com.lp.service.BelegDto;

@HvDtoLogClass(name = HvDtoLogClass.BESTELLUNG)
public class BestellungDto extends BelegDto implements Serializable, IIId, IVersionable {

	private static final long serialVersionUID = 1L;
	private String bestellungartCNr;
	private Date dBelegdatum;
	private Integer lieferantIIdBestelladresse;
	private Integer ansprechpartnerIId;
	private Integer personalIIdAnforderer;
	private Integer lieferantIIdRechnungsadresse;
	private Timestamp dLiefertermin;
	private Short bTeillieferungMoeglich;
	private Integer iLeihtage;
	private BigDecimal nBestellwert;
	private Integer bestelltextIIdKopftext;
	private String cKopftextUebersteuert;
	private Integer bestelltextIIdFusstext;
	private String cFusstextUebersteuert;
	private Timestamp tGedruckt;
	private Integer personalIIdStorniert;
	private Timestamp tStorniert;
	private Integer partnerIIdLieferadresse;
	private Integer anfrageIId;
	private Integer iBestellungIIdRahmenbestellung;
	private Integer iPersonalIIdManuellGeliefert;
	private Timestamp tManuellGeliefert;
	private String xInternerKommentar;
	private String xExternerKommentar;
	private Date tMahnsperreBis;
	private Integer auftragIId;
	private Integer iMahnstufeIId;
	private Timestamp tAenderungsbestellung;
	private Timestamp tVersandzeitpunkt;
	private Integer ansprechpartnerIIdLieferadresse;
	private Integer iAenderungsbestellungVersion;

	private Integer personalIIdInterneranforderer;

	public Integer getPersonalIIdInterneranforderer() {
		return personalIIdInterneranforderer;
	}

	public void setPersonalIIdInterneranforderer(Integer personalIIdInterneranforderer) {
		this.personalIIdInterneranforderer = personalIIdInterneranforderer;
	}

	private Timestamp tVollstaendigGeliefert;

	public Timestamp getTVollstaendigGeliefert() {
		return tVollstaendigGeliefert;
	}

	public void setTTVollstaendigGeliefert(Timestamp tVollstaendigGeliefert) {
		this.tVollstaendigGeliefert = tVollstaendigGeliefert;
	}

	private Timestamp tKommissionierungGeplant;
	private Timestamp tKommissionierungDurchgefuehrt;
	private Timestamp tUebergabeTechnik;

	public Timestamp getTKommissionierungGeplant() {
		return tKommissionierungGeplant;
	}

	public void setTKommissionierungGeplant(Timestamp tKommissionierungGeplant) {
		this.tKommissionierungGeplant = tKommissionierungGeplant;
	}

	public Timestamp getTKommissionierungDurchgefuehrt() {
		return tKommissionierungDurchgefuehrt;
	}

	public void setTKommissionierungDurchgefuehrt(Timestamp tKommissionierungDurchgefuehrt) {
		this.tKommissionierungDurchgefuehrt = tKommissionierungDurchgefuehrt;
	}

	public Timestamp getTUebergabeTechnik() {
		return tUebergabeTechnik;
	}

	public void setTUebergabeTechnik(Timestamp tUebergabeTechnik) {
		this.tUebergabeTechnik = tUebergabeTechnik;
	}

	private String cLieferantenangebot;

	public String getCLieferantenangebot() {
		return cLieferantenangebot;
	}

	public void setCLieferantenangebot(String cLieferantenangebot) {
		this.cLieferantenangebot = cLieferantenangebot;
	}

	private BigDecimal nKorrekturbetrag;

	public BigDecimal getNKorrekturbetrag() {
		return nKorrekturbetrag;
	}

	public void setNKorrekturbetrag(BigDecimal nKorrekturbetrag) {
		this.nKorrekturbetrag = nKorrekturbetrag;
	}

	public Short getBPoenale() {
		return bPoenale;
	}

	public void setBPoenale(Short bPoenale) {
		this.bPoenale = bPoenale;
	}

	private Integer partnerIIdAbholadresse;
	private Short bPoenale;

	public Integer getPartnerIIdAbholadresse() {
		return partnerIIdAbholadresse;
	}

	public void setPartnerIIdAbholadresse(Integer partnerIIdAbholadresse) {
		this.partnerIIdAbholadresse = partnerIIdAbholadresse;
	}

	public Integer getAnsprechpartnerIIdAbholadresse() {
		return ansprechpartnerIIdAbholadresse;
	}

	public void setAnsprechpartnerIIdAbholadresse(Integer ansprechpartnerIIdAbholadresse) {
		this.ansprechpartnerIIdAbholadresse = ansprechpartnerIIdAbholadresse;
	}

	private Integer ansprechpartnerIIdAbholadresse;

	public Date getTMahnsperreBis() {
		return tMahnsperreBis;
	}

	public void setTMahnsperreBis(Date tMahnsperreBis) {
		this.tMahnsperreBis = tMahnsperreBis;
	}

	public String getXInternerKommentar() {
		return xInternerKommentar;
	}

	public void setXInternerKommentar(String xInternerKommentar) {
		this.xInternerKommentar = xInternerKommentar;
	}

	public String getXExternerKommentar() {
		return xExternerKommentar;
	}

	public void setXExternerKommentar(String xExternerKommentar) {
		this.xExternerKommentar = xExternerKommentar;
	}

	public Integer getIBestellungIIdRahmenbestellung() {
		return iBestellungIIdRahmenbestellung;
	}

	public void setIBestellungIIdRahmenbestellung(Integer iBestellungIIdRahmenbestellung) {
		this.iBestellungIIdRahmenbestellung = iBestellungIIdRahmenbestellung;
	}

	public String getBestellungartCNr() {
		return bestellungartCNr;
	}

	public void setBestellungartCNr(String bestellungartCNr) {
		this.bestellungartCNr = bestellungartCNr;
	}

	public Date getDBelegdatum() {
		return dBelegdatum;
	}

	public void setDBelegdatum(Date dBelegdatum) {
		this.dBelegdatum = dBelegdatum;
	}

	public Integer getLieferantIIdBestelladresse() {
		return lieferantIIdBestelladresse;
	}

	public void setLieferantIIdBestelladresse(Integer lieferantIIdBestelladresse) {
		this.lieferantIIdBestelladresse = lieferantIIdBestelladresse;
	}

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getPersonalIIdAnforderer() {
		return personalIIdAnforderer;
	}

	public void setPersonalIIdAnforderer(Integer personalIIdAnforderer) {
		this.personalIIdAnforderer = personalIIdAnforderer;
	}

	public Integer getLieferantIIdRechnungsadresse() {
		return lieferantIIdRechnungsadresse;
	}

	public void setLieferantIIdRechnungsadresse(Integer lieferantIIdRechnungsadresse) {
		this.lieferantIIdRechnungsadresse = lieferantIIdRechnungsadresse;
	}

	public Timestamp getDLiefertermin() {
		return dLiefertermin;
	}

	public void setDLiefertermin(Timestamp dLiefertermin) {
		this.dLiefertermin = dLiefertermin;
	}

	public Short getBTeillieferungMoeglich() {
		return bTeillieferungMoeglich;
	}

	public void setBTeillieferungMoeglich(Short bTeillieferungMoeglich) {
		this.bTeillieferungMoeglich = bTeillieferungMoeglich;
	}

	public Integer getILeihtage() {
		return iLeihtage;
	}

	public void setILeihtage(Integer iLeihtage) {
		this.iLeihtage = iLeihtage;
	}

	public BigDecimal getNBestellwert() {
		return nBestellwert;
	}

	private BigDecimal nTransportkosten;

	public BigDecimal getNTransportkosten() {
		return nTransportkosten;
	}

	public void setNTransportkosten(BigDecimal nTransportkosten) {
		this.nTransportkosten = nTransportkosten;
	}

	public void setNBestellwert(BigDecimal nBestellwert) {
		this.nBestellwert = nBestellwert;
	}

	public Integer getBestelltextIIdKopftext() {
		return bestelltextIIdKopftext;
	}

	public void setBestelltextIIdKopftext(Integer bestelltextIIdKopftext) {
		this.bestelltextIIdKopftext = bestelltextIIdKopftext;
	}

	public String getCKopftextUebersteuert() {
		return cKopftextUebersteuert;
	}

	public void setCKopftextUebersteuert(String cKopftextUebersteuert) {
		this.cKopftextUebersteuert = cKopftextUebersteuert;
	}

	public Integer getBestelltextIIdFusstext() {
		return bestelltextIIdFusstext;
	}

	public void setBestelltextIIdFusstext(Integer bestelltextIIdFusstext) {
		this.bestelltextIIdFusstext = bestelltextIIdFusstext;
	}

	public String getCFusstextUebersteuert() {
		return cFusstextUebersteuert;
	}

	public void setCFusstextUebersteuert(String cFusstextUebersteuert) {
		this.cFusstextUebersteuert = cFusstextUebersteuert;
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

	public Integer getPartnerIIdLieferadresse() {
		return partnerIIdLieferadresse;
	}

	public void setPartnerIIdLieferadresse(Integer partnerIIdLieferadresse) {
		this.partnerIIdLieferadresse = partnerIIdLieferadresse;
	}

	public Integer getAnfrageIId() {
		return anfrageIId;
	}

	public void setAnfrageIId(Integer iIdAnfrageI) {
		this.anfrageIId = iIdAnfrageI;
	}

	public Integer getAuftragIId() {
		return auftragIId;
	}

	public void setAuftragIId(Integer iIdAuftragI) {
		this.auftragIId = iIdAuftragI;
	}

	public Integer getIPersonalIIdManuellGeliefert() {
		return iPersonalIIdManuellGeliefert;
	}

	public void setIPersonalIIdManuellGeliefert(Integer iPersonalIIdManuellGeliefert) {
		this.iPersonalIIdManuellGeliefert = iPersonalIIdManuellGeliefert;
	}

	public Timestamp getTManuellGeliefert() {
		return tManuellGeliefert;
	}

	public void setTManuellGeliefert(Timestamp tManuellGeliefert) {
		this.tManuellGeliefert = tManuellGeliefert;
	}

	public Integer getIMahnstufeIId() {
		return this.iMahnstufeIId;
	}

	public void setIMahnstufeIId(Integer iMahnstufeIId) {
		this.iMahnstufeIId = iMahnstufeIId;
	}

	public Timestamp getTAenderungsbestellung() {
		return this.tAenderungsbestellung;
	}

	public void setTAenderungsbestellung(Timestamp tAenderungsbestellung) {
		this.tAenderungsbestellung = tAenderungsbestellung;
	}

	public Timestamp getTVersandzeitpunkt() {
		return this.tVersandzeitpunkt;
	}

	public void setTVersandzeitpunkt(Timestamp tVersandzeitpunkt) {
		this.tVersandzeitpunkt = tVersandzeitpunkt;
	}

	public Integer getAnsprechpartnerIIdLieferadresse() {
		return ansprechpartnerIIdLieferadresse;
	}

	public void setAnsprechpartnerIIdLieferadresse(Integer ansprechpartnerIIdLieferadresse) {
		this.ansprechpartnerIIdLieferadresse = ansprechpartnerIIdLieferadresse;
	}

	private String cLieferartort;

	public String getCLieferartort() {
		return cLieferartort;
	}

	public void setCLieferartort(String cLieferartort) {
		this.cLieferartort = cLieferartort;
	}

	public Integer getIVersion() {
		return iAenderungsbestellungVersion;
	}

	public void setIVersion(Integer iVersion) {
		this.iAenderungsbestellungVersion = iVersion;
	}

	public String toString() {

		String returnString = super.toString();

		returnString += ", " + bestellungartCNr;
		returnString += ", " + dBelegdatum;
		returnString += ", " + lieferantIIdBestelladresse;
		returnString += ", " + ansprechpartnerIId;
		returnString += ", " + personalIIdAnforderer;
		returnString += ", " + lieferantIIdRechnungsadresse;
		returnString += ", " + dLiefertermin;
		returnString += ", " + bTeillieferungMoeglich;
		returnString += ", " + iLeihtage;
		returnString += ", " + fAllgemeinerRabattsatz;
		returnString += ", " + nBestellwert;
		returnString += ", " + bestelltextIIdKopftext;
		returnString += ", " + cKopftextUebersteuert;
		returnString += ", " + bestelltextIIdFusstext;
		returnString += ", " + cFusstextUebersteuert;
		returnString += ", " + tGedruckt;
		returnString += ", " + personalIIdStorniert;
		returnString += ", " + tStorniert;
		returnString += ", " + partnerIIdLieferadresse;
		returnString += ", " + anfrageIId;
		returnString += ", " + iBestellungIIdRahmenbestellung;
		returnString += ", " + iPersonalIIdManuellGeliefert;
		returnString += ", " + tManuellGeliefert;
		returnString += ", " + xInternerKommentar;
		returnString += ", " + xExternerKommentar;
		returnString += ", " + tMahnsperreBis;
		returnString += ", " + auftragIId;
		returnString += ", " + iMahnstufeIId;
		returnString += ", " + tAenderungsbestellung;
		returnString += ", " + tVersandzeitpunkt;
		returnString += ", " + ansprechpartnerIIdLieferadresse;
		return returnString;
	}

	public Object clone() {
		BestellungDto bestellungDto = (BestellungDto) cloneAsBelegDto(new BestellungDto());

		// iId, cNr null
		bestellungDto.bestellungartCNr = this.bestellungartCNr;
		bestellungDto.dBelegdatum = this.dBelegdatum;
		bestellungDto.lieferantIIdBestelladresse = this.lieferantIIdBestelladresse;
		bestellungDto.ansprechpartnerIId = this.ansprechpartnerIId;
		bestellungDto.personalIIdAnforderer = this.personalIIdAnforderer;
		bestellungDto.lieferantIIdRechnungsadresse = this.lieferantIIdRechnungsadresse;
		bestellungDto.dLiefertermin = this.dLiefertermin;
		bestellungDto.bTeillieferungMoeglich = this.bTeillieferungMoeglich;
		bestellungDto.iLeihtage = this.iLeihtage;
		bestellungDto.fAllgemeinerRabattsatz = this.fAllgemeinerRabattsatz;
		bestellungDto.partnerIIdLieferadresse = this.partnerIIdLieferadresse;
		bestellungDto.xInternerKommentar = this.xInternerKommentar;
		bestellungDto.xExternerKommentar = this.xExternerKommentar;
		bestellungDto.tMahnsperreBis = this.tMahnsperreBis;
		bestellungDto.fWechselkursmandantwaehrungzubelegwaehrung = this.fWechselkursmandantwaehrungzubelegwaehrung;
		// PJ 16719 bestellungDto.iMahnstufeIId = this.iMahnstufeIId;
		bestellungDto.tAenderungsbestellung = this.tAenderungsbestellung;
		bestellungDto.tVersandzeitpunkt = this.tVersandzeitpunkt;
		bestellungDto.ansprechpartnerIIdLieferadresse = this.ansprechpartnerIIdLieferadresse;
		bestellungDto.bPoenale = this.bPoenale;
		bestellungDto.iBestellungIIdRahmenbestellung = this.iBestellungIIdRahmenbestellung;
		bestellungDto.nTransportkosten = this.nTransportkosten;
		// Bestellwert, Kopftext, Fusstext null
		// tGedruckt, Anlegen, Aendern, Stornieren, Manuell erledigen null
		// kein Bezug auf Rahmenbestellung oder Anfrage oder Auftrag

		return bestellungDto;
	}

	public EingangsrechnungDto cloneAsEingangsrechnungDto() {
		EingangsrechnungDto eingangsrechnungDto = new EingangsrechnungDto();
		eingangsrechnungDto.setMandantCNr(this.getMandantCNr());
		eingangsrechnungDto.setLieferantIId(this.getLieferantIIdBestelladresse());
		BigDecimal bdBetrag = this.getNBestellwert();
		if (bdBetrag == null) {
			bdBetrag = BigDecimal.ZERO;
		}
		eingangsrechnungDto.setNBetrag(bdBetrag);
		BigDecimal bdBetragFw = bdBetrag.multiply(new BigDecimal(this.getFWechselkursmandantwaehrungzubelegwaehrung()));
		eingangsrechnungDto.setNBetragfw(bdBetragFw);
		eingangsrechnungDto.setNKurs(new BigDecimal(this.getFWechselkursmandantwaehrungzubelegwaehrung()));
		eingangsrechnungDto.setWaehrungCNr(this.getWaehrungCNr());
		return eingangsrechnungDto;
	}

	public boolean isErledigt() {
		return BestellungFac.BESTELLSTATUS_ERLEDIGT.equals(getStatusCNr());
	}

	public boolean isGeliefert() {
		return BestellungFac.BESTELLSTATUS_GELIEFERT.equals(getStatusCNr());
	}

	public boolean isStorniert() {
		return BestellungFac.BESTELLSTATUS_STORNIERT.equals(getStatusCNr());
	}
	
	@Override
	public boolean hasVersion() {
		return getIVersion() != null;
	}

	@Override
	public Timestamp getTVersion() {
		return getTAenderungsbestellung();
	}

	@Override
	public void setTVersion(Timestamp tVersion) {
		setTAenderungsbestellung(tVersion);
	}
}
