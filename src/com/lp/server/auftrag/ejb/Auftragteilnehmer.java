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
package com.lp.server.auftrag.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "AuftragteilnehmerfindByAuftrag", query = "SELECT OBJECT (o) FROM Auftragteilnehmer o WHERE o.auftragIId=?1 ORDER BY o.iSort"),
		@NamedQuery(name = "AuftragteilnehmerejbSelectMaxISort", query = "SELECT MAX (teilnehmer.iSort) FROM Auftragteilnehmer AS teilnehmer WHERE teilnehmer.auftragIId = ?1"),
		@NamedQuery(name = "AuftragteilnehmerfindByPartnerIidTeilnehmer", query = "SELECT OBJECT (o) FROM Auftragteilnehmer o WHERE o.partnerIIdAuftragteilnehmer=?1") })
@Entity
@Table(name = "AUFT_AUFTRAGTEILNEHMER")
public class Auftragteilnehmer implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "B_ISTEXTERNERTEILNEHMER")
	private Short bIstexternerteilnehmer;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "AUFTRAG_I_ID")
	private Integer auftragIId;

	@Column(name = "FUNKTION_I_ID")
	private Integer funktionIId;

	@Column(name = "PARTNER_I_ID_AUFTRAGTEILNEHMER")
	private Integer partnerIIdAuftragteilnehmer;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Auftragteilnehmer(Integer iId, 
			Integer iSort, 
			Integer auftragIId,
			Integer partnerIIdAuftragteilnehmer,
			Integer funktionIId,
			Integer personalIIdAnlegen) {
		
			
		setIId(iId);				
		setISort(iSort);
		setAuftragIId(auftragIId);
		setPartnerIIdAuftragteilnehmer(partnerIIdAuftragteilnehmer);
		setFunktionIId(funktionIId);
		setPersonalIIdAnlegen(personalIIdAnlegen);

		// NOT NULL Felder, die nicht vom Client befuellt werden, initialisieren
		this.setTAnlegen(new Timestamp(System.currentTimeMillis()));
		this.setBIstexternerteilnehmer(new Short((short) 0));
	}
	
	public Auftragteilnehmer(Integer iId, 
			Integer iSort, 
			Integer auftragIId,
			Integer partnerIIdAuftragteilnehmer,
			Short istexternerteilnehmer,
			Integer funktionIId,
			Integer personalIIdAnlegen) {

		setIId(iId);
		setISort(iSort);
		setAuftragIId(auftragIId);
		setPartnerIIdAuftragteilnehmer(partnerIIdAuftragteilnehmer);
		setFunktionIId(funktionIId);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
		setBIstexternerteilnehmer(istexternerteilnehmer);
	}

	public Auftragteilnehmer() {

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

	public Short getBIstexternerteilnehmer() {
		return this.bIstexternerteilnehmer;
	}

	public void setBIstexternerteilnehmer(Short bIstexternerteilnehmer) {
		this.bIstexternerteilnehmer = bIstexternerteilnehmer;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getAuftragIId() {
		return this.auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public Integer getFunktionIId() {
		return this.funktionIId;
	}

	public void setFunktionIId(Integer funktionIId) {
		this.funktionIId = funktionIId;
	}

	public Integer getPartnerIIdAuftragteilnehmer() {
		return this.partnerIIdAuftragteilnehmer;
	}

	public void setPartnerIIdAuftragteilnehmer(
			Integer partnerIIdAuftragteilnehmer) {
		this.partnerIIdAuftragteilnehmer = partnerIIdAuftragteilnehmer;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

}
