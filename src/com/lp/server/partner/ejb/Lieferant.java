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
package com.lp.server.partner.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "LieferantfindByiIdPartnercNrMandant", query = "SELECT OBJECT (o) FROM Lieferant o WHERE o.partnerIId=?1 and o.mandantCNr=?2"),
		@NamedQuery(name = "LieferantfindBycKundennrcNrMandant", query = "SELECT OBJECT (o) FROM Lieferant o WHERE o.cKundennr=?1 and o.mandantCNr=?2"),
		@NamedQuery(name = "LieferantfindByKontoIIdKreditorenkonto", query = "SELECT OBJECT (o) FROM Lieferant o WHERE o.kontoIIdKreditorenkonto=?1"),
		@NamedQuery(name = "LieferantfindByRechnungsadresseiIdPartnercNrMandant", query = "SELECT OBJECT (o) FROM Lieferant o WHERE o.partnerIIdRechnungsadresse=?1 and o.mandantCNr=?2"),
		@NamedQuery(name = "LieferantfindByPartnerIId", query = "SELECT OBJECT (O) FROM Lieferant o WHERE o.partnerIId=?1") })
@Entity
@Table(name = "PART_LIEFERANT")
public class Lieferant implements Serializable {
	public Lieferant() {
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "B_MOEGLICHERLIEFERANT")
	private Short bMoeglicherlieferant;

	@Column(name = "B_ZOLLIMPORTPAPIER")
	private Short bZollimportpapier;

	
	public Short getBZollimportpapier() {
		return bZollimportpapier;
	}

	public void setBZollimportpapier(Short bZollimportpapier) {
		this.bZollimportpapier = bZollimportpapier;
	}

	@Column(name = "B_BEURTEILEN")
	private Short bBeurteilen;

	@Column(name = "N_MINDESTBESTELLWERT")
	private BigDecimal nMindestbestellwert;
	
	public BigDecimal getNKupferzahl() {
		return nKupferzahl;
	}

	public void setNKupferzahl(BigDecimal kupferzahl) {
		nKupferzahl = kupferzahl;
	}

	@Column(name = "N_KUPFERZAHL")
	private BigDecimal nKupferzahl;
	
	@Column(name = "N_TRANSPORTKOSTENPROLIEFERUNG")
	private BigDecimal nTransportkostenprolieferung;
	
	public BigDecimal getNTransportkostenprolieferung() {
		return nTransportkostenprolieferung;
	}

	public void setNTransportkostenprolieferung(
			BigDecimal transportkostenprolieferung) {
		nTransportkostenprolieferung = transportkostenprolieferung;
	}

	@Column(name = "B_REVERSECHARGE")
	private Short bReversecharge;
	public Short getBReversecharge() {
		return this.bReversecharge;
	}
	
	@Column(name = "B_IGERWERB")
	private Short bIgErwerb;
	public Short getBIgErwerb() {
		return this.bIgErwerb;
	}

	public void setBIgErwerb(Short bIgErwerb) {
		this.bIgErwerb = bIgErwerb;
	}
	public void setBReversecharge(Short bReversecharge) {
		this.bReversecharge = bReversecharge;
	}
	
	@Column(name = "LAGER_I_ID_ZUBUCHUNGSLAGER")
	private Integer lagerIIdZubuchungslager;

	public Integer getLagerIIdZubuchungslager() {
		return this.lagerIIdZubuchungslager;
	}
	public void setLagerIIdZubuchungslager(Integer lagerIIdZubuchungslager) {
		this.lagerIIdZubuchungslager = lagerIIdZubuchungslager;
	}

	
	@Column(name = "N_KREDIT")
	private BigDecimal nKredit;

	@Column(name = "N_JAHRBONUS")
	private BigDecimal nJahrbonus;

	@Column(name = "N_ABUMSATZ")
	private BigDecimal nAbumsatz;

	@Column(name = "N_MINDERMENGENZUSCHLAG")
	private BigDecimal nMindermengenzuschlag;

	@Column(name = "N_RABATT")
	private Double nRabatt;

