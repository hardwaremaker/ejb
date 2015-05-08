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
package com.lp.server.partner.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQueries( {
		@NamedQuery(name = "KontaktfindByPartnerIId", query = "SELECT OBJECT (O) FROM Kontakt o WHERE o.partnerIId=?1") ,
		@NamedQuery(name = "KontaktfindByAnsprechpartnerIId", query = "SELECT OBJECT (O) FROM Kontakt o WHERE o.ansprechpartnerIId=?1") })

@Entity
@Table(name = "PART_KONTAKT")
public class Kontakt implements Serializable {
	public Integer getPartnerIId() {
		return partnerIId;
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;
	@Column(name = "C_TITEL")
	private String cTitel;
	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	public String getCTitel() {
		return cTitel;
	}

	public void setCTitel(String titel) {
		cTitel = titel;
	}

	public Integer integer() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getPersonalIIdZugewiesener() {
		return personalIIdZugewiesener;
	}

	public void setPersonalIIdZugewiesener(Integer personalIIdZugewiesener) {
		this.personalIIdZugewiesener = personalIIdZugewiesener;
	}

	public Integer getKontaktartIId() {
		return kontaktartIId;
	}

	public void setKontaktartIId(Integer kontaktartIId) {
		this.kontaktartIId = kontaktartIId;
	}

	public Timestamp getTKontakt() {
		return tKontakt;
	}

	public void setTKontakt(Timestamp kontakt) {
		tKontakt = kontakt;
	}

	public Timestamp getTKontaktbis() {
		return tKontaktbis;
	}

	public void setTKontaktbis(Timestamp kontaktbis) {
		tKontaktbis = kontaktbis;
	}

	public Timestamp getTWiedervorlage() {
		return tWiedervorlage;
	}

	public void setTWiedervorlage(Timestamp wiedervorlage) {
		tWiedervorlage = wiedervorlage;
	}

	public String getXKommentar() {
		return xKommentar;
	}

	public void setXKommentar(String kommentar) {
		xKommentar = kommentar;
	}

	public Timestamp getTErledigt() {
		return tErledigt;
	}

	public void setTErledigt(Timestamp erledigt) {
		tErledigt = erledigt;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp anlegen) {
		tAnlegen = anlegen;
	}

	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;
	@Column(name = "PERSONAL_I_ID_ZUGEWIESENER")
	private Integer personalIIdZugewiesener;
	@Column(name = "KONTAKTART_I_ID")
	private Integer kontaktartIId;
	@Column(name = "T_KONTAKT")
	private Timestamp tKontakt;
	@Column(name = "T_KONTAKTBIS")
	private Timestamp tKontaktbis;
	@Column(name = "T_WIEDERVORLAGE")
	private Timestamp tWiedervorlage;
	@Column(name = "X_KOMMENTAR")
	private String xKommentar;
	@Column(name = "T_ERLEDIGT")
	private Timestamp tErledigt;
	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;
	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	private static final long serialVersionUID = 1L;

	public Kontakt() {
		super();
	}

	public Kontakt(Integer iId, String cTitel, Integer partnerIId,
			Integer personalIIdZugewiesener, Integer kontaktartIId,
			Timestamp tKontakt, Integer personalIIdAnlegen, Timestamp tAnlegen) {
		setIId(iId);
		setCTitel(cTitel);
		setPartnerIId(partnerIId);
		setPersonalIIdZugewiesener(personalIIdZugewiesener);
		setKontaktartIId(kontaktartIId);
		setTKontakt(tKontakt);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTAnlegen(tAnlegen);
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer id) {
		iId = id;
	}

}
