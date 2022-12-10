package com.lp.server.personal.ejb;

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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "ZahltagFindByIMonatMandantCNr", query = "SELECT OBJECT(C) FROM Zahltag c WHERE c.iMonat = ?1 AND c.mandantCNr = ?2"),
		@NamedQuery(name = "ZahltagFindByMandantCNr", query = "SELECT OBJECT(C) FROM Zahltag c WHERE c.mandantCNr = ?1") })
@Entity
@Table(name = "PERS_ZAHLTAG")
public class Zahltag implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_MONAT")
	private Integer iMonat;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "F_FAKTOR")
	private Double fFaktor;

	public Double getFFaktor() {
		return fFaktor;
	}

	public void setFFaktor(Double fFaktor) {
		this.fFaktor = fFaktor;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	@Column(name = "I_STICHTAG_NETTO")
	private Integer iStichtagNetto;

	public Integer getIMonat() {
		return iMonat;
	}

	public void setIMonat(Integer iMonat) {
		this.iMonat = iMonat;
	}

	public Integer getIStichtagNetto() {
		return iStichtagNetto;
	}

	public void setIStichtagNetto(Integer iStichtagNetto) {
		this.iStichtagNetto = iStichtagNetto;
	}

	public Short getBMonatsletzterNetto() {
		return bMonatsletzterNetto;
	}

	public void setBMonatsletzterNetto(Short bMonatsletzterNetto) {
		this.bMonatsletzterNetto = bMonatsletzterNetto;
	}

	public Integer getIStichtagLohnsteuerRelativZumLetzten() {
		return iStichtagLohnsteuerRelativZumLetzten;
	}

	public void setIStichtagLohnsteuerRelativZumLetzten(
			Integer iStichtagLohnsteuerRelativZumLetzten) {
		this.iStichtagLohnsteuerRelativZumLetzten = iStichtagLohnsteuerRelativZumLetzten;
	}

	public Integer getIStichtagSozialabgabenRelativZumLetzten() {
		return iStichtagSozialabgabenRelativZumLetzten;
	}

	public void setIStichtagSozialabgabenRelativZumLetzten(
			Integer iStichtagSozialabgabenRelativZumLetzten) {
		this.iStichtagSozialabgabenRelativZumLetzten = iStichtagSozialabgabenRelativZumLetzten;
	}

	@Column(name = "B_MONATSLETZTER_NETTO")
	private Short bMonatsletzterNetto;

	@Column(name = "I_STICHTAG_LOHNSTEUER_RELATIV_ZUM_LETZTEN")
	private Integer iStichtagLohnsteuerRelativZumLetzten;

	@Column(name = "I_STICHTAG_SOZIALABGABEN_RELATIV_ZUM_LETZTEN")
	private Integer iStichtagSozialabgabenRelativZumLetzten;

	private static final long serialVersionUID = 1L;

	public Zahltag() {
		super();
	}

	public Zahltag(Integer id, Integer iMonat, String mandantCNr,
			Integer iStichtagNetto, Short bMonatsletzterNetto,
			Integer iStichtagLohnsteuerRelativZumLetzten,
			Integer iStichtagSozialabgabenRelativZumLetzten, Double fFaktor) {
		setIId(id);
		setIMonat(iMonat);
		setMandantCNr(mandantCNr);
		setIStichtagNetto(iStichtagNetto);
		setBMonatsletzterNetto(bMonatsletzterNetto);
		setIStichtagLohnsteuerRelativZumLetzten(iStichtagLohnsteuerRelativZumLetzten);
		setIStichtagSozialabgabenRelativZumLetzten(iStichtagSozialabgabenRelativZumLetzten);
		setFFaktor(fFaktor);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
