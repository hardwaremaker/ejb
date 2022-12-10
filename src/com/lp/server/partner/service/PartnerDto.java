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
import java.sql.Date;
import java.sql.Timestamp;

import org.w3c.dom.Document;

import com.lp.server.artikel.ejb.Lager;
import com.lp.server.partner.ejb.Branche;
import com.lp.server.partner.ejb.Newslettergrund;
import com.lp.server.partner.ejb.Partnerklasse;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCBez;
import com.lp.server.system.service.HvDtoLogIdCnr;
import com.lp.server.system.service.HvDtoLogIgnore;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.util.IIId;
import com.lp.server.util.IModificationData;

@HvDtoLogClass(name = HvDtoLogClass.PARTNER)
public class PartnerDto implements Serializable, IIId, IModificationData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String cIln;
	private Integer iId;

	private String localeCNrKommunikation;

	private String partnerartCNr;
	private byte[] oBild;

	private String cKbez;

	private String anredeCNr;

	private String cName1nachnamefirmazeile1;

	private String cName2vornamefirmazeile2;

	private String cName3vorname2abteilung;

	private String cStrasse;

	private Integer landplzortIId;

	private Integer landplzortIIdPostfach;

	private String cPostfach;

	private Integer brancheIId;

	private Integer partnerklasseIId;

	private Integer partnerIIdVater;

	private String cUid;

	private String xBemerkung;

	private Date dGeburtsdatumansprechpartner;

	private Integer rechtsformIId;

	private Integer partnerIIdEigentuemer;

	private String cFirmenbuchnr;

	private String cTitel;
	private String cNtitel;
	private String cGerichtsstand;

	private Short bVersteckt;

	private Timestamp tAnlegen;

	private Integer personalAnlegenIId;

	private Timestamp tAendern;

	private Integer personalAendernIId;

	private LandplzortDto landplzortDto;

	private LandplzortDto landplzortDto_Postfach;

	private BankDto bankDto;

	private Integer landIIdAbweichendesustland;
	private Integer lagerIIdZiellager;
	private String cFilialnummer;
	private String cAdressart;
	private String cEori;

	private String cFax;
	private String cTelefon;
	private String cHandy;
	private String cDirektfax;
	private String cHomepage;
	private String cEmail;

	private Integer versandwegIId;
	private GeodatenDto geodatenDto;

	public String getCFax() {
		return cFax;
	}

	public void setCFax(String cFax) {
		this.cFax = cFax;
	}

	public String getCTelefon() {
		return cTelefon;
	}

	public void setCTelefon(String cTelefon) {
		this.cTelefon = cTelefon;
	}

	public String getCDirektfax() {
		return cDirektfax;
	}

	public void setCDirektfax(String cDirektfax) {
		this.cDirektfax = cDirektfax;
	}

	public String getCHomepage() {
		return cHomepage;
	}

	public void setCHomepage(String cHomepage) {
		this.cHomepage = cHomepage;
	}

	public String getCEmail() {
		return cEmail;
	}

	public void setCEmail(String cEmail) {
		this.cEmail = cEmail;
	}

	public String getCHandy() {
		return cHandy;
	}

	public void setCHandy(String cHandy) {
		this.cHandy = cHandy;
	}

	public String getCEori() {
		return cEori;
	}

	public void setCEori(String cEori) {
		this.cEori = cEori;
	}

	private Double fGmtversatz;

	public Double getFGmtversatz() {
		return fGmtversatz;
	}

	public void setFGmtversatz(Double gmtversatz) {
		fGmtversatz = gmtversatz;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getLocaleCNrKommunikation() {
		return localeCNrKommunikation;
	}

	public void setLocaleCNrKommunikation(String localeCNrKommunikation) {
		this.localeCNrKommunikation = localeCNrKommunikation;
	}

	public String getPartnerartCNr() {
		return partnerartCNr;
	}

	public void setPartnerartCNr(String partnerartCNr) {
		this.partnerartCNr = partnerartCNr;
	}

	public String getCKbez() {
		return cKbez;
	}

	public void setCKbez(String cKbez) {
		this.cKbez = cKbez;
	}

	public String getAnredeCNr() {
		return anredeCNr;
	}

	public void setAnredeCNr(String anredeCNr) {
		this.anredeCNr = anredeCNr;
	}

	public String getCName1nachnamefirmazeile1() {
		return cName1nachnamefirmazeile1;
	}

	public void setCName1nachnamefirmazeile1(String cName1nachnamefirmazeile1) {
		this.cName1nachnamefirmazeile1 = cName1nachnamefirmazeile1;
	}

	public Document toXML() {
		return null;
	}

	public String getCName2vornamefirmazeile2() {
		return cName2vornamefirmazeile2;
	}

	public void setCName2vornamefirmazeile2(String cName2vornamefirmazeile2) {
		this.cName2vornamefirmazeile2 = cName2vornamefirmazeile2;
	}

	public String getCName3vorname2abteilung() {
		return cName3vorname2abteilung;
	}

	public void setCName3vorname2abteilung(String cName3vorname2abteilung) {
		this.cName3vorname2abteilung = cName3vorname2abteilung;
	}

	public String getCStrasse() {
		return cStrasse;
	}

	public void setCStrasse(String cStrasse) {
		this.cStrasse = cStrasse;
	}

	public Integer getLandplzortIId() {
		return landplzortIId;
	}

	public void setLandplzortIId(Integer landplzortIId) {
		this.landplzortIId = landplzortIId;
	}

	public Integer getLandplzortIIdPostfach() {
		return landplzortIIdPostfach;
	}

	public void setLandplzortIIdPostfach(Integer landplzortIIdPostfach) {
		this.landplzortIIdPostfach = landplzortIIdPostfach;
	}

	public String getCPostfach() {
		return cPostfach;
	}

	public void setCPostfach(String cPostfach) {
		this.cPostfach = cPostfach;
	}

	@HvDtoLogIdCnr(entityClass = Branche.class)
	public Integer getBrancheIId() {
		return brancheIId;
	}

	public void setBrancheIId(Integer brancheIId) {
		this.brancheIId = brancheIId;
	}

	@HvDtoLogIdCnr(entityClass = Partnerklasse.class)
	public Integer getPartnerklasseIId() {
		return partnerklasseIId;
	}

	public void setPartnerklasseIId(Integer partnerklasseIId) {
		this.partnerklasseIId = partnerklasseIId;
	}

	public Integer getPartnerIIdVater() {
		return partnerIIdVater;
	}

	public void setPartnerIIdVater(Integer partnerIIdVater) {
		this.partnerIIdVater = partnerIIdVater;
	}

	public String getCUid() {
		return cUid;
	}

	public void setCUid(String cUid) {
		this.cUid = cUid;
	}

	public String getXBemerkung() {
		return xBemerkung;
	}

	public void setXBemerkung(String xBemerkung) {
		this.xBemerkung = xBemerkung;
	}

	public Date getDGeburtsdatumansprechpartner() {
		return dGeburtsdatumansprechpartner;
	}

	public void setDGeburtsdatumansprechpartner(Date dGeburtsdatumansprechpartner) {
		this.dGeburtsdatumansprechpartner = dGeburtsdatumansprechpartner;
	}

	public Integer getRechtsformIId() {
		return rechtsformIId;
	}

	public void setRechtsformIId(Integer rechtsformIId) {
		this.rechtsformIId = rechtsformIId;
	}

	public String getCNtitel() {
		return this.cNtitel;
	}

	public void setCNtitel(String cNtitel) {
		this.cNtitel = cNtitel;
	}

	public Integer getPartnerIIdEigentuemer() {
		return partnerIIdEigentuemer;
	}

	public void setPartnerIIdEigentuemer(Integer partnerIIdEigentuemer) {
		this.partnerIIdEigentuemer = partnerIIdEigentuemer;
	}

	public String getCFirmenbuchnr() {
		return cFirmenbuchnr;
	}

	public void setCFirmenbuchnr(String cFirmenbuchnr) {
		this.cFirmenbuchnr = cFirmenbuchnr;
	}

	public String getCTitel() {
		return cTitel;
	}

	public void setCTitel(String cTitel) {
		this.cTitel = cTitel;
	}

	public String getCGerichtsstand() {
		return cGerichtsstand;
	}

	public void setCGerichtsstand(String cGerichtsstand) {
		this.cGerichtsstand = cGerichtsstand;
	}

	private Integer newslettergrundIId;

	public void setNewslettergrundIId(Integer newslettergrundIId) {
		this.newslettergrundIId = newslettergrundIId;
	}

	@HvDtoLogIdCBez(entityClass = Newslettergrund.class)
	public Integer getNewslettergrundIId() {
		return newslettergrundIId;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	@HvDtoLogIgnore
	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	@HvDtoLogIgnore
	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	@Override
	@HvDtoLogIgnore
	public Integer getPersonalIIdAnlegen() {
		return personalAnlegenIId;
	}

	@Override
	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalAnlegenIId = personalIIdAnlegen;
	}

	@Override
	@HvDtoLogIgnore
	public Integer getPersonalIIdAendern() {
		return personalAendernIId;
	}

	@Override
	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalAendernIId = personalIIdAendern;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PartnerDto)) {
			return false;
		}
		PartnerDto that = (PartnerDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.localeCNrKommunikation == null ? this.localeCNrKommunikation == null
				: that.localeCNrKommunikation.equals(this.localeCNrKommunikation))) {
			return false;
		}
		if (!(that.partnerartCNr == null ? this.partnerartCNr == null
				: that.partnerartCNr.equals(this.partnerartCNr))) {
			return false;
		}
		if (!(that.cKbez == null ? this.cKbez == null : that.cKbez.equals(this.cKbez))) {
			return false;
		}
		if (!(that.anredeCNr == null ? this.anredeCNr == null : that.anredeCNr.equals(this.anredeCNr))) {
			return false;
		}
		if (!(that.cName1nachnamefirmazeile1 == null ? this.cName1nachnamefirmazeile1 == null
				: that.cName1nachnamefirmazeile1.equals(this.cName1nachnamefirmazeile1))) {
			return false;
		}
		if (!(that.cName2vornamefirmazeile2 == null ? this.cName2vornamefirmazeile2 == null
				: that.cName2vornamefirmazeile2.equals(this.cName2vornamefirmazeile2))) {
			return false;
		}
		if (!(that.cName3vorname2abteilung == null ? this.cName3vorname2abteilung == null
				: that.cName3vorname2abteilung.equals(this.cName3vorname2abteilung))) {
			return false;
		}
		if (!(that.cStrasse == null ? this.cStrasse == null : that.cStrasse.equals(this.cStrasse))) {
			return false;
		}
		if (!(that.landplzortIId == null ? this.landplzortIId == null
				: that.landplzortIId.equals(this.landplzortIId))) {
			return false;
		}
		if (!(that.landplzortIIdPostfach == null ? this.landplzortIIdPostfach == null
				: that.landplzortIIdPostfach.equals(this.landplzortIIdPostfach))) {
			return false;
		}
		if (!(that.cPostfach == null ? this.cPostfach == null : that.cPostfach.equals(this.cPostfach))) {
			return false;
		}
		if (!(that.brancheIId == null ? this.brancheIId == null : that.brancheIId.equals(this.brancheIId))) {
			return false;
		}
		if (!(that.partnerklasseIId == null ? this.partnerklasseIId == null
				: that.partnerklasseIId.equals(this.partnerklasseIId))) {
			return false;
		}
		if (!(that.partnerIIdVater == null ? this.partnerIIdVater == null
				: that.partnerIIdVater.equals(this.partnerIIdVater))) {
			return false;
		}
		if (!(that.cUid == null ? this.cUid == null : that.cUid.equals(this.cUid))) {
			return false;
		}
		if (!(that.xBemerkung == null ? this.xBemerkung == null : that.xBemerkung.equals(this.xBemerkung))) {
			return false;
		}
		if (!(that.dGeburtsdatumansprechpartner == null ? this.dGeburtsdatumansprechpartner == null
				: that.dGeburtsdatumansprechpartner.equals(this.dGeburtsdatumansprechpartner))) {
			return false;
		}
		if (!(that.rechtsformIId == null ? this.rechtsformIId == null
				: that.rechtsformIId.equals(this.rechtsformIId))) {
			return false;
		}
		if (!(that.partnerIIdEigentuemer == null ? this.partnerIIdEigentuemer == null
				: that.partnerIIdEigentuemer.equals(this.partnerIIdEigentuemer))) {
			return false;
		}
		if (!(that.cFirmenbuchnr == null ? this.cFirmenbuchnr == null
				: that.cFirmenbuchnr.equals(this.cFirmenbuchnr))) {
			return false;
		}
		if (!(that.cTitel == null ? this.cTitel == null : that.cTitel.equals(this.cTitel))) {
			return false;
		}
		if (!(that.cGerichtsstand == null ? this.cGerichtsstand == null
				: that.cGerichtsstand.equals(this.cGerichtsstand))) {
			return false;
		}
		if (!(that.bVersteckt == null ? this.bVersteckt == null : that.bVersteckt.equals(this.bVersteckt))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.personalAnlegenIId == null ? this.personalAnlegenIId == null
				: that.personalAnlegenIId.equals(this.personalAnlegenIId))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern.equals(this.tAendern))) {
			return false;
		}
		if (!(that.personalAendernIId == null ? this.personalAendernIId == null
				: that.personalAendernIId.equals(this.personalAendernIId))) {
			return false;
		}

		if (!(that.cEmail == null ? this.cEmail == null : that.cEmail.equals(this.cEmail))) {
			return false;
		}
		if (!(that.cTelefon == null ? this.cTelefon == null : that.cTelefon.equals(this.cTelefon))) {
			return false;
		}
		if (!(that.cFax == null ? this.cFax == null : that.cFax.equals(this.cFax))) {
			return false;
		}
		if (!(that.cHomepage == null ? this.cHomepage == null : that.cHomepage.equals(this.cHomepage))) {
			return false;
		}
		if (!(that.cDirektfax == null ? this.cDirektfax == null : that.cDirektfax.equals(this.cDirektfax))) {
			return false;
		}
		if (!(that.newslettergrundIId == null ? this.newslettergrundIId == null
				: that.newslettergrundIId.equals(this.newslettergrundIId))) {
			return false;
		}

		if (!(that.cIln == null ? this.cIln == null : that.cIln.equals(this.cIln))) {
			return false;
		}
		if (!(that.cEori == null ? this.cEori == null : that.cEori.equals(this.cEori))) {
			return false;
		}
		if (!(that.cFilialnummer == null ? this.cFilialnummer == null
				: that.cFilialnummer.equals(this.cFilialnummer))) {
			return false;
		}
		if (!(that.cNtitel == null ? this.cNtitel == null : that.cNtitel.equals(this.cNtitel))) {
			return false;
		}
		if (!(that.fGmtversatz == null ? this.fGmtversatz == null : that.fGmtversatz.equals(this.fGmtversatz))) {
			return false;
		}
		if (!(that.cAdressart == null ? this.cAdressart == null : that.cAdressart.equals(this.cAdressart))) {
			return false;
		}
		if (!(that.lagerIIdZiellager == null ? this.lagerIIdZiellager == null : that.lagerIIdZiellager.equals(this.lagerIIdZiellager))) {
			return false;
		}
		if (!(that.landIIdAbweichendesustland == null ? this.landIIdAbweichendesustland == null : that.landIIdAbweichendesustland.equals(this.landIIdAbweichendesustland))) {
			return false;
		}


		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.localeCNrKommunikation.hashCode();
		result = 37 * result + this.partnerartCNr.hashCode();
		result = 37 * result + this.cKbez.hashCode();
		result = 37 * result + this.anredeCNr.hashCode();
		result = 37 * result + this.cName1nachnamefirmazeile1.hashCode();
		result = 37 * result + this.cName2vornamefirmazeile2.hashCode();
		result = 37 * result + this.cName3vorname2abteilung.hashCode();
		result = 37 * result + this.cStrasse.hashCode();
		result = 37 * result + this.landplzortIId.hashCode();
		result = 37 * result + this.landplzortIIdPostfach.hashCode();
		result = 37 * result + this.cPostfach.hashCode();
		result = 37 * result + this.brancheIId.hashCode();
		result = 37 * result + this.partnerklasseIId.hashCode();
		result = 37 * result + this.partnerIIdVater.hashCode();
		result = 37 * result + this.cUid.hashCode();
		result = 37 * result + this.xBemerkung.hashCode();
		result = 37 * result + this.dGeburtsdatumansprechpartner.hashCode();
		result = 37 * result + this.rechtsformIId.hashCode();
		result = 37 * result + this.partnerIIdEigentuemer.hashCode();
		result = 37 * result + this.cFirmenbuchnr.hashCode();
		result = 37 * result + this.cTitel.hashCode();
		result = 37 * result + this.cGerichtsstand.hashCode();
		result = 37 * result + this.bVersteckt.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalAnlegenIId.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalAendernIId.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + localeCNrKommunikation;
		returnString += ", " + partnerartCNr;
		returnString += ", " + cKbez;
		returnString += ", " + anredeCNr;
		returnString += ", " + cName1nachnamefirmazeile1;
		returnString += ", " + cName2vornamefirmazeile2;
		returnString += ", " + cName3vorname2abteilung;
		returnString += ", " + cStrasse;
		returnString += ", " + landplzortIId;
		returnString += ", " + landplzortIIdPostfach;
		returnString += ", " + cPostfach;
		returnString += ", " + brancheIId;
		returnString += ", " + partnerklasseIId;
		returnString += ", " + partnerIIdVater;
		returnString += ", " + cUid;
		returnString += ", " + xBemerkung;
		returnString += ", " + dGeburtsdatumansprechpartner;
		returnString += ", " + rechtsformIId;
		returnString += ", " + partnerIIdEigentuemer;
		returnString += ", " + cFirmenbuchnr;
		returnString += ", " + cTitel;
		returnString += ", " + cGerichtsstand;
		returnString += ", " + bVersteckt;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalAnlegenIId;
		returnString += ", " + tAendern;
		returnString += ", " + personalAendernIId;
		return returnString;
	}

	// ***Extra******************************************************************
	// **************
	public BankDto getBankDto() {
		return bankDto;
	}

	public void setBankDto(BankDto bankDto) {
		this.bankDto = bankDto;
	}

	public String formatAdresse() {
		String ret = "";

		LandplzortDto lpo = getLandplzortDto();
		if (lpo != null) {
			// UW 03.03.06 Dieser Teil ersetzt das LKZ aus
			// Helper.formatMandantAdresse
			if (lpo.getLandDto() != null && lpo.getLandDto().getCLkz() != null) {
				ret += lpo.getLandDto().getCLkz().trim() + " -";
			}
			if (lpo.getCPlz() != null) {
				ret += " " + lpo.getCPlz().trim();
			}
			if (lpo.getOrtDto().getCName() != null) {
				ret += " " + lpo.getOrtDto().getCName().trim();
			}
			if (getCStrasse() != null) {
				ret += " " + getCStrasse();
			}
		}
		return ret.trim();
	}

	/**
	 * @todo Achtung beinhaltet AnredeTitelName1Name2 obwohl Methodenname nur
	 *       TitelName1Name2 beinhaltet.
	 * @return String
	 */
	public String formatFixTitelName1Name2() {
		String ret = "";
		if (getAnredeCNr() != null) {
			ret += getAnredeCNr().trim();
		}
		if (getCTitel() != null) {
			ret += " " + getCTitel().trim();
		}
		if (getCName1nachnamefirmazeile1() != null) {
			ret += " " + getCName1nachnamefirmazeile1().trim();
		}
		if (getCName2vornamefirmazeile2() != null) {
			ret += " " + getCName2vornamefirmazeile2().trim();
		}
		if (getCNtitel() != null) {
			ret += " " + getCNtitel().trim();
		}
		return ret.trim();
	}

	public String formatFixName1Name2() {
		String ret = "";
		if (getCName1nachnamefirmazeile1() != null) {
			ret += " " + getCName1nachnamefirmazeile1().trim();
		}
		if (getCName2vornamefirmazeile2() != null) {
			ret += " " + getCName2vornamefirmazeile2().trim();
		}
		return ret.trim();
	}

	// lt. SP7876
	public String formatFixTitelVornameNachnameNTitel() {
		String ret = "";

		if (getCTitel() != null) {
			ret += " " + getCTitel().trim();
		}
		if (getCName2vornamefirmazeile2() != null) {
			ret += " " + getCName2vornamefirmazeile2().trim();
		}
		if (getCName1nachnamefirmazeile1() != null) {
			ret += " " + getCName1nachnamefirmazeile1().trim();
		}
		if (getCNtitel() != null) {
			ret += " " + getCNtitel().trim();
		}
		return ret.trim();

	}

	/**
	 * @deprecated MB. use PartnerFac.formatFixAnredeTitelName2Name1(PartnerDto)
	 * 
	 * @return String
	 */
	public String formatFixAnredeTitelName2Name1() {
		String ret = "";
		if (getAnredeCNr() != null) {
			ret += getAnredeCNr().trim();
		}
		if (getCTitel() != null) {
			ret += " " + getCTitel().trim();
		}
		if (getCName2vornamefirmazeile2() != null) {
			ret += " " + getCName2vornamefirmazeile2().trim();
		}
		if (getCName1nachnamefirmazeile1() != null) {
			ret += " " + getCName1nachnamefirmazeile1().trim();
		}
		if (getCNtitel() != null) {
			ret += " " + getCNtitel().trim();
		}
		return ret.trim();
	}

	public String formatFixName2Name1() {
		String ret = "";
		if (getCName2vornamefirmazeile2() != null) {
			ret += getCName2vornamefirmazeile2().trim();
		}
		if (getCName1nachnamefirmazeile1() != null) {
			ret += " " + getCName1nachnamefirmazeile1().trim();
		}
		return ret.trim();
	}

	/**
	 * Formatiere Anrede, Vorname, Nachname, je nach Partnerart.
	 * 
	 * @return wenn person: getAnredeCNr getCName2vornamefirmazeile2
	 *         getCName1nachnamefirmazeile1 sonst (zB.firma):
	 *         getCName1nachnamefirmazeile1 getCName2vornamefirmazeile2
	 */
	public String formatAnrede() {
		String ret = "";

		// if (getPartnerartCNr().equals(PartnerFac.PARTNERART_PERSON) ||
		// getPartnerartCNr().equals(PartnerFac.PARTNERART_ANSPRECHPARTNER)) {
		// ret = formatFixAnredeTitelName2Name1().trim(); // IMS 1462
		// }
		// else {
		// if (getCName2vornamefirmazeile2() != null) {
		// ret += " " + getCName2vornamefirmazeile2().trim();
		// }
		// if (getCName1nachnamefirmazeile1() != null) {
		// ret += " " + getCName1nachnamefirmazeile1().trim();
		// }
		// }
		String sAnredeCNr = getAnredeCNr();
		if (sAnredeCNr != null && !sAnredeCNr.equals("") && (sAnredeCNr.equals(PartnerFac.PARTNER_ANREDE_FRAU)
				|| sAnredeCNr.equals(PartnerFac.PARTNER_ANREDE_HERR))) {
			// herr frau
			ret = formatFixAnredeTitelName2Name1();
		} else {
			// leer firma
			ret = formatFixName1Name2();
		}

		return ret.trim();
	}

	/**
	 * Formatiert Vorname, Nachname, je nach Partnerart.
	 * 
	 * @return wenn person: getCName2vornamefirmazeile2 getCName1nachnamefirmazeile1
	 *         sonst (zB.firma): getCName1nachnamefirmazeile1
	 *         getCName2vornamefirmazeile2
	 */
	public String formatName() {
		String ret = "";

		String sAnredeCNr = getAnredeCNr();
		if (sAnredeCNr != null && !sAnredeCNr.equals("") && (sAnredeCNr.equals(PartnerFac.PARTNER_ANREDE_FRAU)
				|| sAnredeCNr.equals(PartnerFac.PARTNER_ANREDE_HERR))) {
			// herr frau
			ret = formatFixName2Name1();
		} else {
			// leer firma
			ret = formatFixName1Name2();
		}

		return ret.trim();
	}

	/**
	 * Formatiere Anrede, Titel, Vorname, Nachname, je nach Partnerart.
	 * 
	 * @return wenn person: getAnredeCNr getCTitel getCName2vornamefirmazeile2
	 *         getCName1nachnamefirmazeile1 sonst (zB.firma):
	 *         getCName1nachnamefirmazeile1 getCName2vornamefirmazeile2
	 */
	public String formatTitelAnrede() {
		String ret = "";

		if (getPartnerartCNr() != null && ((getPartnerartCNr().equals(PartnerFac.PARTNERART_PERSON)
				|| getPartnerartCNr().equals(PartnerFac.PARTNERART_ANSPRECHPARTNER)))) {
			if (getAnredeCNr() != null) {
				ret += getAnredeCNr().trim();
			}
			if (getCTitel() != null) {
				ret += " " + getCTitel().trim();
			}
			if (getCName1nachnamefirmazeile1() != null) {
				ret += " " + getCName1nachnamefirmazeile1().trim();
			}
			if (getCName2vornamefirmazeile2() != null) {
				ret += " " + getCName2vornamefirmazeile2().trim();
			}
			if (getCNtitel() != null) {
				ret += " " + getCNtitel().trim();
			}
		} else {
			if (getCName1nachnamefirmazeile1() != null) {
				ret += " " + getCName1nachnamefirmazeile1().trim();
			}
			if (getCName2vornamefirmazeile2() != null) {
				ret += " " + getCName2vornamefirmazeile2().trim();
			}
		}
		return ret.trim();
	}

	/**
	 * Hole LKZ, PLZ, Ortsnamen formatiert.
	 * 
	 * @return String zB. D-83101 Rohrdorf
	 */
	public String formatLKZPLZOrt() {
		String ret = null;
		if (landplzortDto != null) {
			ret = (landplzortDto.getLandDto().getCLkz() != null ? landplzortDto.getLandDto().getCLkz() + "-" : "")
					+ (landplzortDto.getCPlz() != null ? landplzortDto.getCPlz() + " " : "")
					+ (landplzortDto.getOrtDto().getCName() != null ? landplzortDto.getOrtDto().getCName() : "");
		}
		return (ret == null || ret.equals("-")) ? null : ret;
	}

	public String formatLKZ() {
		if (landplzortDto != null && landplzortDto.getLandDto() != null) {
			return landplzortDto.getLandDto().getCLkz();
		} else {
			return null;
		}

	}

	public void setLandplzortDto_Postfach(LandplzortDto landplzortDto_Postfach) {
		this.landplzortDto_Postfach = landplzortDto_Postfach;
	}

	@HvDtoLogIgnore
	public LandplzortDto getLandplzortDto() {
		return landplzortDto;
	}

	@HvDtoLogIgnore
	public LandplzortDto getLandplzortDto_Postfach() {
		return landplzortDto_Postfach;
	}

	public byte[] getOBild() {
		return oBild;
	}

	public Integer getLandIIdAbweichendesustland() {
		return landIIdAbweichendesustland;
	}

	@HvDtoLogIdCnr(entityClass = Lager.class)
	public Integer getLagerIIdZiellager() {
		return lagerIIdZiellager;
	}

	public String getCIln() {
		return cIln;
	}

	public String getCFilialnummer() {
		return cFilialnummer;
	}

	public String getCAdressart() {
		return cAdressart;
	}

	public void setLandplzortDto(LandplzortDto landplzortDto) {
		this.landplzortDto = landplzortDto;
	}

	public void setOBild(byte[] oBild) {
		this.oBild = oBild;
	}

	public void setLandIIdAbweichendesustland(Integer landIIdAbweichendesustland) {
		this.landIIdAbweichendesustland = landIIdAbweichendesustland;
	}

	public void setLagerIIdZiellager(Integer lagerIIdZiellager) {
		this.lagerIIdZiellager = lagerIIdZiellager;
	}

	public void setCIln(String cIln) {
		this.cIln = cIln;
	}

	public void setCFilialnummer(String cFilialnummer) {
		this.cFilialnummer = cFilialnummer;
	}

	public void setCAdressart(String cAdressart) {
		this.cAdressart = cAdressart;
	}

	public void setBVersteckt(boolean value) {
		bVersteckt = new Short((short) (value ? 1 : 0));
	}

	public Integer getVersandwegIId() {
		return versandwegIId;
	}

	public void setVersandwegIId(Integer versandwegIId) {
		this.versandwegIId = versandwegIId;
	}

	public void setGeodatenDto(GeodatenDto geodatenDto) {
		this.geodatenDto = geodatenDto;
	}

	public GeodatenDto getGeodatenDto() {
		return geodatenDto;
	}
}
