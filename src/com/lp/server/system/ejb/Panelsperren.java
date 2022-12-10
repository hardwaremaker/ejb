
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
	@NamedQuery(name = "PanelsperrenfindByBelegartCNrMandantCNrCRessourceUnten", query = "SELECT OBJECT (o) FROM Panelsperren o WHERE o.belegartCNr=?1 AND o.mandantCNr=?2 AND o.cRessourceUnten=?3"),
	@NamedQuery(name = "PanelsperrenfindByBelegartCNrMandantCNr", query = "SELECT OBJECT (o) FROM Panelsperren o WHERE o.belegartCNr=?1 AND o.mandantCNr=?2")})

@Entity
@Table(name = "LP_PANELSPERREN")
public class Panelsperren implements Serializable {

	@Id
	@Column(name = "I_ID")
	private Integer iId;
	
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	@Column(name = "BELEGART_C_NR")
	private String belegartCNr;

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getCRessourceUnten() {
		return cRessourceUnten;
	}

	public void setCRessourceUnten(String cRessourceUnten) {
		this.cRessourceUnten = cRessourceUnten;
	}

	public String getCRessourceOben() {
		return cRessourceOben;
	}

	public void setCRessourceOben(String cRessourceOben) {
		this.cRessourceOben = cRessourceOben;
	}

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;
	
	@Column(name = "C_RESSOURCE_UNTEN")
	private String cRessourceUnten;
	
	@Column(name = "C_RESSOURCE_OBEN")
	private String cRessourceOben;
	
	

	private static final long serialVersionUID = 1L;

	public Panelsperren() {
		super();
	}

	public Panelsperren(Integer iId, String belegartCNr,String mandantCNr, String cRessourceUnten, String cRessourceOben) {
		setIId(iId);
		setBelegartCNr(belegartCNr);
		setMandantCNr(mandantCNr);
		setCRessourceUnten(cRessourceUnten);
		setCRessourceOben(cRessourceOben);
	}

	
}
