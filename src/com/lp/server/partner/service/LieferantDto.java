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
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.artikel.ejb.Lager;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.system.ejb.Lieferart;
import com.lp.server.system.ejb.Zahlungsziel;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCBez;
import com.lp.server.system.service.HvDtoLogIdCnr;
import com.lp.server.system.service.HvDtoLogIgnore;
import com.lp.server.util.IIId;
import com.lp.server.util.IModificationData;
import com.lp.util.Helper;


@HvDtoLogClass(name = HvDtoLogClass.LIEFERANT)
public class LieferantDto implements Serializable, IIId, IModificationData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int I_UPD_KREDITORENKONTO_KEIN_UPDATE = 0;
	public final static int I_UPD_KREDITORENKONTO_UPDATE = 1;
	public final static int I_UPD_KREDITORENKONTO_PRUEFE_AUF_DOPPELTE = 2;
	private int iUpdateKreditorenkonto = I_UPD_KREDITORENKONTO_PRUEFE_AUF_DOPPELTE;

	private Integer iId;
	private Integer partnerIId;
	private String mandantCNr;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Integer mwstsatzbezIId;
	private String waehrungCNr;
	private String verrechnungsartCNr;
	private Integer lieferartIId;
	private Integer zahlungszielIId;
	private Integer lieferbedingungenCNr;
	private BigDecimal nMindestbestellwert;
	private BigDecimal nKredit;
	private BigDecimal nJahrbonus;
	private BigDecimal nAbumsatz;
	private Double nRabatt;
	private BigDecimal nMindermengenzuschlag;
	private Integer kontoIIdKreditorenkonto;
	private Integer iKreditorenkontoAsIntegerNotiId;
	private Integer kontoIIdWarenkonto;
	private String cKundennr;
	private String cHinweisintern;
	private String cHinweisextern;
	private String xKommentar;
	private Short bBeurteilen;
	private Integer iBeurteilung;
	private Short bVersteckt;
	private Integer partnerIIdRechnungsadresse;
	private Short bMoeglicherLieferant = null;
	private Integer iIdKostenstelle;
	private Integer idSpediteur = null;
	private Timestamp tBestellsperream = null;
	private BigDecimal nTransportkostenprolieferung;

	private Integer partnerIIdLieferadresse;

	public Integer getPartnerIIdLieferadresse() {
		return this.partnerIIdLieferadresse;
	}

	public void setPartnerIIdLieferadresse(Integer partnerIIdLieferadresse) {
		this.partnerIIdLieferadresse = partnerIIdLieferadresse;
	}
	
	private Short bVersteckterkunde;
	private Integer reversechargeartId ;
	private String cFremdsystemnr;
	
	private Integer iLiefertag;
	
	
	public Integer getILiefertag() {
		return iLiefertag;
	}

	public void setILiefertag(Integer iLiefertag) {
		this.iLiefertag = iLiefertag;
	}

	
	public Short getBVersteckterkunde() {
		return bVersteckterkunde;
	}

	public void setBVersteckterkunde(Short bVersteckterkunde) {
		this.bVersteckterkunde = bVersteckterkunde;
	}

	private Short bZuschlagInklusive;

	public Short getBZuschlagInklusive() {
		return bZuschlagInklusive;
	}

	public void setBZuschlagInklusive(Short bZuschlagInklusive) {
		this.bZuschlagInklusive = bZuschlagInklusive;
	}
	
	private Timestamp tFreigabe;

	private Timestamp tPersonalFreigabe;

	private Integer personalIIdFreigabe;

	private String cFreigabe;
	private Short bReversecharge;
	private Short bIgErwerb;

//	public Short getBReversecharge() {
//		return this.bReversecharge;
//	}

	public Short getBIgErwerb() {
		return bIgErwerb;
	}

	public boolean getBIgErwerbBoolean() {
		return Helper.short2boolean(bIgErwerb);
	}

