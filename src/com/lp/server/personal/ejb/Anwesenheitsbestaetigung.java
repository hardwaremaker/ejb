
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
package com.lp.server.personal.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.artikel.ejb.ArtikelQuery;

@NamedQueries({
		@NamedQuery(name = "AnwesenheitsbestaetigungFindByPersonalIIdAuftragIId", query = "SELECT OBJECT(C) FROM Anwesenheitsbestaetigung c WHERE c.personalIId = ?1 AND c.auftragIId=?2 ORDER BY c.tUnterschrift ASC"),
		@NamedQuery(name = "AnwesenheitsbestaetigungFindByAuftragIId", query = "SELECT OBJECT(C) FROM Anwesenheitsbestaetigung c WHERE c.auftragIId=?1 ORDER BY c.tUnterschrift ASC")})

@Entity
@Table(name = "PERS_ANWESENHEITSBESTAETIGUNG")
public class Anwesenheitsbestaetigung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "AUFTRAG_I_ID")
	private Integer auftragIId;

	@Column(name = "PROJEKT_I_ID")
	private Integer projektIId;

	@Column(name = "T_UNTERSCHRIFT")
	private Timestamp tUnterschrift;

	@Column(name = "T_VERSANDT")
	private Timestamp tVersandt;

	@Column(name = "O_UNTERSCHRIFT")
	private byte[] oUnterschrift;

	@Column(name = "DATENFORMAT_C_NR")
	private String datenformatCNr;
	
	@Column(name = "I_LFDNR")
	private Integer iLfdnr;
	
	@Column(name = "C_NAME")
	private String cName;

	public String getCName() {
		return cName;
	}

	public void setCName(String cName) {
		this.cName = cName;
	}

	public Integer getILfdnr() {
		return iLfdnr;
	}

	public void setILfdnr(Integer iLfdnr) {
		this.iLfdnr = iLfdnr;
	}

	public byte[] getOPdf() {
		return oPdf;
	}

	public void setOPdf(byte[] oPdf) {
		this.oPdf = oPdf;
	}

	@Column(name = "O_PDF")
	private byte[] oPdf;

	@Column(name = "C_BEMERKUNG")
	private String cBemerkung;

	private static final long serialVersionUID = 1L;

	public Anwesenheitsbestaetigung() {
		super();
	}

	public Anwesenheitsbestaetigung(Integer id, Integer personalIId, Timestamp tUnterschrift, Integer iLfdnr) {
		setIId(id);
		setPersonalIId(personalIId);
		setTUnterschrift(tUnterschrift);
		setILfdnr(iLfdnr);

	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Integer getAuftragIId() {
		return auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	public Timestamp getTUnterschrift() {
		return tUnterschrift;
	}

	public void setTUnterschrift(Timestamp tUnterschrift) {
		this.tUnterschrift = tUnterschrift;
	}

	public Timestamp getTVersandt() {
		return tVersandt;
	}

	public void setTVersandt(Timestamp tVersandt) {
		this.tVersandt = tVersandt;
	}

	public byte[] getOUnterschrift() {
		return oUnterschrift;
	}

	public void setOUnterschrift(byte[] oUnterschrift) {
		this.oUnterschrift = oUnterschrift;
	}

	public String getDatenformatCNr() {
		return datenformatCNr;
	}

	public void setDatenformatCNr(String datenformatCNr) {
		this.datenformatCNr = datenformatCNr;
	}

	public String getCBemerkung() {
		return cBemerkung;
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
