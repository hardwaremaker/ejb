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
package com.lp.server.kueche.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( { @NamedQuery(name = "KassaimportAnzahlSpeiseplanIId", query = "SELECT COUNT (o) FROM Kassaimport AS o WHERE o.speiseplanIId=?1")
})

@Entity
@Table(name = "KUE_KASSAIMPORT")
public class Kassaimport implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "SPEISEPLAN_I_ID")
	private Integer speiseplanIId;

	public Integer getSpeiseplanIId() {
		return speiseplanIId;
	}

	public void setSpeiseplanIId(Integer speiseplanIId) {
		this.speiseplanIId = speiseplanIId;
	}

	@Column(name = "T_IMPORT")
	private Timestamp tImport;

	@Column(name = "T_KASSA")
	private Timestamp tKassa;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "N_PREIS")
	private BigDecimal nPreis;

	private static final long serialVersionUID = 1L;

	public Kassaimport() {
		super();
	}

	public Kassaimport(Integer iId, Timestamp tImport,
			Timestamp tKassa, BigDecimal nMenge, BigDecimal nPreis,
			Integer kundeIId, Integer artikelIId, Integer speiseplanIId) {
		setIId(iId);
		setKundeIId(kundeIId);
		setArtikelIId(artikelIId);

		setTImport(tImport);
		setTKassa(tKassa);
		setNMenge(nMenge);
		setNPreis(nPreis);
		setSpeiseplanIId(speiseplanIId);
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer id) {
		iId = id;
	}

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Timestamp getTImport() {
		return tImport;
	}

	public void setTImport(Timestamp import1) {
		tImport = import1;
	}

	public Timestamp getTKassa() {
		return tKassa;
	}

	public void setTKassa(Timestamp kassa) {
		tKassa = kassa;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal menge) {
		nMenge = menge;
	}

	public BigDecimal getNPreis() {
		return nPreis;
	}

	public void setNPreis(BigDecimal nPreis) {
		this.nPreis = nPreis;
	}

}
