package com.lp.server.eingangsrechnung.ejb;


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
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "AuftragszuordnungverrechnetFindByRechnungpositionIId", query = "SELECT OBJECT(o) FROM Auftragszuordnungverrechnet o WHERE o.rechnungpositionIId=?1"),
		@NamedQuery(name = "AuftragszuordnungverrechnetFindByAuftragszuordnungIId", query = "SELECT OBJECT(o) FROM Auftragszuordnungverrechnet o WHERE o.auftragszuordnungIId=?1") })
@Entity
@Table(name = "ER_AUFTRAGSZUORDNUNGVERRECHNET")
public class Auftragszuordnungverrechnet implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_BETRAG")
	private BigDecimal nBetrag;

	public BigDecimal getNBetrag() {
		return nBetrag;
	}

	public void setNBetrag(BigDecimal nBetrag) {
		this.nBetrag = nBetrag;
	}

	public Integer getAuftragszuordnungIId() {
		return auftragszuordnungIId;
	}

	public void setAuftragszuordnungIId(Integer auftragszuordnungIId) {
		this.auftragszuordnungIId = auftragszuordnungIId;
	}

	public Integer getRechnungpositionIId() {
		return rechnungpositionIId;
	}

	public void setRechnungpositionIId(Integer rechnungpositionIId) {
		this.rechnungpositionIId = rechnungpositionIId;
	}

	@Column(name = "AUFTRAGSZUORDNUNG_I_ID")
	private Integer auftragszuordnungIId;

	@Column(name = "RECHNUNGPOSITION_I_ID")
	private Integer rechnungpositionIId;

	private static final long serialVersionUID = 1L;

	public Auftragszuordnungverrechnet() {
		super();
	}

	public Auftragszuordnungverrechnet(Integer id, Integer rechnungpositionIId, Integer auftragszuordnungIId, BigDecimal nBetrag) {
		setIId(id);
		setAuftragszuordnungIId(auftragszuordnungIId);
		setRechnungpositionIId(rechnungpositionIId);
		setNBetrag(nBetrag);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
