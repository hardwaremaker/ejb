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
package com.lp.server.personal.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "ZutrittsobjektfindByCNr", query = "SELECT OBJECT(C) FROM Zutrittsobjekt c WHERE  c.cNr = ?1"),
		@NamedQuery(name = "ZutrittsobjektfindByZutrittscontrollerIId", query = "SELECT OBJECT(C) FROM Zutrittsobjekt c WHERE  c.zutrittscontrollerIId = ?1") })
@Entity
@Table(name = "PERS_ZUTRITTSOBJEKT")
public class Zutrittsobjekt implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "C_ADRESSE")
	private String cAdresse;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "C_RELAIS")
	private String cRelais;

	@Column(name = "F_OEFFNUNGSZEIT")
	private Double fOeffnungszeit;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "ZUTRITTSCONTROLLER_I_ID")
	private Integer zutrittscontrollerIId;

	@Column(name = "ZUTRITTSLESER_C_NR")
	private String zutrittsleserCNr;

	private static final long serialVersionUID = 1L;

	public Zutrittsobjekt() {
		super();
	}

	public Zutrittsobjekt(Integer id,
			String nr,
			String adresse,
			Integer zutrittscontrollerIId,
			String relais,
			Double oeffnungszeit, 
			String zutrittsleserCNr) {
		setIId(id);
		setCNr(nr);
		setZutrittscontrollerIId(zutrittscontrollerIId);
		setCRelais(relais);
		setFOeffnungszeit(oeffnungszeit);
		setCAdresse(adresse);
		setZutrittsleserCNr(zutrittsleserCNr);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getCAdresse() {
		return this.cAdresse;
	}

	public void setCAdresse(String cAdresse) {
		this.cAdresse = cAdresse;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCRelais() {
		return this.cRelais;
	}

	public void setCRelais(String cRelais) {
		this.cRelais = cRelais;
	}

	public Double getFOeffnungszeit() {
		return this.fOeffnungszeit;
	}

	public void setFOeffnungszeit(Double fOeffnungszeit) {
		this.fOeffnungszeit = fOeffnungszeit;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getZutrittscontrollerIId() {
		return this.zutrittscontrollerIId;
	}

	public void setZutrittscontrollerIId(Integer zutrittscontrollerIId) {
		this.zutrittscontrollerIId = zutrittscontrollerIId;
	}

	public String getZutrittsleserCNr() {
		return this.zutrittsleserCNr;
	}

	public void setZutrittsleserCNr(String zutrittsleserCNr) {
		this.zutrittsleserCNr = zutrittsleserCNr;
	}

}
