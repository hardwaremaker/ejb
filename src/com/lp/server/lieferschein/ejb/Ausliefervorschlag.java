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
package com.lp.server.lieferschein.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "AusliefervorschlagfindByKundeIIdMandantCNr", query = "SELECT OBJECT (o) FROM Ausliefervorschlag o WHERE o.kundeIId=?1 AND o.mandantCNr=?2") })
@Entity
@Table(name = "LS_AUSLIEFERVORSCHLAG")
public class Ausliefervorschlag implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "N_VERFUEGBAR")
	private BigDecimal nVerfuegbar;

	public BigDecimal getNVerfuegbar() {
		return nVerfuegbar;
	}

	public void setNVerfuegbar(BigDecimal nVerfuegbar) {
		this.nVerfuegbar = nVerfuegbar;
	}

	@Column(name = "T_AUSLIEFERTERMIN")
	private Timestamp tAusliefertermin;

	@Column(name = "I_BELEGARTID")
	private Integer iBelegartid;

	@Column(name = "I_BELEGARTPOSITIONID")
	private Integer iBelegartpositionid;

	@Column(name = "BELEGART_C_NR")
	private String belegartCNr;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	@Column(name = "KUNDE_I_ID_LIEFERADRESSE")
	private Integer kundeIIdLieferadresse;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	private static final long serialVersionUID = 1L;

	public Ausliefervorschlag() {
		super();
	}

	public Ausliefervorschlag(Integer id, Timestamp ausliefertermin,
			String belegartCNr, Integer iBelegartid,
			Integer iBelegartpositionid, BigDecimal nMenge, Integer artikelId,
			String mandantCNr, Integer kundeIId, Integer kundeIIdLieferadresse, BigDecimal nVerfuegbar) {
		setIId(id);

		setKundeIId(kundeIId);
		setKundeIIdLieferadresse(kundeIIdLieferadresse);
		setNMenge(nMenge);
		setTAusliefertermin(ausliefertermin);

		setArtikelIId(artikelId);
		setMandantCNr(mandantCNr);
		setBelegartCNr(belegartCNr);
		setIBelegartid(iBelegartid);
		setIBelegartpositionid(iBelegartpositionid);
		setNVerfuegbar(nVerfuegbar);

	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public Timestamp getTAusliefertermin() {
		return tAusliefertermin;
	}

	public void setTAusliefertermin(Timestamp tAusliefertermin) {
		this.tAusliefertermin = tAusliefertermin;
	}

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getKundeIIdLieferadresse() {
		return kundeIIdLieferadresse;
	}

	public void setKundeIIdLieferadresse(Integer kundeIIdLieferadresse) {
		this.kundeIIdLieferadresse = kundeIIdLieferadresse;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIBelegartid() {
		return this.iBelegartid;
	}

	public void setIBelegartid(Integer iBelegartid) {
		this.iBelegartid = iBelegartid;
	}

	public Integer getIBelegartpositionid() {
		return this.iBelegartpositionid;
	}

	public void setIBelegartpositionid(Integer iBelegartpositionid) {
		this.iBelegartpositionid = iBelegartpositionid;
	}

	public String getBelegartCNr() {
		return this.belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}