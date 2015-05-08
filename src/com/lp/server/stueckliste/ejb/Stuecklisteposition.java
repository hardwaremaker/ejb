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
package com.lp.server.stueckliste.ejb;

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
		@NamedQuery(name = "StuecklistepositionejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Stuecklisteposition o WHERE o.stuecklisteIId = ?1"),
		@NamedQuery(name = "StuecklistepositionfindByStuecklisteIId", query = "SELECT OBJECT(o) FROM Stuecklisteposition o WHERE o.stuecklisteIId=?1 ORDER BY o.iSort"),
		@NamedQuery(name = "StuecklistepositionfindByStuecklisteIIdBMitdrucken", query = "SELECT OBJECT(o) FROM Stuecklisteposition o WHERE o.stuecklisteIId = ?1 AND o.bMitdrucken = ?2 ORDER BY o.iSort"),
		@NamedQuery(name = "StuecklistepositionfindByStuecklisteIIdArtikelIId", query = "SELECT OBJECT(o) FROM Stuecklisteposition o WHERE o.stuecklisteIId=?1 and o.artikelIId=?2 ORDER BY o.iSort"),
		@NamedQuery(name = "StuecklistepositionfindByArtikelIId", query = "SELECT OBJECT(o) FROM Stuecklisteposition o WHERE o.artikelIId=?1") ,
		@NamedQuery(name = "StuecklistepositionfindByStuecklisteIIdArtikelIIdOrderByArtikelIId", query = "SELECT OBJECT(o) FROM Stuecklisteposition o WHERE o.stuecklisteIId=?1 ORDER BY o.artikelIId") })
@Entity
@Table(name = "STK_STUECKLISTEPOSITION")
public class Stuecklisteposition implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "B_MITDRUCKEN")
	private Short bMitdrucken;

	@Column(name = "F_DIMENSION1")
	private Float fDimension1;

	@Column(name = "F_DIMENSION2")
	private Float fDimension2;

	@Column(name = "F_DIMENSION3")
	private Float fDimension3;

	@Column(name = "C_POSITION")
	private String cPosition;

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	@Column(name = "I_LFDNUMMER")
	private Integer iLfdnummer;

	@Column(name = "I_SORT")
	private Integer iSort;
	
	@Column(name = "I_BEGINNTERMINOFFSET")
	private Integer iBeginnterminoffset;

	public Integer getIBeginnterminoffset() {
		return iBeginnterminoffset;
	}

	public void setIBeginnterminoffset(Integer iBeginnterminoffset) {
		this.iBeginnterminoffset = iBeginnterminoffset;
	}

	@Column(name = "N_KALKPREIS")
	private BigDecimal nKalkpreis;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "EINHEIT_C_NR")
	private String einheitCNr;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "MONTAGEART_I_ID")
	private Integer montageartIId;

	@Column(name = "STUECKLISTE_I_ID")
	private Integer stuecklisteIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "T_AENDERN_ANSPRECHPARTNER")
	private Timestamp tAendernAnsprechpartner;

	@Column(name = "T_ANLEGEN_ANSPRECHPARTNER")
	private Timestamp tAnlegenAnsprechpartner;

	@Column(name = "ANSPRECHPARTNER_I_ID_ANLEGEN")
	private Integer ansprechpartnerIIdAnlegen;

	@Column(name = "ANSPRECHPARTNER_I_ID_AENDERN")
	private Integer ansprechpartnerIIdAendern;
	
	private static final long serialVersionUID = 1L;

	public Stuecklisteposition() {
		super();
	}

	public Stuecklisteposition(Integer id, Integer stuecklisteIId,
			Integer artikelIId, BigDecimal menge, String einheitCNr,
			Integer montageartIId, Integer sort, Short mitdrucken,
			Integer personalIIdAnlegen2,
			Integer personalIIdAendern2,Integer iBeginnterminoffset) {
		setIId(id);
		setStuecklisteIId(stuecklisteIId);
		setArtikelIId(artikelIId);
		setNMenge(menge);
		setEinheitCNr(einheitCNr);
		setMontageartIId(montageartIId);
		setISort(sort);
		setBMitdrucken(mitdrucken);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		setTAendern(t);
		setTAnlegen(t);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendern(personalIIdAendern2);
		setIBeginnterminoffset(iBeginnterminoffset);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public Short getBMitdrucken() {
		return this.bMitdrucken;
	}

	public void setBMitdrucken(Short bMitdrucken) {
		this.bMitdrucken = bMitdrucken;
	}

	public Float getFDimension1() {
		return this.fDimension1;
	}

	public void setFDimension1(Float fDimension1) {
		this.fDimension1 = fDimension1;
	}

	public Float getFDimension2() {
		return this.fDimension2;
	}

	public void setFDimension2(Float fDimension2) {
		this.fDimension2 = fDimension2;
	}

	public Float getFDimension3() {
		return this.fDimension3;
	}

	public void setFDimension3(Float fDimension3) {
		this.fDimension3 = fDimension3;
	}

	public String getCPosition() {
		return this.cPosition;
	}

	public void setCPosition(String cPosition) {
		this.cPosition = cPosition;
	}

	public String getCKommentar() {
		return this.cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public Integer getILfdnummer() {
		return this.iLfdnummer;
	}

	public void setILfdnummer(Integer iLfdnummer) {
		this.iLfdnummer = iLfdnummer;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public BigDecimal getNKalkpreis() {
		return this.nKalkpreis;
	}

	public void setNKalkpreis(BigDecimal nKalkpreis) {
		this.nKalkpreis = nKalkpreis;
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

	public String getEinheitCNr() {
		return this.einheitCNr;
	}

	public void setEinheitCNr(String einheitCNr) {
		this.einheitCNr = einheitCNr;
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

	public Integer getMontageartIId() {
		return this.montageartIId;
	}

	public void setMontageartIId(Integer montageartIId) {
		this.montageartIId = montageartIId;
	}

	public Integer getStuecklisteIId() {
		return this.stuecklisteIId;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Timestamp getTAendernAnsprechpartner() {
		return tAendernAnsprechpartner;
	}

	public void setTAendernAnsprechpartner(Timestamp tAendernAnsprechpartner) {
		this.tAendernAnsprechpartner = tAendernAnsprechpartner;
	}

	public Integer getAnsprechpartnerIIdAnlegen() {
		return ansprechpartnerIIdAnlegen;
	}

	public void setAnsprechpartnerIIdAnlegen(Integer ansprechpartnerIIdAnlegen) {
		this.ansprechpartnerIIdAnlegen = ansprechpartnerIIdAnlegen;
	}

	public Integer getAnsprechpartnerIIdAendern() {
		return ansprechpartnerIIdAendern;
	}

	public void setAnsprechpartnerIIdAendern(Integer ansprechpartnerIIdAendern) {
		this.ansprechpartnerIIdAendern = ansprechpartnerIIdAendern;
	}

	public Timestamp getTAnlegenAnsprechpartner() {
		return tAnlegenAnsprechpartner;
	}

	public void setTAnlegenAnsprechpartner(Timestamp tAnlegenAnsprechpartner) {
		this.tAnlegenAnsprechpartner = tAnlegenAnsprechpartner;
	}
}
