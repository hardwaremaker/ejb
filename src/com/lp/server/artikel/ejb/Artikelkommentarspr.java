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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "ArtikelkommentarsprfindByArtikelkommentarIIdLocaleCNr", query = "SELECT OBJECT (o) FROM Artikelkommentarspr o WHERE o.pk.artikelkommentarIId=?1 AND o.pk.localeCNr=?2"),
		@NamedQuery(name = "ArtikelkommentarsprfindByArtikelkommentarIId", query = "SELECT OBJECT(C) FROM Artikelkommentarspr c WHERE c.pk.artikelkommentarIId = ?1"),
		@NamedQuery(name = ArtikelkommentarsprQuery.ByChangedDate, query = "SELECT OBJECT(C) FROM Artikelkommentarspr c WHERE c.tAendern >= :changed")
		})
@Entity
@Table(name = "WW_ARTIKELKOMMENTARSPR")
public class Artikelkommentarspr implements Serializable {
	@EmbeddedId
	private ArtikelkommentarsprPK pk;

	@Column(name = "X_KOMMENTAR")
	private String xKommentar;

	@Column(name = "O_MEDIA")
	private byte[] oMedia;

	@Column(name = "C_DATEINAME")
	private String cDateiname;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "T_FILEDATUM")
	private Timestamp tFiledatum;

	public Timestamp getTFiledatum() {
		return tFiledatum;
	}

	public void setTFiledatum(Timestamp tFiledatum) {
		this.tFiledatum = tFiledatum;
	}

	private static final long serialVersionUID = 1L;

	public Artikelkommentarspr() {
		super();
	}

	public Artikelkommentarspr(Integer id, String locUiAsString,
			Integer personalIIdAendern, Timestamp tAendern) {
		pk = new ArtikelkommentarsprPK(id, locUiAsString);
		setTAendern(tAendern);
		setPersonalIIdAendern(personalIIdAendern);
	}

	public ArtikelkommentarsprPK getPk() {
		return this.pk;
	}

	public void setPk(ArtikelkommentarsprPK pk) {
		this.pk = pk;
	}

	public String getXKommentar() {
		return this.xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public byte[] getOMedia() {
		return this.oMedia;
	}

	public void setOMedia(byte[] oMedia) {
		this.oMedia = oMedia;
	}

	public String getCDateiname() {
		return this.cDateiname;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

}
