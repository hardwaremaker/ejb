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
		@NamedQuery(name = "VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIId", query = "SELECT OBJECT (o) FROM VkPreisfindungEinzelverkaufspreis o WHERE o.mandantCNr=?1 AND o.artikelIId=?2"),
		@NamedQuery(name = "VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdGueltigab", query = "SELECT OBJECT (o) FROM VkPreisfindungEinzelverkaufspreis o WHERE o.mandantCNr=?1 AND o.artikelIId=?2 AND o.tVerkaufspreisbasisgueltigab=?3"),
		@NamedQuery(name = "VkPreisfindungEinzelverkaufspreisfindByArtikelIId", query = "SELECT OBJECT (o) FROM VkPreisfindungEinzelverkaufspreis o WHERE o.artikelIId=?1"),
		@NamedQuery(name = "VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdAbGueltigab", query = "SELECT OBJECT (o) FROM VkPreisfindungEinzelverkaufspreis o WHERE o.mandantCNr=?1 AND o.artikelIId=?2 AND o.tVerkaufspreisbasisgueltigab >= ?3"),
		@NamedQuery(name = "VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdBisGueltigab", query = "SELECT OBJECT (o) FROM VkPreisfindungEinzelverkaufspreis o WHERE o.mandantCNr=?1 AND o.artikelIId=?2 AND o.tVerkaufspreisbasisgueltigab<=?3 ORDER BY o.tVerkaufspreisbasisgueltigab DESC"),
		@NamedQuery(name = VkPreisfindungEinzelverkaufspreisQuery.ByMandantCnrChangedDate, query="SELECT OBJECT (o) FROM VkPreisfindungEinzelverkaufspreis o WHERE o.mandantCNr= :mandant AND o.tAendern >= :changed")})
@Entity
@Table(name = "WW_VKPFARTIKELVERKAUFSPREISBASIS")
public class VkPreisfindungEinzelverkaufspreis implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_VERKAUFSPREISBASIS")
	private BigDecimal nVerkaufspreisbasis;

	@Column(name = "T_VERKAUFSPREISBASISGUELTIGAB")
	private Date tVerkaufspreisbasisgueltigab;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;
	
	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;
	
	@Column(name = "T_AENDERN")
	private Timestamp tAendern;
	
	
		

	private static final long serialVersionUID = 1L;

	public VkPreisfindungEinzelverkaufspreis() {
		super();
	}

	public VkPreisfindungEinzelverkaufspreis(Integer keyEinzelverkaufspreis,
			String mandantCNr2,
			Integer artikelIId,
			BigDecimal verkaufspreisbasis,
			Date verkaufspreisbasisgueltigab,Timestamp tAendern,Integer personalIIdAendern) {
		setIId(keyEinzelverkaufspreis);
		setArtikelIId(artikelIId);
		setNVerkaufspreisbasis(verkaufspreisbasis);
		setTVerkaufspreisbasisgueltigab(verkaufspreisbasisgueltigab);
		setMandantCNr(mandantCNr2);
		setTAendern(tAendern);
		setPersonalIIdAendern(personalIIdAendern);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNVerkaufspreisbasis() {
		return this.nVerkaufspreisbasis;
	}

	public void setNVerkaufspreisbasis(BigDecimal nVerkaufspreisbasis) {
		this.nVerkaufspreisbasis = nVerkaufspreisbasis;
	}

	public Date getTVerkaufspreisbasisgueltigab() {
		return this.tVerkaufspreisbasisgueltigab;
	}

	public void setTVerkaufspreisbasisgueltigab(
			Date tVerkaufspreisbasisgueltigab) {
		this.tVerkaufspreisbasisgueltigab = tVerkaufspreisbasisgueltigab;
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