//	public void setBReversecharge(Short bReversecharge) {
//		this.bReversecharge = bReversecharge;
//	}

	public void setBIgErwerb(Short bIgErwerb) {
		this.bIgErwerb = bIgErwerb;
	}

	public void setBIgErwerb(boolean bIgErwerb) {
		this.bIgErwerb = Helper.boolean2Short(bIgErwerb);
	}

	private Short bZollimportpapier;

	public Short getBZollimportpapier() {
		return bZollimportpapier;
	}

	public void setBZollimportpapier(Short bZollimportpapier) {
		this.bZollimportpapier = bZollimportpapier;
	}

	public Timestamp getTFreigabe() {
		return tFreigabe;
	}

	public void setTFreigabe(Timestamp tFreigabe) {
		this.tFreigabe = tFreigabe;
	}

	private Integer lagerIIdZubuchungslager;

	@HvDtoLogIdCnr(entityClass = Lager.class)
	public Integer getLagerIIdZubuchungslager() {
		return this.lagerIIdZubuchungslager;
	}

	public void setLagerIIdZubuchungslager(Integer lagerIIdZubuchungslager) {
		this.lagerIIdZubuchungslager = lagerIIdZubuchungslager;
	}

	public Timestamp getTPersonalFreigabe() {
		return tPersonalFreigabe;
	}

	public void setTPersonalFreigabe(Timestamp tPersonalFreigabe) {
		this.tPersonalFreigabe = tPersonalFreigabe;
	}

	public Integer getPersonalIIdFreigabe() {
		return personalIIdFreigabe;
	}

	public void setPersonalIIdFreigabe(Integer personalIIdFreigabe) {
		this.personalIIdFreigabe = personalIIdFreigabe;
	}

	public String getCFreigabe() {
		return cFreigabe;
	}

	public void setCFreigabe(String cFreigabe) {
		this.cFreigabe = cFreigabe;
	}

	private BigDecimal nKupferzahl = null;

	public BigDecimal getNKupferzahl() {
		return nKupferzahl;
	}

	public void setNKupferzahl(BigDecimal kupferzahl) {
		nKupferzahl = kupferzahl;
	}

	public BigDecimal getNTransportkostenprolieferung() {
		return nTransportkostenprolieferung;
	}

	public void setNTransportkostenprolieferung(
			BigDecimal transportkostenprolieferung) {
		nTransportkostenprolieferung = transportkostenprolieferung;
	}

	private PartnerDto partnerDto = new PartnerDto();
	private PartnerDto partnerRechnungsadresseDto = new PartnerDto();

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}
	@HvDtoLogIgnore
	public Timestamp getTAnlegen() {
		return tAnlegen;
	}
	@HvDtoLogIgnore
	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}
	@HvDtoLogIgnore
	public Timestamp getTAendern() {
		return tAendern;
	}
	@HvDtoLogIgnore
	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getMwstsatzbezIId() {
		return mwstsatzbezIId;
	}

	public void setMwstsatzbezIId(Integer mwstsatzbezIId) {
		this.mwstsatzbezIId = mwstsatzbezIId;
	}

	public String getWaehrungCNr() {
		return waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	public String getVerrechnungsartCNr() {
		return verrechnungsartCNr;
	}

	public void setVerrechnungsartCNr(String verrechnungsartCNr) {
		this.verrechnungsartCNr = verrechnungsartCNr;
	}
	@HvDtoLogIdCnr(entityClass = Lieferart.class)
	public Integer getLieferartIId() {
		return lieferartIId;
	}

	public void setLieferartIId(Integer lieferartIId) {
		this.lieferartIId = lieferartIId;
	}

	@HvDtoLogIdCBez(entityClass = Zahlungsziel.class)
	public Integer getZahlungszielIId() {
		return zahlungszielIId;
	}

	public void setZahlungszielIId(Integer zahlungszielIId) {
		this.zahlungszielIId = zahlungszielIId;
	}

	public Integer getLieferbedingungenCNr() {
		return lieferbedingungenCNr;
	}

	public void setLieferbedingungenCNr(Integer lieferbedingungenCNr) {
		this.lieferbedingungenCNr = lieferbedingungenCNr;
	}

	public BigDecimal getNMindestbestellwert() {
		return nMindestbestellwert;
	}

	public void setNMindestbestellwert(BigDecimal nMindestbestellwert) {
		this.nMindestbestellwert = nMindestbestellwert;
	}

	public BigDecimal getNKredit() {
		return nKredit;
	}

	public void setNKredit(BigDecimal nKredit) {
		this.nKredit = nKredit;
	}

	public BigDecimal getNJahrbonus() {
		return nJahrbonus;
	}

	public void setNJahrbonus(BigDecimal nJahrbonus) {
		this.nJahrbonus = nJahrbonus;
	}

	public BigDecimal getNAbumsatz() {
		return nAbumsatz;
	}

	public void setNAbumsatz(BigDecimal nAbumsatz) {
		this.nAbumsatz = nAbumsatz;
	}

	public Double getNRabatt() {
		return nRabatt;
	}

	public void setNRabatt(Double ddRabattI) {
		this.nRabatt = ddRabattI;
	}

	public BigDecimal getNMindermengenzuschlag() {
		return nMindermengenzuschlag;
	}

	public void setNMindermengenzuschlag(BigDecimal nMindermengenzuschlag) {
		this.nMindermengenzuschlag = nMindermengenzuschlag;
	}

	@HvDtoLogIdCnr(entityClass = Konto.class)
	public Integer getKontoIIdKreditorenkonto() {
		return kontoIIdKreditorenkonto;
	}

	public void setKontoIIdKreditorenkonto(Integer kontoIIdKreditorenkonto) {
		this.kontoIIdKreditorenkonto = kontoIIdKreditorenkonto;
	}
	@HvDtoLogIdCnr(entityClass = Konto.class)
	public Integer getKontoIIdWarenkonto() {
		return kontoIIdWarenkonto;
	}

	public void setKontoIIdWarenkonto(Integer kontoIIdWarenkonto) {
		this.kontoIIdWarenkonto = kontoIIdWarenkonto;
	}

	public String getCKundennr() {
		return cKundennr;
	}

	public void setCKundennr(String cKundennr) {
		this.cKundennr = cKundennr;
	}

	public String getCHinweisintern() {
		return cHinweisintern;
	}

	public void setCHinweisintern(String cHinweisintern) {
		this.cHinweisintern = cHinweisintern;
	}

	public String getCHinweisextern() {
		return cHinweisextern;
	}

	public void setCHinweisextern(String cHinweisextern) {
		this.cHinweisextern = cHinweisextern;
	}

	public String getXKommentar() {
		return xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public Short getBBeurteilen() {
		return bBeurteilen;
	}

	public void setBBeurteilen(Short bBeurteilen) {
		this.bBeurteilen = bBeurteilen;
	}

	public Integer getIBeurteilung() {
		return iBeurteilung;
	}

	public void setIBeurteilung(Integer iBeurteilung) {
		this.iBeurteilung = iBeurteilung;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public Integer getPartnerIIdRechnungsadresse() {
		return partnerIIdRechnungsadresse;
	}
	@HvDtoLogIgnore
	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public Short getBMoeglicherLieferant() {
		return bMoeglicherLieferant;
	}
	@HvDtoLogIgnore
	public PartnerDto getPartnerRechnungsadresseDto() {
		return partnerRechnungsadresseDto;
	}

	public Integer getIIdKostenstelle() {
		return iIdKostenstelle;
	}

	public Integer getIdSpediteur() {
		return idSpediteur;
	}

	@HvDtoLogIgnore
	public Integer getIKreditorenkontoAsIntegerNotiId() {
		return iKreditorenkontoAsIntegerNotiId;
	}

	public void setPartnerIIdRechnungsadresse(Integer partnerIIdRechnungsadresse) {
		this.partnerIIdRechnungsadresse = partnerIIdRechnungsadresse;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public void setBMoeglicherLieferant(Short bMoeglicherLieferant) {
		this.bMoeglicherLieferant = bMoeglicherLieferant;
	}

	public void setTAnlegen(Timestamp tAnlegenI) {
		this.tAnlegen = tAnlegenI;
	}

	public void setTAendern(Timestamp tAendernI) {
		this.tAendern = tAendernI;
	}

	public void setPartnerRechnungsadresseDto(
			PartnerDto partnerRechnungsadresseDto) {
		this.partnerRechnungsadresseDto = partnerRechnungsadresseDto;
	}

	public void setIIdKostenstelle(Integer iIdKostenstelle) {
		this.iIdKostenstelle = iIdKostenstelle;
	}

	public void setIdSpediteur(Integer idSpediteur) {
		this.idSpediteur = idSpediteur;
	}
	@HvDtoLogIgnore
	public int getUpdateModeKreditorenkonto() {
		return iUpdateKreditorenkonto;
	}

	public void setIKreditorenkontoAsIntegerNotiId(
			Integer iKreditorenkontoAsIntegerNotiIdI) {
		this.iKreditorenkontoAsIntegerNotiId = iKreditorenkontoAsIntegerNotiIdI;
	}

	public void setUpdateModeKreditorenkonto(int iUpdateKreditorenkonto) {
		this.iUpdateKreditorenkonto = iUpdateKreditorenkonto;
	}

	public Timestamp getTBestellsperream() {
		return this.tBestellsperream;
	}

	public void setTBestellsperream(Timestamp tBestellsperream) {
		this.tBestellsperream = tBestellsperream;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LieferantDto)) {
			return false;
		}
		LieferantDto that = (LieferantDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.partnerIId == null ? this.partnerIId == null
				: that.partnerIId.equals(this.partnerIId))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		if (!(that.mwstsatzbezIId == null ? this.mwstsatzbezIId == null
				: that.mwstsatzbezIId.equals(this.mwstsatzbezIId))) {
			return false;
		}
		if (!(that.waehrungCNr == null ? this.waehrungCNr == null
				: that.waehrungCNr.equals(this.waehrungCNr))) {
			return false;
		}
		if (!(that.verrechnungsartCNr == null ? this.verrechnungsartCNr == null
				: that.verrechnungsartCNr.equals(this.verrechnungsartCNr))) {
			return false;
		}
		if (!(that.lieferartIId == null ? this.lieferartIId == null
				: that.lieferartIId.equals(this.lieferartIId))) {
			return false;
		}
		if (!(that.zahlungszielIId == null ? this.zahlungszielIId == null
				: that.zahlungszielIId.equals(this.zahlungszielIId))) {
			return false;
		}
		if (!(that.lieferbedingungenCNr == null ? this.lieferbedingungenCNr == null
				: that.lieferbedingungenCNr.equals(this.lieferbedingungenCNr))) {
			return false;
		}
		if (!(that.nMindestbestellwert == null ? this.nMindestbestellwert == null
				: that.nMindestbestellwert.equals(this.nMindestbestellwert))) {
			return false;
		}
		if (!(that.nKredit == null ? this.nKredit == null : that.nKredit
				.equals(this.nKredit))) {
			return false;
		}
		if (!(that.nJahrbonus == null ? this.nJahrbonus == null
				: that.nJahrbonus.equals(this.nJahrbonus))) {
			return false;
		}
		if (!(that.nAbumsatz == null ? this.nAbumsatz == null : that.nAbumsatz
				.equals(this.nAbumsatz))) {
			return false;
		}
		if (!(that.nRabatt == null ? this.nRabatt == null : that.nRabatt
				.equals(this.nRabatt))) {
			return false;
		}
		if (!(that.nMindermengenzuschlag == null ? this.nMindermengenzuschlag == null
				: that.nMindermengenzuschlag.equals(this.nMindermengenzuschlag))) {
			return false;
		}
		if (!(that.kontoIIdKreditorenkonto == null ? this.kontoIIdKreditorenkonto == null
				: that.kontoIIdKreditorenkonto
						.equals(this.kontoIIdKreditorenkonto))) {
			return false;
		}
		if (!(that.kontoIIdWarenkonto == null ? this.kontoIIdWarenkonto == null
				: that.kontoIIdWarenkonto.equals(this.kontoIIdWarenkonto))) {
			return false;
		}
		if (!(that.cKundennr == null ? this.cKundennr == null : that.cKundennr
				.equals(this.cKundennr))) {
			return false;
		}
		if (!(that.cHinweisintern == null ? this.cHinweisintern == null
				: that.cHinweisintern.equals(this.cHinweisintern))) {
			return false;
		}
		if (!(that.cHinweisextern == null ? this.cHinweisextern == null
				: that.cHinweisextern.equals(this.cHinweisextern))) {
			return false;
		}
		if (!(that.xKommentar == null ? this.xKommentar == null
				: that.xKommentar.equals(this.xKommentar))) {
			return false;
		}
		if (!(that.bBeurteilen == null ? this.bBeurteilen == null
				: that.bBeurteilen.equals(this.bBeurteilen))) {
			return false;
		}
		if (!(that.iBeurteilung == null ? this.iBeurteilung == null
				: that.iBeurteilung.equals(this.iBeurteilung))) {
			return false;
		}
		if (!(that.bVersteckt == null ? this.bVersteckt == null
				: that.bVersteckt.equals(this.bVersteckt))) {
			return false;
		}
		if (!(that.partnerIIdRechnungsadresse == null ? this.partnerIIdRechnungsadresse == null
				: that.partnerIIdRechnungsadresse
						.equals(this.partnerIIdRechnungsadresse))) {
			return false;
		}
		if (!(that.tBestellsperream == null ? this.tBestellsperream == null
				: that.tBestellsperream.equals(this.tBestellsperream))) {
			return false;
		}
		if (!(that.reversechargeartId == null ? this.reversechargeartId == null
				: that.reversechargeartId.equals(this.reversechargeartId))) {
			return false;
		}
		if (!(that.cFremdsystemnr == null ? this.cFremdsystemnr == null 
				: that.cFremdsystemnr.equals(this.cFremdsystemnr))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.mwstsatzbezIId.hashCode();
		result = 37 * result + this.waehrungCNr.hashCode();
		result = 37 * result + this.verrechnungsartCNr.hashCode();
		result = 37 * result + this.lieferartIId.hashCode();
		result = 37 * result + this.zahlungszielIId.hashCode();
		result = 37 * result + this.lieferbedingungenCNr.hashCode();
		result = 37 * result + this.nMindestbestellwert.hashCode();
		result = 37 * result + this.nKredit.hashCode();
		result = 37 * result + this.nJahrbonus.hashCode();
		result = 37 * result + this.nAbumsatz.hashCode();
		result = 37 * result + this.nRabatt.hashCode();
		result = 37 * result + this.nMindermengenzuschlag.hashCode();
		result = 37 * result + this.kontoIIdKreditorenkonto.hashCode();
		result = 37 * result + this.kontoIIdWarenkonto.hashCode();
		result = 37 * result + this.cKundennr.hashCode();
		result = 37 * result + this.cHinweisintern.hashCode();
		result = 37 * result + this.cHinweisextern.hashCode();
		result = 37 * result + this.xKommentar.hashCode();
		result = 37 * result + this.bBeurteilen.hashCode();
		result = 37 * result + this.iBeurteilung.hashCode();
		result = 37 * result + this.bVersteckt.hashCode();
		result = 37 * result + this.partnerIIdRechnungsadresse.hashCode();
		result = 37 * result + this.tBestellsperream.hashCode();
		result = 37 * result + this.reversechargeartId.hashCode();
		result = 37 * result + this.cFremdsystemnr.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + partnerIId;
		returnString += ", " + mandantCNr;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + mwstsatzbezIId;
		returnString += ", " + waehrungCNr;
		returnString += ", " + verrechnungsartCNr;
		returnString += ", " + lieferartIId;
		returnString += ", " + zahlungszielIId;
		returnString += ", " + lieferbedingungenCNr;
		returnString += ", " + nMindestbestellwert;
		returnString += ", " + nKredit;
		returnString += ", " + nJahrbonus;
		returnString += ", " + nAbumsatz;
		returnString += ", " + nRabatt;
		returnString += ", " + nMindermengenzuschlag;
		returnString += ", " + kontoIIdKreditorenkonto;
		returnString += ", " + kontoIIdWarenkonto;
		returnString += ", " + cKundennr;
		returnString += ", " + cHinweisintern;
		returnString += ", " + cHinweisextern;
		returnString += ", " + xKommentar;
		returnString += ", " + bBeurteilen;
		returnString += ", " + iBeurteilung;
		returnString += ", " + bVersteckt;
		returnString += ", " + partnerIIdRechnungsadresse;
		returnString += ", " + tBestellsperream;
		return returnString;
	}

	public Integer getReversechargeartId() {
		return reversechargeartId;
	}

	public void setReversechargeartId(Integer reversechargeartId) {
		this.reversechargeartId = reversechargeartId;
	}

	public String getCFremdsystemnr() {
		return cFremdsystemnr;
	}

	public void setCFremdsystemnr(String cFremdsystemnr) {
		this.cFremdsystemnr = cFremdsystemnr;
	}
}
