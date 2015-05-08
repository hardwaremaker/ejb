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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name = Artikellog.FindByArtikelIId, query = "SELECT OBJECT(C) FROM Artikellog c WHERE c.artikelIId = :iid")})
@Entity
@Table(name = "WW_ARTIKELLOG")
public class Artikellog implements Serializable {
	public static final String FindByArtikelIId = "ArtikellogFindByArtikelIId";
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_KEY")
	private String cKey;

	@Column(name = "C_VON")
	private String cVon;

	@Column(name = "C_NACH")
	private String cNach;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "LOCALE_C_NR")
	private String localeCNr;

	public String getLocaleCNr() {
		return localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp aendern) {
		tAendern = aendern;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	private static final long serialVersionUID = 1L;

	public Artikellog() {
		super();
	}

	public Artikellog(Integer id, Integer artikelIId, String cKey, String cVon,
			String cNach, String localeCNr, Integer personalIId,
			Timestamp tAendern) {
		setIId(id);
		setArtikelIId(artikelIId);
		setCKey(cKey);
		setCVon(cVon);
		setCNach(cNach);
		setPersonalIId(personalIId);
		setTAendern(tAendern);
		setLocaleCNr(localeCNr);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCKey() {
		return cKey;
	}

	public void setCKey(String cKey) {
		this.cKey = cKey;
	}

	public String getCVon() {
		return cVon;
	}

	public void setCVon(String cVon) {
		this.cVon = cVon;
	}

	public String getCNach() {
		return cNach;
	}

	public void setCNach(String cNach) {
		this.cNach = cNach;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}
