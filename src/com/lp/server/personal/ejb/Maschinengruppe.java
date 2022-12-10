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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
	@NamedQuery(name = MaschinengruppeQuery.ByCBez, query = "SELECT OBJECT(C) FROM Maschinengruppe c WHERE c.mandantCNr = :mandantCnr AND c.cBez = :cBez"),
	@NamedQuery(name = MaschinengruppeQuery.ByCKbez, query = "SELECT OBJECT(C) FROM Maschinengruppe c WHERE c.mandantCNr = :mandantCnr AND c.cKbez = :cKbez"),
	@NamedQuery(name = MaschinengruppeQuery.NextISort, query = "SELECT MAX (o.iSort) FROM Maschinengruppe o WHERE o.mandantCNr = ?1"),
	@NamedQuery(name = MaschinengruppeQuery.ByFertigungsgruppe, query = "SELECT OBJECT(C) FROM Maschinengruppe c WHERE c.mandantCNr = :mandantCnr AND c.fertigungsgruppeIId = :fertigungsgruppe")})
@Entity
@Table(name = "PERS_MASCHINENGRUPPE")
public class Maschinengruppe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BEZ")
	private String cBez;
	
	@Column(name = "C_KBEZ")
	private String cKbez;

	public String getCKbez() {
		return cKbez;
	}

	public void setCKbez(String cKbez) {
		this.cKbez = cKbez;
	}

	@Column(name = "B_AUSLASTUNGSANZEIGE")
	private Short bAuslastungsanzeige;
	
	@Column(name = "FERTIGUNGSGRUPPE_I_ID")
	private Integer fertigungsgruppeIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "I_SORT")
	private Integer iSort;
	
	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	
	public Maschinengruppe() {
	}

	public Maschinengruppe(Integer id, String mandantCnr,String kbez,String bez, Short bAuslastungsanzeige, Integer fertigungsgruppeIId, Integer iSort) {
		setIId(id);
		setCKbez(kbez);
		setCBez(bez);
		setMandantCNr(mandantCnr);
		setBAuslastungsanzeige(bAuslastungsanzeige);
		setFertigungsgruppeIId(fertigungsgruppeIId);
		setISort(iSort);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Short getBAuslastungsanzeige() {
		return bAuslastungsanzeige;
	}

	public void setBAuslastungsanzeige(Short bAuslastungsanzeige) {
		this.bAuslastungsanzeige = bAuslastungsanzeige;
	}
	
	public Integer getFertigungsgruppeIId() {
		return fertigungsgruppeIId;
	}
	
	public void setFertigungsgruppeIId(Integer fertigungsgruppeIId) {
		this.fertigungsgruppeIId = fertigungsgruppeIId;
	}
	
	public String getMandantCNr() {
		return mandantCNr;
	}
	
	public void setMandantCNr(String mandantCnr) {
		this.mandantCNr = mandantCnr;
	}
}
