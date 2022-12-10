
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
package com.lp.server.rechnung.ejb;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "VerrechnungsmodelltagfindByVerrechnungsmodellIIdTagesartIIdZeitraum", query = "SELECT OBJECT(C) FROM Verrechnungsmodelltag c WHERE c.verrechnungsmodellIId = ?1 AND  c.tagesartIId = ?2 AND c.artikelIIdGebucht = ?3 AND c.uZeitraumVon IS NOT NULL AND c.uZeitraumBis IS NOT NULL ORDER BY c.uZeitraumVon ASC "),
		@NamedQuery(name = "VerrechnungsmodelltagfindByVerrechnungsmodellIIdTagesartIIdZeitraumTVon", query = "SELECT OBJECT(C) FROM Verrechnungsmodelltag c WHERE c.verrechnungsmodellIId = ?1 AND  c.tagesartIId = ?2 AND c.artikelIIdGebucht = ?3 AND c.uZeitraumVon IS NOT NULL AND c.uZeitraumBis IS NOT NULL AND c.uZeitraumVon <=?4 ORDER BY c.uZeitraumVon DESC "),
		@NamedQuery(name = "VerrechnungsmodelltagfindByVerrechnungsmodellIIdTagesartIIdDauer", query = "SELECT OBJECT(C) FROM Verrechnungsmodelltag c WHERE c.verrechnungsmodellIId = ?1 AND  c.tagesartIId = ?2 AND c.artikelIIdGebucht = ?3 AND c.uDauerAb IS NOT NULL  ORDER BY c.uDauerAb DESC "),
		@NamedQuery(name = "VerrechnungsmodelltagfindByVerrechnungsmodellIIdTagesartIId", query = "SELECT OBJECT(C) FROM Verrechnungsmodelltag c WHERE c.verrechnungsmodellIId = ?1 AND  c.tagesartIId = ?2")})

@Entity
@Table(name = "RECH_VERRECHNUNGSMODELLTAG")
public class Verrechnungsmodelltag implements Serializable {

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	@Column(name = "VERRECHNUNGSMODELL_I_ID")
	private Integer verrechnungsmodellIId;

	public Integer getVerrechnungsmodellIId() {
		return verrechnungsmodellIId;
	}

	public void setVerrechnungsmodellIId(Integer verrechnungsmodellIId) {
		this.verrechnungsmodellIId = verrechnungsmodellIId;
	}

	@Column(name = "TAGESART_I_ID")
	private Integer tagesartIId;

	@Column(name = "ARTIKEL_I_ID_GEBUCHT")
	private Integer artikelIIdGebucht;

	@Column(name = "ARTIKEL_I_ID_ZUVERRECHNEN")
	private Integer artikelIIdZuverrechnen;

	@Column(name = "U_DAUER_AB")
	private Time uDauerAb;

	@Column(name = "U_ZEITRAUM_VON")
	private Time uZeitraumVon;

	@Column(name = "U_ZEITRAUM_BIS")
	private Time uZeitraumBis;

	
	@Column(name = "B_ENDEDESTAGES")
	private Short bEndedestages;
	
	
	
	public Short getBEndedestages() {
		return bEndedestages;
	}

	public void setBEndedestages(Short bEndedestages) {
		this.bEndedestages = bEndedestages;
	}

	private static final long serialVersionUID = 1L;

	public Verrechnungsmodelltag() {
		super();
	}

	public Verrechnungsmodelltag(Integer id, Integer tagesartIId, Integer artikelIIdGebucht,
			Integer artikelIIdZuverrechnen, Integer verrechnungsmodellIId, Short bEndedestages) {
		setIId(id);
		setTagesartIId(tagesartIId);
		setArtikelIIdGebucht(artikelIIdGebucht);
		setArtikelIIdZuverrechnen(artikelIIdZuverrechnen);
		setVerrechnungsmodellIId(verrechnungsmodellIId);
		setBEndedestages(bEndedestages);

	}

	public Integer getTagesartIId() {
		return tagesartIId;
	}

	public void setTagesartIId(Integer tagesartIId) {
		this.tagesartIId = tagesartIId;
	}

	public Integer getArtikelIIdGebucht() {
		return artikelIIdGebucht;
	}

	public void setArtikelIIdGebucht(Integer artikelIIdGebucht) {
		this.artikelIIdGebucht = artikelIIdGebucht;
	}

	public Integer getArtikelIIdZuverrechnen() {
		return artikelIIdZuverrechnen;
	}

	public void setArtikelIIdZuverrechnen(Integer artikelIIdZuverrechnen) {
		this.artikelIIdZuverrechnen = artikelIIdZuverrechnen;
	}

	public Time getUDauerAb() {
		return uDauerAb;
	}

	public void setUDauerAb(Time uDauerAb) {
		this.uDauerAb = uDauerAb;
	}

	public Time getUZeitraumVon() {
		return uZeitraumVon;
	}

	public void setUZeitraumVon(Time uZeitraumVon) {
		this.uZeitraumVon = uZeitraumVon;
	}

	public Time getUZeitraumBis() {
		return uZeitraumBis;
	}

	public void setUZeitraumBis(Time uZeitraumBis) {
		this.uZeitraumBis = uZeitraumBis;
	}

}
