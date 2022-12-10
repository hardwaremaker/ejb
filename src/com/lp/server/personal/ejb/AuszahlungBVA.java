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
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "AuszahlungBVAFindByPersonalIIdTDatum", query = "SELECT OBJECT(C) FROM AuszahlungBVA c WHERE c.personalIId = ?1 AND c.tDatum = ?2"),
		@NamedQuery(name = "AuszahlungBVAfindByPersonalIIdTDatumVonTDatumBis", query = "SELECT OBJECT(C) FROM AuszahlungBVA c WHERE c.personalIId = ?1 AND c.tDatum >= ?2 AND c.tDatum <= ?3") })
@Entity
@Table(name = "PERS_AUSZAHLUNG_BVA")
public class AuszahlungBVA implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_DATUM")
	private Timestamp tDatum;

	@Column(name = "N_GLEITZEIT")
	private BigDecimal nGleitzeit;

	@Column(name = "N_UEST50_GZ")
	private BigDecimal nUestd50Gz;

	@Column(name = "N_UEST50_GZ_ZUSCHLAG")
	private BigDecimal nUestd50Gz_Zuschlag;

	@Column(name = "N_UEST50")
	private BigDecimal nUestd50;

	@Column(name = "N_UEST50_ZUSCHLAG")
	private BigDecimal nUestd50_Zuschlag;

	@Column(name = "N_UEST100")
	private BigDecimal nUestd100;

	@Column(name = "N_UEST100_ZUSCHLAG")
	private BigDecimal nUestd100_Zuschlag;

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	private static final long serialVersionUID = 1L;

	public AuszahlungBVA() {
		super();
	}

	public AuszahlungBVA(Integer id, Integer personalIId2, Timestamp tDatum,
			Integer personalIIdAendern2, BigDecimal nGleitzeit,
			BigDecimal nUestd50Gz, BigDecimal nUestd50Gz_Zuschlag,
			BigDecimal nUestd50, BigDecimal nUestd50_Zuschlag,
			BigDecimal nUestd100, BigDecimal nUestd100_Zuschlag,
			Timestamp tAendern) {
		setIId(id);
		setPersonalIId(personalIId2);
		setTDatum(tDatum);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(tAendern);
		setNGleitzeit(nGleitzeit);
		setNUestd100(nUestd100);
		setNUestd100_Zuschlag(nUestd100_Zuschlag);
		setNUestd50(nUestd50);
		setNUestd50_Zuschlag(nUestd50_Zuschlag);
		setNUestd50Gz(nUestd50Gz);
		setNUestd50Gz_Zuschlag(nUestd50Gz_Zuschlag);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Timestamp getTDatum() {
		return tDatum;
	}

	public void setTDatum(Timestamp tDatum) {
		this.tDatum = tDatum;
	}

	public BigDecimal getNGleitzeit() {
		return nGleitzeit;
	}

	public void setNGleitzeit(BigDecimal nGleitzeit) {
		this.nGleitzeit = nGleitzeit;
	}

	public BigDecimal getNUestd50Gz() {
		return nUestd50Gz;
	}

	public void setNUestd50Gz(BigDecimal nUestd50Gz) {
		this.nUestd50Gz = nUestd50Gz;
	}

	public BigDecimal getNUestd50Gz_Zuschlag() {
		return nUestd50Gz_Zuschlag;
	}

	public void setNUestd50Gz_Zuschlag(BigDecimal nUestd50Gz_Zuschlag) {
		this.nUestd50Gz_Zuschlag = nUestd50Gz_Zuschlag;
	}

	public BigDecimal getNUestd50() {
		return nUestd50;
	}

	public void setNUestd50(BigDecimal nUestd50) {
		this.nUestd50 = nUestd50;
	}

	public BigDecimal getNUestd50_Zuschlag() {
		return nUestd50_Zuschlag;
	}

	public void setNUestd50_Zuschlag(BigDecimal nUestd50_Zuschlag) {
		this.nUestd50_Zuschlag = nUestd50_Zuschlag;
	}

	public BigDecimal getNUestd100() {
		return nUestd100;
	}

	public void setNUestd100(BigDecimal nUestd100) {
		this.nUestd100 = nUestd100;
	}

	public BigDecimal getnUestd100_Zuschlag() {
		return nUestd100_Zuschlag;
	}

	public void setNUestd100_Zuschlag(BigDecimal nUestd100_Zuschlag) {
		this.nUestd100_Zuschlag = nUestd100_Zuschlag;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}
}
