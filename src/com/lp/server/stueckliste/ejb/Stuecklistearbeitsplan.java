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
package com.lp.server.stueckliste.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "StuecklistearbeitsplanejbSelectNextReihung", query = "SELECT MAX (o.iArbeitsgang) FROM Stuecklistearbeitsplan o WHERE o.stuecklisteIId = ?1"),
		@NamedQuery(name = "StuecklistearbeitsplanfindByStuecklisteIIdIArbeitsgangnummer", query = "SELECT OBJECT(o) FROM Stuecklistearbeitsplan o WHERE o.stuecklisteIId=?1 AND o.iArbeitsgang=?2 ORDER BY o.iUnterarbeitsgang"),
		@NamedQuery(name = "StuecklistearbeitsplanfindByStuecklisteIId", query = "SELECT OBJECT(o) FROM Stuecklistearbeitsplan o WHERE o.stuecklisteIId=?1 ORDER BY o.iArbeitsgang,o.iUnterarbeitsgang ASC"),
		@NamedQuery(name = "StuecklistearbeitsplanfindByStuecklistepositionIId", query = "SELECT OBJECT(o) FROM Stuecklistearbeitsplan o WHERE o.stuecklistepositionIId=?1"),
		@NamedQuery(name = "StuecklistearbeitsplanfindByArtikelIId", query = "SELECT OBJECT(o) FROM Stuecklistearbeitsplan o WHERE o.artikelIId=?1")})
@Entity
@Table(name = "STK_STUECKLISTEARBEITSPLAN")
public class Stuecklistearbeitsplan implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_ARBEITSGANG")
	private Integer iArbeitsgang;

	@Column(name = "L_STUECKZEIT")
	private Long lStueckzeit;

	@Column(name = "L_RUESTZEIT")
	private Long lRuestzeit;

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	@Column(name = "I_UNTERARBEITSGANG")
	private Integer iUnterarbeitsgang;
	
	@Column(name = "I_MASCHINENVERSATZTAGE")
	private Integer iMaschinenversatztage;
	
	@Column(name = "STUECKLISTEPOSITION_I_ID")
	private Integer stuecklistepositionIId;
	
	public Integer getStuecklistepositionIId() {
		return stuecklistepositionIId;
	}

	public void setStuecklistepositionIId(Integer stuecklistepositionIId) {
		this.stuecklistepositionIId = stuecklistepositionIId;
	}

	public Integer getIMaschinenversatztage() {
		return iMaschinenversatztage;
	}

	public void setIMaschinenversatztage(Integer maschinenversatztage) {
		iMaschinenversatztage = maschinenversatztage;
	}

	public Integer getIUnterarbeitsgang() {
		return iUnterarbeitsgang;
	}

	public void setIUnterarbeitsgang(Integer unterarbeitsgang) {
		iUnterarbeitsgang = unterarbeitsgang;
	}

	@Column(name = "X_LANGTEXT")
	private String xLangtext;

	@Column(name = "MASCHINE_I_ID")
	private Integer maschineIId;

	@Column(name = "STUECKLISTE_I_ID")
	private Integer stuecklisteIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;
	
	@Column(name = "AGART_C_NR")
	private String agartCNr;
	
	@Column(name = "I_AUFSPANNUNG")
	private Integer iAufspannung;

	
	@Column(name = "B_NURMASCHINENZEIT")
	private Short bNurmaschinenzeit;
	
	public Short getBNurmaschinenzeit() {
		return bNurmaschinenzeit;
	}

	public void setBNurmaschinenzeit(Short nurmaschinenzeit) {
		bNurmaschinenzeit = nurmaschinenzeit;
	}

	public String getAgartCNr() {
		return agartCNr;
	}

	public void setAgartCNr(String agartCNr) {
		this.agartCNr = agartCNr;
	}

	public Integer getIAufspannung() {
		return iAufspannung;
	}

	public void setIAufspannung(Integer aufspannung) {
		iAufspannung = aufspannung;
	}

	private static final long serialVersionUID = 1L;

	public Stuecklistearbeitsplan() {
		super();
	}

	public Stuecklistearbeitsplan(Integer id, Integer stuecklisteIId,
			Integer arbeitsgang, Integer artikelIId, Long stueckzeit,
			Long ruestzeit,Short bNurmaschinenzeit) {
		setIId(id);
		setStuecklisteIId(stuecklisteIId);
		setIArbeitsgang(arbeitsgang);
		setArtikelIId(artikelIId);
		setLStueckzeit(stueckzeit);
		setLRuestzeit(ruestzeit);
		setBNurmaschinenzeit(bNurmaschinenzeit);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIArbeitsgang() {
		return this.iArbeitsgang;
	}

	public void setIArbeitsgang(Integer iArbeitsgang) {
		this.iArbeitsgang = iArbeitsgang;
	}

	public Long getLStueckzeit() {
		return this.lStueckzeit;
	}

	public void setLStueckzeit(Long lStueckzeit) {
		this.lStueckzeit = lStueckzeit;
	}

	public Long getLRuestzeit() {
		return this.lRuestzeit;
	}

	public void setLRuestzeit(Long lRuestzeit) {
		this.lRuestzeit = lRuestzeit;
	}

	public String getCKommentar() {
		return this.cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public String getXLangtext() {
		return this.xLangtext;
	}

	public void setXLangtext(String xLangtext) {
		this.xLangtext = xLangtext;
	}

	public Integer getMaschineIId() {
		return this.maschineIId;
	}

	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}

	public Integer getStuecklisteIId() {
		return this.stuecklisteIId;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}
