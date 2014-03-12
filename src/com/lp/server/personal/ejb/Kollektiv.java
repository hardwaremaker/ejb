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
package com.lp.server.personal.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( { @NamedQuery(name = "KollektivfindByCBez", query = "SELECT OBJECT(C) FROM Kollektiv c WHERE c.cBez = ?1") })
@Entity
@Table(name = "PERS_KOLLEKTIV")
public class Kollektiv implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "B_VERBRAUCHEUESTD")
	private Short bVerbraucheuestd;

	@Column(name = "N_NORMALSTUNDEN")
	private BigDecimal nNormalstunden;

	@Column(name = "N_FAKTORMEHRSTD")
	private BigDecimal nFaktormehrstd;

	@Column(name = "N_FAKTORUESTD50")
	private BigDecimal nFaktoruestd50;

	@Column(name = "N_FAKTORUESTD100")
	private BigDecimal nFaktoruestd100;
	
	@Column(name = "N_FAKTORUESTD200")
	private BigDecimal nFaktoruestd200;
	
	public BigDecimal getNFaktoruestd200() {
		return nFaktoruestd200;
	}

	public void setNFaktoruestd200(BigDecimal faktoruestd200) {
		nFaktoruestd200 = faktoruestd200;
	}

	public BigDecimal getN200prozentigeab() {
		return n200prozentigeab;
	}

	public void setN200prozentigeab(BigDecimal n200prozentigeab) {
		this.n200prozentigeab = n200prozentigeab;
	}

	@Column(name = "N_200PROZENTIGEAB")
	private BigDecimal n200prozentigeab;

	@Column(name = "U_BLOCKZEITAB")
	private Time uBlockzeitab;

	@Column(name = "U_BLOCKZEITBIS")
	private Time uBlockzeitbis;

	@Column(name = "B_UESTDABSOLLSTDERBRACHT")
	private Short bUestdabsollstderbracht;

	@Column(name = "B_UESTDVERTEILEN")
	private Short bUestdverteilen;
	
	public Short getBUestdverteilen() {
		return bUestdverteilen;
	}

	public void setBUestdverteilen(Short uestdverteilen) {
		bUestdverteilen = uestdverteilen;
	}

	public Short getBUestdabsollstderbracht() {
		return bUestdabsollstderbracht;
	}

	public void setBUestdabsollstderbracht(Short uestdabsollstderbracht) {
		bUestdabsollstderbracht = uestdabsollstderbracht;
	}

	private static final long serialVersionUID = 1L;

	public Kollektiv() {
		super();
	}

	public Kollektiv(Integer id, String bez, Short bVerbraucheuestd,
			BigDecimal faktoruestd50, BigDecimal faktoruestd100, BigDecimal faktormehrstd, Short bUestdabsollstderbracht, Short bUestverteilen,BigDecimal faktoruestd200,BigDecimal n200prozentigeab) {
		setIId(id);
		setCBez(bez);
		setBVerbraucheuestdt(bVerbraucheuestd);
		setNFaktoruestd50(faktoruestd50);
		setNFaktoruestd100(faktoruestd100);
		setNFaktormehrstd(faktormehrstd);
		setBUestdabsollstderbracht(bUestdabsollstderbracht);
		setBUestdverteilen(bUestverteilen);
		setN200prozentigeab(n200prozentigeab);
		setNFaktoruestd200(faktoruestd200);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Short getBVerbraucheuestd() {
		return this.bVerbraucheuestd;
	}

	public void setBVerbraucheuestdt(Short bVerbraucheuestd) {
		this.bVerbraucheuestd = bVerbraucheuestd;
	}

	public BigDecimal getNNormalstunden() {
		return this.nNormalstunden;
	}

	public void setNNormalstunden(BigDecimal nNormalstunden) {
		this.nNormalstunden = nNormalstunden;
	}

	public BigDecimal getNFaktoruestd50() {
		return this.nFaktoruestd50;
	}

	public void setNFaktoruestd50(BigDecimal nFaktoruestd50) {
		this.nFaktoruestd50 = nFaktoruestd50;
	}
	public BigDecimal getNFaktormehrstd() {
		return this.nFaktormehrstd;
	}

	public void setNFaktormehrstd(BigDecimal nFaktormehrstd) {
		this.nFaktormehrstd = nFaktormehrstd;
	}

	public BigDecimal getNFaktoruestd100() {
		return this.nFaktoruestd100;
	}

	public void setNFaktoruestd100(BigDecimal nFaktoruestd100) {
		this.nFaktoruestd100 = nFaktoruestd100;
	}

	public Time getUBlockzeitab() {
		return this.uBlockzeitab;
	}

	public void setUBlockzeitab(Time uBlockzeitab) {
		this.uBlockzeitab = uBlockzeitab;
	}

	public Time getUBlockzeitbis() {
		return this.uBlockzeitbis;
	}

	public void setUBlockzeitbis(Time uBlockzeitbis) {
		this.uBlockzeitbis = uBlockzeitbis;
	}

}
