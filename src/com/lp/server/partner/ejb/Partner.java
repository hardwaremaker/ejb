/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.partner.ejb;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.ICBez;
import com.lp.server.util.IIId;

@NamedQueries( {
	@NamedQuery(name = "PartnerfindByCName1", query = "SELECT OBJECT(C) FROM Partner c WHERE c.cName1nachnamefirmazeile1 like ?1"),
	@NamedQuery(name = PartnerQuery.ByUID, query = "SELECT OBJECT(O) FROM Partner o WHERE REPLACE(o.cUid, ' ', '') like :uid"),
	@NamedQuery(name = PartnerQuery.ByLowerCName1, query = "SELECT OBJECT(C) FROM Partner c WHERE LOWER(c.cName1nachnamefirmazeile1) like :cname"),
	@NamedQuery(name = PartnerQuery.ByKbez, query = "SELECT OBJECT(C) FROM Partner c WHERE LOWER(c.cKbez) like :kbez")
	})
@Entity
@Table(name = "PART_PARTNER")
public class Partner implements Serializable, IIId, ICBez {	
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_KBEZ")
	private String cKbez;

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	@Column(name = "C_NAME1NACHNAMEFIRMAZEILE1")
	private String cName1nachnamefirmazeile1;

	@Column(name = "C_EORI")
	private String cEori;

	
	public String getCEori() {
		return cEori;
	}

	public void setCEori(String cEori) {
		this.cEori = cEori;
	}


	@Column(name = "C_HOMEPAGE")
	private String cHomepage;
	
	@Column(name = "C_FAX")
	private String cFax;
	
	@Column(name = "C_TELEFON")
	private String cTelefon;
	
	@Column(name = "C_HANDY")
	private String cHandy;
	
	@Column(name = "C_DIREKTFAX")
	private String cDirektfax;
	
	@Column(name = "C_EMAIL")
	private String cEmail;
	
	
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
	
	
	public String getCHomepage() {
		return cHomepage;
	}

