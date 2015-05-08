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
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "VkpfartikelpreislistefindByMandantCNrAndCNr", query = "SELECT OBJECT (o) FROM Vkpfartikelpreisliste o WHERE o.mandantCNr=?1 AND o.cNr=?2"),
		@NamedQuery(name = "VkpfartikelpreislistefindByMandantCNr", query = "SELECT OBJECT (o) FROM Vkpfartikelpreisliste o WHERE o.mandantCNr=?1 ORDER BY o.iSort"),
		@NamedQuery(name = "VkpfartikelpreislistefindByCFremdsystemnr", query = "SELECT OBJECT (o) FROM Vkpfartikelpreisliste o WHERE o.cFremdsystemnr=?1 ORDER BY o.iSort"),
		@NamedQuery(name = "VkpfartikelpreislisteejbSelectMaxISort", query = "SELECT MAX (preisliste.iSort) FROM Vkpfartikelpreisliste AS preisliste WHERE preisliste.mandantCNr=?1"),
		@NamedQuery(name = "VkpfartikelpreislistefindByMandantCNrBPreislisteaktiv", query = "SELECT OBJECT (o) FROM Vkpfartikelpreisliste o WHERE o.mandantCNr=?1 AND o.bPreislisteaktiv=?2 ORDER BY o.iSort") })
@Entity
@Table(name = "WW_VKPFARTIKELPREISLISTE")
public class Vkpfartikelpreisliste implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "B_PREISLISTEAKTIV")
	private Short bPreislisteaktiv;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "WAEHRUNG_C_NR")
	private String waehrungCNr;

	@Column(name = "C_FREMDSYSTEMNR")
	private String cFremdsystemnr;

	@Column(name = "WEBSHOP_I_ID")
	private Integer webshopIId;
	
	
	public Integer getWebshopIId() {
		return webshopIId;
	}

	public void setWebshopIId(Integer webshopIId) {
		this.webshopIId = webshopIId;
	}

	
	@Column(name = "N_STANDARDRABATTSATZ")
	private BigDecimal nStandardrabattsatz;
	
	public BigDecimal getNStandardrabattsatz() {
		return nStandardrabattsatz;
	}

	public void setNStandardrabattsatz(BigDecimal standardrabattsatz) {
		nStandardrabattsatz = standardrabattsatz;
	}

	public String getCFremdsystemnr() {
		return cFremdsystemnr;
	}

	public void setCFremdsystemnr(String fremdsystemnr) {
		cFremdsystemnr = fremdsystemnr;
	}

	private static final long serialVersionUID = 1L;

	public Vkpfartikelpreisliste() {
		super();
	}

	public Vkpfartikelpreisliste(Integer idPreisliste,
			String mandantCNr,
			Integer sort, 
			String nr,
			String waehrungCNr) {
		setIId(idPreisliste);
		setMandantCNr(mandantCNr);
		setISort(sort);
		setCNr(nr);
		// CMP alle NOT NULL und default felder hier setzen
	    setBPreislisteaktiv(new Short( (short) 1));
	    
		setWaehrungCNr(waehrungCNr);
	}
	
	public Vkpfartikelpreisliste(Integer idPreisliste,
			String mandantCNr,
			Integer sort, 
			String nr,
			Short preislisteaktiv,
			String waehrungCNr) {
		setIId(idPreisliste);
		setMandantCNr(mandantCNr);
		setISort(sort);
		setCNr(nr);
	    setBPreislisteaktiv(preislisteaktiv);
		setWaehrungCNr(waehrungCNr);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Short getBPreislisteaktiv() {
		return this.bPreislisteaktiv;
	}

	public void setBPreislisteaktiv(Short bPreislisteaktiv) {
		this.bPreislisteaktiv = bPreislisteaktiv;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getWaehrungCNr() {
		return this.waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

}
