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
package com.lp.server.finanz.ejb;

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
		@NamedQuery(name = Buchungdetail.QueryBuchungdetailfindByBuchungIID, query = "SELECT OBJECT(o) FROM Buchungdetail o WHERE o.buchungIId=?1 ORDER BY o.iId"),
		@NamedQuery(name = "BuchungdetailfindByKontoIIdGegenKtoIIdBuchungIId", query = "SELECT OBJECT(o) FROM Buchungdetail o WHERE o.kontoIId=?1 AND o.kontoIIdGegenkonto=?2 AND o.buchungIId=?3"),
		@NamedQuery(name = "BuchungdetailMaxIAusziffern", query = "SELECT MAX(o.iAusziffern) FROM Buchungdetail o WHERE o.kontoIId=?1"),
		@NamedQuery(name = "BuchungdetailfindByKontoIId", query = "SELECT OBJECT(o) FROM Buchungdetail o WHERE o.kontoIId=?1"),
		@NamedQuery(name = Buchungdetail.QueryBuchungdetailfindByKontoIIdBuchungIId, query = "SELECT OBJECT(o) FROM Buchungdetail o WHERE o.kontoIId=?1 AND o.buchungIId=?2") })
@Entity
@Table(name = "FB_BUCHUNGDETAIL")
public class Buchungdetail implements Serializable {
	public static final String QueryBuchungdetailfindByBuchungIID = "BuchungdetailfindByBuchungIId" ;
	public static final String QueryBuchungdetailfindByKontoIIdBuchungIId = "BuchungdetailfindByKontoIIdBuchungIId";

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_UST")
	private BigDecimal nUst;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "I_AUSZUG")
	private Integer iAuszug;

	@Column(name = "BUCHUNG_I_ID")
	private Integer buchungIId;

	@Column(name = "KONTO_I_ID")
	private Integer kontoIId;

	@Column(name = "KONTO_I_ID_GEGENKONTO")
	private Integer kontoIIdGegenkonto;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "BUCHUNGDETAILART_C_NR")
	private String buchungdetailartCNr;

	@Column(name = "N_BETRAG")
	private BigDecimal nBetrag;

	@Column(name = "I_AUSZIFFERN")
	private Integer iAusziffern;

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	private static final long serialVersionUID = 1L;

	public Buchungdetail() {
		super();
	}

	public Buchungdetail(Integer id, Integer buchungIId, Integer kontoIId,
			BigDecimal betrag, String buchungdetailartCNr, BigDecimal ust,
			Integer personalIIdAnlegen, Integer personalIIdAendern) {
		setIId(id);
		setBuchungIId(buchungIId);
		setKontoIId(kontoIId);
		setNBetrag(betrag);
		setBuchungdetailartCNr(buchungdetailartCNr);
		setNUst(ust);
		// Setzen der NOT NULL Felder
		Timestamp now = new Timestamp(System.currentTimeMillis());
		this.setTAnlegen(now);
		this.setTAendern(now);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNBetrag() {
		return this.nBetrag;
	}

	public void setNBetrag(BigDecimal nBetrag) {
		this.nBetrag = nBetrag;
	}

	public BigDecimal getNUst() {
		return this.nUst;
	}

	public void setNUst(BigDecimal nUst) {
		this.nUst = nUst;
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


	public Integer getIAuszug() {
		return this.iAuszug;
	}

	public void setIAuszug(Integer iAuszug) {
		this.iAuszug = iAuszug;
	}
	public Integer getBuchungIId() {
		return this.buchungIId;
	}

	public void setBuchungIId(Integer buchungIId) {
		this.buchungIId = buchungIId;
	}

	public Integer getKontoIId() {
		return this.kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public Integer getKontoIIdGegenkonto() {
		return this.kontoIIdGegenkonto;
	}

	public void setKontoIIdGegenkonto(Integer kontoIIdGegenkonto) {
		this.kontoIIdGegenkonto = kontoIIdGegenkonto;
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

	public void setBuchungdetailartCNr(String buchungdetailartCNr) {
		this.buchungdetailartCNr = buchungdetailartCNr;
	}

	public String getBuchungdetailartCNr() {
		return buchungdetailartCNr;
	}

	public void setIAusziffern(Integer iAusziffern) {
		this.iAusziffern = iAusziffern;
	}

	public Integer getIAusziffern() {
		return iAusziffern;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

}
