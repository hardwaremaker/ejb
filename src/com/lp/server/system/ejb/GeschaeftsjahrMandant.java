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
package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.util.Helper;

@NamedQueries( 	{
		@NamedQuery(name = GeschaeftsjahrMandantQuery.ByMandant,     query = "SELECT OBJECT(o) FROM GeschaeftsjahrMandant o WHERE o.mandantCNr = :mandant ORDER BY o.tBeginndatum DESC"),
		@NamedQuery(name = GeschaeftsjahrMandantQuery.ByYear,        query = "SELECT OBJECT(o) FROM GeschaeftsjahrMandant o WHERE o.iGeschaeftsjahr = :year ORDER BY o.mandantCNr"),
		@NamedQuery(name = GeschaeftsjahrMandantQuery.ByDateMandant, query = "SELECT OBJECT(o) FROM GeschaeftsjahrMandant o WHERE o.mandantCNr = :mandant AND o.tBeginndatum<= :date ORDER BY o.tBeginndatum DESC"),
		@NamedQuery(name = GeschaeftsjahrMandantQuery.ByYearMandant, query = "SELECT OBJECT(o) FROM GeschaeftsjahrMandant o WHERE o.iGeschaeftsjahr = :year AND o.mandantCNr = :mandant")		})

@Entity
@Table(name = "LP_GESCHAEFTSJAHRMANDANT")
public class GeschaeftsjahrMandant implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;
	
	@Column(name = "I_GESCHAEFTSJAHR")
	private Integer iGeschaeftsjahr;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;
	
	@Column(name = "T_BEGINNDATUM")
	private Timestamp tBeginndatum;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "T_SPERRE")
	private Timestamp tSperre;
	
	@Column(name = "PERSONAL_I_ID_SPERRE")
	private Integer personalIIdSperre;
	
	private static final long serialVersionUID = 1L;

	public GeschaeftsjahrMandant() {
		super();
	}

	public GeschaeftsjahrMandant(Integer id, Integer geschaeftsjahr, String mandant, Timestamp beginndatum,
			Integer personalIIdAnlegen2) {
		setiId(id) ;
		setIGeschaeftsjahr(geschaeftsjahr);
		setMandantCNr(mandant) ;
		setTBeginndatum(beginndatum);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
	}

	public Integer getiId() {
		return iId;
	}

	public void setiId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIGeschaeftsjahr() {
		return this.iGeschaeftsjahr;
	}

	public void setIGeschaeftsjahr(Integer iGeschaeftsjahr) {
		this.iGeschaeftsjahr = iGeschaeftsjahr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Timestamp getTBeginndatum() {
		// Achtung: keine Uhrzeit!
		return Helper.cutTimestamp(this.tBeginndatum);
	}

	public void setTBeginndatum(Timestamp tBeginndatum) {
		// Achtung: keine Uhrzeit!
		this.tBeginndatum = Helper.cutTimestamp(tBeginndatum);
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public void setTSperre(Timestamp tSperre) {
		this.tSperre = tSperre;
	}

	public Timestamp getTSperre() {
		return tSperre;
	}

	public void setPersonalIIdSperre(Integer personalIIdSperre) {
		this.personalIIdSperre = personalIIdSperre;
	}

	public Integer getPersonalIIdSperre() {
		return personalIIdSperre;
	}
}
