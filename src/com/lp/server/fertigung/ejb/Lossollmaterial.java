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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.system.service.ITablenames;

@NamedQueries({
		@NamedQuery(name = LossollmaterialQuery.ByLosIId, query = "SELECT OBJECT(o) FROM Lossollmaterial o WHERE o.losIId=?1"),
		@NamedQuery(name = "LossollmaterialfindByLosIIdArtikelIId", query = "SELECT OBJECT(o) FROM Lossollmaterial o WHERE o.losIId=?1 AND o.artikelIId=?2 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "LossollmaterialfindByLossollmaterialIIdOriginal", query = "SELECT OBJECT(o) FROM Lossollmaterial o WHERE o.lossollmaterialIIdOriginal=?1 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "LossollmaterialfindByLosIIdOrderByISort", query = "SELECT OBJECT(o) FROM Lossollmaterial o WHERE o.losIId=?1 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "LossollmaterialMaxISortFindByLosIId", query = "SELECT MAX(o.iSort) FROM Lossollmaterial o WHERE o.losIId=?1"),
		@NamedQuery(name = LossollmaterialQuery.ByLosIIdArtklaIIds, query = "SELECT OBJECT(o) FROM Lossollmaterial o LEFT JOIN o.artikel a WHERE o.losIId=:losId AND a.artklaIId IN (:artklaIds) ORDER BY o.iSort ASC")})
@Entity
@Table(name = ITablenames.FERT_LOSSOLLMATERIAL)
public class Lossollmaterial implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;
	
	@Column(name = "B_DRINGEND")
	private Short bDringend;
	

	public Short getBDringend() {
		return bDringend;
	}

	public void setBDringend(Short bDringend) {
		this.bDringend = bDringend;
	}

	@Column(name = "B_RUESTMENGE")
	private Short bRuestmenge;

	public Short getBRuestmenge() {
		return bRuestmenge;
	}

	public void setBRuestmenge(Short bRuestmenge) {
		this.bRuestmenge = bRuestmenge;
	}

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
	
	@Column(name = "N_MENGE_STKLPOS")
	private BigDecimal nMengeStklPos;

	@Column(name = "EINHEIT_C_NR_STKLPOS")
	private String einheitCNrStklPos;
	
	@Column(name = "N_MENGE_PRO_LOS")
	private BigDecimal nMengeProLos;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARTIKEL_I_ID", referencedColumnName = "I_ID", insertable = false, updatable = false)
	private Artikel artikel;
	
	@Column(name = "T_EXPORT_BEGINN")
	private Timestamp tExportBeginn;
	
	@Column(name = "T_EXPORT_ENDE")
	private Timestamp tExportEnde;
	
	@Column(name = "C_FEHLERCODE")
	private String cFehlercode;
	
	@Column(name = "C_FEHLERTEXT")
	private String cFehlertext;

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

	public Lossollmaterial(Integer id, Integer losIId2, Integer artikelIId2,
			BigDecimal menge, String einheitCNr2, Integer montageartIId2,
			Integer sort, Short nachtraeglich, BigDecimal sollpreis,
			Integer personalIIdAendern2, Integer iBeginnterminoffset,
			Short bRuestmenge,Short bDringend, BigDecimal nMengeProLos) {
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
		setBRuestmenge(bRuestmenge);
		setBDringend(bDringend);
		setNMengeProLos(nMengeProLos);
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

	public BigDecimal getNMengeStklPos() {
		return nMengeStklPos;
	}
	
	public void setNMengeStklPos(BigDecimal nMengeStklPos) {
		this.nMengeStklPos = nMengeStklPos;
	}
	
	public String getEinheitCNrStklPos() {
		return einheitCNrStklPos;
	}
	
	public void setEinheitCNrStklPos(String einheitCNrStklPos) {
		this.einheitCNrStklPos = einheitCNrStklPos;
	}

	public BigDecimal getNMengeProLos() {
		return nMengeProLos;
	}

	public void setNMengeProLos(BigDecimal nMengeProLos) {
		this.nMengeProLos = nMengeProLos;
	}

	public void setTExportBeginn(Timestamp tExportBeginn) {
		this.tExportBeginn = tExportBeginn;
	}
	public Timestamp getTExportBeginn() {
		return tExportBeginn;
	}
	
	public void setTExportEnde(Timestamp tExportEnde) {
		this.tExportEnde = tExportEnde;
	}
	public Timestamp getTExportEnde() {
		return tExportEnde;
	}
	
	public void setCFehlertext(String cFehlertext) {
		this.cFehlertext = cFehlertext;
	}
	public String getCFehlertext() {
		return cFehlertext;
	}
	
	public void setCFehlercode(String cFehlercode) {
		this.cFehlercode = cFehlercode;
	}
	public String getCFehlercode() {
		return cFehlercode;
	}
}
