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
package com.lp.server.artikel.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = Fasessioneintrag.FindByFasessionIId, query = "SELECT OBJECT(C) FROM Fasessioneintrag c WHERE c.fasessionIId = ?1"),
	@NamedQuery(name = "FasessioneintragFindByLosIId", query = "SELECT OBJECT(C) FROM Fasessioneintrag c WHERE c.losIId = ?1")})
@Entity
@Table(name = "WW_FASESSIONEINTRAG")
public class Fasessioneintrag implements Serializable {
	public static final String FindByFasessionIId = "FasessioneintragFindByFasessionIId";
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "FASESSION_I_ID")
	private Integer fasessionIId;

	@Column(name = "C_SNRCHNR")
	private String cSnrchnr;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "LAGER_I_ID")
	private Integer lagerIId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "LIEFERSCHEIN_I_ID")
	private Integer lieferscheinIId;

	@Column(name = "LOS_I_ID")
	private Integer losIId;

	@Column(name = "AUFTRAG_I_ID")
	private Integer auftragIId;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;
	
	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;
	
	
	@Column(name = "ARTIKEL_I_ID_OFFENERAG")
	private Integer artikelIIdOffenerag;
	
	public Integer getArtikelIIdOffenerag() {
		return artikelIIdOffenerag;
	}

	public void setArtikelIIdOffenerag(Integer artikelIIdOffenerag) {
		this.artikelIIdOffenerag = artikelIIdOffenerag;
	}
	
	public Integer getLosIId() {
		return losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public Integer getAuftragIId() {
		return auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public Integer getLagerIId() {
		return lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

	private static final long serialVersionUID = 1L;

	public Fasessioneintrag() {
		super();
	}

	public Fasessioneintrag(Integer id, Integer fasessionIId,
			Integer artikelIId, Integer lagerIId, BigDecimal nMenge, Integer personalIIdAnlegen, Timestamp tAnlegen) {
		setIId(id);
		setFasessionIId(fasessionIId);
		setArtikelIId(artikelIId);
		setLagerIId(lagerIId);
		setNMenge(nMenge);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTAnlegen(tAnlegen);

	}

	public Integer getFasessionIId() {
		return fasessionIId;
	}

	public void setFasessionIId(Integer fasessionIId) {
		this.fasessionIId = fasessionIId;
	}

	public String getCSnrchnr() {
		return cSnrchnr;
	}

	public void setCSnrchnr(String cSnrchnr) {
		this.cSnrchnr = cSnrchnr;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public Integer getLieferscheinIId() {
		return lieferscheinIId;
	}

	public void setLieferscheinIId(Integer lieferscheinIId) {
		this.lieferscheinIId = lieferscheinIId;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}
