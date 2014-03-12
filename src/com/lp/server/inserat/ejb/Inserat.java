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
package com.lp.server.inserat.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "InseratfindByMandantCNrCnr", query = "SELECT OBJECT (o) FROM Inserat o WHERE o.mandantCNr=?1 AND o.cNr=?2"),
		@NamedQuery(name = "InseratfindByLieferantIId", query = "SELECT OBJECT (o) FROM Inserat o WHERE o.lieferantIId=?1"),
		@NamedQuery(name = "InseratfindByAnsprechpartnerIIdLieferant", query = "SELECT OBJECT (o) FROM Inserat o WHERE o.ansprechpartnerIIdLieferant=?1"),
		@NamedQuery(name = "InseratfindByBestellpositionIId", query = "SELECT OBJECT (o) FROM Inserat o WHERE o.bestellpositionIId=?1") })
@Entity
@Table(name = "IV_INSERAT")
public class Inserat implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "T_BELEGDATUM")
	private Timestamp tBelegdatum;

	@Column(name = "F_KD_RABATT")
	private Double fKdRabatt;
	@Column(name = "F_KD_ZUSATZRABATT")
	private Double fKdZusatzrabatt;
	@Column(name = "F_KD_NACHLASS")
	private Double fKdNachlass;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "C_RUBRIK")
	private String cRubrik;

	@Column(name = "C_RUBRIK2")
	private String cRubrik2;

	@Column(name = "C_STICHWORT")
	private String cStichwort;

	@Column(name = "C_STICHWORT2")
	private String cStichwort2;

	public String getCStichwort() {
		return cStichwort;
	}

	public void setCStichwort(String cStichwort) {
		this.cStichwort = cStichwort;
	}

	public String getCStichwort2() {
		return cStichwort2;
	}

	public void setCStichwort2(String cStichwort2) {
		this.cStichwort2 = cStichwort2;
	}

	@Column(name = "C_MEDIUM")
	private String cMedium;

	@Column(name = "X_ANHANG")
	private String xAnhang;

	@Column(name = "LIEFERANT_I_ID")
	private Integer lieferantIId;

	@Column(name = "ANSPRECHPARTNER_I_ID_LIEFERANT")
	private Integer ansprechpartnerIIdLieferant;
	@Column(name = "F_LF_RABATT")
	private Double fLFRabatt;
	@Column(name = "F_LF_ZUSATZRABATT")
	private Double fLfZusatzrabatt;
	@Column(name = "F_LF_NACHLASS")
	private Double fLfNachlass;
	@Column(name = "X_ANHANG_LF")
	private String xAnhangLf;

	@Column(name = "T_TERMIN")
	private Timestamp tTermin;

	@Column(name = "ARTIKEL_I_ID_INSERATART")
	private Integer artikelIIdInseratart;

	@Column(name = "BESTELLPOSITION_I_ID")
	private Integer bestellpositionIId;

	public Integer getBestellpositionIId() {
		return bestellpositionIId;
	}

	public void setBestellpositionIId(Integer bestellpositionIId) {
		this.bestellpositionIId = bestellpositionIId;
	}

	@Column(name = "STATUS_C_NR")
	private String statusCNr;

	@Column(name = "PERSONAL_I_ID_VERTRETER")
	private Integer personalIIdVertreter;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "N_NETTOEINZELPREIS_EK")
	private BigDecimal nNettoeinzelpreisEk;

	@Column(name = "N_NETTOEINZELPREIS_VK")
	private BigDecimal nNettoeinzelpreisVk;

	@Column(name = "T_ERSCHIENEN")
	private Timestamp tErschienen;
	@Column(name = "PERSONAL_I_ID_ERSCHIENEN")
	private Integer personalIIdErschienen;
	@Column(name = "T_VERRECHNEN")
	private Timestamp tVerrechnen;
	@Column(name = "PERSONAL_I_ID_VERRECHNEN")
	private Integer personalIIdVerrechnen;
	
	@Column(name = "T_MANUELLVERRECHNEN")
	private Timestamp tManuellverrechnen;
	public Timestamp getTManuellverrechnen() {
		return tManuellverrechnen;
	}

	public void setTManuellverrechnen(Timestamp tManuellverrechnen) {
		this.tManuellverrechnen = tManuellverrechnen;
	}

	public Integer getPersonalIIdManuellverrechnen() {
		return personalIIdManuellverrechnen;
	}

	public void setPersonalIIdManuellverrechnen(Integer personalIIdManuellverrechnen) {
		this.personalIIdManuellverrechnen = personalIIdManuellverrechnen;
	}

	@Column(name = "PERSONAL_I_ID_MANUELLVERRECHNEN")
	private Integer personalIIdManuellverrechnen;
	

	@Column(name = "C_GESTOPPT")
	private String cGestoppt;

	@Column(name = "B_DRUCK_BESTELLUNG_LF")
	private Short bDruckBestellungLf;
	@Column(name = "B_DRUCK_BESTELLUNG_KD")
	private Short bDruckBestellungKd;
	@Column(name = "B_DRUCK_RECHNUNG_KD")
	private Short bDruckRechnungKd;
	@Column(name = "T_TERMIN_BIS")
	private Timestamp tTerminBis;

	@Column(name = "PERSONAL_I_ID_MANUELLERLEDIGT")
	private Integer personalIIdManuellerledigt;
	@Column(name = "T_MANUELLERLEDIGT")
	private Timestamp tManuellerledigt;

	@Column(name = "PERSONAL_I_ID_GESTOPPT")
	private Integer personalIIdGestoppt;
	@Column(name = "T_GESTOPPT")
	private Timestamp tGestoppt;

	
	public Integer getPersonalIIdGestoppt() {
		return personalIIdGestoppt;
	}

	public void setPersonalIIdGestoppt(Integer personalIIdGestoppt) {
		this.personalIIdGestoppt = personalIIdGestoppt;
	}

	public Timestamp getTGestoppt() {
		return tGestoppt;
	}

	public void setTGestoppt(Timestamp tGestoppt) {
		this.tGestoppt = tGestoppt;
	}

	public Timestamp getTManuellerledigt() {
		return this.tManuellerledigt;
	}

	public void setTManuellerledigt(Timestamp tManuellerledigt) {
		this.tManuellerledigt = tManuellerledigt;
	}
	
	public Integer getPersonalIIdManuellerledigt() {
		return this.personalIIdManuellerledigt;
	}

	public void setPersonalIIdManuellerledigt(Integer personalIIdManuellerledigt) {
		this.personalIIdManuellerledigt = personalIIdManuellerledigt;
	}
	public Short getBDruckBestellungLf() {
		return bDruckBestellungLf;
	}

	public void setBDruckBestellungLf(Short bDruckBestellungLf) {
		this.bDruckBestellungLf = bDruckBestellungLf;
	}

	public Short getBDruckBestellungKd() {
		return bDruckBestellungKd;
	}

	public void setBDruckBestellungKd(Short bDruckBestellungKd) {
		this.bDruckBestellungKd = bDruckBestellungKd;
	}

	public Short getBDruckRechnungKd() {
		return bDruckRechnungKd;
	}

	public void setBDruckRechnungKd(Short bDruckRechnungKd) {
		this.bDruckRechnungKd = bDruckRechnungKd;
	}

	public Timestamp getTTerminBis() {
		return tTerminBis;
	}

	public void setTTerminBis(Timestamp tTerminBis) {
		this.tTerminBis = tTerminBis;
	}

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "B_WERTAUFTEILEN")
	private Short bWertaufteilen;
	
	public Short getBWertaufteilen() {
		return bWertaufteilen;
	}

	public void setBWertaufteilen(Short bWertaufteilen) {
		this.bWertaufteilen = bWertaufteilen;
	}

	private static final long serialVersionUID = 1L;

	public Inserat(Integer id, String nr, String mandantCNr, Double fKdRabatt,
			Double fKdZusatzrabatt, Double fKdNachlass, Integer lieferantIId,
			Timestamp tBelegdatum, Double fLfRabatt, Double fLfZusatzrabatt,
			Double fLfNachlass, Timestamp tTermin, String statusCNr,
			Integer artikelIIdInseratart, Integer personalIIdAnlegen,
			Integer personalIIdAendern, Integer personalIIdVertreter,
			BigDecimal nMenge, BigDecimal nPreisEK, BigDecimal nPReisVK,
			Timestamp tAnlegen, Timestamp tAendern, Short bDruckBestellungLf,
			Short bDruckBestellungKd, Short bDruckRechnungKd,Short bWertaufteilen) {
		setIId(id);
		setCNr(nr);
		setMandantCNr(mandantCNr);
		setTBelegdatum(tBelegdatum);
		setFKdRabatt(fKdRabatt);
		setFKdZusatzrabatt(fKdZusatzrabatt);
		setFKdNachlass(fKdNachlass);
		setLieferantIId(lieferantIId);
		setFLFRabatt(fLfRabatt);
		setFLfZusatzrabatt(fLfZusatzrabatt);
		setFLfNachlass(fLfNachlass);
		setTTermin(tTermin);
		setStatusCNr(statusCNr);
		setArtikelIIdInseratart(artikelIIdInseratart);
		setPersonalIIdVertreter(personalIIdVertreter);
		setNMenge(nMenge);
		setNNettoeinzelpreisEk(nPreisEK);
		setNNettoeinzelpreisVk(nPReisVK);

		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setTAnlegen(tAnlegen);
		setTAendern(tAendern);
		setBDruckBestellungKd(bDruckBestellungKd);
		setBDruckBestellungLf(bDruckBestellungLf);
		setBDruckRechnungKd(bDruckRechnungKd);
		setBWertaufteilen(bWertaufteilen);

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

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Timestamp getTBelegdatum() {
		return tBelegdatum;
	}

	public void setTBelegdatum(Timestamp tBelegdatum) {
		this.tBelegdatum = tBelegdatum;
	}

	public Double getFKdRabatt() {
		return fKdRabatt;
	}

	public void setFKdRabatt(Double fKdRabatt) {
		this.fKdRabatt = fKdRabatt;
	}

	public Double getFKdZusatzrabatt() {
		return fKdZusatzrabatt;
	}

	public void setFKdZusatzrabatt(Double fKdZusatzrabatt) {
		this.fKdZusatzrabatt = fKdZusatzrabatt;
	}

	public Double getFKdNachlass() {
		return fKdNachlass;
	}

	public void setFKdNachlass(Double fKdNachlass) {
		this.fKdNachlass = fKdNachlass;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCRubrik() {
		return cRubrik;
	}

	public void setCRubrik(String cRubrik) {
		this.cRubrik = cRubrik;
	}

	public String getCRubrik2() {
		return cRubrik2;
	}

	public void setCRubrik2(String cRubrik2) {
		this.cRubrik2 = cRubrik2;
	}

	public String getCMedium() {
		return cMedium;
	}

	public void setCMedium(String cMedium) {
		this.cMedium = cMedium;
	}

	public String getXAnhang() {
		return xAnhang;
	}

	public void setXAnhang(String xAnhang) {
		this.xAnhang = xAnhang;
	}

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	public Integer getAnsprechpartnerIIdLieferant() {
		return ansprechpartnerIIdLieferant;
	}

	public void setAnsprechpartnerIIdLieferant(
			Integer ansprechpartnerIIdLieferant) {
		this.ansprechpartnerIIdLieferant = ansprechpartnerIIdLieferant;
	}

	public Double getFLFRabatt() {
		return fLFRabatt;
	}

	public void setFLFRabatt(Double fLFRabatt) {
		this.fLFRabatt = fLFRabatt;
	}

	public Double getFLfZusatzrabatt() {
		return fLfZusatzrabatt;
	}

	public void setFLfZusatzrabatt(Double fLfZusatzrabatt) {
		this.fLfZusatzrabatt = fLfZusatzrabatt;
	}

	public Double getFLfNachlass() {
		return fLfNachlass;
	}

	public void setFLfNachlass(Double fLfNachlass) {
		this.fLfNachlass = fLfNachlass;
	}

	public String getXAnhangLf() {
		return xAnhangLf;
	}

	public void setXAnhangLf(String xAnhangLf) {
		this.xAnhangLf = xAnhangLf;
	}

	public Timestamp getTTermin() {
		return tTermin;
	}

	public void setTTermin(Timestamp tTermin) {
		this.tTermin = tTermin;
	}

	public Integer getArtikelIIdInseratart() {
		return artikelIIdInseratart;
	}

	public void setArtikelIIdInseratart(Integer artikelIIdInseratart) {
		this.artikelIIdInseratart = artikelIIdInseratart;
	}

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	public Integer getPersonalIIdVertreter() {
		return personalIIdVertreter;
	}

	public void setPersonalIIdVertreter(Integer personalIIdVertreter) {
		this.personalIIdVertreter = personalIIdVertreter;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public BigDecimal getNNettoeinzelpreisEk() {
		return nNettoeinzelpreisEk;
	}

	public void setNNettoeinzelpreisEk(BigDecimal nNettoeinzelpreisEk) {
		this.nNettoeinzelpreisEk = nNettoeinzelpreisEk;
	}

	public BigDecimal getNNettoeinzelpreisVk() {
		return nNettoeinzelpreisVk;
	}

	public void setNNettoeinzelpreisVk(BigDecimal nNettoeinzelpreisVk) {
		this.nNettoeinzelpreisVk = nNettoeinzelpreisVk;
	}

	public Timestamp getTErschienen() {
		return tErschienen;
	}

	public void setTErschienen(Timestamp tErschienen) {
		this.tErschienen = tErschienen;
	}

	public Integer getPersonalIIdErschienen() {
		return personalIIdErschienen;
	}

	public void setPersonalIIdErschienen(Integer personalIIdErschienen) {
		this.personalIIdErschienen = personalIIdErschienen;
	}

	public Timestamp getTVerrechnen() {
		return tVerrechnen;
	}

	public void setTVerrechnen(Timestamp tVerrechnen) {
		this.tVerrechnen = tVerrechnen;
	}

	public Integer getPersonalIIdVerrechnen() {
		return personalIIdVerrechnen;
	}

	public void setPersonalIIdVerrechnen(Integer personalIIdVerrechnen) {
		this.personalIIdVerrechnen = personalIIdVerrechnen;
	}

	public String getCGestoppt() {
		return cGestoppt;
	}

	public void setCGestoppt(String cGestoppt) {
		this.cGestoppt = cGestoppt;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
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

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Inserat() {

	}

}
