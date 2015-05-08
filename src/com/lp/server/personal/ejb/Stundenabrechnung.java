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

@NamedQueries( {
		@NamedQuery(name = "StundenabrechnungfindByPersonalIIdTDatum", query = "SELECT OBJECT(C) FROM Stundenabrechnung c WHERE c.personalIId = ?1 AND c.tDatum = ?2"),
		@NamedQuery(name = "StundenabrechnungfindByPersonalIIdTDatumVonTDatumBis", query = "SELECT OBJECT(C) FROM Stundenabrechnung c WHERE c.personalIId = ?1 AND c.tDatum >= ?2 AND c.tDatum <= ?3") })
@Entity
@Table(name = "PERS_STUNDENABRECHNUNG")
public class Stundenabrechnung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_DATUM")
	private Timestamp tDatum;

	@Column(name = "N_MEHRSTUNDEN")
	private BigDecimal nMehrstunden;

	@Column(name = "N_UESTFREI50")
	private BigDecimal nUestfrei50;

	@Column(name = "N_UESTPFLICHTIG50")
	private BigDecimal nUestpflichtig50;

	@Column(name = "N_UESTFREI100")
	private BigDecimal nUestfrei100;

	@Column(name = "N_UESTPFLICHTIG100")
	private BigDecimal nUestpflichtig100;

	@Column(name = "N_GUTSTUNDEN")
	private BigDecimal nGutstunden;

	@Column(name = "N_QUALIFIKATIONSPRAEMIE")
	private BigDecimal nQualifikationspraemie;

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "N_NORMALSTUNDEN")
	private BigDecimal nNormalstunden;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "N_UEST200")
	private BigDecimal nUest200;
	
	@Column(name = "N_QUALIFIKATIONSFAKTOR")
	private BigDecimal nQualifikationsfaktor;
	
	
	public BigDecimal getNQualifikationsfaktor() {
		return nQualifikationsfaktor;
	}

	public void setNQualifikationsfaktor(BigDecimal nQualifikationsfaktor) {
		this.nQualifikationsfaktor = nQualifikationsfaktor;
	}

	public BigDecimal getNUest200() {
		return nUest200;
	}

	public void setNUest200(BigDecimal uest200) {
		nUest200 = uest200;
	}

	private static final long serialVersionUID = 1L;

	public Stundenabrechnung() {
		super();
	}

	public Stundenabrechnung(Integer id,
			Integer personalIId2, 
			Timestamp datum,
			Integer personalIIdAendern2) {
		setIId(id);
		setPersonalIId(personalIId2);
		setTDatum(datum);
		setPersonalIIdAendern(personalIIdAendern2);
        setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		setNMehrstunden(new BigDecimal(0));
		setNQualifikationspraemie(new BigDecimal(0));
		setNUestfrei100(new BigDecimal(0));
		setNUestfrei50(new BigDecimal(0));
		setNGutstunden(new BigDecimal(0));
		setNUestpflichtig100(new BigDecimal(0));
		setNUestpflichtig50(new BigDecimal(0));
		setNUestpflichtig50(new BigDecimal(0));
		setNNormalstunden(new BigDecimal(0));
		setNUest200(new BigDecimal(0));
}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTDatum() {
		return this.tDatum;
	}

	public void setTDatum(Timestamp tDatum) {
		this.tDatum = tDatum;
	}

	public BigDecimal getNMehrstunden() {
		return this.nMehrstunden;
	}

	public void setNMehrstunden(BigDecimal nMehrstunden) {
		this.nMehrstunden = nMehrstunden;
	}

	public BigDecimal getNUestfrei50() {
		return this.nUestfrei50;
	}

	public void setNUestfrei50(BigDecimal nUestfrei50) {
		this.nUestfrei50 = nUestfrei50;
	}

	public BigDecimal getNUestpflichtig50() {
		return this.nUestpflichtig50;
	}

	public void setNUestpflichtig50(BigDecimal nUestpflichtig50) {
		this.nUestpflichtig50 = nUestpflichtig50;
	}

	public BigDecimal getNUestfrei100() {
		return this.nUestfrei100;
	}

	public void setNUestfrei100(BigDecimal nUestfrei100) {
		this.nUestfrei100 = nUestfrei100;
	}

	public BigDecimal getNUestpflichtig100() {
		return this.nUestpflichtig100;
	}

	public void setNUestpflichtig100(BigDecimal nUestpflichtig100) {
		this.nUestpflichtig100 = nUestpflichtig100;
	}

	public BigDecimal getNGutstunden() {
		return this.nGutstunden;
	}

	public void setNGutstunden(BigDecimal nGutstunden) {
		this.nGutstunden = nGutstunden;
	}

	public BigDecimal getNQualifikationspraemie() {
		return this.nQualifikationspraemie;
	}

	public void setNQualifikationspraemie(BigDecimal nQualifikationspraemie) {
		this.nQualifikationspraemie = nQualifikationspraemie;
	}

	public String getCKommentar() {
		return this.cKommentar;
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

	public BigDecimal getNNormalstunden() {
		return this.nNormalstunden;
	}

	public void setNNormalstunden(BigDecimal nNormalstunden) {
		this.nNormalstunden = nNormalstunden;
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
