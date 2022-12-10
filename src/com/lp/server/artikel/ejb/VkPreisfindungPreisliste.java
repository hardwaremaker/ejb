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

@NamedQueries( {
		@NamedQuery(name = "VkPreisfindungPreislistefindByArtikelIIdVkpfartikelpreislisteIId", query = "SELECT OBJECT (o) FROM VkPreisfindungPreisliste o WHERE o.artikelIId=?1 AND o.vkpfartikelpreislisteIId=?2"),
		@NamedQuery(name = "VkPreisfindungPreislistefindByVkpfartikelpreislisteIIdArtikelIIdPreisgueltigab", query = "SELECT OBJECT (o) FROM VkPreisfindungPreisliste o WHERE o.vkpfartikelpreislisteIId=?1 AND o.artikelIId=?2 AND o.tPreisgueltigab=?3"),
		@NamedQuery(name = "VkPreisfindungPreislistefindByVkpfartikelpreislisteIId", query = "SELECT OBJECT (o) FROM VkPreisfindungPreisliste o WHERE o.vkpfartikelpreislisteIId=?1"),
		@NamedQuery(name = "VkPreisfindungPreislistefindByArtikelIId", query = "SELECT OBJECT (o) FROM VkPreisfindungPreisliste o WHERE o.artikelIId = ?1"),
		@NamedQuery(name = "VkPreisfindungPreislistefindByArtikelIIdAbTPreisgueltigab", query = "SELECT OBJECT (o) FROM VkPreisfindungPreisliste o WHERE o.artikelIId=?1 AND o.tPreisgueltigab > ?2"),
		@NamedQuery(name = "VkPreisfindungPreislistefindByArtikelIIdVkpfartikelpreislisteIIdBisTPreisgueltigab", query = "SELECT OBJECT (o) FROM VkPreisfindungPreisliste o WHERE o.artikelIId=?1 AND o.vkpfartikelpreislisteIId=?2 AND o.tPreisgueltigab<=?3 ORDER BY o.tPreisgueltigab DESC"),
		@NamedQuery(name = VkPreisfindungPreislisteQuery.ByChangedDate, query = "SELECT OBJECT (o) FROM VkPreisfindungPreisliste o WHERE o.tAendern >= :changed")})
@Entity
@Table(name = "WW_VKPFARTIKELPREIS")
public class VkPreisfindungPreisliste implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_PREISGUELTIGAB")
	private Date tPreisgueltigab;

	@Column(name = "N_ARTIKELSTANDARDRABATTSATZ")
	private BigDecimal nArtikelstandardrabattsatz;

	@Column(name = "N_ARTIKELFIXPREIS")
	private BigDecimal nArtikelfixpreis;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "VKPFARTIKELPREISLISTE_I_ID")
	private Integer vkpfartikelpreislisteIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;
	
	@Column(name = "T_AENDERN")
	private Timestamp tAendern;
	
	@Column(name = "C_BEMERKUNG")
	private String cBemerkung;
	public String getCBemerkung() {
		return cBemerkung;
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}
	
	private static final long serialVersionUID = 1L;

	public VkPreisfindungPreisliste() {
		super();
	}

	public VkPreisfindungPreisliste(Integer keyPreisliste,
			Integer vkpfartikelpreislisteIId2, Integer artikelIId2,
			Date preisgueltigab, BigDecimal artikelstandardrabattsatz,Timestamp tAendern,Integer personalIIdAendern) {
		setIId(keyPreisliste);
		setVkpfartikelpreislisteIId(vkpfartikelpreislisteIId2);
		setArtikelIId(artikelIId2);
		setTPreisgueltigab(preisgueltigab);
		setNArtikelstandardrabattsatz(artikelstandardrabattsatz);
		setTAendern(tAendern);
		setPersonalIIdAendern(personalIIdAendern);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Date getTPreisgueltigab() {
		return this.tPreisgueltigab;
	}

	public void setTPreisgueltigab(Date tPreisgueltigab) {
		this.tPreisgueltigab = tPreisgueltigab;
	}

	public BigDecimal getNArtikelstandardrabattsatz() {
		return this.nArtikelstandardrabattsatz;
	}

	public void setNArtikelstandardrabattsatz(
			BigDecimal nArtikelstandardrabattsatz) {
		this.nArtikelstandardrabattsatz = nArtikelstandardrabattsatz;
	}

	public BigDecimal getNArtikelfixpreis() {
		return this.nArtikelfixpreis;
	}

	public void setNArtikelfixpreis(BigDecimal nArtikelfixpreis) {
		this.nArtikelfixpreis = nArtikelfixpreis;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getVkpfartikelpreislisteIId() {
		return this.vkpfartikelpreislisteIId;
	}

	public void setVkpfartikelpreislisteIId(Integer vkpfartikelpreislisteIId) {
		this.vkpfartikelpreislisteIId = vkpfartikelpreislisteIId;
	}
	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}
	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}
	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}
}
