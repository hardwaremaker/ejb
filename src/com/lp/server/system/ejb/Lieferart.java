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
package com.lp.server.system.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "LieferartfindAll", query = "SELECT OBJECT(o) FROM Lieferart o"),
		@NamedQuery(name = "LieferartfindByCNr", query = "SELECT OBJECT(o) FROM Lieferart o WHERE o.cNr = ?1"),
		@NamedQuery(name = Lieferart.QueryByCNrMandantCNr, query = "SELECT OBJECT(o) FROM Lieferart o WHERE o.cNr = ?1 AND o.mandantCNr = ?2") })
@Entity
@Table(name = "LP_LIEFERART")
public class Lieferart implements Serializable {
	public final static String QueryByCNrMandantCNr = "LieferartfindbyCNrMandantCNr" ;
	
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "B_FRACHTKOSTENALSERLEDIGTVERBUCHEN")
	private Short bFrachtkostenalserledigtverbuchen;

	@Column(name = "C_VERSANDORT")
	private String cVersandort;

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;


	private static final long serialVersionUID = 1L;

	public Lieferart() {
		super();
	}

	public Lieferart(Integer id, String nr,
			Short frachtkostenalserledigtverbuchen, String mandantCNr,
			Short versteckt) {
		setIId(id);
		setCNr(nr);
		setBFrachtkostenalserledigtverbuchen(frachtkostenalserledigtverbuchen);
		setMandantCNr(mandantCNr);
		setBVersteckt(versteckt);
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

	public Short getBFrachtkostenalserledigtverbuchen() {
		return this.bFrachtkostenalserledigtverbuchen;
	}

	public void setBFrachtkostenalserledigtverbuchen(
			Short bFrachtkostenalserledigtverbuchen) {
		this.bFrachtkostenalserledigtverbuchen = bFrachtkostenalserledigtverbuchen;
	}

	public String getCVersandort() {
		return this.cVersandort;
	}

	public void setCVersandort(String cVersandort) {
		this.cVersandort = cVersandort;
	}

	public Short getBVersteckt() {
		return this.bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandant) {
		this.mandantCNr = mandant;
	}



}