	@Column(name = "C_KUNDENNR")
	private String cKundennr;

	@Column(name = "C_HINWEISINTERN")
	private String cHinweisintern;

	@Column(name = "C_HINWEISEXTERN")
	private String cHinweisextern;

	@Column(name = "X_KOMMENTAR")
	private String xKommentar;

	@Column(name = "I_BEURTEILUNG")
	private Integer iBeurteilung;

	@Column(name = "KONTO_I_ID_WARENKONTO")
	private Integer kontoIIdWarenkonto;

	@Column(name = "KONTO_I_ID_KREDITORENKONTO")
	private Integer kontoIIdKreditorenkonto;

	@Column(name = "KOSTENSTELLE_I_ID")
	private Integer kostenstelleIId;

	@Column(name = "LIEFERART_I_ID")
	private Integer lieferartIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "MWSTSATZ_I_ID")
	private Integer mwstsatzIId;

	@Column(name = "SPEDITEUR_I_ID")
	private Integer spediteurIId;

	@Column(name = "WAEHRUNG_C_NR")
	private String waehrungCNr;

	@Column(name = "ZAHLUNGSZIEL_I_ID")
	private Integer zahlungszielIId;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	@Column(name = "PARTNER_I_ID_RECHNUNGSADRESSE")
	private Integer partnerIIdRechnungsadresse;


	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;
	
	@Column(name= "T_BESTELLSPERREAM")
	private Timestamp tBestellsperream;

	@Column(name = "T_FREIGABE")
	private Timestamp tFreigabe;
	
	@Column(name = "T_PERSONAL_FREIGABE")
	private Timestamp tPersonalFreigabe;
	
	@Column(name = "PERSONAL_I_ID_FREIGABE")
	private Integer personalIIdFreigabe;

	@Column(name = "C_FREIGABE")
	private String cFreigabe;
	
	public Timestamp getTFreigabe() {
		return tFreigabe;
	}

	public void setTFreigabe(Timestamp tFreigabe) {
		this.tFreigabe = tFreigabe;
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

	private static final long serialVersionUID = 1L;

	public Lieferant(Integer iId,
			Integer partnerIId, 
			String mandantCNr,
			Integer personalAnlegenIId,
			Integer personalAendernIId,
			String cWaehrung,
			Short moeglicherlieferant,
			Short beurteilen,
			Integer spediteurIId,
			Integer lieferartIId, 
			Integer zahlungszielIId, Short bReversecharge, Short bIgErwerb, Integer lagerIIdZubuchungslager,Short bZollimportpapier) {
		setIId(iId);
		setPartnerIId(partnerIId);
		setMandantCNr(mandantCNr);
		setPersonalIIdAnlegen(personalAnlegenIId);
		setPersonalIIdAendern(personalAendernIId);
		setWaehrungCNr(cWaehrung);

	    //Setzte die defaults fuer die Notnuller
	    setBBeurteilen(beurteilen);
	    setBMoeglicherlieferant(moeglicherlieferant);
	    setTAendern(new Timestamp(System.currentTimeMillis()));
	    setTAnlegen(new Timestamp(System.currentTimeMillis()));
		setLieferartIId(lieferartIId);
		setZahlungszielIId(zahlungszielIId);
		setSpediteurIId(spediteurIId);
		setBReversecharge(bReversecharge);
		setBIgErwerb(bIgErwerb);
		setLagerIIdZubuchungslager(lagerIIdZubuchungslager);
		setBZollimportpapier(bZollimportpapier);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
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

	public Short getBMoeglicherlieferant() {
		return this.bMoeglicherlieferant;
	}

	public void setBMoeglicherlieferant(Short bMoeglicherlieferant) {
		this.bMoeglicherlieferant = bMoeglicherlieferant;
	}

	public Short getBBeurteilen() {
		return this.bBeurteilen;
	}

	public void setBBeurteilen(Short bBeurteilen) {
		this.bBeurteilen = bBeurteilen;
	}

	public BigDecimal getNMindestbestellwert() {
		return this.nMindestbestellwert;
	}

	public void setNMindestbestellwert(BigDecimal nMindestbestellwert) {
		this.nMindestbestellwert = nMindestbestellwert;
	}

	public BigDecimal getNKredit() {
		return this.nKredit;
	}

	public void setNKredit(BigDecimal nKredit) {
		this.nKredit = nKredit;
	}

	public BigDecimal getNJahrbonus() {
		return this.nJahrbonus;
	}

	public void setNJahrbonus(BigDecimal nJahrbonus) {
		this.nJahrbonus = nJahrbonus;
	}

	public BigDecimal getNAbumsatz() {
		return this.nAbumsatz;
	}

	public void setNAbumsatz(BigDecimal nAbumsatz) {
		this.nAbumsatz = nAbumsatz;
	}

	public BigDecimal getNMindermengenzuschlag() {
		return this.nMindermengenzuschlag;
	}

	public void setNMindermengenzuschlag(BigDecimal nMindermengenzuschlag) {
		this.nMindermengenzuschlag = nMindermengenzuschlag;
	}

	public Double getNRabatt() {
		return this.nRabatt;
	}

	public void setNRabatt(Double nRabatt) {
		this.nRabatt = nRabatt;
	}

	public String getCKundennr() {
		return this.cKundennr;
	}

	public void setCKundennr(String cKundennr) {
		this.cKundennr = cKundennr;
	}

	public String getCHinweisintern() {
		return this.cHinweisintern;
	}

	public void setCHinweisintern(String cHinweisintern) {
		this.cHinweisintern = cHinweisintern;
	}

	public String getCHinweisextern() {
		return this.cHinweisextern;
	}

	public void setCHinweisextern(String cHinweisextern) {
		this.cHinweisextern = cHinweisextern;
	}

	public String getXKommentar() {
		return this.xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public Integer getIBeurteilung() {
		return this.iBeurteilung;
	}

	public void setIBeurteilung(Integer iBeurteilung) {
		this.iBeurteilung = iBeurteilung;
	}

	public Integer getKontoIIdWarenkonto() {
		return this.kontoIIdWarenkonto;
	}

	public void setKontoIIdWarenkonto(Integer kontoIIdWarenkonto) {
		this.kontoIIdWarenkonto = kontoIIdWarenkonto;
	}

	public Integer getKontoIIdKreditorenkonto() {
		return this.kontoIIdKreditorenkonto;
	}

	public void setKontoIIdKreditorenkonto(Integer kontoIIdKreditorenkonto) {
		this.kontoIIdKreditorenkonto = kontoIIdKreditorenkonto;
	}

	public Integer getKostenstelleIId() {
		return this.kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public Integer getLieferartIId() {
		return this.lieferartIId;
	}

	public void setLieferartIId(Integer lieferartIId) {
		this.lieferartIId = lieferartIId;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getMwstsatzIId() {
		return this.mwstsatzIId;
	}

	public void setMwstsatzIId(Integer mwstsatzIId) {
		this.mwstsatzIId = mwstsatzIId;
	}

	public Integer getSpediteurIId() {
		return this.spediteurIId;
	}

	public void setSpediteurIId(Integer spediteurIId) {
		this.spediteurIId = spediteurIId;
	}

	public String getWaehrungCNr() {
		return this.waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	public Integer getZahlungszielIId() {
		return this.zahlungszielIId;
	}

	public void setZahlungszielIId(Integer zahlungszielIId) {
		this.zahlungszielIId = zahlungszielIId;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getPartnerIIdRechnungsadresse() {
		return this.partnerIIdRechnungsadresse;
	}

	public void setPartnerIIdRechnungsadresse(Integer partnerIIdRechnungsadresse) {
		this.partnerIIdRechnungsadresse = partnerIIdRechnungsadresse;
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
	

	public Timestamp getTBestellsperream() {
		return this.tBestellsperream;
	}

	public void setTBestellsperream(Timestamp tBestellsperream) {
		this.tBestellsperream = tBestellsperream;
	}



}
