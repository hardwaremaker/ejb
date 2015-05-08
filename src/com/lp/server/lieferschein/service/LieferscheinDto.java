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
package com.lp.server.lieferschein.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.eingangsrechnung.ejb.Eingangsrechnung;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCnr;
import com.lp.server.system.service.HvDtoLogIgnore;
import com.lp.service.BelegVerkaufDto;

@HvDtoLogClass(name = HvDtoLogClass.LIEFERSCHEIN)
public class LieferscheinDto extends BelegVerkaufDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer rechnungIId;
	private String lieferscheinartCNr;
	private String belegartCNr;
	private Short bVerrechenbar;
	
	private Integer kundeIIdLieferadresse;
	private Integer ansprechpartnerIId;
	private Integer personalIIdVertreter;
	private Integer kundeIIdRechnungsadresse;
	private String cBezProjektbezeichnung;
	private String cBestellnummer;
	private Integer lagerIId;
	private Integer ziellagerIId;
	private Integer kostenstelleIId;
	private Timestamp tLiefertermin;
	private Timestamp tRueckgabetermin;
	private BigDecimal nGesamtwertInLieferscheinwaehrung;
	private BigDecimal nGestehungswertInMandantenwaehrung;
	private Short bMindermengenzuschlag;
	private Integer iAnzahlPakete;
	private Double fGewichtLieferung;
	private String cVersandnummer;
	private Timestamp tGedruckt;
	private Integer personalIIdManuellErledigt;
	private Timestamp tManuellErledigt;
	private Integer personalIIdStorniert;
	private Timestamp tStorniert;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Integer lieferscheintextIIdDefaultKopftext;
	private String cLieferscheinKopftextUeberschrieben;
	private Integer lieferscheintextIIdDefaultFusstext;
	private String cLieferscheinFusstextUeberschrieben;
	private Integer auftragIId;
	private String cKommission;
	private Integer begruendungIId;

	private Integer projektIId;
	private Timestamp tLieferaviso ;
	private Integer personalIIdLieferaviso ;
	
	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	private Integer ansprechpartnerIIdRechnungsadresse;

	public Integer getAnsprechpartnerIIdRechnungsadresse() {
		return ansprechpartnerIIdRechnungsadresse;
	}

	public void setAnsprechpartnerIIdRechnungsadresse(
			Integer ansprechpartnerIIdRechnungsadresse) {
		this.ansprechpartnerIIdRechnungsadresse = ansprechpartnerIIdRechnungsadresse;
	}
	private Integer eingangsrechnungIdZollexport;

	@HvDtoLogIdCnr(entityClass = Eingangsrechnung.class)
	public Integer getEingangsrechnungIdZollexport() {
		return eingangsrechnungIdZollexport;
	}

	public void setEingangsrechnungIdZollexport(
			Integer eingangsrechnungIdZollexport) {
		this.eingangsrechnungIdZollexport = eingangsrechnungIdZollexport;
	}

	private Timestamp tZollexportpapier;
	private Integer personalIIdZollexportpapier;
	private String cZollexportpapier;

	public String getCZollexportpapier() {
		return cZollexportpapier;
	}

	public void setCZollexportpapier(String cZollexportpapier) {
		this.cZollexportpapier = cZollexportpapier;
	}

	public Timestamp getTZollexportpapier() {
		return tZollexportpapier;
	}

	public void setTZollexportpapier(Timestamp tZollexportpapier) {
		this.tZollexportpapier = tZollexportpapier;
	}

	public Integer getPersonalIIdZollexportpapier() {
		return personalIIdZollexportpapier;
	}

	public void setPersonalIIdZollexportpapier(
			Integer personalIIdZollexportpapier) {
		this.personalIIdZollexportpapier = personalIIdZollexportpapier;
	}

	public Integer getBegruendungIId() {
		return begruendungIId;
	}

	public void setBegruendungIId(Integer begruendungIId) {
		this.begruendungIId = begruendungIId;
	}

	public String getCKommission() {
		return cKommission;
	}

	public void setCKommission(String kommission) {
		cKommission = kommission;
	}

	public LieferscheinDto() {
		// auftragmappings = new LieferscheinauftragmappingDto[1];
	}

	

	public Integer getRechnungIId() {
		return rechnungIId;
	}

	public void setRechnungIId(Integer rechnungIId) {
		this.rechnungIId = rechnungIId;
	}

	public String getLieferscheinartCNr() {
		return lieferscheinartCNr;
	}

	public void setLieferscheinartCNr(String lieferscheinartIId) {
		this.lieferscheinartCNr = lieferscheinartIId;
	}

	public String getBelegartCNr() {
		return this.belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public Short getBVerrechenbar() {
		return this.bVerrechenbar;
	}

	public void setBVerrechenbar(Short verrechenbar) {
		this.bVerrechenbar = verrechenbar;
	}


	public Integer getKundeIIdLieferadresse() {
		return kundeIIdLieferadresse;
	}

	public void setKundeIIdLieferadresse(Integer kundeIIdLieferadresse) {
		this.kundeIIdLieferadresse = kundeIIdLieferadresse;
	}

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getPersonalIIdVertreter() {
		return personalIIdVertreter;
	}

	public void setPersonalIIdVertreter(Integer personalIIdVertreter) {
		this.personalIIdVertreter = personalIIdVertreter;
	}

	public Integer getKundeIIdRechnungsadresse() {
		return kundeIIdRechnungsadresse;
	}

	public void setKundeIIdRechnungsadresse(Integer kundeIIdRechnungsadresse) {
		this.kundeIIdRechnungsadresse = kundeIIdRechnungsadresse;
	}

	public String getCBezProjektbezeichnung() {
		return cBezProjektbezeichnung;
	}

	public void setCBezProjektbezeichnung(String cBezProjektbezeichnung) {
		this.cBezProjektbezeichnung = cBezProjektbezeichnung;
	}

	public String getCBestellnummer() {
		return cBestellnummer;
	}

	public void setCBestellnummer(String cBestellnummer) {
		this.cBestellnummer = cBestellnummer;
	}

	public Integer getLagerIId() {
		return lagerIId;
	}

	public void setLagerIId(Integer pkLager) {
		this.lagerIId = pkLager;
	}

	public Integer getKostenstelleIId() {
		return kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public Timestamp getTLiefertermin() {
		return tLiefertermin;
	}

	public void setTLiefertermin(Timestamp tLiefertermin) {
		this.tLiefertermin = tLiefertermin;
	}

	public Timestamp getTRueckgabetermin() {
		return tRueckgabetermin;
	}

	public void setTRueckgabetermin(Timestamp tRueckgabetermin) {
		this.tRueckgabetermin = tRueckgabetermin;
	}

	public Integer getLieferartIId() {
		return lieferartIId;
	}

	public void setLieferartIId(Integer lieferartIId) {
		this.lieferartIId = lieferartIId;
	}

	public Short getBMindermengenzuschlag() {
		return bMindermengenzuschlag;
	}

	public void setBMindermengenzuschlag(Short bMindermengenzuschlag) {
		this.bMindermengenzuschlag = bMindermengenzuschlag;
	}

	public Integer getIAnzahlPakete() {
		return iAnzahlPakete;
	}

	public void setIAnzahlPakete(Integer iAnzahlPakete) {
		this.iAnzahlPakete = iAnzahlPakete;
	}

	public Double getFGewichtLieferung() {
		return fGewichtLieferung;
	}

	public void setFGewichtLieferung(Double fGewichtLieferung) {
		this.fGewichtLieferung = fGewichtLieferung;
	}

	public String getCVersandnummer() {
		return cVersandnummer;
	}

	public void setCVersandnummer(String cVersandnummer) {
		this.cVersandnummer = cVersandnummer;
	}

	public Timestamp getTGedruckt() {
		return tGedruckt;
	}

	public void setTGedruckt(Timestamp tGedruckt) {
		this.tGedruckt = tGedruckt;
	}

	public Integer getPersonalIIdManuellErledigt() {
		return this.personalIIdManuellErledigt;
	}

	public void setPersonalIIdManuellErledigt(Integer personalIIdManuellErledigt) {
		this.personalIIdManuellErledigt = personalIIdManuellErledigt;
	}

	public Timestamp getTManuellErledigt() {
		return this.tManuellErledigt;
	}

	public void setTManuellErledigt(Timestamp tManuellErledigt) {
		this.tManuellErledigt = tManuellErledigt;
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
	@HvDtoLogIgnore
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

	public BigDecimal getNGesamtwertInLieferscheinwaehrung() {
		return nGesamtwertInLieferscheinwaehrung;
	}

	public void setNGesamtwertInLieferscheinwaehrung(
			BigDecimal nGesamtwertInLieferscheinwaehrung) {
		this.nGesamtwertInLieferscheinwaehrung = nGesamtwertInLieferscheinwaehrung;
	}

	public BigDecimal getNGestehungswertInMandantenwaehrung() {
		return nGestehungswertInMandantenwaehrung;
	}

	public void setNGestehungswertInMandantenwaehrung(
			BigDecimal nGestehungswertInMandantenwaehrung) {
		this.nGestehungswertInMandantenwaehrung = nGestehungswertInMandantenwaehrung;
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

	public Integer getLieferscheintextIIdDefaultKopftext() {
		return lieferscheintextIIdDefaultKopftext;
	}

	public void setLieferscheintextIIdDefaultKopftext(
			Integer lieferscheintextIIdDefaultKopftext) {
		this.lieferscheintextIIdDefaultKopftext = lieferscheintextIIdDefaultKopftext;
	}

	public String getCLieferscheinKopftextUeberschrieben() {
		return cLieferscheinKopftextUeberschrieben;
	}

	public void setCLieferscheinKopftextUeberschrieben(
			String cLieferscheinKopftextUeberschrieben) {
		this.cLieferscheinKopftextUeberschrieben = cLieferscheinKopftextUeberschrieben;
	}

	public Integer getLieferscheintextIIdDefaultFusstext() {
		return lieferscheintextIIdDefaultFusstext;
	}

	public void setLieferscheintextIIdDefaultFusstext(
			Integer lieferscheintextIIdDefaultFusstext) {
		this.lieferscheintextIIdDefaultFusstext = lieferscheintextIIdDefaultFusstext;
	}

	public String getCLieferscheinFusstextUeberschrieben() {
		return cLieferscheinFusstextUeberschrieben;
	}

	public void setCLieferscheinFusstextUeberschrieben(
			String cLieferscheinFusstextUeberschrieben) {
		this.cLieferscheinFusstextUeberschrieben = cLieferscheinFusstextUeberschrieben;
	}

	public Integer getAuftragIId() {
		return auftragIId;
	}

	public Integer getZiellagerIId() {
		return ziellagerIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public void setZiellagerIId(Integer ziellagerIId) {
		this.ziellagerIId = ziellagerIId;
	}

	private String cLieferartort;

	public String getCLieferartort() {
		return cLieferartort;
	}

	public void setCLieferartort(String cLieferartort) {
		this.cLieferartort = cLieferartort;
	}
	
	public Timestamp getTLieferaviso() {
		return tLieferaviso;
	}

	public void setTLieferaviso(Timestamp tLieferaviso) {
		this.tLieferaviso = tLieferaviso;
	}
	
	public Integer getPersonalIIdLieferaviso() {
		return personalIIdLieferaviso;
	}

	public void setPersonalIIdLieferaviso(Integer personalIIdLieferaviso) {
		this.personalIIdLieferaviso = personalIIdLieferaviso;
	}

	// public LieferscheinauftragmappingDto[] getAuftragmappings() {
	// return this.auftragmappings;
	// }
	//
	// public void setAuftragmappings(LieferscheinauftragmappingDto[] mappings)
	// {
	// this.auftragmappings = mappings;
	// }
	//
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LieferscheinDto)) {
			return false;
		}
		LieferscheinDto that = (LieferscheinDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.rechnungIId == null ? this.rechnungIId == null
				: that.rechnungIId.equals(this.rechnungIId))) {
			return false;
		}
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr))) {
			return false;
		}
		if (!(that.lieferscheinartCNr == null ? this.lieferscheinartCNr == null
				: that.lieferscheinartCNr.equals(this.lieferscheinartCNr))) {
			return false;
		}
		if (!(that.statusCNr == null ? this.statusCNr == null
				: that.statusCNr.equals(this.statusCNr))) {
			return false;
		}
		if (!(that.belegartCNr == null ? this.belegartCNr == null
				: that.belegartCNr.equals(this.belegartCNr))) {
			return false;
		}
		if (!(that.bVerrechenbar == null ? this.bVerrechenbar == null
				: that.bVerrechenbar.equals(this.bVerrechenbar))) {
			return false;
		}
		if (!(that.tBelegdatum == null ? this.tBelegdatum == null
				: that.tBelegdatum.equals(this.tBelegdatum))) {
			return false;
		}
		if (!(that.kundeIIdLieferadresse == null ? this.kundeIIdLieferadresse == null
				: that.kundeIIdLieferadresse.equals(this.kundeIIdLieferadresse))) {
			return false;
		}
		if (!(that.ansprechpartnerIId == null ? this.ansprechpartnerIId == null
				: that.ansprechpartnerIId.equals(this.ansprechpartnerIId))) {
			return false;
		}
		if (!(that.personalIIdVertreter == null ? this.personalIIdVertreter == null
				: that.personalIIdVertreter.equals(this.personalIIdVertreter))) {
			return false;
		}
		if (!(that.kundeIIdRechnungsadresse == null ? this.kundeIIdRechnungsadresse == null
				: that.kundeIIdRechnungsadresse
						.equals(this.kundeIIdRechnungsadresse))) {
			return false;
		}
		if (!(that.cBezProjektbezeichnung == null ? this.cBezProjektbezeichnung == null
				: that.cBezProjektbezeichnung
						.equals(this.cBezProjektbezeichnung))) {
			return false;
		}
		if (!(that.cBestellnummer == null ? this.cBestellnummer == null
				: that.cBestellnummer.equals(this.cBestellnummer))) {
			return false;
		}
		if (!(that.lagerIId == null ? this.lagerIId == null : that.lagerIId
				.equals(this.lagerIId))) {
			return false;
		}
		if (!(that.ziellagerIId == null ? this.ziellagerIId == null
				: that.ziellagerIId.equals(this.ziellagerIId))) {
			return false;
		}

		if (!(that.kostenstelleIId == null ? this.kostenstelleIId == null
				: that.kostenstelleIId.equals(this.kostenstelleIId))) {
			return false;
		}
		if (!(that.tLiefertermin == null ? this.tLiefertermin == null
				: that.tLiefertermin.equals(this.tLiefertermin))) {
			return false;
		}
		if (!(that.tRueckgabetermin == null ? this.tRueckgabetermin == null
				: that.tRueckgabetermin.equals(this.tRueckgabetermin))) {
			return false;
		}
		if (!(that.fVersteckterAufschlag == null ? this.fVersteckterAufschlag == null
				: that.fVersteckterAufschlag.equals(this.fVersteckterAufschlag))) {
			return false;
		}
		if (!(that.fAllgemeinerRabattsatz == null ? this.fAllgemeinerRabattsatz == null
				: that.fAllgemeinerRabattsatz
						.equals(this.fAllgemeinerRabattsatz))) {
			return false;
		}
		if (!(that.lieferartIId == null ? this.lieferartIId == null
				: that.lieferartIId.equals(this.lieferartIId))) {
			return false;
		}
		if (!(that.bMindermengenzuschlag == null ? this.bMindermengenzuschlag == null
				: that.bMindermengenzuschlag.equals(this.bMindermengenzuschlag))) {
			return false;
		}
		if (!(that.iAnzahlPakete == null ? this.iAnzahlPakete == null
				: that.iAnzahlPakete.equals(this.iAnzahlPakete))) {
			return false;
		}
		if (!(that.fGewichtLieferung == null ? this.fGewichtLieferung == null
				: that.fGewichtLieferung.equals(this.fGewichtLieferung))) {
			return false;
		}
		if (!(that.cVersandnummer == null ? this.cVersandnummer == null
				: that.cVersandnummer.equals(this.cVersandnummer))) {
			return false;
		}
		if (!(that.tGedruckt == null ? this.tGedruckt == null : that.tGedruckt
				.equals(this.tGedruckt))) {
			return false;
		}
		if (!(that.personalIIdManuellErledigt == null ? this.personalIIdManuellErledigt == null
				: that.personalIIdManuellErledigt
						.equals(this.personalIIdManuellErledigt))) {
			return false;
		}
		if (!(that.tManuellErledigt == null ? this.tManuellErledigt == null
				: that.tManuellErledigt.equals(this.tManuellErledigt))) {
			return false;
		}
		if (!(that.personalIIdStorniert == null ? this.personalIIdStorniert == null
				: that.personalIIdStorniert.equals(this.personalIIdStorniert))) {
			return false;
		}
		if (!(that.tStorniert == null ? this.tStorniert == null
				: that.tStorniert.equals(this.tStorniert))) {
			return false;
		}
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.waehrungCNr == null ? this.waehrungCNr == null
				: that.waehrungCNr.equals(this.waehrungCNr))) {
			return false;
		}
		if (!(that.nGesamtwertInLieferscheinwaehrung == null ? this.nGesamtwertInLieferscheinwaehrung == null
				: that.nGesamtwertInLieferscheinwaehrung
						.equals(this.nGesamtwertInLieferscheinwaehrung))) {
			return false;
		}
		if (!(that.nGestehungswertInMandantenwaehrung == null ? this.nGestehungswertInMandantenwaehrung == null
				: that.nGestehungswertInMandantenwaehrung
						.equals(this.nGestehungswertInMandantenwaehrung))) {
			return false;
		}
		if (!(that.zahlungszielIId == null ? this.zahlungszielIId == null
				: that.zahlungszielIId.equals(this.zahlungszielIId))) {
			return false;
		}
		if (!(that.spediteurIId == null ? this.spediteurIId == null
				: that.spediteurIId.equals(this.spediteurIId))) {
			return false;
		}
		if (!(that.lieferscheintextIIdDefaultKopftext == null ? this.lieferscheintextIIdDefaultKopftext == null
				: that.lieferscheintextIIdDefaultKopftext
						.equals(this.lieferscheintextIIdDefaultKopftext))) {
			return false;
		}
		if (!(that.cLieferscheinKopftextUeberschrieben == null ? this.cLieferscheinKopftextUeberschrieben == null
				: that.cLieferscheinKopftextUeberschrieben
						.equals(this.cLieferscheinKopftextUeberschrieben))) {
			return false;
		}
		if (!(that.lieferscheintextIIdDefaultFusstext == null ? this.lieferscheintextIIdDefaultFusstext == null
				: that.lieferscheintextIIdDefaultFusstext
						.equals(this.lieferscheintextIIdDefaultFusstext))) {
			return false;
		}
		if (!(that.cLieferscheinFusstextUeberschrieben == null ? this.cLieferscheinFusstextUeberschrieben == null
				: that.cLieferscheinFusstextUeberschrieben
						.equals(this.cLieferscheinFusstextUeberschrieben))) {
			return false;
		}
		if (!(that.auftragIId == null ? this.auftragIId == null
				: that.auftragIId.equals(this.auftragIId))) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.rechnungIId.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.statusCNr.hashCode();
		result = 37 * result + this.bVerrechenbar.hashCode();
		result = 37 * result + this.lieferscheinartCNr.hashCode();
		result = 37 * result + this.belegartCNr.hashCode();
		result = 37 * result + this.tBelegdatum.hashCode();
		result = 37 * result + this.kundeIIdLieferadresse.hashCode();
		result = 37 * result + this.personalIIdVertreter.hashCode();
		result = 37 * result + this.ansprechpartnerIId.hashCode();
		result = 37 * result + this.kundeIIdRechnungsadresse.hashCode();
		result = 37 * result + this.cBezProjektbezeichnung.hashCode();
		result = 37 * result + this.cBestellnummer.hashCode();
		result = 37 * result + this.lagerIId.hashCode();
		result = 37 * result + this.ziellagerIId.hashCode();
		result = 37 * result + this.kostenstelleIId.hashCode();
		result = 37 * result + this.tLiefertermin.hashCode();
		result = 37 * result + this.tRueckgabetermin.hashCode();
		result = 37 * result + this.fVersteckterAufschlag.hashCode();
		result = 37 * result + this.fAllgemeinerRabattsatz.hashCode();
		result = 37 * result + this.lieferartIId.hashCode();
		result = 37 * result + this.bMindermengenzuschlag.hashCode();
		result = 37 * result + this.iAnzahlPakete.hashCode();
		result = 37 * result + this.fGewichtLieferung.hashCode();
		result = 37 * result + this.cVersandnummer.hashCode();
		result = 37 * result + this.tGedruckt.hashCode();
		result = 37 * result + this.personalIIdManuellErledigt.hashCode();
		result = 37 * result + this.tManuellErledigt.hashCode();
		result = 37 * result + this.personalIIdStorniert.hashCode();
		result = 37 * result + this.tStorniert.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.waehrungCNr.hashCode();
		result = 37 * result
				+ this.getNGesamtwertInLieferscheinwaehrung().hashCode();
		result = 37 * result
				+ this.getNGestehungswertInMandantenwaehrung().hashCode();
		result = 37 * result + this.zahlungszielIId.hashCode();
		result = 37 * result + this.spediteurIId.hashCode();
		result = 37 * result
				+ this.lieferscheintextIIdDefaultKopftext.hashCode();
		result = 37 * result
				+ this.cLieferscheinKopftextUeberschrieben.hashCode();
		result = 37 * result
				+ this.lieferscheintextIIdDefaultFusstext.hashCode();
		result = 37 * result
				+ this.cLieferscheinFusstextUeberschrieben.hashCode();
		result = 37 * result + this.auftragIId.hashCode();

		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + rechnungIId;
		returnString += ", " + cNr;
		returnString += ", " + lieferscheinartCNr;
		returnString += ", " + statusCNr;
		returnString += ", " + belegartCNr;
		returnString += ", " + bVerrechenbar;
		returnString += ", " + tBelegdatum;
		returnString += ", " + kundeIIdLieferadresse;
		returnString += ", " + ansprechpartnerIId;
		returnString += ", " + personalIIdVertreter;
		returnString += ", " + kundeIIdRechnungsadresse;
		returnString += ", " + cBezProjektbezeichnung;
		returnString += ", " + cBestellnummer;
		returnString += ", " + lagerIId;
		returnString += ", " + ziellagerIId;
		returnString += ", " + kostenstelleIId;
		returnString += ", " + tLiefertermin;
		returnString += ", " + tRueckgabetermin;
		returnString += ", " + fVersteckterAufschlag;
		returnString += ", " + fAllgemeinerRabattsatz;
		returnString += ", " + lieferartIId;
		returnString += ", " + bMindermengenzuschlag;
		returnString += ", " + iAnzahlPakete;
		returnString += ", " + fGewichtLieferung;
		returnString += ", " + cVersandnummer;
		returnString += ", " + tGedruckt;
		returnString += ", " + personalIIdManuellErledigt;
		returnString += ", " + tManuellErledigt;
		returnString += ", " + personalIIdStorniert;
		returnString += ", " + tStorniert;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		returnString += ", " + mandantCNr;
		returnString += ", " + waehrungCNr;
		returnString += ", " + fWechselkursmandantwaehrungzubelegwaehrung;
		returnString += ", " + nGesamtwertInLieferscheinwaehrung;
		returnString += ", " + nGestehungswertInMandantenwaehrung;
		returnString += ", " + zahlungszielIId;
		returnString += ", " + spediteurIId;
		returnString += ", " + lieferscheintextIIdDefaultKopftext;
		returnString += ", " + cLieferscheinKopftextUeberschrieben;
		returnString += ", " + lieferscheintextIIdDefaultFusstext;
		returnString += ", " + cLieferscheinFusstextUeberschrieben;
		returnString += ", " + auftragIId;

		return returnString;
	}
}
