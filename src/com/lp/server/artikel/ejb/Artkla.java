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
package com.lp.server.artikel.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.ICNr;

@NamedQueries( {
		@NamedQuery(name = "ArtklafindByCNrMandantCNr", query = "SELECT OBJECT(o) FROM Artkla o WHERE o.cNr = ?1 AND o.mandantCNr = ?2"),
		@NamedQuery(name = "ArtklafindAll", query = "SELECT OBJECT(o) FROM Artkla o ORDER BY o.cNr"),
		@NamedQuery(name = "ArtklafindByMandantCNr", query = "SELECT OBJECT(o) FROM Artkla o WHERE o.mandantCNr = ?1 ORDER BY o.cNr") })
@Entity
@Table(name = "WW_ARTKLA")
public class Artkla implements Serializable, ICNr {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "B_TOPS")
	private Short bTops;

	@Column(name = "ARTKLA_I_ID")
	private Integer artklaIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private static final long serialVersionUID = 1L;

	public Artkla() {
		super();
	}

	public Artkla(Integer id, String nr, Short tops, String mandantCNr) {
		setCNr(nr);
		setIId(id);
		setBTops(tops);
		setMandantCNr(mandantCNr);
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

	public Short getBTops() {
		return this.bTops;
	}

	public void setBTops(Short bTops) {
		this.bTops = bTops;
	}

	public Integer getArtklaIId() {
		return this.artklaIId;
	}

	public void setArtklaIId(Integer artklaIId) {
		this.artklaIId = artklaIId;
	}

}
