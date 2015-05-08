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
package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "AutomatikjobfindByISort", query = "SELECT OBJECT (o) FROM Automatikjobs o WHERE o.iSort=?1"),
		@NamedQuery(name = "AutomatikjobfindByBActive", query = "SELECT OBJECT (o) FROM Automatikjobs o WHERE o.bActive=?1"),
		@NamedQuery(name = "AutomatikjobfindByBMonthjob", query = "SELECT OBJECT (o) FROM Automatikjobs o WHERE o.bMonthjob=?1"),
		@NamedQuery(name = "AutomatikjobfindBydNextperform", query = "SELECT OBJECT (o) FROM Automatikjobs o WHERE o.dNextperform=?1"),
		@NamedQuery(name = "AutomatikjobfindByCName", query = "SELECT OBJECT (o) FROM Automatikjobs o WHERE o.cName=?1"),
		@NamedQuery(name = "AutomatikjobfindByCMandantCNr", query = "SELECT OBJECT (o) FROM Automatikjobs o WHERE o.mandantCNr=?1"),
		@NamedQuery(name = "AutomatikjobfindByIAutomatikjobtypeIid", query = "SELECT OBJECT (o) FROM Automatikjobs o WHERE o.iAutomatikjobtypeIid=?1")
		})
@Entity
@Table(name = "LP_AUTOMATIKJOBS")
public class Automatikjobs implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "C_NAME")
	private String cName;

	@Column(name = "C_BESCHREIBUNG")
	private String cBeschreibung;

	@Column(name = "B_ACTIVE")
	private Integer bActive;

	@Column(name = "D_LASTPERFORMED")
	private Date dLastperformed;

	@Column(name = "D_NEXTPERFORM")
	private Date dNextperform;

	@Column(name = "I_INTERVALL")
	private Integer iIntervall;

	@Column(name = "B_MONTHJOB")
	private Integer bMonthjob;

	@Column(name = "B_PERFORMONNONWORKINGDAYS")
	private Integer bPerformonnonworkingdays;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "I_AUTOMATIKJOBTYPE_IID")
	private Integer iAutomatikjobtypeIid;

	private static final long serialVersionUID = 1L;

	public Automatikjobs() {
		super();
	}
	
	public Automatikjobs(Integer id) {
		setIId(id);
	}
	
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getCName() {
		return this.cName;
	}

	public void setCName(String cName) {
		this.cName = cName;
	}

	public String getCBeschreibung() {
		return this.cBeschreibung;
	}

	public void setCBeschreibung(String cBeschreibung) {
		this.cBeschreibung = cBeschreibung;
	}

	public Integer getBActive() {
		return this.bActive;
	}

	public void setBActive(Integer bActive) {
		this.bActive = bActive;
	}

	public Date getDLastperformed() {
		return this.dLastperformed;
	}

	public void setDLastperformed(Date dLastperformed) {
		this.dLastperformed = dLastperformed;
	}

	public Date getDNextperform() {
		return this.dNextperform;
	}

	public void setDNextperform(Date dNextperform) {
		this.dNextperform = dNextperform;
	}

	public Integer getIIntervall() {
		return this.iIntervall;
	}

	public void setIIntervall(Integer iIntervall) {
		this.iIntervall = iIntervall;
	}

	public Integer getBMonthjob() {
		return this.bMonthjob;
	}

	public void setBMonthjob(Integer bMonthjob) {
		this.bMonthjob = bMonthjob;
	}

	public Integer getBPerformonnonworkingdays() {
		return this.bPerformonnonworkingdays;
	}

	public void setBPerformonnonworkingdays(Integer bPerformonnonworkingdays) {
		this.bPerformonnonworkingdays = bPerformonnonworkingdays;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getIAutomatikjobtypeIid() {
		return this.iAutomatikjobtypeIid;
	}

	public void setIAutomatikjobtypeIid(Integer iAutomatikjobtypeIid) {
		this.iAutomatikjobtypeIid = iAutomatikjobtypeIid;
	}

}
