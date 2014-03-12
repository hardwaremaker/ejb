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
package com.lp.server.instandhaltung.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "WartungsschritteejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Wartungsschritte o WHERE o.geraetIId = ?1"),
		@NamedQuery(name = "WartungsschrittefindByGeraetIId", query = "SELECT OBJECT(o) FROM Wartungsschritte o WHERE o.geraetIId=?1") })
@Entity
@Table(name = "IS_WARTUNGSSCHRITTE")
public class Wartungsschritte implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_SORT")
	private Integer iSort;

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "L_DAUER")
	private Long lDauer;

	@Column(name = "AUFTRAGWIEDERHOLUNGSINTERVALL_C_NR")
	private String auftragwiederholungsintervallCNr;

	@Column(name = "TAGESART_I_ID")
	private Integer tagesartIId;

	@Column(name = "GERAET_I_ID")
	private Integer geraetIId;

	@Column(name = "T_ABDURCHFUEHREN")
	private java.sql.Timestamp tAbdurchfuehren;

	@Column(name = "PERSONALGRUPPE_I_ID")
	private Integer personalgruppeIId;
	@Column(name = "LIEFERANT_I_ID")
	private Integer lieferantIId;

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	public String getCBemerkunglieferant() {
		return cBemerkunglieferant;
	}

	public void setCBemerkunglieferant(String cBemerkunglieferant) {
		this.cBemerkunglieferant = cBemerkunglieferant;
	}

	@Column(name = "C_BEMERKUNGLIEFERANT")
	private String cBemerkunglieferant;

	@Column(name = "C_BEMERKUNG")
	private String cBemerkung;

	public String getCBemerkung() {
		return cBemerkung;
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}

	public Integer getPersonalgruppeIId() {
		return personalgruppeIId;
	}

	public void setPersonalgruppeIId(Integer personalgruppeIId) {
		this.personalgruppeIId = personalgruppeIId;
	}

	private static final long serialVersionUID = 1L;

	public Wartungsschritte() {
		super();
	}

	public Wartungsschritte(Integer id, Integer geraetIId, Integer artikelIId,
			java.sql.Timestamp tAbdurchfuehren, Long dauer,
			String auftragwiederholungsintervallCNr, Integer iSort,
			Integer personalgruppeIId) {
		setIId(id);
		setGeraetIId(geraetIId);
		setTAbdurchfuehren(tAbdurchfuehren);
		setArtikelIId(artikelIId);
		setLDauer(dauer);
		setAuftragwiederholungsintervallCNr(auftragwiederholungsintervallCNr);
		setISort(iSort);
		setPersonalgruppeIId(personalgruppeIId);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Long getLDauer() {
		return lDauer;
	}

	public void setLDauer(Long lDauer) {
		this.lDauer = lDauer;
	}

	public String getAuftragwiederholungsintervallCNr() {
		return auftragwiederholungsintervallCNr;
	}

	public void setAuftragwiederholungsintervallCNr(
			String auftragwiederholungsintervallCNr) {
		this.auftragwiederholungsintervallCNr = auftragwiederholungsintervallCNr;
	}

	public Integer getTagesartIId() {
		return tagesartIId;
	}

	public void setTagesartIId(Integer tagesartIId) {
		this.tagesartIId = tagesartIId;
	}

	public Integer getGeraetIId() {
		return geraetIId;
	}

	public void setGeraetIId(Integer geraetIId) {
		this.geraetIId = geraetIId;
	}

	public java.sql.Timestamp getTAbdurchfuehren() {
		return tAbdurchfuehren;
	}

	public void setTAbdurchfuehren(java.sql.Timestamp tAbdurchfuehren) {
		this.tAbdurchfuehren = tAbdurchfuehren;
	}

}
