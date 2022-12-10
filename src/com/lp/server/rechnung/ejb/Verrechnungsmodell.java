
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
package com.lp.server.rechnung.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "VerrechnungsmodellFindByMandantCNrCBez", query = "SELECT OBJECT(o) FROM Verrechnungsmodell o WHERE o.mandantCNr = ?1 AND o.cBez = ?2") })

@Entity
@Table(name = "RECH_VERRECHNUNGSMODELL")
public class Verrechnungsmodell implements Serializable {

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	public Short getBVersteckt() {
		return this.bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	@Column(name = "B_NACH_ARTIKEL_VERDICHTEN")
	private Short bNachArtikelVerdichten;

	public Short getBNachArtikelVerdichten() {
		return bNachArtikelVerdichten;
	}

	public void setBNachArtikelVerdichten(Short bNachArtikelVerdichten) {
		this.bNachArtikelVerdichten = bNachArtikelVerdichten;
	}

	@Column(name = "B_PREISE_AUS_AUFTRAG")
	private Short bPreiseAusAuftrag;

	public Short getBPreiseAusAuftrag() {
		return bPreiseAusAuftrag;
	}

	public void setBPreiseAusAuftrag(Short bPreiseAusAuftrag) {
		this.bPreiseAusAuftrag = bPreiseAusAuftrag;
	}

	@Column(name = "ARTIKEL_I_ID_REISESPESEN")
	private Integer artikelIIdReisespesen;
	@Column(name = "ARTIKEL_I_ID_REISEKILOMETER")
	private Integer artikelIIdReisekilometer;
	@Column(name = "ARTIKEL_I_ID_TELEFON")
	private Integer artikelIIdTelefon;
	@Column(name = "ARTIKEL_I_ID_ER")
	private Integer artikelIIdEr;

	public Integer getArtikelIIdReisespesen() {
		return artikelIIdReisespesen;
	}

	public void setArtikelIIdReisespesen(Integer artikelIIdReisespesen) {
		this.artikelIIdReisespesen = artikelIIdReisespesen;
	}

	public Integer getArtikelIIdReisekilometer() {
		return artikelIIdReisekilometer;
	}

	public void setArtikelIIdReisekilometer(Integer artikelIIdReisekilometer) {
		this.artikelIIdReisekilometer = artikelIIdReisekilometer;
	}

	public Integer getArtikelIIdTelefon() {
		return artikelIIdTelefon;
	}

	public void setArtikelIIdTelefon(Integer artikelIIdTelefon) {
		this.artikelIIdTelefon = artikelIIdTelefon;
	}

	public Integer getArtikelIIdEr() {
		return artikelIIdEr;
	}

	public void setArtikelIIdEr(Integer artikelIIdEr) {
		this.artikelIIdEr = artikelIIdEr;
	}

	public Double getFAufschlagEr() {
		return fAufschlagEr;
	}

	public void setFAufschlagEr(Double fAufschlagEr) {
		this.fAufschlagEr = fAufschlagEr;
	}

	@Column(name = "F_AUFSCHLAG_ER")
	private Double fAufschlagEr;

	private static final long serialVersionUID = 1L;

	public Verrechnungsmodell() {
		super();
	}

	public Verrechnungsmodell(Integer id, String mandantCNr, String cBez, Integer artikelIIdReisespesen,
			Integer artikelIIdReisekilometer, Integer artikelIIdTelefon, Integer artikelIIdEr, Double fAufschlagEr,
			Short bVersteckt, Short bPreiseAusAuftrag, Short bNachArtikelVerdichten) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setCBez(cBez);
		setArtikelIIdReisespesen(artikelIIdReisespesen);
		setArtikelIIdReisekilometer(artikelIIdReisekilometer);
		setArtikelIIdTelefon(artikelIIdTelefon);
		setArtikelIIdEr(artikelIIdEr);
		setFAufschlagEr(fAufschlagEr);
		setBVersteckt(bVersteckt);
		setBPreiseAusAuftrag(bPreiseAusAuftrag);
		setBNachArtikelVerdichten(bNachArtikelVerdichten);
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

}
