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
		@NamedQuery(name = "LagerfindByMandantCNr", query = "SELECT OBJECT(C) FROM Lager c WHERE c.mandantCNr = ?1"),
		@NamedQuery(name = "LagerfindByCNrByMandantCNr", query = "SELECT OBJECT(C) FROM Lager c WHERE c.cNr = ?1 AND  c.mandantCNr = ?2"),
		@NamedQuery(name = "LagerfindByMandantCNrOrderByILoslagersort", query = "SELECT OBJECT(C) FROM Lager c WHERE c.mandantCNr = ?1 AND c.iLoslagersort IS NOT NULL ORDER BY c.iLoslagersort ASC"),
		@NamedQuery(name = "LagerfindByMandantCNrLagerartCNr", query = "SELECT OBJECT (o) FROM Lager o WHERE o.mandantCNr=?1 AND o.lagerartCNr=?2") })
@Entity
@Table(name = "WW_LAGER")
public class Lager implements Serializable, ICNr {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "LAGERART_C_NR")
	private String lagerartCNr;
	
	@Column(name = "B_BESTELLVORSCHLAG")
	private Short bBestellvorschlag;
	
	@Column(name = "B_INTERNEBESTELLUNG")
	private Short bInternebestellung;
	
	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	@Column(name = "B_KONSIGNATIONSLAGER")
	private Short bKonsignationslager;


	public Short getBKonsignationslager() {
		return bKonsignationslager;
	}

	public void setBKonsignationslager(Short bKonsignationslager) {
		this.bKonsignationslager = bKonsignationslager;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setBVersteckt(Short versteckt) {
		bVersteckt = versteckt;
	}

	@Column(name = "I_LOSLAGERSORT")
	private Integer iLoslagersort;
	
	
	
	
	public Integer getILoslagersort() {
		return iLoslagersort;
	}

	public void setILoslagersort(Integer iLoslagersort) {
		this.iLoslagersort = iLoslagersort;
	}

	public Short getBBestellvorschlag() {
		return this.bBestellvorschlag;
	}

	public void setBBestellvorschlag(Short bBestellvorschlag) {
		this.bBestellvorschlag = bBestellvorschlag;
	}

	public Short getBInternebestellung() {
		return bInternebestellung;
	}

	public void setBInternebestellung(Short bInternebestellung) {
		this.bInternebestellung = bInternebestellung;
	}

	private static final long serialVersionUID = 1L;

	public Lager() {
		super();
	}

	public Lager(Integer id, String nr, String lagerartCNr2, String mandantCNr2, Short bBestellvorschlag, Short bInternebestellung, Short bVersteckt, Short bKonsignationslager) {
		setIId(id);
		setCNr(nr);
		setLagerartCNr(lagerartCNr2);
		setMandantCNr(mandantCNr2);
		setBBestellvorschlag(bBestellvorschlag);
		setBInternebestellung(bInternebestellung);
		setBVersteckt(bVersteckt);
		setBKonsignationslager(bKonsignationslager);
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

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getLagerartCNr() {
		return this.lagerartCNr;
	}

	public void setLagerartCNr(String lagerartCNr) {
		this.lagerartCNr = lagerartCNr;
	}

}