	public void setCHomepage(String cHomepage) {
		this.cHomepage = cHomepage;
	}
	
	
	
	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "C_NAME2VORNAMEFIRMAZEILE2")
	private String cName2vornamefirmazeile2;

	@Column(name = "C_NAME3VORNAME2ABTEILUNG")
	private String cName3vorname2abteilung;

	@Column(name = "C_STRASSE")
	private String cStrasse;

	@Column(name = "C_POSTFACH")
	private String cPostfach;

	@Column(name = "C_UID")
	private String cUid;

	@Column(name = "X_BEMERKUNG")
	private String xBemerkung;

	@Column(name = "T_GEBURTSDATUMANSPRECHPARTNER")
	private Date tGeburtsdatumansprechpartner;

	@Column(name = "C_FIRMENBUCHNR")
	private String cFirmenbuchnr;

	@Column(name = "C_TITEL")
	private String cTitel;

	@Column(name = "C_NTITEL")
	private String cNtitel;
	
	@Column(name = "C_GERICHTSSTAND")
	private String cGerichtsstand;

	@Column(name = "O_BILD")
	private byte[] oBild;

	@Column(name = "C_ILN")
	private String cIln;

	@Column(name = "C_FILIALNUMMER")
	private String cFilialnummer;

	@Column(name = "C_ADRESSART")
	private String cAdressart;

	@Column(name = "LAND_I_ID_ABWEICHENDESUSTLAND")
	private Integer landIIdAbweichendesustland;

	@Column(name = "LANDPLZORT_I_ID_POSTFACH")
	private Integer landplzortIIdPostfach;

	@Column(name = "LANDPLZORT_I_ID")
	private Integer landplzortIId;

	@Column(name = "LOCALE_C_NR_KOMMUNIKATION")
	private String localeCNrKommunikation;

	@Column(name = "RECHTSFORM_I_ID")
	private Integer rechtsformIId;

	@Column(name = "ANREDE_C_NR")
	private String anredeCNr;

	@Column(name = "BRANCHE_I_ID")
	private Integer brancheIId;

	@Column(name = "PARTNER_I_ID_EIGENTUEMER")
	private Integer partnerIIdEigentuemer;

	@Column(name = "PARTNER_I_ID_VATER")
	private Integer partnerIIdVater;

	@Column(name = "PARTNERART_C_NR")
	private String partnerartCNr;

	@Column(name = "PARTNERKLASSE_I_ID")
	private Integer partnerklasseIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "LAGER_I_ID_ZIELLAGER")
	private Integer lagerIIdZiellager;

	@Column(name = "F_GMTVERSATZ")
	private Double fGmtversatz;

	@Column(name = "VERSANDWEG_I_ID")
	private Integer versandwegIId ;

	
	public Double getFGmtversatz() {
		return fGmtversatz;
	}

	public void setFGmtversatz(Double gmtversatz) {
		fGmtversatz = gmtversatz;
	}

	public Partner(){}


	private static final long serialVersionUID = 1L;

	public Partner(Integer iId,
			String localeCNrKommunikation,
			String partnerartCNr,
			String cKBez,
			Short bVersteckt,
			String cName1nachnamefirmazeile1,
			Integer personalAnlegenIId,
			Integer personalAendernIId) {

		setIId(iId);
		setLocaleCNrKommunikation(localeCNrKommunikation);
		setPartnerartCNr(partnerartCNr);
		setCKbez(cKBez);
		setCName1nachnamefirmazeile1(cName1nachnamefirmazeile1);
		setBVersteckt(bVersteckt);
		//die ts anlegen, aendern nur am server
	    setTAnlegen(new Timestamp(System.currentTimeMillis()));
	    setTAendern(new Timestamp(System.currentTimeMillis()));
		setPersonalIIdAnlegen(personalAnlegenIId);
		setPersonalIIdAendern(personalAendernIId);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCKbez() {
		return this.cKbez;
	}

	public void setCKbez(String cKbez) {
		this.cKbez = cKbez;
	}

	public Short getBVersteckt() {
		return this.bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public String getCName1nachnamefirmazeile1() {
		return this.cName1nachnamefirmazeile1;
	}

	public void setCName1nachnamefirmazeile1(String cName1nachnamefirmazeile1) {
		this.cName1nachnamefirmazeile1 = cName1nachnamefirmazeile1;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public String getCName2vornamefirmazeile2() {
		return this.cName2vornamefirmazeile2;
	}

	public void setCName2vornamefirmazeile2(String cName2vornamefirmazeile2) {
		this.cName2vornamefirmazeile2 = cName2vornamefirmazeile2;
	}

	public String getCName3vorname2abteilung() {
		return this.cName3vorname2abteilung;
	}

	public void setCName3vorname2abteilung(String cName3vorname2abteilung) {
		this.cName3vorname2abteilung = cName3vorname2abteilung;
	}

	public String getCStrasse() {
		return this.cStrasse;
	}

	public void setCStrasse(String cStrasse) {
		this.cStrasse = cStrasse;
	}

	public String getCPostfach() {
		return this.cPostfach;
	}

	public void setCPostfach(String cPostfach) {
		this.cPostfach = cPostfach;
	}

	public String getCUid() {
		return this.cUid;
	}

	public void setCUid(String cUid) {
		this.cUid = cUid;
	}

	public String getXBemerkung() {
		return this.xBemerkung;
	}

	public void setXBemerkung(String xBemerkung) {
		this.xBemerkung = xBemerkung;
	}

	public Date getTGeburtsdatumansprechpartner() {
		return this.tGeburtsdatumansprechpartner;
	}

	public void setTGeburtsdatumansprechpartner(
			Date tGeburtsdatumansprechpartner) {
		this.tGeburtsdatumansprechpartner = tGeburtsdatumansprechpartner;
	}

	public String getCFirmenbuchnr() {
		return this.cFirmenbuchnr;
	}

	public void setCFirmenbuchnr(String cFirmenbuchnr) {
		this.cFirmenbuchnr = cFirmenbuchnr;
	}

	public String getCTitel() {
		return this.cTitel;
	}

	public void setCTitel(String cTitel) {
		this.cTitel = cTitel;
	}

	public String getCNtitel() {
		return this.cNtitel;
	}

	public void setCNtitel(String cNtitel) {
		this.cNtitel = cNtitel;
	}

	public String getCGerichtsstand() {
		return this.cGerichtsstand;
	}

	public void setCGerichtsstand(String cGerichtsstand) {
		this.cGerichtsstand = cGerichtsstand;
	}

	public byte[] getOBild() {
		return this.oBild;
	}

	public void setOBild(byte[] oBild) {
		this.oBild = oBild;
	}

	public String getCIln() {
		return this.cIln;
	}

	public void setCIln(String cIln) {
		this.cIln = cIln;
	}

	public String getCFilialnummer() {
		return this.cFilialnummer;
	}

	public void setCFilialnummer(String cFilialnummer) {
		this.cFilialnummer = cFilialnummer;
	}

	public String getCAdressart() {
		return this.cAdressart;
	}

	public void setCAdressart(String cAdressart) {
		this.cAdressart = cAdressart;
	}

	public Integer getLandIIdAbweichendesustland() {
		return this.landIIdAbweichendesustland;
	}

	public void setLandIIdAbweichendesustland(Integer landIIdAbweichendesustland) {
		this.landIIdAbweichendesustland = landIIdAbweichendesustland;
	}

	public Integer getLandplzortIIdPostfach() {
		return this.landplzortIIdPostfach;
	}

	public void setLandplzortIIdPostfach(Integer landplzortIIdPostfach) {
		this.landplzortIIdPostfach = landplzortIIdPostfach;
	}

	public Integer getLandplzortIId() {
		return this.landplzortIId;
	}

	public void setLandplzortIId(Integer landplzortIId) {
		this.landplzortIId = landplzortIId;
	}

	public String getLocaleCNrKommunikation() {
		return this.localeCNrKommunikation;
	}

	public void setLocaleCNrKommunikation(String localeCNrKommunikation) {
		this.localeCNrKommunikation = localeCNrKommunikation;
	}

	public Integer getRechtsformIId() {
		return this.rechtsformIId;
	}

	public void setRechtsformIId(Integer rechtsformIId) {
		this.rechtsformIId = rechtsformIId;
	}

	public String getAnredeCNr() {
		return this.anredeCNr;
	}

	public void setAnredeCNr(String anredeCNr) {
		this.anredeCNr = anredeCNr;
	}

	public Integer getBrancheIId() {
		return this.brancheIId;
	}

	public void setBrancheIId(Integer brancheIId) {
		this.brancheIId = brancheIId;
	}

	public Integer getPartnerIIdEigentuemer() {
		return this.partnerIIdEigentuemer;
	}

	public void setPartnerIIdEigentuemer(Integer partnerIIdEigentuemer) {
		this.partnerIIdEigentuemer = partnerIIdEigentuemer;
	}

	public Integer getPartnerIIdVater() {
		return this.partnerIIdVater;
	}

	public void setPartnerIIdVater(Integer partnerIIdVater) {
		this.partnerIIdVater = partnerIIdVater;
	}

	public String getPartnerartCNr() {
		return this.partnerartCNr;
	}

	public void setPartnerartCNr(String partnerartCNr) {
		this.partnerartCNr = partnerartCNr;
	}

	public Integer getPartnerklasseIId() {
		return this.partnerklasseIId;
	}

	public void setPartnerklasseIId(Integer partnerklasseIId) {
		this.partnerklasseIId = partnerklasseIId;
	}

	
	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getLagerIIdZiellager() {
		return this.lagerIIdZiellager;
	}

	public void setLagerIIdZiellager(Integer lagerIIdZiellager) {
		this.lagerIIdZiellager = lagerIIdZiellager;
	}

	@Override
	public String getCBez() {
		return getCKbez() ;
	}

	@Override
	public void setCBez(String cnr) {
		setCKbez(cnr) ;
	}

	public Integer getVersandwegIId() {
		return versandwegIId;
	}

	public void setVersandwegIId(Integer versandwegIId) {
		this.versandwegIId = versandwegIId;
	}	
}
