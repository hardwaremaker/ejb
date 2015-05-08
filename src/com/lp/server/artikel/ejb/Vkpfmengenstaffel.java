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
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.partner.ejbfac.VkpfmengenstaffelQuery;

@NamedQueries({
		@NamedQuery(name = "VkpfMengenstaffelfindByUniqueKey", query = "SELECT OBJECT (o) FROM Vkpfmengenstaffel o WHERE o.artikelIId=?1 AND o.nMenge=?2 AND o.tPreisgueltigab=?3 AND o.vkpfartikelpreislisteIId=?4"),
		@NamedQuery(name = "VkpfMengenstaffelfindByUniqueKey2", query = "SELECT OBJECT (o) FROM Vkpfmengenstaffel o WHERE o.artikelIId=?1 AND o.nMenge=?2 AND o.tPreisgueltigab=?3 AND o.vkpfartikelpreislisteIId IS NULL"),
		@NamedQuery(name = "VkpfMengenstaffelfindByArtikelIId", query = "SELECT OBJECT (o) FROM Vkpfmengenstaffel o WHERE o.artikelIId=?1 ORDER BY o.tPreisgueltigab ASC"),
		@NamedQuery(name = "VkpfMengenstaffelfindByArtikelIIdNMenge", query = "SELECT OBJECT (o) FROM Vkpfmengenstaffel o WHERE o.artikelIId=?1 AND o.nMenge=?2 ORDER BY o.nMenge, o.tPreisgueltigab DESC"),
		@NamedQuery(name = "VkpfMengenstaffelfindByArtikelIIdNMengeGueltigkeitsdatum", query = "SELECT OBJECT (o) FROM Vkpfmengenstaffel o WHERE o.artikelIId=?1 AND o.nMenge<=?2 AND ((o.tPreisgueltigab<=?3 AND o.tPreisgueltigbis>=?3) OR (o.tPreisgueltigab<=?3 AND o.tPreisgueltigbis IS NULL)) ORDER BY o.nMenge"),
		@NamedQuery(name = "VkpfMengenstaffelfindByArtikelIIdGueltigkeitsdatum", query = "SELECT OBJECT (o) FROM Vkpfmengenstaffel o WHERE o.artikelIId=?1 AND ((o.tPreisgueltigab<=?2 AND o.tPreisgueltigbis>=?2) OR (o.tPreisgueltigab<=?2 AND o.tPreisgueltigbis IS NULL)) ORDER BY o.nMenge ASC, o.tPreisgueltigab DESC"),
		@NamedQuery(name = "VkpfMengenstaffelfindByArtikelIIdAbGueltigab", query = "SELECT OBJECT (o) FROM Vkpfmengenstaffel o WHERE o.artikelIId=?1 AND o.tPreisgueltigab > ?2"),
		@NamedQuery(name = "VkpfMengenstaffelfindByArtikelIIdBisGueltigab", query = "SELECT OBJECT (o) FROM Vkpfmengenstaffel o WHERE o.artikelIId=?1 AND o.tPreisgueltigab<=?2 ORDER BY o.tPreisgueltigab DESC"),
		@NamedQuery(name = VkpfmengenstaffelQuery.ByChangedDate, query = "SELECT OBJECT (o) FROM Vkpfmengenstaffel o WHERE o.tAendern >= :changed")})
@Entity
@Table(name = "WW_VKPFMENGENSTAFFEL")
public class Vkpfmengenstaffel implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "F_ARTIKELSTANDARDRABATTSATZ")
	private Double fArtikelstandardrabattsatz;

	@Column(name = "N_ARTIKELFIXPREIS")
	private BigDecimal nArtikelfixpreis;

	@Column(name = "T_PREISGUELTIGAB")
	private Date tPreisgueltigab;

	@Column(name = "T_PREISGUELTIGBIS")
	private Date tPreisgueltigbis;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "B_ALLEPREISLISTEN")
	private Short bAllepreislisten;
	
	public Short getBAllepreislisten() {
		return bAllepreislisten;
	}

	public void setBAllepreislisten(Short bAllepreislisten) {
		this.bAllepreislisten = bAllepreislisten;
	}

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "VKPFARTIKELPREISLISTE_I_ID")
	private Integer vkpfartikelpreislisteIId;

	public Integer getVkpfartikelpreislisteIId() {
		return vkpfartikelpreislisteIId;
	}

	public void setVkpfartikelpreislisteIId(Integer vkpfartikelpreislisteIId) {
		this.vkpfartikelpreislisteIId = vkpfartikelpreislisteIId;
	}

	private static final long serialVersionUID = 1L;

	public Vkpfmengenstaffel() {
		super();
	}

	public Vkpfmengenstaffel(Integer id, Integer artikelIId, BigDecimal menge,
			Double artikelstandardrabattsatz, Date preisgueltigab,
			Integer personalIIdAendern, Short bAllepreislisten) {
		setIId(id);
		setArtikelIId(artikelIId);
		setNMenge(menge);
		setTPreisgueltigab(preisgueltigab);
		setPersonalIIdAendern(personalIIdAendern);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setFArtikelstandardrabattsatz(artikelstandardrabattsatz);
		setBAllepreislisten(bAllepreislisten);
	}

	public Vkpfmengenstaffel(Integer id, Integer artikelIId, BigDecimal menge,
			Date preisgueltigab, Integer personalIIdAendern,Short bAllepreislisten) {
		setIId(id);
		setArtikelIId(artikelIId);
		setNMenge(menge);
		setTPreisgueltigab(preisgueltigab);
		setPersonalIIdAendern(personalIIdAendern);
		setFArtikelstandardrabattsatz(new Double(0));
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setBAllepreislisten(bAllepreislisten);
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

	public Double getFArtikelstandardrabattsatz() {
		return this.fArtikelstandardrabattsatz;
	}

	public void setFArtikelstandardrabattsatz(Double fArtikelstandardrabattsatz) {
		this.fArtikelstandardrabattsatz = fArtikelstandardrabattsatz;
	}

	public BigDecimal getNArtikelfixpreis() {
		return this.nArtikelfixpreis;
	}

	public void setNArtikelfixpreis(BigDecimal nArtikelfixpreis) {
		this.nArtikelfixpreis = nArtikelfixpreis;
	}

	public Date getTPreisgueltigab() {
		return this.tPreisgueltigab;
	}

	public void setTPreisgueltigab(Date tPreisgueltigab) {
		this.tPreisgueltigab = tPreisgueltigab;
	}

	public Date getTPreisgueltigbis() {
		return this.tPreisgueltigbis;
	}

	public void setTPreisgueltigbis(Date tPreisgueltigbis) {
		this.tPreisgueltigbis = tPreisgueltigbis;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}
