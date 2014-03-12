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
package com.lp.server.partner.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "PartnerbankfindByPartnerIIdBankPartnerIId", query = "SELECT OBJECT(c) FROM Partnerbank c WHERE c.partnerIId = ?1 AND c.partnerbankIId = ?2 AND c.cKtonr = ?3"),
		@NamedQuery(name = "PartnerbankejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Partnerbank o WHERE o.partnerIId = ?1"),
		@NamedQuery(name = "PartnerbankfindByPartnerIId", query = "SELECT OBJECT(c) FROM Partnerbank c WHERE c.partnerIId = ?1 ORDER BY c.iSort ASC"),
		@NamedQuery(name = "PartnerbankfindByBankPartnerIId", query = "SELECT OBJECT(c) FROM Partnerbank c WHERE c.partnerbankIId = ?1") })
@Entity
@Table(name = "PART_PARTNERBANK")
public class Partnerbank implements Serializable {
	public Partnerbank() {
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_KTONR")
	private String cKtonr;

	@Column(name = "C_IBAN")
	private String cIban;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "PARTNERBANK_I_ID")
	private Integer partnerbankIId;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	private static final long serialVersionUID = 1L;

	public Partnerbank(Integer iId, Integer partnerIId, Integer partnerbankIId,
			String cKtonr, Integer iSort) {
		setIId(iId);
		setPartnerIId(partnerIId);
		setPartnerbankIId(partnerbankIId);
		setCKtonr(cKtonr);
		setISort(iSort);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCKtonr() {
		return this.cKtonr;
	}

	public void setCKtonr(String cKtonr) {
		this.cKtonr = cKtonr;
	}

	public String getCIban() {
		return this.cIban;
	}

	public void setCIban(String cIban) {
		this.cIban = cIban;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getPartnerbankIId() {
		return this.partnerbankIId;
	}

	public void setPartnerbankIId(Integer partnerbankIId) {
		this.partnerbankIId = partnerbankIId;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}
}
