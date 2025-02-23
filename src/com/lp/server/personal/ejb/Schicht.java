
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

@NamedQueries({ @NamedQuery(name = Schicht.QueryFindByMandantCNrCBez, query = "SELECT OBJECT(o) FROM Schicht o WHERE o.mandantCNr = ?1 AND o.cBez = ?2") })
@Entity
@Table(name = "PERS_SCHICHT")
public class Schicht implements Serializable {
	public final static String QueryFindByMandantCNrCBez = "SchichtFindByMandantCNrCBez" ;
	
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "B_PAUSENABZIEHEN")
	private Short bPausenabziehen;
	
	@Column(name = "B_BEGRENZT_AUF_TAGESSOLL")
	private Short bBegrenztAufTagessoll;
	
	public Short getBBegrenztAufTagessoll() {
		return bBegrenztAufTagessoll;
	}

	public void setBBegrenztAufTagessoll(Short bBegrenztAufTagessoll) {
		this.bBegrenztAufTagessoll = bBegrenztAufTagessoll;
	}

	public Short getBPausenabziehen() {
		return bPausenabziehen;
	}

	public void setBPausenabziehen(Short bPausenabziehen) {
		this.bPausenabziehen = bPausenabziehen;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private static final long serialVersionUID = 1L;

	public Schicht() {
		super();
	}

	public Schicht(Integer id, String mandantCNr, String cBez, Short bPausenabziehen, Short bBegrenztAufTagessoll) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setCBez(cBez);
		setBPausenabziehen(bPausenabziehen);
		setBBegrenztAufTagessoll(bBegrenztAufTagessoll);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String bez) {
		cBez = bez;
	}

}
