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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;

@NamedQueries( 
		{ @NamedQuery(name = "UvaverprobungfindLastByFinanzamtIIdMandant", query = "SELECT OBJECT(o) FROM Uvaverprobung o WHERE o.finanzamtIId=?1 AND o.mandantCNr=?2 order by o.iGeschaeftsjahr DESC, o.iMonat DESC"),
		  @NamedQuery(name = "UvaverprobungfindByGeschaeftsjahrMonatFinanzamtMandant", query = "SELECT OBJECT(o) FROM Uvaverprobung o WHERE o.iGeschaeftsjahr=?1 AND o.iMonat=?2 AND o.finanzamtIId=?3 AND o.mandantCNr=?4"),
		  @NamedQuery(name = "UvaverprobungfindAll", query = "SELECT OBJECT(o) FROM Uvaverprobung o WHERE o.mandantCNr=?1")})

@Entity
@Table(name = ITablenames.FB_UVAVERPROBUNG)
public class Uvaverprobung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "GESCHAEFTSJAHR_I_GESCHAEFTSJAHR")
	private Integer iGeschaeftsjahr;

	@Column(name = "I_MONAT")
	private Integer iMonat;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "FINANZAMT_I_ID")
	private Integer finanzamtIId;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Uvaverprobung() {
		super();
	}

	public Uvaverprobung(Integer id, Integer iGeschaeftsjahr, Integer iMonat, String mandantCNr, Integer finanzamtIId,
			Integer personalIIdAnlegen) {
		setIId(id);
		setGeschaeftsjahr(iGeschaeftsjahr);
		setIMonat(iMonat);
		setMandantCNr(mandantCNr);
		setFinanzamtIId(finanzamtIId);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		// Setzen der NOT NULL felder
	    Timestamp now = new Timestamp(System.currentTimeMillis());
	    this.setTAnlegen(now);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getGeschaeftsjahr() {
		return this.iGeschaeftsjahr;
	}

	public void setGeschaeftsjahr(Integer iGeschaeftsjahr) {
		this.iGeschaeftsjahr = iGeschaeftsjahr;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public void setIMonat(Integer iMonat) {
		this.iMonat = iMonat;
	}

	public Integer getIMonat() {
		return iMonat;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setFinanzamtIId(Integer finanzamtIId) {
		this.finanzamtIId = finanzamtIId;
	}

	public Integer getFinanzamtIId() {
		return finanzamtIId;
	}

}
