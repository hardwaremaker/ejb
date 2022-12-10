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
package com.lp.server.anfrage.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.IISort;
import com.lp.server.util.IPositionIIdArtikelset;

@NamedQueries({
		@NamedQuery(name = "AnfragepositionfindByAnfrage", query = "SELECT OBJECT (o) FROM Anfrageposition o WHERE o.anfrageIId=?1 ORDER BY o.iSort"),
		@NamedQuery(name = "AnfragepositionejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Anfrageposition o WHERE o.anfrageIId = ?1"),
		@NamedQuery(name = "AnfragepositionFindByAnfragepositionIdZugehoerig", query = "SELECT o FROM Anfrageposition o WHERE o.anfragepositionIdZugehoerig = ?1 ORDER BY o.iSort"),
		@NamedQuery(name = "AnfragepositionfindByLossollmaterialIId", query = "SELECT OBJECT (o) FROM Anfrageposition o WHERE o.lossollmaterialIId=?1"),
		@NamedQuery(name = "AnfragepositionfindByAnfrageIIdArtikelIId", query = "SELECT OBJECT (o) FROM Anfrageposition o WHERE o.anfrageIId=?1 AND o.artikelIId=?2") })
@Entity
@Table(name = "ANF_ANFRAGEPOSITION")
public class Anfrageposition implements Serializable, IPositionIIdArtikelset,  IISort {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "B_ARTIKELBEZEICHNUNGUEBERSTEUERT")
	private Short bArtikelbezeichnunguebersteuert;

	@Column(name = "X_TEXTINHALT")
	private String xTextinhalt;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "N_RICHTPREIS")
	private BigDecimal nRichtpreis;

	@Column(name = "C_ZBEZ")
	private String cZbez;

	@Column(name = "ANFRAGE_I_ID")
	private Integer anfrageIId;

	@Column(name = "ANFRAGEPOSITIONART_C_NR")
	private String anfragepositionartCNr;

	@Column(name = "EINHEIT_C_NR")
	private String einheitCNr;

	@Column(name = "MEDIASTANDARD_I_ID")
	private Integer mediastandardIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "ANFRAGEPOSITION_I_ID_ZUGEHOERIG")
	private Integer anfragepositionIdZugehoerig;

	public Integer getAnfragepositionIdZugehoerig() {
		return anfragepositionIdZugehoerig;
	}

	public void setAnfragepositionIdZugehoerig(
			Integer anfragepositionIdZugehoerig) {
		this.anfragepositionIdZugehoerig = anfragepositionIdZugehoerig;
	}

	@Column(name = "LOSSOLLMATERIAL_I_ID")
	private Integer lossollmaterialIId;

	public Integer getLossollmaterialIId() {
		return lossollmaterialIId;
	}

	public void setLossollmaterialIId(Integer lossollmaterialIId) {
		this.lossollmaterialIId = lossollmaterialIId;
	}
	
	private static final long serialVersionUID = 1L;

	public Anfrageposition() {
		super();
	}

	public Anfrageposition(Integer idAnfrageposition, Integer belegIId,
			Integer sort, String anfragepositionartCNr) {
		super();
		setIId(idAnfrageposition);
		setAnfrageIId(belegIId);
		setISort(sort);
		setAnfragepositionartCNr(anfragepositionartCNr);
		setBArtikelbezeichnunguebersteuert(new Short((short) 0));

	}

	public Anfrageposition(Integer idAnfrageposition, Integer belegIId,
			Integer sort, String anfragepositionartCNr,
			Short artikelbezeichnunguebersteuert) {
		super();
		setIId(idAnfrageposition);
		setAnfrageIId(belegIId);
		setISort(sort);
		setAnfragepositionartCNr(anfragepositionartCNr);
		setBArtikelbezeichnunguebersteuert(artikelbezeichnunguebersteuert);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Short getBArtikelbezeichnunguebersteuert() {
		return this.bArtikelbezeichnunguebersteuert;
	}

	public void setBArtikelbezeichnunguebersteuert(
			Short bArtikelbezeichnunguebersteuert) {
		this.bArtikelbezeichnunguebersteuert = bArtikelbezeichnunguebersteuert;
	}

	public String getXTextinhalt() {
		return this.xTextinhalt;
	}

	public void setXTextinhalt(String xTextinhalt) {
		this.xTextinhalt = xTextinhalt;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public BigDecimal getNRichtpreis() {
		return this.nRichtpreis;
	}

	public void setNRichtpreis(BigDecimal nRichtpreis) {
		this.nRichtpreis = nRichtpreis;
	}

	public String getCZbez() {
		return this.cZbez;
	}

	public void setCZbez(String cZbez) {
		this.cZbez = cZbez;
	}

	public Integer getAnfrageIId() {
		return this.anfrageIId;
	}

	public void setAnfrageIId(Integer anfrageiId) {
		this.anfrageIId = anfrageiId;
	}

	public String getAnfragepositionartCNr() {
		return this.anfragepositionartCNr;
	}

	public void setAnfragepositionartCNr(String anfragepositionartCNr) {
		this.anfragepositionartCNr = anfragepositionartCNr;
	}

	public String getEinheitCNr() {
		return this.einheitCNr;
	}

	public void setEinheitCNr(String einheitCNr) {
		this.einheitCNr = einheitCNr;
	}

	public Integer getMediastandardIId() {
		return this.mediastandardIId;
	}

	public void setMediastandardIId(Integer mediastandard) {
		this.mediastandardIId = mediastandard;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikeliId) {
		this.artikelIId = artikeliId;
	}

	// SP5044
	@Override
	public Integer getPositionIIdArtikelset() {

		return anfragepositionIdZugehoerig;
	}

	@Override
	public void setPositionIIdArtikelset(Integer anfragepositionIdZugehoerig) {
		this.anfragepositionIdZugehoerig = anfragepositionIdZugehoerig;

	}

}
