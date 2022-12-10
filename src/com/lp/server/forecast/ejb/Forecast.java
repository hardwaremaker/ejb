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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;

//@NamedQueries({ @NamedQuery(name = "GewerkFindByMandantCNrCBez", query = "SELECT OBJECT(o) FROM Gewerk o WHERE o.mandantCNr = ?1 AND o.cBez = ?2") })
@Entity
@Table(name = ITablenames.FC_FORECAST)
public class Forecast implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "C_PROJEKT")
	private String cProjekt;


	@Column(name = "C_PFAD_FORECASTAUFTRAG")
	private String cPfadForecastauftrag;
	public String getCPfadForecastauftrag() {
		return cPfadForecastauftrag;
	}

	public void setCPfadForecastauftrag(String cPfadForecastauftrag) {
		this.cPfadForecastauftrag = cPfadForecastauftrag;
	}

	public String getCPfadCow() {
		return cPfadCow;
	}

	public void setCPfadCow(String cPfadCow) {
		this.cPfadCow = cPfadCow;
	}

	public String getCPfadCod() {
		return cPfadCod;
	}

	public void setCPfadCod(String cPfadCod) {
		this.cPfadCod = cPfadCod;
	}

	public String getCPfadLinienabruf() {
		return cPfadLinienabruf;
	}

	public void setCPfadLinienabruf(String cPfadLinienabruf) {
		this.cPfadLinienabruf = cPfadLinienabruf;
	}

	public String getImportdefCNrForecastauftrag() {
		return importdefCNrForecastauftrag;
	}

	public void setImportdefCNrForecastauftrag(String importdefCNrForecastauftrag) {
		this.importdefCNrForecastauftrag = importdefCNrForecastauftrag;
	}

	public String getImportdefCNrCow() {
		return importdefCNrCow;
	}

	public void setImportdefCNrCow(String importdefCNrCow) {
		this.importdefCNrCow = importdefCNrCow;
	}

	public String getImportdefCNrCod() {
		return importdefCNrCod;
	}

	public void setImportdefCNrCod(String importdefCNrCod) {
		this.importdefCNrCod = importdefCNrCod;
	}

	public String getImportdefCNrLinienabruf() {
		return importdefCNrLinienabruf;
	}

	public void setImportdefCNrLinienabruf(String importdefCNrLinienabruf) {
		this.importdefCNrLinienabruf = importdefCNrLinienabruf;
	}

	@Column(name = "C_PFAD_COW")
	private String cPfadCow;
	@Column(name = "C_PFAD_COD")
	private String cPfadCod;
	@Column(name = "C_PFAD_LINIENABRUF")
	private String cPfadLinienabruf;
	
	@Column(name = "IMPORTDEF_C_NR_FORECASTAUFTRAG")
	private String importdefCNrForecastauftrag;
	@Column(name = "IMPORTDEF_C_NR_COW")
	private String importdefCNrCow;
	@Column(name = "IMPORTDEF_C_NR_COD")
	private String importdefCNrCod;
	@Column(name = "IMPORTDEF_C_NR_LINIENABRUF")
	private String importdefCNrLinienabruf;
	
	@Column(name = "I_TAGE_COD")
	private Integer iTageCod;
	@Column(name = "I_WOCHEN_COW")
	private Integer iWochenCow;
	public Integer getITageCod() {
		return iTageCod;
	}

	@Column(name = "I_TAGE_GUELTIG")
	private Integer iTageGueltig;
	
	public Integer getITageGueltig() {
		return iTageGueltig;
	}

	public void setITageGueltig(Integer iTageGueltig) {
		this.iTageGueltig = iTageGueltig;
	}

	public void setITageCod(Integer iTageCod) {
		this.iTageCod = iTageCod;
	}

	public Integer getIWochenCow() {
		return iWochenCow;
	}

	public void setIWochenCow(Integer iWochenCow) {
		this.iWochenCow = iWochenCow;
	}

	public Integer getIMonateFca() {
		return iMonateFca;
	}

	public void setIMonateFca(Integer iMonateFca) {
		this.iMonateFca = iMonateFca;
	}

	@Column(name = "I_MONATE_FCA")
	private Integer iMonateFca;
	
	
	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getCProjekt() {
		return cProjekt;
	}

	public void setCProjekt(String cProjekt) {
		this.cProjekt = cProjekt;
	}

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}


	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	@Column(name = "STATUS_C_NR")
	private String statusCNr;

	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	private static final long serialVersionUID = 1L;

	public Forecast() {
		super();
	}

	public Forecast(Integer id, String cNr, Integer kundeIId,String statusCNr, Integer iTageCod, Integer iWochenCow, Integer iMonateFca) {
		setIId(id);

		setCNr(cNr);
		setKundeIId(kundeIId);
		
		setStatusCNr(statusCNr);
		
		setITageCod(iTageCod);
		setIWochenCow(iWochenCow);
		setIMonateFca(iMonateFca);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}



}
