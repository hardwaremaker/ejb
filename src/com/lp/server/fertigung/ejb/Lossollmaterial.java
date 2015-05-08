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
package com.lp.server.fertigung.ejb;

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
		@NamedQuery(name = "LossollmaterialfindByLosIId", query = "SELECT OBJECT(o) FROM Lossollmaterial o WHERE o.losIId=?1"),
		@NamedQuery(name = "LossollmaterialfindByLosIIdArtikelIId", query = "SELECT OBJECT(o) FROM Lossollmaterial o WHERE o.losIId=?1 AND o.artikelIId=?2 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "LossollmaterialfindByLossollmaterialIIdOriginal", query = "SELECT OBJECT(o) FROM Lossollmaterial o WHERE o.lossollmaterialIIdOriginal=?1 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "LossollmaterialfindByLosIIdOrderByISort", query = "SELECT OBJECT(o) FROM Lossollmaterial o WHERE o.losIId=?1 ORDER BY o.iSort ASC") })
@Entity
@Table(name = "FERT_LOSSOLLMATERIAL")
public class Lossollmaterial implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

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

	@Column(name = "B_NACHTRAEGLICH")
	private Short bNachtraeglich;

	@Column(name = "N_SOLLPREIS")
	private BigDecimal nSollpreis;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "LOS_I_ID")
	private Integer losIId;

	@Column(name = "EINHEIT_C_NR")
	private String einheitCNr;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "MONTAGEART_I_ID")
	private Integer montageartIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;
	
	@Column(name = "LOSSOLLMATERIAL_I_ID_ORIGINAL")
	private Integer lossollmaterialIIdOriginal;
	
	public Integer getLossollmaterialIIdOriginal() {
		return lossollmaterialIIdOriginal;
	}

	public void setLossollmaterialIIdOriginal(Integer lossollmaterialIIdOriginal) {
		this.lossollmaterialIIdOriginal = lossollmaterialIIdOriginal;
	}

	@Column(name = "I_BEGINNTERMINOFFSET")
	private Integer iBeginnterminoffset;

	public Integer getIBeginnterminoffset() {
		return iBeginnterminoffset;
	}

	public void setIBeginnterminoffset(Integer iBeginnterminoffset) {
		this.iBeginnterminoffset = iBeginnterminoffset;
	}
	
	private static final long serialVersionUID = 1L;

	public Lossollmaterial() {
		super();
	}

	public Lossollmaterial(Integer id,
			Integer losIId2,
			Integer artikelIId2,
			BigDecimal menge,
			String einheitCNr2,
			Integer montageartIId2,
			Integer sort,
			Short nachtraeglich,
			BigDecimal sollpreis,
			Integer personalIIdAendern2
			,Integer iBeginnterminoffset) {
		setIId(id);
		setLosIId(losIId2);
		setArtikelIId(artikelIId2);
		setNMenge(menge);
		setEinheitCNr(einheitCNr2);
		setMontageartIId(montageartIId2);
		setISort(sort);
		setBNachtraeglich(nachtraeglich);
		setPersonalIIdAendern(personalIIdAendern2);
		setNSollpreis(sollpreis);
		// Setzen der NOT NULL felder
		Timestamp now = new Timestamp(System.currentTimeMillis());
		this.setTAendern(now);
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

	public Short getBNachtraeglich() {
		return this.bNachtraeglich;
	}

	public void setBNachtraeglich(Short bNachtraeglich) {
		this.bNachtraeglich = bNachtraeglich;
	}

	public BigDecimal getNSollpreis() {
		return this.nSollpreis;
	}

	public void setNSollpreis(BigDecimal nSollpreis) {
		this.nSollpreis = nSollpreis;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getLosIId() {
		return this.losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public String getEinheitCNr() {
		return this.einheitCNr;
	}

	public void setEinheitCNr(String einheitCNr) {
		this.einheitCNr = einheitCNr;
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

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}
