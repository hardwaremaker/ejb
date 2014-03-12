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
package com.lp.server.partner.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( { @NamedQuery(name = "LieferantbeurteilungfindByLieferantIIdTDatum", query = "SELECT OBJECT(o) FROM Lieferantbeurteilung o WHERE o.lieferantIId = ?1 AND o.tDatum = ?2"),
	@NamedQuery(name = "LieferantbeurteilungfindByLieferantIId", query = "SELECT OBJECT(o) FROM Lieferantbeurteilung o WHERE o.lieferantIId = ?1"),
	@NamedQuery(name = "LieferantbeurteilungfindByLetzteBeurteilungByLieferantIId", query = "SELECT OBJECT(o) FROM Lieferantbeurteilung o WHERE o.lieferantIId = ?1 AND o.tDatum < ?2 ORDER BY o.tDatum DESC") })
@Entity
@Table(name = "PART_LIEFERANTBEURTEILUNG")
public class Lieferantbeurteilung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer id) {
		iId = id;
	}

	public Timestamp getTDatum() {
		return tDatum;
	}

	public void setTDatum(Timestamp datum) {
		tDatum = datum;
	}

	public Short getBGesperrt() {
		return bGesperrt;
	}

	public void setBGesperrt(Short gesperrt) {
		bGesperrt = gesperrt;
	}

	public Integer getIPunkte() {
		return iPunkte;
	}

	public void setIPunkte(Integer punkte) {
		iPunkte = punkte;
	}

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	@Column(name = "T_DATUM")
	private Timestamp tDatum;

	@Column(name = "B_GESPERRT")
	private Short bGesperrt;

	@Column(name = "B_MANUELLGEAENDERT")
	private Short bManuellgeaendert;

	@Column(name = "C_KLASSE")
	private String cKlasse;

	public Short getBManuellgeaendert() {
		return bManuellgeaendert;
	}

	public void setBManuellgeaendert(Short manuellgeaendert) {
		bManuellgeaendert = manuellgeaendert;
	}

	public String getCKlasse() {
		return cKlasse;
	}

	public void setCKlasse(String klasse) {
		cKlasse = klasse;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String kommentar) {
		cKommentar = kommentar;
	}

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	@Column(name = "I_PUNKTE")
	private Integer iPunkte;

	@Column(name = "LIEFERANT_I_ID")
	private Integer lieferantIId;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;
	
	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp aendern) {
		tAendern = aendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	private static final long serialVersionUID = 1L;

	public Lieferantbeurteilung() {
		super();
	}

	public Lieferantbeurteilung(Integer id, Integer lieferantIId,
			Timestamp tDatum, Short bGesperrt, Integer iPunkte,Integer personalIIdAendern,Timestamp tAendern,Short bManuellgeaendert, String cKlasse) {
		setIId(id);
		setLieferantIId(lieferantIId);
		setTDatum(tDatum);
		setIPunkte(iPunkte);
		setBGesperrt(bGesperrt);
		setPersonalIIdAendern(personalIIdAendern);
		setTAendern(tAendern);
		setCKlasse(cKlasse);
		setBManuellgeaendert(bManuellgeaendert);
	}

}
