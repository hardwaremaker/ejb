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
package com.lp.server.finanz.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FB_BUCHUNGSARTSPR")
public class Buchungsartspr implements Serializable {
	@EmbeddedId
	private BuchungsartsprPK pk;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "BUCHUNGSART_C_NR",insertable = false, updatable = false)
	private String buchungsartCNr;

	@Column(name = "LOCALE_C_NR",insertable = false, updatable = false)
	private String localeCNr;

	private static final long serialVersionUID = 1L;

	public Buchungsartspr() {
		super();
	}
	
	public Buchungsartspr(String buchungsartCNr, String localeCNr) {
		setPk(new BuchungsartsprPK(buchungsartCNr,localeCNr) );
	}	

	public BuchungsartsprPK getPk() {
		return this.pk;
	}

	public void setPk(BuchungsartsprPK pk) {
		this.pk = pk;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getBuchungsartCNr() {
		return this.buchungsartCNr;
	}

	public void setBuchungsartCnr(String buchungsartCNr) {
		this.buchungsartCNr = buchungsartCNr;
	}

	public String getLocaleCNr() {
		return this.localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

}
