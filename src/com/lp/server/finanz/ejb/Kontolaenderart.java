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
package com.lp.server.finanz.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( 
		{ @NamedQuery(name = "KontolaenderartfindBykontoIIdUebersetzt", query = "SELECT OBJECT(o) FROM Kontolaenderart o WHERE o.kontoIIdUebersetzt=?1") })

@Entity
@Table(name = "FB_KONTOLAENDERART")
public class Kontolaenderart implements Serializable {
	@EmbeddedId
	private KontolaenderartPK pk;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "KONTO_I_ID_UEBERSETZT")
	private Integer kontoIIdUebersetzt;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Kontolaenderart() {
		super();
	}

	public Kontolaenderart(Integer kontoIId, String laenderartCNr, Integer finanzamtIId, 
			String mandantCNr, Integer kontoIIdUebersetzt, Integer personalIIdAnlegen,
			Integer personalIIdAendern) {
		setPk(new KontolaenderartPK(kontoIId, laenderartCNr, finanzamtIId, mandantCNr));
		setKontoIIdUebersetzt(kontoIIdUebersetzt);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		// Setzen der NOT NULL felder
		Timestamp now = new Timestamp(System.currentTimeMillis());
		this.setTAendern(now);
		this.setTAnlegen(now);
	}

	public KontolaenderartPK getPk() {
		return this.pk;
	}

	public void setPk(KontolaenderartPK pk) {
		this.pk = pk;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getKontoIId() {
		return pk.getKontoIId();
	}

	public void setKontoIId(Integer kontoIId) {
		pk.setKontoIId(kontoIId);
	}

	public Integer getKontoIIdUebersetzt() {
		return this.kontoIIdUebersetzt;
	}

	public void setKontoIIdUebersetzt(Integer kontoIIdUebersetzt) {
		this.kontoIIdUebersetzt = kontoIIdUebersetzt;
	}

	public String getLaenderartCNr() {
		return pk.getLaenderartCNr();
	}

	public void setLaenderartCNr(String laenderartCNr) {
		this.pk.setLaenderartCNr(laenderartCNr);
	}

	public Integer getFinanzamtIId() {
		return pk.getFinanzamtIId();
	}

	public void setFinanzamtIId(Integer finanzamtIId) {
		this.pk.setFinanzamtIId(finanzamtIId);
	}
	public String getMandantCNr() {
		return pk.getMandantCNr();
	}

	public void setMandantCNr(String mandantCNr) {
		this.pk.setMandantCNr(mandantCNr);
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

}
