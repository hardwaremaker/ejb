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
package com.lp.server.projekt.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "BereichFindByMandantCNrCBez", query = "SELECT OBJECT(o) FROM Bereich o WHERE o.mandantCNr = ?1 AND o.cBez = ?2"),
		@NamedQuery(name = "BereichFindByMandantCNr", query = "SELECT OBJECT(o) FROM Bereich o WHERE o.mandantCNr = ?1 ORDER BY o.iSort"),
		@NamedQuery(name = "BereichejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Bereich o WHERE  o.mandantCNr = ?1") })
@Entity
@Table(name = "PROJ_BEREICH")
public class Bereich implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "B_DURCHGEFUEHRT_VON_IN_OFFENE")
	private Short bDurchgefuehrtVonInOffene;

	@Column(name = "B_DETAILTEXT_IST_PFLICHTFELD")
	private Short bDetailtextIstPflichtfeld;
	
	public Short getBDetailtextIstPflichtfeld() {
		return bDetailtextIstPflichtfeld;
	}

	public void setBDetailtextIstPflichtfeld(Short bDetailtextIstPflichtfeld) {
		this.bDetailtextIstPflichtfeld = bDetailtextIstPflichtfeld;
	}

	public Short getBDurchgefuehrtVonInOffene() {
		return bDurchgefuehrtVonInOffene;
	}

	public void setBDurchgefuehrtVonInOffene(Short bDurchgefuehrtVonInOffene) {
		this.bDurchgefuehrtVonInOffene = bDurchgefuehrtVonInOffene;
	}

	@Column(name = "B_PROJEKT_MIT_ARTIKEL")
	private Short bProjektMitArtikel;

	public Short getBProjektMitArtikel() {
		return bProjektMitArtikel;
	}

	public void setBProjektMitArtikel(Short bProjektMitArtikel) {
		this.bProjektMitArtikel = bProjektMitArtikel;
	}

	public Short getBProjektArtikeleindeutig() {
		return bProjektArtikeleindeutig;
	}

	public void setBProjektArtikeleindeutig(Short bProjektArtikeleindeutig) {
		this.bProjektArtikeleindeutig = bProjektArtikeleindeutig;
	}

	public Short getBProjektArtikelPflichtfeld() {
		return bProjektArtikelPflichtfeld;
	}

	public void setBProjektArtikelPflichtfeld(Short bProjektArtikelPflichtfeld) {
		this.bProjektArtikelPflichtfeld = bProjektArtikelPflichtfeld;
	}

	@Column(name = "B_PROJEKT_ARTIKEL_EINDEUTIG")
	private Short bProjektArtikeleindeutig;
	@Column(name = "B_PROJEKT_ARTIKEL_PFLICHTFELD")
	private Short bProjektArtikelPflichtfeld;

	@Column(name = "B_PROJEKT_MIT_BETREIBER")
	private Short bProjektMitBetreiber;

	public Short getBProjektMitBetreiber() {
		return bProjektMitBetreiber;
	}

	public void setBProjektMitBetreiber(Short bProjektMitBetreiber) {
		this.bProjektMitBetreiber = bProjektMitBetreiber;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private static final long serialVersionUID = 1L;

	public Bereich() {
		super();
	}

	public Bereich(Integer id, String mandantCNr, String cBez, Short bProjektMitBetreiber, Short bProjektMitArtikel,
			Short bProjektArtikeleindeutig, Short bProjektArtikelPflichtfeld,Short bDurchgefuehrtVonInOffene, Short bDetailtextIstPflichtfeld, Integer iSort) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setCBez(cBez);
		setISort(iSort);
		setBProjektMitBetreiber(bProjektMitBetreiber);
		setBProjektMitArtikel(bProjektMitArtikel);
		setBProjektArtikeleindeutig(bProjektArtikeleindeutig);
		setBProjektArtikelPflichtfeld(bProjektArtikelPflichtfeld);
		setBDurchgefuehrtVonInOffene(bDurchgefuehrtVonInOffene);
		setBDetailtextIstPflichtfeld(bDetailtextIstPflichtfeld);

	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String bez) {
		cBez = bez;
	}

}
