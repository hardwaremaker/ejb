package com.lp.server.system.ejb;

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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "BelegartmediaFindByUsecaseIdIKeyISort", query = "SELECT OBJECT (o) FROM Belegartmedia o WHERE o.usecaseId=?1 AND o.iKey=?2 AND o.iSort=?3"),
		@NamedQuery(name = "BelegartmediaFindByUsecaseIdIKey", query = "SELECT OBJECT (o) FROM Belegartmedia o WHERE o.usecaseId=?1 AND o.iKey=?2 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "BelegartmediaCountByUsecaseIdIKey", query = "SELECT count (o.iId) FROM Belegartmedia o WHERE o.usecaseId=?1 AND o.iKey=?2")})
@Entity
@Table(name = "LP_BELEGARTMEDIA")
public class Belegartmedia implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "USECASE_ID")
	private Integer usecaseId;

	@Column(name = "I_KEY")
	private Integer iKey;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "DATENFORMAT_C_NR")
	private String datenformatCNr;

	@Column(name = "X_TEXT")
	private String xText;

	@Column(name = "I_AUSRICHTUNG")
	private Integer iAusrichtung;

	@Column(name = "O_MEDIA")
	private byte[] oMedia;
	
	@Column(name = "C_DATEINAME")
	private String cDateiname;

	public String getCDateiname() {
		return cDateiname;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
	}

	private static final long serialVersionUID = 1L;

	public Belegartmedia() {
		super();
	}

	public Belegartmedia(Integer id, Integer usecaseId, Integer iKey, Integer iSort, String datenformatCNr,
			Integer iAusrichtung) {
		setIId(id);
		setUsecaseId(usecaseId);
		setIKey(iKey);
		setISort(iSort);
		setDatenformatCNr(datenformatCNr);
		setIAusrichtung(iAusrichtung);
	}

	public Integer getUsecaseId() {
		return usecaseId;
	}

	public void setUsecaseId(Integer usecaseId) {
		this.usecaseId = usecaseId;
	}

	public Integer getIKey() {
		return iKey;
	}

	public void setIKey(Integer iKey) {
		this.iKey = iKey;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getXText() {
		return xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public Integer getIAusrichtung() {
		return iAusrichtung;
	}

	public void setIAusrichtung(Integer iAusrichtung) {
		this.iAusrichtung = iAusrichtung;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getDatenformatCNr() {
		return this.datenformatCNr;
	}

	public void setDatenformatCNr(String datenformatCNr) {
		this.datenformatCNr = datenformatCNr;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public byte[] getOMedia() {
		return this.oMedia;
	}

	public void setOMedia(byte[] oMedia) {
		this.oMedia = oMedia;
	}

}
