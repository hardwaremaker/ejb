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
package com.lp.server.system.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "ArbeitsplatzparameterfindByArbeitsplatzIIdParameterCNr", query = "SELECT OBJECT (o) FROM Arbeitsplatzparameter o WHERE o.arbeitsplatzIId = ?1 AND o.parameterCNr = ?2"),
		@NamedQuery(name = "ArbeitsplatzparameterfindByArbeitsplatzIId", query = "SELECT OBJECT (o) FROM Arbeitsplatzparameter o WHERE o.arbeitsplatzIId = ?1") })
@Entity
@Table(name = "LP_ARBEITSPLATZPARAMETER")
public class Arbeitsplatzparameter implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_WERT")
	private String cWert;

	@Column(name = "ARBEITSPLATZ_I_ID")
	private Integer arbeitsplatzIId;

	@Column(name = "PARAMETER_C_NR")
	private String parameterCNr;

	private static final long serialVersionUID = 1L;

	public Arbeitsplatzparameter() {
		super();
	}

	public Arbeitsplatzparameter(Integer id, Integer arbeitsplatzIId2,
			String parameterCNr2) {
		setIId(id);
		setArbeitsplatzIId(arbeitsplatzIId2);
		setParameterCNr(parameterCNr2);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCWert() {
		return this.cWert;
	}

	public void setCWert(String cWert) {
		this.cWert = cWert;
	}

	public Integer getArbeitsplatzIId() {
		return this.arbeitsplatzIId;
	}

	public void setArbeitsplatzIId(Integer arbeitsplatz) {
		this.arbeitsplatzIId = arbeitsplatz;
	}

	public String getParameterCNr() {
		return this.parameterCNr;
	}

	public void setParameterCNr(String parameter) {
		this.parameterCNr = parameter;
	}

}
