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
package com.lp.server.auftrag.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "ZahlungsplanmeilensteinFindByMeilensteinIIdZahlungplanIId", query = "SELECT OBJECT (O) FROM Zahlungsplanmeilenstein o WHERE o.meilensteinIId=?1 AND o.zahlungsplanIId = ?2"),
		@NamedQuery(name = "ZahlungsplanmeilensteinFindByZahlungplanIId", query = "SELECT OBJECT (O) FROM Zahlungsplanmeilenstein o WHERE o.zahlungsplanIId = ?1 ORDER BY o.iSort"),
		@NamedQuery(name = "ZahlungsplanmeilensteinFindByZahlungplanIIdOrderByTErledigt", query = "SELECT OBJECT (O) FROM Zahlungsplanmeilenstein o WHERE o.zahlungsplanIId = ?1 ORDER BY o.tErledigt DESC"),
		@NamedQuery(name = "ZahlungsplanmeilensteinejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Zahlungsplanmeilenstein AS o WHERE o.zahlungsplanIId=?1") })
@Entity
@Table(name = "AUFT_ZAHLUNGSPLANMEILENSTEIN")
public class Zahlungsplanmeilenstein implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "MEILENSTEIN_I_ID")
	private Integer meilensteinIId;

	@Column(name = "ZAHLUNGSPLAN_I_ID")
	private Integer zahlungsplanIId;

	@Column(name = "I_SORT")
	private Integer iSort;

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getMeilensteinIId() {
		return meilensteinIId;
	}

	public void setMeilensteinIId(Integer meilensteinIId) {
		this.meilensteinIId = meilensteinIId;
	}

	public Integer getZahlungsplanIId() {
		return zahlungsplanIId;
	}

	public void setZahlungsplanIId(Integer zahlungsplanIId) {
		this.zahlungsplanIId = zahlungsplanIId;
	}

	public java.sql.Timestamp getTErledigt() {
		return tErledigt;
	}

	public void setTErledigt(java.sql.Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	@Column(name = "X_TEXT")
	private String xText;

	@Column(name = "T_ERLEDIGT")
	private java.sql.Timestamp tErledigt;

	@Column(name = "PERSONAL_I_ID_ERLEDIGT")
	private Integer personalIIdErledigt;

	public Integer getPersonalIIdErledigt() {
		return personalIIdErledigt;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	private static final long serialVersionUID = 1L;

	public Zahlungsplanmeilenstein() {
		super();
	}

	public Zahlungsplanmeilenstein(Integer id, Integer zahlungsplanIId,
			Integer meilensteinIId, Integer iSort) {
		setIId(id);
		setZahlungsplanIId(zahlungsplanIId);
		setMeilensteinIId(meilensteinIId);
		setISort(iSort);
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public String getXText() {
		return xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
