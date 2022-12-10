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

@NamedQueries( { @NamedQuery(name = "AutoMahnungsversandfindByMandantCNr", query = "SELECT OBJECT (o) FROM AutoMahnungsversand o WHERE o.mandantCNr=?1") })
@Entity
@Table(name = "AUTO_MAHNUNGSVERSAND")
public class AutoMahnungsversand implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "MAHNLAUF_I_ID")
	private Integer mahnlaufIId;

	@Column(name = "C_DRUCKER")
	private String cDrucker;
	
	@Column(name = "C_EMAIL_ZUSAETZILCH")
	private String cEmailZusaetzlich;

	public String getCEmailZusaetzlich() {
		return cEmailZusaetzlich;
	}

	public void setCEmailZusaetzlich(String cEmailZusaetzlich) {
		this.cEmailZusaetzlich = cEmailZusaetzlich;
	}

	@Column(name = "C_VERSANDART")
	private String cVersandart;

	@Column(name = "B_RAHMENBESTELLUNGMAHNEN")
	private Integer bRahmenbestellungmahnen;

	private static final long serialVersionUID = 1L;

	public AutoMahnungsversand() {
		super();
	}
	
	public AutoMahnungsversand(Integer id) {
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

	public Integer getMahnlaufIId() {
		return this.mahnlaufIId;
	}

	public void setMahnlaufIId(Integer mahnlaufIId) {
		this.mahnlaufIId = mahnlaufIId;
	}

	public String getCDrucker() {
		return this.cDrucker;
	}

	public void setCDrucker(String cDrucker) {
		this.cDrucker = cDrucker;
	}

	public String getCVersandart() {
		return this.cVersandart;
	}

	public void setCVersandart(String cVersandart) {
		this.cVersandart = cVersandart;
	}

	public Integer getBRahmenbestellungmahnen() {
		return this.bRahmenbestellungmahnen;
	}

	public void setBRahmenbestellungmahnen(Integer bRahmenbestellungmahnen) {
		this.bRahmenbestellungmahnen = bRahmenbestellungmahnen;
	}

}
