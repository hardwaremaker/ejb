package com.lp.server.forecast.ejb;

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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = ForecastauftragQuery.ByForecastlieferadresseStatusCNr, query = "SELECT OBJECT(o) FROM Forecastauftrag o WHERE o.fclieferadresseIId = ?1  AND o.statusCNr = ?2") })
@Entity
@Table(name = "FC_FORECASTAUFTRAG")
public class Forecastauftrag implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "FCLIEFERADRESSE_I_ID")
	private Integer fclieferadresseIId;

	public Integer getFclieferadresseIId() {
		return fclieferadresseIId;
	}

	public void setFclieferadresseIId(Integer fclieferadresseIId) {
		this.fclieferadresseIId = fclieferadresseIId;
	}

	@Column(name = "X_KOMMENTAR")
	private String xKommentar;
	

	public String getXKommentar() {
		return xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}
	
	@Column(name = "T_FREIGABE")
	private java.sql.Timestamp tFreigabe;

	@Column(name = "PERSONAL_I_ID_FREIGABE")
	private Integer personalIIdFreigabe;

	public java.sql.Timestamp getTFreigabe() {
		return tFreigabe;
	}

	public void setTFreigabe(java.sql.Timestamp tFreigabe) {
		this.tFreigabe = tFreigabe;
	}

	public Integer getPersonalIIdFreigabe() {
		return personalIIdFreigabe;
	}

	public void setPersonalIIdFreigabe(Integer personalIIdFreigabe) {
		this.personalIIdFreigabe = personalIIdFreigabe;
	}
	
	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	



	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	@Column(name = "C_BEMERKUNG")
	private String cBemerkung;

	

	public String getCBemerkung() {
		return cBemerkung;
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	@Column(name = "STATUS_C_NR")
	private String statusCNr;

	private static final long serialVersionUID = 1L;

	public Forecastauftrag() {
		super();
	}

	public Forecastauftrag(Integer id, Integer fclieferadresseIId, Timestamp tAnlegen
			, String statusCNr) {
		setIId(id);
		setFclieferadresseIId(fclieferadresseIId);
		setTAnlegen(tAnlegen);
	
		setStatusCNr(statusCNr);
	

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
