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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( { @NamedQuery(name = "ArtikellagerfindByArtikelIId", query = "SELECT OBJECT(C) FROM Artikellager c WHERE c.pk.artikelIId = ?1") })
@Entity
@Table(name = "WW_ARTIKELLAGER")
public class Artikellager implements Serializable {
	@EmbeddedId
	private ArtikellagerPK pk;

	@Column(name = "N_GESTEHUNGSPREIS")
	private BigDecimal nGestehungspreis;

	@Column(name = "N_LAGERSTAND")
	private BigDecimal nLagerstand;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	private static final long serialVersionUID = 1L;

	public Artikellager() {
		super();
	}

	public Artikellager(Integer artikelIId,
			Integer lagerIId,
			String mandant2) {
		setPk(new ArtikellagerPK(artikelIId, lagerIId));
		setNLagerstand(new BigDecimal(0));
		setNGestehungspreis(new BigDecimal(0));
		setMandantCNr(mandant2);
	}
	
	public Artikellager(Integer artikelIId,
			Integer lagerIId,
			BigDecimal gestehungspreis,
			BigDecimal lagerstand,
			String mandant2) {
		setPk(new ArtikellagerPK(artikelIId, lagerIId));
		setNLagerstand(lagerstand);
		setNGestehungspreis(gestehungspreis);
		setMandantCNr(mandant2);
	}
	
	public Artikellager(ArtikellagerPK artikellagerPK,String mandandtCnr){
		setPk(artikellagerPK);
		setMandantCNr(mandandtCnr);
		setNLagerstand(new BigDecimal(0));
	    setNGestehungspreis(new BigDecimal(0));
	}

	public ArtikellagerPK getPk() {
		return this.pk;
	}

	public void setPk(ArtikellagerPK pk) {
		this.pk = pk;
	}

	public BigDecimal getNGestehungspreis() {
		return this.nGestehungspreis;
	}

	public void setNGestehungspreis(BigDecimal nGestehungspreis) {
		this.nGestehungspreis = nGestehungspreis;
	}

	public BigDecimal getNLagerstand() {
		return this.nLagerstand;
	}

	public void setNLagerstand(BigDecimal nLagerstand) {
		this.nLagerstand = nLagerstand;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

}
