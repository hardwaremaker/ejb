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
package com.lp.server.artikel.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "ArtikelkommentarartfindByCNr", query = "SELECT OBJECT(o) FROM Artikelkommentarart o WHERE o.cNr = ?1"),
		@NamedQuery(name = "ArtikelkommentarartfindByBWebshop", query = "SELECT OBJECT(o) FROM Artikelkommentarart o WHERE o.bWebshop = 1") })
@Entity
@Table(name = "WW_ARTIKELKOMMENTARART")
public class Artikelkommentarart implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "BRANCHE_I_ID")
	private Integer brancheId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "B_WEBSHOP")
	private Short bWebshop;
	
	@Column(name = "B_TOOLTIP")
	private Short bTooltip;
	
	@Column(name = "B_DETAIL")
	private Short bDetail;
	

	public Short getBDetail() {
		return bDetail;
	}

	public void setBDetail(Short bDetail) {
		this.bDetail = bDetail;
	}

	public Short getBWebshop() {
		return this.bWebshop;
	}

	public void setBWebshop(Short bWebshop) {
		this.bWebshop = bWebshop;
	}

	public Integer getBrancheId() {
		return brancheId;
	}

	public void setBrancheId(Integer brancheId) {
		this.brancheId = brancheId;
	}

	private static final long serialVersionUID = 1L;

	public Artikelkommentarart() {
		super();
	}

	public Artikelkommentarart(Integer id, String nr, Short bWebshop, Short bTooltip, Short bDetail) {
		setIId(id);
		setCNr(nr);
		setBWebshop(bWebshop);
		setBTooltip(bTooltip);
		setBDetail(bDetail);
	}

	public Short getBTooltip() {
		return bTooltip;
	}

	public void setBTooltip(Short bTooltip) {
		this.bTooltip = bTooltip;
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

}
