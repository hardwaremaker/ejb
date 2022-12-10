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
package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "MandantfindByPartnerIId", query = "SELECT OBJECT(o) FROM Mandant o WHERE o.partnerIId=?1"),
		@NamedQuery(name = "MandantfindAlleMandanten", query = "SELECT OBJECT(o) FROM Mandant o") })
@Entity
@Table(name = "LP_MANDANT")
public class Mandant implements Serializable {
	@Id
	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "C_KBEZ")
	private String cKbez;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "BANKVERBINDUNG_I_ID_MANDANT")
	private Integer bankverbindungIIdMandant;

	@Column(name = "KOSTENSTELLE_I_ID")
	private Integer kostenstelleIId;

	@Column(name = "LIEFERART_I_ID_KUNDE")
	private Integer lieferartIIdKunde;

	@Column(name = "LIEFERART_I_ID_LIEFERANT")
	private Integer lieferartIIdLieferant;

	@Column(name = "MWSTSATZ_I_ID_STANDARDINLANDMWSTSATZ")
	private Integer mwstsatzIIdStandardinlandmwstsatz;

	@Column(name = "SPEDITEUR_I_ID_KUNDE")
	private Integer spediteurIIdKunde;

	@Column(name = "SPEDITEUR_I_ID_LIEFERANT")
	private Integer spediteurIIdLieferant;

	@Column(name = "WAEHRUNG_C_NR")
	private String waehrungCNr;
	
	@Column(name = "WAEHRUNG_C_NR_ZUSAETZLICH")
	private String waehrungCNrZusaetzlich;

	public String getWaehrungCNrZusaetzlich() {
		return waehrungCNrZusaetzlich;
	}

	public void setWaehrungCNrZusaetzlich(String waehrungCNrZusaetzlich) {
		this.waehrungCNrZusaetzlich = waehrungCNrZusaetzlich;
	}
	
	@Column(name = "ZAHLUNGSZIEL_I_ID_ANZAHLUNG")
	private Integer zahlungszielIIdAnzahlung;
	
	public Integer getZahlungszielIIdAnzahlung() {
		return zahlungszielIIdAnzahlung;
	}

	public void setZahlungszielIIdAnzahlung(Integer zahlungszielIIdAnzahlung) {
		this.zahlungszielIIdAnzahlung = zahlungszielIIdAnzahlung;
	}

	@Column(name = "I_MAXPERSONEN")
	private Integer iMaxpersonen;
	
	public Integer getIMaxpersonen() {
		return iMaxpersonen;
	}

	public void setIMaxpersonen(Integer iMaxpersonen) {
		this.iMaxpersonen = iMaxpersonen;
	}

	@Column(name = "VERRECHNUNGSMODELL_I_ID")
	private Integer verrechnungsmodellIId;
	
	

	public Integer getVerrechnungsmodellIId() {
		return verrechnungsmodellIId;
	}

	public void setVerrechnungsmodellIId(Integer verrechnungsmodellIId) {
		this.verrechnungsmodellIId = verrechnungsmodellIId;
	}
	@Column(name = "ZAHLUNGSZIEL_I_ID_KUNDE")
	private Integer zahlungszielIIdKunde;

	@Column(name = "ZAHLUNGSZIEL_I_ID_LIEFERANT")
	private Integer zahlungszielIIdLieferant;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "VKPFARTIKELPREISLISTE_I_ID")
	private Integer vkpfartikelpreislisteIId;
	
	@Column(name= "MWSTSATZ_I_ID_STANDARDAUSLANDMWSTSATZ")
	private Integer mwstsatzIIdStandardauslandmwstsatz;

	@Column(name= "PARTNER_I_ID_LIEFERADRESSE")
	private Integer partnerIIdLieferadresse;

	@Column(name= "I_BENUTZERMAX")
	private Integer iBenutzerMax;
	
	@Column(name= "B_DEMO")
	private Short bDemo;
	
	@Column(name= "O_HASH")
	private byte[] oHash;
	
	@Column(name = "O_CODE")
	private byte[] oCode;
	
	@Column(name = "PARTNER_I_ID_FINANZAMT")
	private Integer partnerIIdFinanzamt;

	@Column(name = "I_JAHRE_RUECKDATIERBAR")
	private Integer jahreRueckdatierbar;
	
	
	@Column(name = "LAGER_I_ID_ZIELLAGER")
	private Integer lagerIIdZiellager;

	public Integer getLagerIIdZiellager() {
		return lagerIIdZiellager;
	}

	public void setLagerIIdZiellager(Integer lagerIIdZiellager) {
		this.lagerIIdZiellager = lagerIIdZiellager;
	}
	
	
	@Column(name= "B_PREISLISTE_FUER_NEUKUNDE")
	private Short bPreislisteFuerNeukunde;
	
	public Short getBPreislisteFuerNeukunde() {
		return bPreislisteFuerNeukunde;
	}

	public void setBPreislisteFuerNeukunde(Short bPreislisteFuerNeukunde) {
		this.bPreislisteFuerNeukunde = bPreislisteFuerNeukunde;
	}

	@Column(name= "B_AGB_ANHANG")
	private Short bAgbAnhang;
	public Short getBAgbAnhang() {
		return bAgbAnhang;
	}

	public void setBAgbAnhang(Short bAgbAnhang) {
		this.bAgbAnhang = bAgbAnhang;
	}

	public Short getBAgbAngebot() {
		return bAgbAngebot;
	}

	public void setBAgbAngebot(Short bAgbAngebot) {
		this.bAgbAngebot = bAgbAngebot;
	}

	public Short getBAgbAuftrag() {
		return bAgbAuftrag;
	}

	public void setBAgbAuftrag(Short bAgbAuftrag) {
		this.bAgbAuftrag = bAgbAuftrag;
	}

	public Short getBAgbLieferschein() {
		return bAgbLieferschein;
	}

	public void setBAgbLieferschein(Short bAgbLieferschein) {
		this.bAgbLieferschein = bAgbLieferschein;
	}

	public Short getBAgbRechnung() {
		return bAgbRechnung;
	}

	public void setBAgbRechnung(Short bAgbRechnung) {
		this.bAgbRechnung = bAgbRechnung;
	}

	public Short getBAgbAnfrage() {
		return bAgbAnfrage;
	}

	public void setBAgbAnfrage(Short bAgbAnfrage) {
		this.bAgbAnfrage = bAgbAnfrage;
	}

	public Short getBAgbBestellung() {
		return bAgbBestellung;
	}

	public void setBAgbBestellung(Short bAgbBestellung) {
		this.bAgbBestellung = bAgbBestellung;
	}

	@Column(name= "B_AGB_ANGEBOT")
	private Short bAgbAngebot;
	@Column(name= "B_AGB_AUFTRAG")
	private Short bAgbAuftrag;
	@Column(name= "B_AGB_LIEFERSCHEIN")
	private Short bAgbLieferschein;
	@Column(name= "B_AGB_RECHNUNG")
	private Short bAgbRechnung;
	@Column(name= "B_AGB_ANFRAGE")
	private Short bAgbAnfrage;
	@Column(name= "B_AGB_BESTELLUNG")
	private Short bAgbBestellung;
	
	@Column(name = "C_GLAEUBIGER")
	private String cGlauebiger;
	
	@Column(name = "KOSTENSTELLE_I_ID_FIBU")
	private Integer kostenstelleIIdFibu;

	@Column(name= "MWSTSATZ_I_ID_STANDARDDRITTLANDMWSTSATZ")
	private Integer mwstsatzIIdStandarddrittlandmwstsatz;
	
	@Column(name= "KUNDE_I_ID_STUECKLISTE")
	private Integer kundeIIdStueckliste;
	
	@Column(name = "C_INTRASTATREGION")
	private String cIntrastatRegion;

	
	public String getCGlauebiger() {
		return cGlauebiger;
	}

	public void setCGlauebiger(String cGlauebiger) {
		this.cGlauebiger = cGlauebiger;
	}

	public Integer getKostenstelleIIdFibu() {
		return kostenstelleIIdFibu;
	}

	public void setKostenstelleIIdFibu(Integer kostenstelleIIdFibu) {
		this.kostenstelleIIdFibu = kostenstelleIIdFibu;
	}

	public Integer getKundeIIdStueckliste() {
		return kundeIIdStueckliste;
	}

	public Integer getJahreRueckdatierbar() {
		return jahreRueckdatierbar;
	}
	
	public void setJahreRueckdatierbar(Integer jahreRueckdatierbar) {
		this.jahreRueckdatierbar = jahreRueckdatierbar;
	}

	public void setKundeIIdStueckliste(Integer kundeIIdStueckliste) {
		this.kundeIIdStueckliste = kundeIIdStueckliste;
	}


	public Integer getMwstsatzIIdStandarddrittlandmwstsatz() {
		return mwstsatzIIdStandarddrittlandmwstsatz;
	}





	public void setMwstsatzIIdStandarddrittlandmwstsatz(
			Integer mwstsatzIIdStandarddrittlandmwstsatz) {
		this.mwstsatzIIdStandarddrittlandmwstsatz = mwstsatzIIdStandarddrittlandmwstsatz;
	}





	public Integer getPartnerIIdLieferadresse() {
		return partnerIIdLieferadresse;
	}
	
	
	
	
	
	public void setPartnerIIdLieferadresse(Integer partnerIIdLieferadresse) {
		this.partnerIIdLieferadresse = partnerIIdLieferadresse;
	}

	public String getIntrastatRegion() {
		return cIntrastatRegion;
	}

	public void setIntrastatRegion(String intrastatRegion) {
		this.cIntrastatRegion = intrastatRegion;
	}

	private static final long serialVersionUID = 1L;

	public Mandant() {
		super();
	}

	public Mandant(String nr, String kbez, Integer partnerIId,
			String waehrungCNr, Integer anlegen, Integer aendern,
			Integer spediteurIIdKunde2, Integer lieferartIIdKunde2,
			Integer zahlungszielIIdKunde2, Integer vkpfArtikelpreislisteIId,
			Integer mwstsatzbezIIdStandardinlandmwstsatz,
			Integer mwstsatzbezIIdStandardauslandmwstsatz,
			Integer lieferartIIdLF, Integer spediteurIIdLF,
			Integer zahlungszielIIdLF,
			Integer iPartnerIIdLieferadresse, Integer iMaxpersonen) {

		setCNr(nr);
		setCKbez(kbez);
		setPartnerIId(partnerIId);
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
	    setTAendern(new Timestamp(System.currentTimeMillis()));
		setWaehrungCNr(waehrungCNr);
		setPersonalIIdAnlegen(anlegen);
		setPersonalIIdAendern(aendern);
		setSpediteurIIdKunde(spediteurIIdKunde2);
		setLieferartIIdKunde(lieferartIIdKunde2);
		setZahlungszielIIdKunde(zahlungszielIIdKunde2);
		setVkpfartikelpreislisteIId(vkpfArtikelpreislisteIId);
		setMwstsatzIIdStandardinlandmwstsatz(mwstsatzbezIIdStandardinlandmwstsatz);
		setMwstsatzIIdStandardauslandmwstsatz(mwstsatzbezIIdStandardauslandmwstsatz);
		setLieferartIIdLieferant(lieferartIIdLF);
		setSpediteurIIdLieferant(spediteurIIdLF);
		setZahlungszielIIdLieferant(zahlungszielIIdLF);
		setPartnerIIdLieferadresse(iPartnerIIdLieferadresse);
		setIMaxpersonen(iMaxpersonen);
		this.bDemo = 0;
		this.jahreRueckdatierbar = 1;
		this.bAgbAnfrage = 0;
		this.bAgbAngebot = 0;
		this.bAgbAnhang = 0;
		this.bAgbAuftrag = 0;
		this.bAgbBestellung = 0;
		this.bAgbLieferschein = 0;
		this.bAgbRechnung = 0;
		this.bPreislisteFuerNeukunde = 1;
	}
	

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getCKbez() {
		return this.cKbez;
	}

	public void setCKbez(String cKbez) {
		this.cKbez = cKbez;
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

	public Integer getBankverbindungIIdMandant() {
		return this.bankverbindungIIdMandant;
	}

	public void setBankverbindungIIdMandant(Integer bankverbindungIIdMandant) {
		this.bankverbindungIIdMandant = bankverbindungIIdMandant;
	}

	public Integer getKostenstelleIId() {
		return this.kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelle) {
		this.kostenstelleIId = kostenstelle;
	}

	public Integer getLieferartIIdKunde() {
		return this.lieferartIIdKunde;
	}

	public void setLieferartIIdKunde(Integer lieferartIIdKunde) {
		this.lieferartIIdKunde = lieferartIIdKunde;
	}

	public Integer getLieferartIIdLieferant() {
		return this.lieferartIIdLieferant;
	}

	public void setLieferartIIdLieferant(Integer lieferartIIdLieferant) {
		this.lieferartIIdLieferant = lieferartIIdLieferant;
	}

	public Integer getMwstsatzIIdStandardinlandmwstsatz() {
		return this.mwstsatzIIdStandardinlandmwstsatz;
	}

	public void setMwstsatzIIdStandardinlandmwstsatz(
			Integer mwstsatzIIdStandardinlandmwstsatz) {
		this.mwstsatzIIdStandardinlandmwstsatz = mwstsatzIIdStandardinlandmwstsatz;
	}
	
	public Integer getMwstsatzIIdStandardauslandmwstsatz() {
		return this.mwstsatzIIdStandardauslandmwstsatz;
	}

	public void setMwstsatzIIdStandardauslandmwstsatz(
			Integer mwstsatzIIdStandardauslandmwstsatz) {
		this.mwstsatzIIdStandardauslandmwstsatz = mwstsatzIIdStandardauslandmwstsatz;
	}

	public Integer getSpediteurIIdKunde() {
		return this.spediteurIIdKunde;
	}

	public void setSpediteurIIdKunde(Integer spediteurIIdKunde) {
		this.spediteurIIdKunde = spediteurIIdKunde;
	}

	public Integer getSpediteurIIdLieferant() {
		return this.spediteurIIdLieferant;
	}

	public void setSpediteurIIdLieferant(Integer spediteurIIdLieferant) {
		this.spediteurIIdLieferant = spediteurIIdLieferant;
	}

	public String getWaehrungCNr() {
		return this.waehrungCNr;
	}

	public void setWaehrungCNr(String waehrung) {
		this.waehrungCNr = waehrung;
	}

	public Integer getZahlungszielIIdKunde() {
		return this.zahlungszielIIdKunde;
	}

	public void setZahlungszielIIdKunde(Integer zahlungszielIIdKunde) {
		this.zahlungszielIIdKunde = zahlungszielIIdKunde;
	}

	public Integer getZahlungszielIIdLieferant() {
		return this.zahlungszielIIdLieferant;
	}

	public void setZahlungszielIIdLieferant(Integer zahlungszielIIdLieferant) {
		this.zahlungszielIIdLieferant = zahlungszielIIdLieferant;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partner) {
		this.partnerIId = partner;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getVkpfartikelpreislisteIId() {
		return this.vkpfartikelpreislisteIId;
	}

	public void setVkpfartikelpreislisteIId(Integer vkpfartikelpreisliste) {
		this.vkpfartikelpreislisteIId = vkpfartikelpreisliste;
	}

	public Integer getIBenutzerMax() {
		return iBenutzerMax;
	}

	public void setIBenutzerMax(Integer iBenutzerMax) {
		this.iBenutzerMax = iBenutzerMax ;
	}

	public Short getBDemo() {
		return bDemo;
	}

	public byte[] getOHash() {
		return oHash;
	}

	public void setOCode(byte[] oCode) {
		this.oCode = oCode;
	}

	public byte[] getOCode() {
		return oCode;
	}





	public void setPartnerIIdFinanzamt(Integer partnerIIdFinanzamt) {
		this.partnerIIdFinanzamt = partnerIIdFinanzamt;
	}





	public Integer getPartnerIIdFinanzamt() {
		return partnerIIdFinanzamt;
	}

}
